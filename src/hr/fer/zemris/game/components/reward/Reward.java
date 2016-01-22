package hr.fer.zemris.game.components.reward;

import hr.fer.zemris.game.components.IComponent;
import hr.fer.zemris.game.components.bird.Bird;
import hr.fer.zemris.util.RandomProvider;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.LinkedList;
import java.util.Random;
import java.util.stream.IntStream;

public class Reward extends Circle implements IComponent, Comparable<Reward> {

	private double height;

	private static Random random = RandomProvider.get();

	private static final double REWARD_RADIUS = 20;
	private static final double MINIMUM_Y_OFFSET = 50;
	private LinkedList<Image> rewardFrames = new LinkedList<>();

	public Reward(double rewardCenterX, double height) {
		this.height = height;
		
		setCenterX(rewardCenterX);
		setRadius(REWARD_RADIUS);
		//setFill(Color.CORAL);
		setVisible(false);
		
		randomizeYPosition();
		loadRewardFrames();
		updateFrame();
	}

	public void translateReward(double dx) {
		setCenterX(getCenterX() - dx);
	}

	@Override
	public double getRightMostX() {
		return getCenterX() + getRadius();
	}

	@Override
	public double getLeftMostX() {
		return getCenterX() - getRadius();
	}

	public boolean intersects(Bird bird) {
		Bounds birdBounds = bird.getBoundsInParent();
		Bounds rewardBounds = getBoundsInParent();
		return birdBounds.intersects(rewardBounds) && isVisible();
	}

	@Override
	public int compareTo(Reward o) {
		return Double.compare(this.getCenterX(), o.getCenterX());
	}

	public void randomizeYPosition() {
		double centerY = random.nextDouble() *
				(height - 2 * MINIMUM_Y_OFFSET - 2 * REWARD_RADIUS) +
				MINIMUM_Y_OFFSET + REWARD_RADIUS;
		setCenterY(centerY);
	}

	//TODO
	private void loadRewardFrames() {
        IntStream.range(0, 5).forEach(i -> {
            rewardFrames.add(new Image(getClass().getResourceAsStream("coinFrame" + i + ".png")));
        });
    }

	 public void updateFrame() {
	        Image frame = rewardFrames.removeFirst();
	        setFill(new ImagePattern(frame));
	        rewardFrames.add(frame);
	    }

	@Override
	public void translate(double dx) {
		translateReward(dx);
	}

}
