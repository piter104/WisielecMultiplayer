package sample;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        text.setX(100);
        text.setY(80);
        text.setText("Wisielec");

        Button playButton = new Button("graj");
        playButton.setLayoutX(170);
        playButton.setLayoutY(120);

        PathTransition pathTransition = new PathTransition();

        //Setting the duration of the transition
        pathTransition.setDuration(Duration.millis(1000));

//        //Setting the node for the transition
//        pathTransition.setNode(circle);

        playButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("Pisz kod to sobie pograsz");
                pathTransition.play();
            }
        }));

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

        //Setting the path for the transition
        pathTransition.setPath(path);

        //Wybieramy co ma być widoczne
        Group root = new Group();

        ObservableList list = root.getChildren();
        list.addAll(text, path, circle, playButton);

        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }
}
