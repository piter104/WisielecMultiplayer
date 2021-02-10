package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    public static Stage stg;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stg = primaryStage;
        Parent start = FXMLLoader.load(getClass().getResource("/fxmlFiles/Menu.fxml"));
        stg.setTitle("Wisielec");
        stg.setResizable(false);
        primaryStage.setScene(new Scene(start));
        primaryStage.show();
    }
}
