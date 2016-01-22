package hr.fer.zemris.game.components.pipes;

import hr.fer.zemris.game.components.IComponent;
import hr.fer.zemris.game.components.bird.Bird;
import hr.fer.zemris.util.RandomProvider;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

import java.util.Random;
import java.util.stream.Stream;

public class PipePair extends Group implements IComponent, Comparable<PipePair> {

    public Rectangle upperBody;
    public Rectangle lowerBody;
    public Rectangle upperHead;
    public Rectangle lowerHead;
    private Random random = RandomProvider.get();

    private double headHight;
    private double height;
    private double gap;
    private double direction;

    private static final double HEAD_X_OFFSET = 5;
    private static final double MINIMUM_Y_OFFSET = 100;

    private static Stop[] stops = new Stop[] { new Stop(0, Color.LIGHTGREEN), new Stop(1, Color.DARKGREEN) };
    private static LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
    private static Color c2 = Color.BLACK;

    public PipePair(double lastPipeX, double gap, double pipeWidth, double height) {
        this.headHight = pipeWidth * 0.75;
    	this.height = height;
        this.gap = gap;
        this.direction = 1;

        this.upperBody = new Rectangle(lastPipeX, 0, pipeWidth, height);

        upperHead = new Rectangle(upperBody.getWidth() + 2 * HEAD_X_OFFSET, headHight);
        upperHead.xProperty().bind(upperBody.xProperty().subtract(HEAD_X_OFFSET));
        upperHead.yProperty().bind(upperBody.yProperty().add(upperBody.getHeight()).subtract(headHight));

        double lowerY = height + gap;

        this.lowerBody = new Rectangle(lastPipeX, lowerY, pipeWidth, height);

        lowerHead = new Rectangle(lowerBody.getWidth() + 2 * HEAD_X_OFFSET, headHight);
        lowerHead.xProperty().bind(lowerBody.xProperty().subtract(HEAD_X_OFFSET));
        lowerHead.yProperty().bind(lowerBody.yProperty());

        lowerHead.setFill(lg1);
        lowerBody.setFill(lg1);
        upperHead.setFill(lg1);
        upperBody.setFill(lg1);
        lowerHead.setStroke(c2);
        lowerHead.setStrokeWidth(2);
        lowerBody.setStroke(c2);
        lowerBody.setStrokeWidth(2);
        upperHead.setStroke(c2);
        upperHead.setStrokeWidth(2);
        upperBody.setStroke(c2);
        upperBody.setStrokeWidth(2);

        //////////////////////////////////////////////////////////////////
        getChildren().addAll(upperBody, lowerBody, upperHead, lowerHead);
        //////////////////////////////////////////////////////////////////

        randomizeYPositions();
    }

    @Override
    public double getRightMostX() {
        return upperBody.getX() + upperBody.getWidth();
    }

    @Override
    public double getLeftMostX() {
        return upperBody.getX();
    }

    public boolean intersects(Bird bird) {
        Bounds birdBounds = bird.getBoundsInParent();

        Bounds upperBodyBounds = upperBody.getBoundsInParent();
        Bounds lowerBodyBounds = lowerBody.getBoundsInParent();

        Bounds upperHeadBounds = upperHead.getBoundsInParent();
        Bounds lowerHeadBounds = lowerHead.getBoundsInParent();

        return 	birdBounds.intersects(upperBodyBounds) ||
        		birdBounds.intersects(lowerBodyBounds) ||
        		birdBounds.intersects(upperHeadBounds) ||
        		birdBounds.intersects(lowerHeadBounds);
    }

    public Stream<Node> getPipes() {
        return Stream.of(upperBody, lowerBody);
    }

    @Override
    public int compareTo(PipePair o) {
    	return Double.compare(this.getLeftMostX(), o.getLeftMostX());
    }

    public void translatePair(double dx) {
        setPairXPosition(upperBody.getX() - dx);
    }

    public void setPairXPosition(double x) {
        upperBody.setX(x);
        lowerBody.setX(x);
    }

    public void randomizeYPositions() {
        double yUpper = random.nextDouble() * (height - gap - MINIMUM_Y_OFFSET - MINIMUM_Y_OFFSET) + MINIMUM_Y_OFFSET - height;
        double yLower = yUpper + height + gap;

        upperBody.setY(yUpper);
        lowerBody.setY(yLower);
    }

    public void setPairYPosition(double y) {
        if (upperBody.getY() + height + direction * y < MINIMUM_Y_OFFSET
                || lowerBody.getY() + direction * y > height - MINIMUM_Y_OFFSET) {
            direction *= -1;
        }

        upperBody.setY(upperBody.getY() + direction * y);
        lowerBody.setY(lowerBody.getY() + direction * y);
    }

    public double getDirection() {
        return direction;
    }

    @Override
    public void translate(double dx) {
        translatePair(dx);
    }

}
