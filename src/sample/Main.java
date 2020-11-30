package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    private int number = 10;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent start = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Wisielec");

        Text text = new Text();
        text.setFont(new Font(45));
        text.setX(50);
        text.setY(100);
        text.setText("Wisielec");

        Drawing drawing = new Drawing();

        // to poniżej będzie game loopie

        if (number <= 10) {
            drawing.draw(number);
            number++;
        } else {
            //koniec gry
        }

        Path path = drawing.getPath();
        Circle circle = drawing.getCircle();

        //Wybieramy co ma być widoczne
        Group root = new Group();

        ObservableList list = root.getChildren();
        list.addAll(text, path, circle);

        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }
}
