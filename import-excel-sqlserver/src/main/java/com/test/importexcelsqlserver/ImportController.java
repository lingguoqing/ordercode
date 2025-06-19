package com.test.importexcelsqlserver;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javafx.concurrent.Task;
import javafx.application.Platform;

import java.io.File;
import java.util.*;

@Component
public class ImportController {

    private static final Logger logger = LoggerFactory.getLogger(ImportController.class);

    @FXML
    private ComboBox<String> tableComboBox;

    @FXML
    private TextField filePathField;

    @FXML
    private TextArea logArea;

    @FXML
    private Button importButton;

    @FXML
    private Button exportTemplateButton;

    private JdbcTemplate jdbcTemplate;
    private String selectedTable;
    private List<String> tableColumns;
    // 字段注释到字段名的映射（中文->英文），顺序与数据库一致
    private LinkedHashMap<String, String> commentToColumnMap = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        // 初始化数据库连接
        jdbcTemplate = DatabaseConfig.getJdbcTemplate();

        // 获取所有表名（MySQL）
        List<String> tables = jdbcTemplate.queryForList(
            "SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE()",
            String.class
        );

        tableComboBox.getItems().addAll(tables);
        tableComboBox.setOnAction(e -> handleTableSelection());
    }

    private void handleTableSelection() {
        selectedTable = tableComboBox.getValue();
        if (selectedTable != null) {
            // 获取选中表的所有非id字段的列名和注释（MySQL）
            commentToColumnMap.clear();
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "SELECT column_name, column_comment FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = ? AND column_name <> 'id' ORDER BY ordinal_position",
                selectedTable
            );
            List<String> comments = new ArrayList<>();
            for (Map<String, Object> col : columns) {
                String name = (String) col.get("column_name");
                String comment = (String) col.get("column_comment");
                String key = (comment == null || comment.isEmpty()) ? name : comment;
                commentToColumnMap.put(key, name);
                comments.add(key);
            }
            logArea.appendText("已选择表: " + selectedTable + "\n");
            logger.info("已选择表: " + selectedTable);
            logArea.appendText("表结构: " + String.join(", ", comments) + "\n");
            logger.info("表结构: " + String.join(", ", comments));
        }
    }

    @FXML
    private void handleBrowseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls")
        );

        File file = fileChooser.showOpenDialog(filePathField.getScene().getWindow());
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleImport() {
        importButton.setDisable(true);
        logArea.appendText("开始导入...\n");
        Task<Void> importTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (selectedTable == null) {
                    Platform.runLater(() -> showAlert("请先选择数据表！"));
                    return null;
                }
                String filePath = filePathField.getText();
                if (filePath.isEmpty()) {
                    Platform.runLater(() -> showAlert("请选择Excel文件！"));
                    return null;
                }
                try {
                    // 读取Excel文件的第一行作为表头（不抛异常）
                    List<String> excelHeaders = new ArrayList<>();
                    EasyExcel.read(filePath, new AnalysisEventListener<Map<Integer, String>>() {
                        @Override
                        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                            excelHeaders.addAll(headMap.values());
                        }
                        @Override
                        public void invoke(Map<Integer, String> data, AnalysisContext context) {}
                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {}
                    }).sheet().headRowNumber(1).doRead();

                    // 检查Excel表头是否与数据库表结构匹配
                    if (!validateHeaders(excelHeaders)) {
                        return null;
                    }

                    // 开始导入数据
                    importData(filePath, excelHeaders);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        logArea.appendText("错误: " + e.getMessage() + "\n");
                        logger.error("导入异常", e);
                        showAlert("导入失败：" + e.getMessage());
                    });
                }
                return null;
            }
            @Override
            protected void succeeded() {
                importButton.setDisable(false);
            }
            @Override
            protected void failed() {
                importButton.setDisable(false);
            }
            @Override
            protected void cancelled() {
                importButton.setDisable(false);
            }
        };
        new Thread(importTask).start();
    }

    private boolean validateHeaders(List<String> excelHeaders) {
        // 检查Excel表头（中文）是否包含所有必需的数据库列
        for (String comment : commentToColumnMap.keySet()) {
            if (!excelHeaders.contains(comment)) {
                String message = "Excel文件缺少必需的列: " + comment;
                logArea.appendText(message + "\n");
                logger.info(message);
                showAlert(message);
                return false;
            }
        }
        return true;
    }

    private void importData(String filePath, List<String> headers) {
        final int[] successCount = {0};
        final int[] failCount = {0};
        // 生成字段名顺序列表
        List<String> dbFields = new ArrayList<>();
        for (String comment : headers) {
            if (commentToColumnMap.containsKey(comment)) {
                dbFields.add(commentToColumnMap.get(comment));
            }
        }
        EasyExcel.read(filePath, new AnalysisEventListener<Map<Integer, String>>() {
            @Override
            public void invoke(Map<Integer, String> data, AnalysisContext context) {
                try {
                    // 构建插入SQL
                    StringBuilder sql = new StringBuilder("INSERT INTO " + selectedTable + " (");
                    StringBuilder values = new StringBuilder(") VALUES (");
                    for (int i = 0; i < dbFields.size(); i++) {
                        if (i > 0) {
                            sql.append(", ");
                            values.append(", ");
                        }
                        sql.append(dbFields.get(i));
                        values.append("?");
                    }
                    sql.append(values).append(")");
                    // 准备参数
                    Object[] params = new Object[dbFields.size()];
                    for (int i = 0; i < dbFields.size(); i++) {
                        params[i] = data.get(i);
                    }
                    // 执行插入
                    jdbcTemplate.update(sql.toString(), params);
                    successCount[0]++;
                    if (successCount[0] % 100 == 0) {
                        logArea.appendText("已成功导入 " + successCount[0] + " 条数据\n");
                        logger.info("已成功导入 " + successCount[0] + " 条数据");
                    }
                } catch (Exception e) {
                    failCount[0]++;
                    String userMsg;
                    String errMsg = e.getMessage();
                    if (errMsg != null) {
                        if (errMsg.contains("Duplicate entry")) {
                            userMsg = "导入失败：有重复的数据（如主键或唯一字段已存在）";
                        } else if (errMsg.contains("Data truncation") || errMsg.contains("Incorrect") || errMsg.contains("format")) {
                            userMsg = "导入失败：数据格式有误，请检查日期、数字等格式";
                        } else if (errMsg.contains("cannot be null") || errMsg.contains("NULL") || errMsg.contains("not null")) {
                            userMsg = "导入失败：有必填项未填写";
                        } else {
                            userMsg = "导入失败，请检查数据格式或联系管理员";
                        }
                    } else {
                        userMsg = "导入失败，请检查数据格式或联系管理员";
                    }
                    logArea.appendText(userMsg + "\n");
                    logger.error("导入失败", e);
                }
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                logArea.appendText("导入完成！成功: " + successCount[0] + " 条，失败: " + failCount[0] + " 条\n");
                logger.info("导入完成！成功: " + successCount[0] + " 条，失败: " + failCount[0] + " 条");
            }
        }).sheet().doRead();
    }

    @FXML
    private void handleExportTemplate() {
        if (selectedTable == null) {
            showAlert("请先选择数据表！");
            return;
        }
        try {
            // 查询字段名和注释，排除id字段（MySQL）
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "SELECT column_name, column_comment FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = ? AND column_name <> 'id' ORDER BY ordinal_position",
                selectedTable
            );
            if (columns.isEmpty()) {
                showAlert("未找到可导出的字段");
                return;
            }
            // 生成中文表头
            List<String> header = new ArrayList<>();
            for (Map<String, Object> col : columns) {
                String comment = (String) col.get("column_comment");
                String name = (String) col.get("column_name");
                header.add((comment == null || comment.isEmpty()) ? name : comment);
            }
            // 选择保存路径
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("保存Excel模板");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            fileChooser.setInitialFileName(selectedTable + "_模板.xlsx");
            File file = fileChooser.showSaveDialog(exportTemplateButton.getScene().getWindow());
            if (file == null) return;
            // 写入Excel
            List<List<String>> headList = new ArrayList<>();
            for (String h : header) headList.add(Collections.singletonList(h));
            // 生成一行示例数据
            List<String> exampleRow = new ArrayList<>();
            for (Map<String, Object> col : columns) {
                String name = (String) col.get("column_name");
                exampleRow.add("示例数据" + name);
            }
            List<List<String>> data = new ArrayList<>();
            data.add(exampleRow);
            EasyExcel.write(file).head(headList).sheet("模板").doWrite(data);
            logArea.appendText("模板导出成功: " + file.getAbsolutePath() + "\n");
            showAlert("模板导出成功: " + file.getAbsolutePath());
        } catch (Exception e) {
            logArea.appendText("模板导出失败: " + e.getMessage() + "\n");
            logger.error("模板导出失败", e);
            showAlert("模板导出失败: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 