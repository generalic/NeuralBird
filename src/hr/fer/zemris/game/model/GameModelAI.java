package hr.fer.zemris.game.model;

import hr.fer.zemris.game.components.IComponent;
import hr.fer.zemris.game.components.pipes.PipePair;
import hr.fer.zemris.game.components.reward.Reward;
import hr.fer.zemris.game.environment.Constants;
import hr.fer.zemris.game.environment.EnvironmentVariables;
import hr.fer.zemris.game.environment.IEnvironmentListener;
import hr.fer.zemris.game.environment.IEnvironmentProvider;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameModelAI extends GameModel implements IEnvironmentProvider {

    private List<IEnvironmentListener> listeners = new ArrayList<>();

    @Override
    protected Constants provideConstants() {
        return Constants.PlayerConstants;
    }

    @Override
    protected void scanEnvironment() {
        List<Double> distances = traceTubes(nearestPipePair);
        double birdHeight = gameDimension.getHeight() - bird.getCenterY();

        Optional<Reward> nearestReward = getNearestRewardAheadOfBird(nearestPipePair);

        double distanceToReward = 0;
        double relativeHeightToReward = 0;
        if (nearestReward.isPresent()) {
            distanceToReward = traceReward(nearestReward.get());
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

//       System.out.println(distances.get(0));
//       System.out.println(distances.get(1));
//       System.out.println(birdHeight);
//       System.out.println(nearestPipePair.getDirection());
//       System.out.println(distanceToReward);
//       System.out.println(distanceToReward != 0 ? 1 : -1);
//       System.out.println(angle);
//       System.out.println("-------------");

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

    List<Line> pipeTraceLines = new ArrayList<>();

    private List<Double> traceTubes(PipePair pair) {
        Bounds upperTubeBounds = pair.upperHead.getBoundsInParent();
        Bounds lowerTubeBounds = pair.lowerHead.getBoundsInParent();

        Point2D p1 = new Point2D(bird.getCenterX(), bird.getCenterY());

        Point2D p2 = new Point2D(upperTubeBounds.getMinX(), upperTubeBounds.getMaxY());
        Point2D p3 = new Point2D(lowerTubeBounds.getMinX(), lowerTubeBounds.getMinY());

        Point2D p4 = new Point2D(upperTubeBounds.getMaxX(), upperTubeBounds.getMaxY());
        Point2D p5 = new Point2D(lowerTubeBounds.getMaxX(), lowerTubeBounds.getMinY());

		if (!pipeTraceLines.isEmpty()) {
			group.getChildren().removeAll(pipeTraceLines);
			pipeTraceLines.clear();
		}

        double distanceToUpperLeftSide = getDistanceBetweenPoints(p1, p2);
        double distanceToLowerLeftSide = getDistanceBetweenPoints(p1, p3);
        double distanceToUpperRightSide = getDistanceBetweenPoints(p1, p4);
        double distanceToLowerRightSide = getDistanceBetweenPoints(p1, p5);

        group.getChildren().addAll(pipeTraceLines);

        return Stream.of(
        		distanceToLowerLeftSide, distanceToUpperLeftSide,
        		distanceToLowerRightSide, distanceToUpperRightSide
        		).collect(Collectors.toList());
    }

	private void addTraceLine(Point2D p1, Point2D p2) {
		Line traceLine = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		traceLine.setStrokeWidth(3);
		traceLine.setStroke(Color.RED);
		pipeTraceLines.add(traceLine);
	}

	private double getDistanceBetweenPoints(Point2D p1, Point2D p2) {
        addTraceLine(p1, p2);
		double dx1 = p2.getX() - p1.getX();
        double dy1 = p2.getY() - p1.getY();
        return Math.sqrt(dx1 * dx1 + dy1 * dy1);
    }

    private List<Line> rewardTraceLines = new ArrayList<>();

    private double traceReward(Reward reward) {
        Point2D p1 = new Point2D(bird.getCenterX(), bird.getCenterY());
        Point2D p2 = new Point2D(reward.getCenterX(), reward.getCenterY());

        double dx = p2.getX() - p1.getX();
        if (!rewardTraceLines.isEmpty()) {
            group.getChildren().removeAll(rewardTraceLines);
			rewardTraceLines.clear();
        }
        Line line = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        line.setStrokeWidth(3);
        line.setStroke(Color.AQUAMARINE);
		rewardTraceLines.add(line);

        group.getChildren().addAll(rewardTraceLines);
        return dx;

//        double dy = p2.getY() - p1.getY();
//
//        double distanceToReward = Math.sqrt(dx * dx + dy * dy);
//
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
