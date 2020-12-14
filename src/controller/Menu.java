package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Menu {
    public void pressButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/GamePane.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            Game gameController = fxmlLoader.getController();
            gameController.initData(root, stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
