package com.playbacktoolgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

import java.io.File;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private Slider volumeSlider;

    private MediaPlayer mediaPlayer;
    private File audioFile;

    @FXML
    protected void onChooseFileClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择一个MP3文件");
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("MP3 音频文件", "*.mp3")
        );
        File file = fileChooser.showOpenDialog(welcomeText.getScene().getWindow());
        if (file != null) {
            if (file.getName().toLowerCase().endsWith(".mp3")) {
                audioFile = file;
                welcomeText.setText("已选择: " + file.getName());
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                Media media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setVolume(volumeSlider.getValue());
            } else {
                welcomeText.setText("错误: 请选择一个有效的MP3文件！");
                audioFile = null;
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                }
            }
        }
    }

    @FXML
    protected void onPlayClick() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            welcomeText.setText("正在播放: " + (audioFile != null ? audioFile.getName() : ""));
        } else {
            welcomeText.setText("请先上传一个MP3文件");
        }
    }

    @FXML
    protected void onStopClick() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            welcomeText.setText("已停止");
        }
    }

    @FXML
    protected void onVolumeChange() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volumeSlider.getValue());
        }
    }
}