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

    private JdbcTemplate jdbcTemplate;
    private String selectedTable;
    private List<String> tableColumns;

    @FXML
    public void initialize() {
        // 初始化数据库连接
        jdbcTemplate = DatabaseConfig.getJdbcTemplate();

        // 获取所有表名
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
            // 获取选中表的所有列名
            tableColumns = jdbcTemplate.queryForList(
                    "SELECT column_name FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = ?",
                    String.class,
                    selectedTable
            );
            logArea.appendText("已选择表: " + selectedTable + "\n");
            logger.info("已选择表: " + selectedTable);
            logArea.appendText("表结构: " + String.join(", ", tableColumns) + "\n");
            logger.info("表结构: " + String.join(", ", tableColumns));
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
        // 检查Excel表头是否包含所有必需的数据库列
        for (String dbColumn : tableColumns) {
            if (!excelHeaders.contains(dbColumn)) {
                String message = "Excel文件缺少必需的列: " + dbColumn;
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

        EasyExcel.read(filePath, new AnalysisEventListener<Map<Integer, String>>() {
            @Override
            public void invoke(Map<Integer, String> data, AnalysisContext context) {
                try {
                    // 构建插入SQL
                    StringBuilder sql = new StringBuilder("INSERT INTO " + selectedTable + " (");
                    StringBuilder values = new StringBuilder(") VALUES (");

                    for (int i = 0; i < headers.size(); i++) {
                        if (i > 0) {
                            sql.append(", ");
                            values.append(", ");
                        }
                        sql.append(headers.get(i));
                        values.append("?");
                    }
                    sql.append(values).append(")");

                    // 准备参数
                    Object[] params = new Object[headers.size()];
                    for (int i = 0; i < headers.size(); i++) {
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
                    logArea.appendText("导入失败: " + e.getMessage() + "\n");
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 