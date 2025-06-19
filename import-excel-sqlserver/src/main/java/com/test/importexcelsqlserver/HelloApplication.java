package com.test.importexcelsqlserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/test/importexcelsqlserver/import-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("Excel导入工具");
            stage.setScene(scene);
            stage.show();
            System.out.println("主界面已显示");
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("启动失败");
            alert.setHeaderText("主界面加载失败");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch();
    }
} 