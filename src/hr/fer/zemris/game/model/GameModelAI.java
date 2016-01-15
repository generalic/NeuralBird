package hr.fer.zemris.game.model;

import hr.fer.zemris.game.components.IComponent;
import hr.fer.zemris.game.components.pipes.PipePair;
import hr.fer.zemris.game.components.reward.Reward;
import hr.fer.zemris.game.environment.EnvironmentVariables;
import hr.fer.zemris.game.environment.IEnvironmentListener;
import hr.fer.zemris.game.environment.IEnvironmentProvider;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GameModelAI extends GameModel implements IEnvironmentProvider {

	private List<IEnvironmentListener> listeners = new ArrayList<>();

    @Override
    protected void scanEnvironment() {
        List<Double> distances = traceTubes(nearestPipePair);
        double birdHeight = gameDimension.getHeight() - bird.getCenterY();

        Optional<Reward> nearestReward = getNearestRewardAheadOfBird(nearestPipePair);

        double distanceToReward = 0;
        double relativeHeightToReward = 0;
        if (nearestReward.isPresent()) {
            distanceToReward = traceReward(nearestReward.get()).get(0);
            relativeHeightToReward = nearestReward.get().getCenterY() - bird.getCenterY();
        } else {
            group.getChildren().removeAll(rewardTraceLines);
        }

        distances.add(distanceToReward);

        double angle = 0;
        if(Double.compare(distanceToReward, 0) != 0) {
            angle=Math.atan(relativeHeightToReward / distanceToReward);
        }

        EnvironmentVariables variables = new EnvironmentVariables(
			distances.get(0),
			distances.get(1),
			distances.get(2),
			distances.get(3),
			birdHeight,
			nearestPipePair.getDirection(),
			distanceToReward,
			distanceToReward != 0 ? 1 : -1,
			angle
        );

        listeners.forEach(l -> l.environmentChanged(this, variables));
    }

    private Optional<Reward> getNearestRewardAheadOfBird(IComponent nearestPipePair) {
        return getNearestComponentAheadOfBird(rewards)
        		.filter(IComponent::isVisible)
                .filter(p -> p.getLeftMostX() <= nearestPipePair.getLeftMostX())
                .findFirst();
    }

    private <T extends IComponent> Stream<T> getNearestComponentAheadOfBird(List<T> components) {
        return components.stream()
        		.filter(p -> p.getLeftMostX() > bird.getBoundsInParent().getMaxX())
        		.sorted();
    }


	private abstract class AbstractTracer {

		protected List<Line> lines;
		protected List<Point2D> points = new ArrayList<>();
		protected List<Double> distances = new ArrayList<>();

		public AbstractTracer(List<Line> lines) {
			this.lines = lines;
		}

		public final List<Double> trace() {
			setPoints();

			if (!lines.isEmpty()) {
				group.getChildren().removeAll(lines);
				lines.clear();
			}

			setDistances();

			group.getChildren().addAll(lines);

			return distances;
		}

		protected abstract void setPoints();

		protected abstract void setDistances();

	}

    List<Line> pipeTraceLines = new ArrayList<>();

    private List<Double> traceTubes(PipePair pair) {
		return new AbstractTracer(pipeTraceLines) {

			@Override
			protected void setPoints() {
				Bounds upperTubeBounds = pair.upperHead.getBoundsInParent();
				Bounds lowerTubeBounds = pair.lowerHead.getBoundsInParent();

				points.add(new Point2D(bird.getCenterX(), bird.getCenterY()));

				points.add(new Point2D(lowerTubeBounds.getMinX(), lowerTubeBounds.getMinY()));
				points.add(new Point2D(upperTubeBounds.getMinX(), upperTubeBounds.getMaxY()));

				points.add(new Point2D(lowerTubeBounds.getMaxX(), lowerTubeBounds.getMinY()));
				points.add(new Point2D(upperTubeBounds.getMaxX(), upperTubeBounds.getMaxY()));
			}

			@Override
			protected void setDistances() {
				distances.add(getDistanceBetweenPoints(points.get(0), points.get(1), lines));
				distances.add(getDistanceBetweenPoints(points.get(0), points.get(2), lines));
				distances.add(getDistanceBetweenPoints(points.get(0), points.get(3), lines));
				distances.add(getDistanceBetweenPoints(points.get(0), points.get(4), lines));
			}

		}.trace();
    }

    private List<Line> rewardTraceLines = new ArrayList<>();

    private List<Double> traceReward(Reward reward) {
		return new AbstractTracer(rewardTraceLines) {

			@Override
			protected void setPoints() {
				points.add(new Point2D(bird.getCenterX(), bird.getCenterY()));
				points.add(new Point2D(reward.getCenterX(), reward.getCenterY()));
			}

			@Override
			protected void setDistances() {
				Point2D p1 = points.get(0);
				Point2D p2 = points.get(1);
				distances.add(p2.getX() - p1.getX());

				getDistanceBetweenPoints(points.get(0), points.get(1), lines);
			}

		}.trace();
    }

	private double getDistanceBetweenPoints(Point2D p1, Point2D p2, Collection<Line> lines) {
		Line traceLine = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		traceLine.setStrokeWidth(3);
		traceLine.setStroke(Color.RED);
		lines.add(traceLine);
		traceLine.visibleProperty().bind(traceable);

		double dx1 = p2.getX() - p1.getX();
		double dy1 = p2.getY() - p1.getY();
		return Math.sqrt(dx1 * dx1 + dy1 * dy1);
	}

    @Override
    public void addEnvironmentListener(IEnvironmentListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeEnvironmentListener(IEnvironmentListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void react() {
        jumpBird();
    }

}
