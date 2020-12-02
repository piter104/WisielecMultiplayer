package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage stg;
    private int number = 0;
    private String letters = "";
    private String word = "Konczak";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stg = primaryStage;
        Parent start = FXMLLoader.load(getClass().getResource("/fxmlFiles/Menu.fxml"));
        primaryStage.setTitle("Wisielec");

//        Text text = new Text();
//        text.setFont(new Font(45));
//        text.setX(100);
//        text.setY(80);
//        text.setText("Wisielec");
//
//        Button playButton = new Button("graj");
//        playButton.setLayoutX(170);
//        playButton.setLayoutY(120);
//
//
//
//        PathTransition pathTransition = new PathTransition();
//
//        //Setting the duration of the transition
//        pathTransition.setDuration(Duration.millis(1000));
//
////        //Setting the node for the transition
////        pathTransition.setNode(circle);
//
//        Drawing drawing = new Drawing();
//
//
//        playButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent event) {
//                System.out.println("Pisz kod to sobie pograsz");
//                pathTransition.play();
//            }
//        }));
//
//        // create a textfield
//        TextField b = new TextField();
//
//        // create a tile pane
//        TilePane r = new TilePane();
//
//        // create a label
//        Label l = new Label("tu pojawiają się wpisane litery");
//
//        // action event
//        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent e)
//            {
//                if(b.getText().length() == 1) {
//                    if (letters.length() != 0) {
//                        letters = letters + ", " + b.getText();
//                    } else {
//                        letters = b.getText();
//                    }
//                    if(word.indexOf(b.getText()) == -1) {
//                        if (number < 10) {
//                            drawing.draw(number);
//                            number++;
//                        } else {
//                            //koniec gry
//                        }
//                    }
//                }
//                b.clear();
//                l.setText(letters);
//            }
//        };
//
//
//
//        // when enter is pressed
//        b.setOnAction(event);
//
//        // add textfield
//        r.getChildren().add(b);
//        r.getChildren().add(l);
//
//        // to poniżej będzie game loopie
//        Path path = drawing.getPath();
//        Circle circle = drawing.getCircle();
//
//        //Setting the path for the transition
//        pathTransition.setPath(path);
//
//        //Wybieramy co ma być widoczne
//        Group root = new Group();
//
//        ObservableList list = root.getChildren();
//        list.addAll(text, path, circle, playButton, r);

//        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.setScene(new Scene(start));


        primaryStage.show();
    }
}
