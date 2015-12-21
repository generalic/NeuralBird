package hr.fer.zemris.game.components.bird;

import java.util.LinkedList;
import java.util.stream.IntStream;

import hr.fer.zemris.game.components.IComponent;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Bird extends Circle implements IComponent {

    private static final double BIRD_RADIUS = 20;

    private double currentVelocity;
    private LinkedList<Image> birdFrames = new LinkedList<>();

    public Bird(double centerX, double centerY) {
        super(centerX, centerY, BIRD_RADIUS);
        this.currentVelocity = 0;
        loadBirdFrames();
        updateFrame();
    }

    public double getCurrentVelocity() {

        return currentVelocity;
    }

    public void setCurrentVelocity(double velocity) {
        this.currentVelocity = velocity;
    }

    public void updateFrame() {
        Image frame = birdFrames.removeFirst();
        setFill(new ImagePattern(frame));
        birdFrames.add(frame);
    }

    private void loadBirdFrames() {
        IntStream.range(0, 3).forEach(i -> {
            birdFrames.add(new Image(getClass().getResourceAsStream("birdFrameHoliday" + i + ".png")));
        });
    }

	@Override
	public void translate(double dx) {
	}

	@Override
	public double getRightMostX() {
		return getCenterX() + getRadius();
	}

	@Override
	public double getLeftMostX() {
		return getCenterX() - getRadius();
	}

}
