package controller;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Drawing;
import sample.Main;

public class Menu {
    public void pressButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/GamePane.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Group group = new Group(root);

            PathTransition pathTransition = new PathTransition();
            Drawing drawing = new Drawing();

            pathTransition.setPath(drawing.getPath());
            group.getChildren().add(drawing.getPath());
            group.getChildren().add(drawing.getCircle());
            drawing.draw(9);

            stage.setScene(new Scene(group));
            stage.show();
            Main.stg.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
