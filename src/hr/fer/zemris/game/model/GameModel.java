package hr.fer.zemris.game.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hr.fer.zemris.game.components.IComponent;
import hr.fer.zemris.game.components.bird.Bird;
import hr.fer.zemris.game.components.pipes.PipePair;
import hr.fer.zemris.game.components.reward.Reward;
import hr.fer.zemris.game.environment.Constants;
import hr.fer.zemris.game.environment.EnvironmentVariables;
import hr.fer.zemris.game.environment.IEnvironmentListener;
import hr.fer.zemris.game.environment.IEnvironmentProvider;
import hr.fer.zemris.game.physics.Physics;
import hr.fer.zemris.util.RandomProvider;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GameModel implements IEnvironmentProvider {

    /**
     * Debug varijabla da ne moram dolje uvijek zakomentirati na checkCollisions(). Kad je {@code false} igra se nebude
     * zaustavila.
     */
    private static final boolean PAUSE_GAME = true;

    private Dimension2D dimension = new Dimension2D(1000, 600);

    public static Random random = RandomProvider.get();

    protected Bird bird;

    protected BooleanProperty jump;
    
    private Constants constants;
    
    
    protected LinkedList<PipePair> pipesPairs = new LinkedList<>();

    private PipePair lastPassed;

    protected LinkedList<Reward> rewards = new LinkedList<>();

    private Group group = new Group();

    private int pipeCounter = 0;

    private List<IEnvironmentListener> listeners = new ArrayList<>();

    public GameModel() {
        initModel();
    }

    private void initModel() {
    	constants = new Constants();
        this.bird = new Bird(dimension.getWidth() / 3, dimension.getHeight() / 2);
        initialiseEnvironment();
        jump = new SimpleBooleanProperty(false);
        lastPassed = getNearestPairAheadOfBird().get();
        
    }

    public Group getGroup() {
        group.getChildren().add(bird);
        group.getChildren().addAll(pipesPairs);
        group.getChildren().addAll(rewards);

        return group;
    }

    public void reset() {
        group.getChildren().clear();
        pipesPairs.clear();
        rewards.clear();

        initModel();
    }

    public void jumpBird() {
        jump.set(true);
    }

    private void initialiseEnvironment() {
        double nextPipeX = dimension.getWidth() + constants.INITIAL_PIPE_OFFSET;
        double nextRewardCenterX = nextPipeX + constants.PIPE_WIDTH + constants.PIPE_GAP_X / 2;
        for (int i = 0; i < constants.NUMBER_OF_PIPES; i++) {
            nextPipeX = initialisePipePair(nextPipeX);
            nextRewardCenterX = initialiseReward(nextRewardCenterX);
        }
    }

    private abstract class AbstractInitiaiser<T extends IComponent> {

        private List<T> components;

        public AbstractInitiaiser(List<T> components) {
            super();
            this.components = components;
        }

        public final double initialiseComponent(double nextComponentX) {

            T c = createComponent(nextComponentX);
            nextComponentX = calculateOffset(c);
            components.add(c);
            return nextComponentX;
        }

        protected abstract T createComponent(double nextComponentX);

        protected abstract double calculateOffset(T component);

    }

    private double initialisePipePair(double nextPipeX) {
        return new AbstractInitiaiser<PipePair>(pipesPairs) {

            @Override
            protected PipePair createComponent(double nextComponentX) {
                return new PipePair(nextPipeX, constants.PIPE_GAP_Y, constants.PIPE_WIDTH, dimension.getHeight());
            }

            @Override
            protected double calculateOffset(PipePair component) {
                return component.getRightMostX() + constants.PIPE_GAP_X;
            }

        }.initialiseComponent(nextPipeX);
    }

    private double initialiseReward(double nextRewardCenterX) {
        return new AbstractInitiaiser<Reward>(rewards) {

            @Override
            protected Reward createComponent(double nextComponentX) {
                return new Reward(nextRewardCenterX, dimension.getHeight());
            }

            @Override
            protected double calculateOffset(Reward component) {
                return component.getCenterX() + constants.REWARD_GAP_X;
            }

        }.initialiseComponent(nextRewardCenterX);
    }

    private abstract class AbstractMover<T extends IComponent> {
        private LinkedList<T> components;

        public AbstractMover(LinkedList<T> components) {
            super();
            this.components = components;
        }

        public final void move(int time) {
            components.forEach(this::translate);

            T first = components.peekFirst();
            if (first.getRightMostX() < 0) {
                T last = components.peekLast();
                putFirstBehindLast(first, last);
                components.addLast(components.removeFirst());
            }
        }

        protected abstract void translate(T component);

        protected abstract void putFirstBehindLast(T first, T last);

    }

    private void movePipes(int time) {
        new AbstractMover<PipePair>(pipesPairs) {

            @Override
            protected void translate(PipePair component) {
                double shiftX = Physics.calculateShiftX(constants.PIPES_SPEED_X, time);
                component.translatePair(shiftX);
                double shiftY = Physics.calculateShiftX(constants.PIPES_SPEED_Y, time);
                component.setPairYPosition(shiftY);
            }

            @Override
            protected void putFirstBehindLast(PipePair first, PipePair last) {
                first.setPairXPosition(last.getRightMostX() + constants.PIPE_GAP_X);
                first.randomizeYPositions();
            }

        }.move(time);
    }

    private void moveRewards(int time) {
        new AbstractMover<Reward>(rewards) {

            @Override
            protected void translate(Reward component) {
                double shiftX = Physics.calculateShiftX(constants.REWARD_SPEED_X, time);
                component.translateReward(shiftX);
                component.updateFrame();
            }

            @Override
            protected void putFirstBehindLast(Reward first, Reward last) {
                first.setCenterX(last.getCenterX() + constants.REWARD_GAP_X);
                first.randomizeYPosition();
                first.setVisible(random.nextDouble() < constants.REWARD_PROBABILITY);
            }

        }.move(time);
    }

    private void moveBird(int time) {
        if (jump.get()) {
            double shiftY = Physics.calculateShiftY(constants.JUMP_SPEED, time);
            bird.setCurrentVelocity(constants.JUMP_SPEED);
            bird.setCenterY(bird.getCenterY() + shiftY);
            jump.set(false);
        } else {
            double shiftY = Physics.calculateShiftY(bird.getCurrentVelocity(), time);
            bird.setCurrentVelocity(Physics.calculateVelocity(bird.getCurrentVelocity(), time));
            bird.setCenterY(bird.getCenterY() + shiftY);
        }
        bird.updateFrame();
 
    }

    public boolean update(int time) {
        if (checkCollisions() && PAUSE_GAME) {
            return false;
        }

        if (isRewardCollected()) {
            pipeCounter += constants.REWARD_COLLECTED_BONUS;
        }

        movePipes(time);
        moveRewards(time);
        moveBird(time);

        PipePair nearestPipePair = getNearestPairAheadOfBird().get();

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

        if (!nearestPipePair.equals(lastPassed)) {
            pipeCounter += constants.PIPE_PASSED_BONUS;
            lastPassed = nearestPipePair;
        }
        double angle =0;
        if(Double.compare(distanceToReward, 0)!=0){
        	angle=Math.atan(relativeHeightToReward/distanceToReward);
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

        return true;
    }

    private boolean isRewardCollected() {
        return rewards.stream()
        		.filter(r -> r.intersects(bird))
        		.peek(r -> r.setVisible(false))
        		.findAny()
        		.isPresent();
    }

    public int getCurrentScore() {
        return pipeCounter;
    }

    private boolean checkCollisions() {
        boolean intersection = pipesPairs
        		.stream()
        		.filter(p -> p.intersects(bird))
        		.findAny()
        		.isPresent();
        return intersection || isBirdOutOfBounds();
    }

    private boolean isBirdOutOfBounds() {
        Bounds birdBounds = bird.getBoundsInParent();
        return birdBounds.getMaxY() > dimension.getHeight() || birdBounds.getMinY() < 0;
    }

    /**
     * Vraca dvije cijevi(par).<br>
     * Prva(index = 0) je ona gore, druga(index = 1) je ona dolje.
     *
     * @param
     * @return
     */
    // TO BUDEMO KORISTILI ZA GLEDANJE DI JE KOJA CIJEV KOD UČENJA MREZE
//    private Optional<PipePair> getNearestPairAheadOfBird() {
//        return getNearestComponentAheadOfBird(pipesPairs)
//        		.findFirst();
//    }
    private Optional<PipePair> getNearestPairAheadOfBird() {
    	return pipesPairs.stream()
    			.filter(p -> p.getRightMostX() > bird.getLeftMostX())
        		.sorted()
        		.findFirst();
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
    
    public Constants getConstants(){
    	return constants;
    }
    
    public void setConstants(Constants constants){
    	this.constants = constants;
    }
    


}
