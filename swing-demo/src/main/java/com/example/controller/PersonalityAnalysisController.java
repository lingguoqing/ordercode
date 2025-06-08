package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PersonalityAnalysisController {

    private static final String promat = "你是一个专业的八字命理分析师和星座专家。\n" +
            "\n" +
            "请根据用户提供的【生辰八字】（出生日期和具体时间，精确到分钟）和【性别】，为用户进行以下分析并严格按照以下结构和内容输出：\n" +
            "\n" +
            "1.  **五行能量条（请用文字描述金、木、水、火、土五种元素的相对能量强弱，例如：金：强，木：弱，水：中，火：强，土：弱。请务必列出所有五行，并用“强”、“中”、“弱”或更详细的程度来描述）**\n" +
            "\n" +
            "2.  **总结**\n" +
            "    *   根据八字分析，总结其五行旺衰情况（例如：旺金, 水弱, 木弱）。\n" +
            "    *   指出其本命佛（例如：本命佛大日如来）。\n" +
            "\n" +
            "3.  **分析**\n" +
            "    *   详细分析其体内五行元素的比例和不平衡之处（例如：您体内的金元素比较多，水木弱导致五行不均衡）。\n" +
            "    *   阐述五行不均衡对其运势的影响，包括财运、事业、平安、姻缘等方面（例如：运势会比较差，运势包括财运、事业、平安、姻缘等）。\n" +
            "    *   指出需要平衡的五行（例如：水木平衡五行）。\n" +
            "\n" +
            "4.  **建议**\n" +
            "    *   根据五行缺失情况，提出具体的调和建议，包括建议引入哪些元素（例如：您体内水木元素显得相对匮乏，这导致了五行的不平衡。为了调和这一状态，提升您的整体运势，建议您引入水元素）。\n" +
            "    *   说明平衡五行带来的积极影响（例如：通过平衡五行，您将能够迎来更加顺遂的人生旅程）。\n" +
            "\n" +
            "5.  **佩戴**\n" +
            "    *   根据分析结果，给出具体的佩戴建议，包括属性和本命佛加持（例如：定制水属性+木属性+本命佛加持）。\n" +
            "\n" +
            "6.  **星座与性格**\n" +
            "    *   根据出生日期，分析用户的【星座】。\n" +
            "    *   结合星座特点和八字五行情况，描述用户的【性格】特征。";

    private ToggleGroup calendarType;

    @FXML
    private RadioButton lunarCalendarRadio;

    @FXML
    private RadioButton solarCalendarRadio;


    private ToggleGroup gender;

    @FXML
    private RadioButton maleRadio;

    @FXML
    private RadioButton femaleRadio;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField timeField;

    @FXML
    private TextField nameField;

    @FXML
    private void initialize() {
        calendarType = new ToggleGroup();
        solarCalendarRadio.setToggleGroup(calendarType);
        lunarCalendarRadio.setToggleGroup(calendarType);

        gender = new ToggleGroup();
        maleRadio.setToggleGroup(gender);
        femaleRadio.setToggleGroup(gender);

        // Add listener to enforce numeric input and range for timeField
        timeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                timeField.setText(oldValue);
            } else {
                try {
                    int time = Integer.parseInt(newValue);
                    if (time < 0 || time > 23) {
                        timeField.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    // This catch block will only be reached if newValue is empty string
                    // or if it's not a valid integer. We already filtered non-digits, so it's mainly for empty string.
                }
            }
        });
    }

    @FXML
    private void handleAnalyzeButton() {
        String name = nameField.getText();
        String date = (datePicker.getValue() != null) ? datePicker.getValue().toString() : "";
        String time = timeField.getText();
        System.out.println(name.trim().isEmpty());
        System.out.println(name.trim().isEmpty() && (date.isEmpty() && time.isEmpty()));
        if ((date.isEmpty() && time.isEmpty()) && name.trim().isEmpty()) {
            showAlert(AlertType.ERROR, "输入错误", "姓名不能为空，且年月日和时不能同时为空，请至少填写一个。");
            return;
        }

        String calendar = "";
        if (solarCalendarRadio.isSelected()) {
            calendar = solarCalendarRadio.getText();
        } else if (lunarCalendarRadio.isSelected()) {
            calendar = lunarCalendarRadio.getText();
        }

        String selectedGender = "";
        if (maleRadio.isSelected()) {
            selectedGender = maleRadio.getText();
        } else if (femaleRadio.isSelected()) {
            selectedGender = femaleRadio.getText();
        }

        System.out.println("姓名: " + name);
        System.out.println("日历类型: " + calendar);
        System.out.println("性别: " + selectedGender);
        System.out.println("年月日: " + date);
        System.out.println("时: " + time);
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}