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
import hr.fer.zemris.game.environment.EnvironmentVariables;
import hr.fer.zemris.game.environment.IEnvironmentListener;
import hr.fer.zemris.game.environment.IEnvironmentProvider;
import hr.fer.zemris.game.physics.Physics;
import hr.fer.zemris.util.RandomProvider;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class GameModel implements IEnvironmentProvider {

    /**
     * Debug varijabla da ne moram dolje uvijek zakomentirati na checkCollisions(). Kad je {@code false} igra se nebude
     * zaustavila.
     */
    private static final boolean PAUSE_GAME = true;
	
    private static final int NUMBER_OF_PIPES = 5;
    private static final double PIPES_SPEED_X = 10;
    private static final double REWARD_SPEED_X = PIPES_SPEED_X;
    private static final double PIPES_SPEED_Y = 5;
    private static final double JUMP_SPEED = -25;
    private static final double PIPE_GAP_X = 350;
    private static final double PIPE_GAP_Y = 210;
    private static final double PIPE_WIDTH = 60;
    private static final double INITIAL_PIPE_OFFSET = 100;
    private static final double REWARD_GAP_X = PIPE_GAP_X + PIPE_WIDTH;
    private static final int PIPE_PASSED_BONUS = 1;
    private static final int REWARD_COLLECTED_BONUS = 5;
    private static final double REWARD_PROBABILITY = 0.5;

    private Dimension2D dimension = new Dimension2D(1000, 600);

    public static Random random = RandomProvider.get();

    private Bird bird;

    private boolean jump;
    
    private LinkedList<PipePair> pipesPairs = new LinkedList<>();

    private PipePair lastPassed;

    private LinkedList<Reward> rewards = new LinkedList<>();

    private Group group = new Group();

    private int pipeCounter = 0;

    private List<IEnvironmentListener> listeners = new ArrayList<>();

    public GameModel() {
        this.bird = new Bird(dimension.getWidth() / 3, dimension.getHeight() / 4);
        initialiseEnvironment();
        jump = false;
        lastPassed = getNearestPairAheadOfBird().get();
    }

    public Scene getScene() {
        group.getChildren().add(bird);
        group.getChildren().addAll(pipesPairs);
        group.getChildren().addAll(rewards);
        Pane root = new Pane();

        root.getChildren().addAll(group);
        root.setStyle("-fx-background-color: mediumslateblue");

        return new Scene(root, dimension.getWidth(), dimension.getHeight());
    }
    
    public void fillGroup(Group scene) {
        group.getChildren().add(bird);
        group.getChildren().addAll(pipesPairs);
        group.getChildren().addAll(rewards);


        Pane root = new Pane();
        
        root.getChildren().addAll(group);
        root.setStyle("-fx-background-color: mediumslateblue");
        scene.getChildren().add(root);
    }

    public void jumpBird() {
        jump = true;
    }

    private void initialiseEnvironment() {
        double nextPipeX = dimension.getWidth() + INITIAL_PIPE_OFFSET;
        double nextRewardCenterX = nextPipeX + PIPE_WIDTH + PIPE_GAP_X / 2;
        for (int i = 0; i < NUMBER_OF_PIPES; i++) {
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
                return new PipePair(nextPipeX, PIPE_GAP_Y, PIPE_WIDTH, dimension.getHeight());
            }

            @Override
            protected double calculateOffset(PipePair component) {
                return component.getRightMostX() + PIPE_GAP_X;
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
                return component.getCenterX() + REWARD_GAP_X;
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
                double shiftX = Physics.calculateShiftX(PIPES_SPEED_X, time);
                component.translatePair(shiftX);
                double shiftY = Physics.calculateShiftX(PIPES_SPEED_Y, time);
                component.setPairYPosition(shiftY);
            }

            @Override
            protected void putFirstBehindLast(PipePair first, PipePair last) {
                first.setPairXPosition(last.getRightMostX() + PIPE_GAP_X);
                first.randomizeYPositions();
            }

        }.move(time);
    }

    private void moveRewards(int time) {
        new AbstractMover<Reward>(rewards) {

            @Override
            protected void translate(Reward component) {
                double shiftX = Physics.calculateShiftX(REWARD_SPEED_X, time);
                component.translateReward(shiftX);
            }

            @Override
            protected void putFirstBehindLast(Reward first, Reward last) {
                first.setCenterX(last.getCenterX() + REWARD_GAP_X);
                first.randomizeYPosition();
                first.setVisible(random.nextDouble() < REWARD_PROBABILITY);
            }

        }.move(time);
    }

    private void moveBird(int time) {
        if (jump) {
            double shiftY = Physics.calculateShiftY(JUMP_SPEED, time);
            bird.setCurrentVelocity(JUMP_SPEED);
            bird.setCenterY(bird.getCenterY() + shiftY);
            jump = false;
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
            pipeCounter += REWARD_COLLECTED_BONUS;
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
            pipeCounter += PIPE_PASSED_BONUS;
            lastPassed = nearestPipePair;
        }
        double angle =0;
        if(Double.compare(distanceToReward, 0)!=0){
        	angle=Math.atan(relativeHeightToReward/distanceToReward);
        }
        
        EnvironmentVariables variables = new EnvironmentVariables(
        		distances.get(0),
        		distances.get(1),
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
     * @param entities
     * @return
     */
    // TO BUDEMO KORISTILI ZA GLEDANJE DI JE KOJA CIJEV KOD UČENJA MREZE
    private Optional<PipePair> getNearestPairAheadOfBird() {
        return getNearestComponentAheadOfBird(pipesPairs)
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

        double dx1 = p2.getX() - p1.getX();
        double dy1 = p2.getY() - p1.getY();

        double distanceToUpper = Math.sqrt(dx1 * dx1 + dy1 * dy1);

        double dx2 = p3.getX() - p1.getX();
        double dy2 = p3.getY() - p1.getY();

        double distanceToLower = Math.sqrt(dx2 * dx2 + dy2 * dy2);

        if (!pipeTracers.isEmpty()) {
            group.getChildren().removeAll(pipeTracers);
            pipeTracers.clear();
        }

        Line lineLower = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        lineLower.setStrokeWidth(3);
        lineLower.setStroke(Color.RED);
        pipeTracers.add(lineLower);

        Line lineUpper = new Line(p1.getX(), p1.getY(), p3.getX(), p3.getY());
        lineUpper.setStrokeWidth(3);
        lineUpper.setStroke(Color.RED);
        pipeTracers.add(lineUpper);

        group.getChildren().addAll(pipeTracers);

        return Stream.of(distanceToLower, distanceToUpper).collect(Collectors.toList());
    }

    private List<Line> rewardTracers = new ArrayList<>();

    private double traceReward(Reward reward) {
        Point2D p1 = new Point2D(bird.getCenterX(), bird.getCenterY());
        Point2D p2 = new Point2D(reward.getCenterX(), reward.getCenterY());

        double dx = p2.getX() - p1.getX();
        return dx;
        
//        double dy = p2.getY() - p1.getY();
//
//        double distanceToReward = Math.sqrt(dx * dx + dy * dy);
//
//        if (!rewardTracers.isEmpty()) {
//            group.getChildren().removeAll(rewardTracers);
//            rewardTracers.clear();
//        }
//
//        Line line = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
//        line.setStrokeWidth(3);
//        line.setStroke(Color.AQUAMARINE);
//        rewardTracers.add(line);
//
//        group.getChildren().addAll(rewardTracers);

        
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
