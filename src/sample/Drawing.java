package sample;

import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.List;

public class Drawing {
    private double x = 125;
    private double y = 125;
    private double xMove;
    private double yMove;
    private List<PathElement> objects = new ArrayList<>();
    private Path path = new Path();
    private Circle circle;

    public Drawing(double xMove, double yMove) {
        this.xMove = xMove;
        this.yMove = yMove;
        objects.add(new MoveTo(x + xMove, 3 * y + yMove));
        objects.add(new LineTo(x + xMove, 1.5 * y + yMove));
        objects.add(new LineTo(2 * x + xMove, 1.5 * y + yMove));
        objects.add(new LineTo(2 * x + xMove, 3 * y + yMove));
        objects.add(new MoveTo(1.5 * x + xMove, 1.5 * y + yMove));
        objects.add(new LineTo(1.5 * x + xMove, 1.9 * y + yMove));
        objects.add(null); // to po to żeby w tym miejscu ryować kółko czyli głowę xD
        objects.add(new MoveTo(1.5 * x + xMove, 2 * y + yMove));
        objects.add(new LineTo(1.5 * x + xMove, 2.5 * y + yMove));
        objects.add(new MoveTo(1.5 * x + xMove, 2.5 * y + yMove));
        objects.add(new LineTo(1.35 * x + xMove, 2.6 * y + yMove));
        objects.add(new MoveTo(1.5 * x + xMove, 2.5 * y + yMove));
        objects.add(new LineTo(1.65 * x + xMove, 2.6 * y + yMove));
        objects.add(new MoveTo(1.5 * x + xMove, 2.25 * y + yMove));
        objects.add(new LineTo(1.35 * x + xMove, 2.15 * y + yMove));
        objects.add(new MoveTo(1.5 * x + xMove, 2.25 * y + yMove));
        objects.add(new LineTo(1.65 * x + xMove, 2.15 * y + yMove));
        circle = new Circle(1.5 * x + xMove, 2 * y + yMove, 0);
    }

    public Circle getCircle() {
        return circle;
    }

    public Path getPath() {
        return path;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void draw(int number) {
        for (int i = 0; i <= number; i++) {
            if (objects.get(i) != null) {
                this.path.getElements().add(objects.get(i));
                if (objects.get(i) instanceof MoveTo) {
                    ++number;
                }
            } else {
                circle = new Circle(1.5 * x + xMove, 2 * y + yMove, 15);
            }
        }
    }
}
