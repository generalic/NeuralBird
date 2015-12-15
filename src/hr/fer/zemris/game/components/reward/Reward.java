package hr.fer.zemris.game.components.reward;

import java.util.Random;
import java.util.stream.Stream;

import hr.fer.zemris.game.components.IComponent;
import hr.fer.zemris.game.components.bird.Bird;
import hr.fer.zemris.util.RandomProvider;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Reward extends Circle implements IComponent, Comparable<Reward> {

	private double height;

	private static Random random = RandomProvider.get();

	private static final double REWARD_RADIUS = 20;
	private static final double MINIMUM_Y_OFFSET = 50;

	public Reward(double rewardCenterX, double height) {
		this.height = height;
		setCenterX(rewardCenterX);
		setRadius(REWARD_RADIUS);
		setFill(Color.CORAL);
		setVisible(false);

		randomizeYPosition();
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

	public Stream<Node> getPipes() {
		return Stream.of(this);
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

	@Override
	public void translate(double dx) {
		translateReward(dx);
	}

}
