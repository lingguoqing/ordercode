Error occurred during initialization of boot layer
java.lang.module.FindException: Module javafx.controls not found
在vm options中写的如下，还是报错
--module-path 
"E:\apache-maven-3.9.9\repository\org\openjfx\javafx-controls\17.0.6\javafx-controls-17.0.6.jar;
E:\apache-maven-3.9.9\repository\org\openjfx\javafx-fxml\17.0.6\javafx-fxml-17.0.6.jar;
E:\apache-maven-3.9.9\repository\org\openjfx\javafx-media\17.0.6\javafx-media-17.0.6.jar"
--add-modules javafx.controls,javafx.fxml,javafx.media