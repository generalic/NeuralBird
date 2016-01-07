package hr.fer.zemris.game.model;

import hr.fer.zemris.game.components.IComponent;
import hr.fer.zemris.game.components.bird.Bird;
import hr.fer.zemris.game.components.pipes.PipePair;
import hr.fer.zemris.game.components.reward.Reward;
import hr.fer.zemris.game.environment.Constants;
import hr.fer.zemris.game.environment.EnvironmentVariables;
import hr.fer.zemris.game.environment.IEnvironmentListener;
import hr.fer.zemris.game.environment.IEnvironmentProvider;
import javafx.beans.property.SimpleBooleanProperty;
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

	private int numOfPipesPassed=0;

	public GameModelAI() {
	  	//setConstantSettings();
    	constants = Constants.AIConst;
    	this.bird = new Bird(dimension.getWidth() / 3, dimension.getHeight() / 2);
        initialiseEnvironment();
        jump = new SimpleBooleanProperty(false);
        lastPassed = getNearestPairAheadOfBird().get();
	}
	
	public int getNumOfPipesPassed(){
		return numOfPipesPassed;
	}
	
    private List<IEnvironmentListener> listeners = new ArrayList<>();

    @Override
    protected void scanEnvironment() {

        PipePair nearestPipePair = getNearestPairAheadOfBird().get();
		if (!nearestPipePair.equals(lastPassed)) {
			score.set(score.get() + constants.PIPE_PASSED_BONUS);
			numOfPipesPassed++;
			lastPassed = nearestPipePair;
		}

        List<Double> distances = traceTubes(nearestPipePair);
        double birdHeight = dimension.getHeight() - bird.getCenterY();

        Optional<Reward> nearestReward = getNearestRewardAheadOfBird(nearestPipePair);

        double distanceToReward = 0;
        double relativeHeightToReward = 0;
        if (nearestReward.isPresent()) {
            distanceToReward = traceReward(nearestReward.get());
            relativeHeightToReward = nearestReward.get().getCenterY()-bird.getCenterY();
        } else {
            group.getChildren().removeAll(rewardTracers);
        }

        distances.add(distanceToReward);

        double angle = 0;
        if(Double.compare(distanceToReward, 0) != 0){
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
        		.filter(p -> p.getLeftMostX() - bird.getBoundsInParent().getMaxX() > 0)
        		.sorted();
    }

    List<Line> pipeTracers = new ArrayList<>();

    private List<Double> traceTubes(PipePair pair) {
        Bounds upperTubeBounds = pair.upperHead.getBoundsInParent();
        Bounds lowerTubeBounds = pair.lowerHead.getBoundsInParent();

        // System.out.println(pair.upperHead.getY() + pair.upperHead.getHeight());
        // System.out.println(upperTubeBounds.getMaxY());
        // System.out.println();

        Point2D p1 = new Point2D(bird.getCenterX(), bird.getCenterY());

        Point2D p2 = new Point2D(upperTubeBounds.getMinX(), upperTubeBounds.getMaxY());
        Point2D p3 = new Point2D(lowerTubeBounds.getMinX(), lowerTubeBounds.getMinY());

        Point2D p4 = new Point2D(upperTubeBounds.getMaxX(), upperTubeBounds.getMaxY());
        Point2D p5 = new Point2D(lowerTubeBounds.getMaxX(), lowerTubeBounds.getMinY());

        double dx1 = p2.getX() - p1.getX();
        double dy1 = p2.getY() - p1.getY();

        double distanceToUpperLeftSide = Math.sqrt(dx1 * dx1 + dy1 * dy1);

        double dx2 = p3.getX() - p1.getX();
        double dy2 = p3.getY() - p1.getY();

        double distanceToLowerLeftSide = Math.sqrt(dx2 * dx2 + dy2 * dy2);

        double dx3 = p4.getX() - p1.getX();
        double dy3 = p4.getY() - p1.getY();

        double distanceToUpperRightSide = Math.sqrt(dx3 * dx3 + dy3 * dy3);

        double dx4 = p5.getX() - p1.getX();
        double dy4 = p5.getY() - p1.getY();

        double distanceToLowerRightSide = Math.sqrt(dx4 * dx4 + dy4 * dy4);

        if (!pipeTracers.isEmpty()) {
            group.getChildren().removeAll(pipeTracers);
            pipeTracers.clear();
        }

        Line lineLowerLeftSide = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        lineLowerLeftSide.setStrokeWidth(3);
        lineLowerLeftSide.setStroke(Color.RED);
        pipeTracers.add(lineLowerLeftSide);

        Line lineUpperLeftSide = new Line(p1.getX(), p1.getY(), p3.getX(), p3.getY());
        lineUpperLeftSide.setStrokeWidth(3);
        lineUpperLeftSide.setStroke(Color.RED);
        pipeTracers.add(lineUpperLeftSide);

        Line lineLowerRightSide = new Line(p1.getX(), p1.getY(), p4.getX(), p4.getY());
        lineLowerRightSide.setStrokeWidth(3);
        lineLowerRightSide.setStroke(Color.DEEPPINK);
        pipeTracers.add(lineLowerRightSide);

        Line lineUpperRightSide = new Line(p1.getX(), p1.getY(), p5.getX(), p5.getY());
        lineUpperRightSide.setStrokeWidth(3);
        lineUpperRightSide.setStroke(Color.DEEPPINK);
        pipeTracers.add(lineUpperRightSide);

        group.getChildren().addAll(pipeTracers);

        return Stream.of(
        		distanceToLowerLeftSide, distanceToUpperLeftSide,
        		distanceToLowerRightSide, distanceToUpperRightSide
        		).collect(Collectors.toList());
    }

    private List<Line> rewardTracers = new ArrayList<>();

    private double traceReward(Reward reward) {
        Point2D p1 = new Point2D(bird.getCenterX(), bird.getCenterY());
        Point2D p2 = new Point2D(reward.getCenterX(), reward.getCenterY());

        double dx = p2.getX() - p1.getX();
        if (!rewardTracers.isEmpty()) {
            group.getChildren().removeAll(rewardTracers);
            rewardTracers.clear();
        }
        Line line = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        line.setStrokeWidth(3);
        line.setStroke(Color.AQUAMARINE);
        rewardTracers.add(line);

        group.getChildren().addAll(rewardTracers);
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
