package sample;

import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.List;

public class Drawing {
    private double x = 125;
    private double y = 125;
    private List<PathElement> objects = new ArrayList<>();
    private Path path = new Path();
    private Circle circle;

    public Drawing() {
        objects.add(new MoveTo(x, 3 * y));
        objects.add(new LineTo(x, 1.5 * y));
        objects.add(new LineTo(2 * x, 1.5 * y));
        objects.add(new LineTo(2 * x, 3 * y));
        objects.add(new MoveTo(1.5 * x, 1.5 * y));
        objects.add(new LineTo(1.5 * x, 1.9 * y));
        objects.add(null); // to po to żeby w tym miejscu ryować kółko czyli głowę xD
        objects.add(new MoveTo(1.5 * x, 2 * y));
        objects.add(new LineTo(1.5 * x, 2.5 * y));
        objects.add(new MoveTo(1.5 * x, 2.5 * y));
        objects.add(new LineTo(1.35 * x, 2.6 * y));
        objects.add(new MoveTo(1.5 * x, 2.5 * y));
        objects.add(new LineTo(1.65 * x, 2.6 * y));
        objects.add(new MoveTo(1.5 * x, 2.25 * y));
        objects.add(new LineTo(1.35 * x, 2.15 * y));
        objects.add(new MoveTo(1.5 * x, 2.25 * y));
        objects.add(new LineTo(1.65 * x, 2.15 * y));
        circle = new Circle(1.5 * x, 2 * y, 0);
    }

    public Circle getCircle() {
        return circle;
    }

    public Path getPath() {
        return path;
    }

    public void draw(int number) {
        for (int i = 0; i <= number; i++) {
            if (objects.get(i) != null) {
                this.path.getElements().add(objects.get(i));
                if (objects.get(i) instanceof MoveTo) {
                    ++number;
                }
            } else {
                circle = new Circle(1.5 * x, 2 * y, 15);
            }
        }
    }
}
