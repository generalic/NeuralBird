package hr.fer.zemris.game.model;

import hr.fer.zemris.game.components.IComponent;
import hr.fer.zemris.game.components.bird.Bird;
import hr.fer.zemris.game.components.pipes.PipePair;
import hr.fer.zemris.game.components.reward.Reward;
import hr.fer.zemris.game.environment.Constants;
import hr.fer.zemris.game.physics.Physics;
import hr.fer.zemris.util.RandomProvider;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class GameModel {

    /**
     * Debug varijabla da ne moram dolje uvijek zakomentirati na checkCollisions(). Kad je {@code false} igra se nebude
     * zaustavila.
     */
    private static final boolean PAUSE_GAME = true;

    protected Dimension2D dimension = new Dimension2D(1000, 600);

    public static Random random = RandomProvider.get();

    protected Bird bird;

    protected BooleanProperty jump;

	protected Constants constants;

    protected LinkedList<PipePair> pipesPairs = new LinkedList<>();

	protected PipePair lastPassed;

    protected LinkedList<Reward> rewards = new LinkedList<>();

	protected Group group = new Group();

	protected IntegerProperty score = new SimpleIntegerProperty(0);

    public GameModel() {
        initModel();
    }

    private void initModel() {
    	constants = providerConstants();
    	this.bird = new Bird(dimension.getWidth() / 3, dimension.getHeight() / 2);
        initialiseEnvironment();
        jump = new SimpleBooleanProperty(false);
        lastPassed = getNearestPairAheadOfBird().get();

    }
	
	protected abstract Constants providerConstants();
	
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

    protected void initialiseEnvironment() {
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
			score.set(score.get() + constants.REWARD_COLLECTED_BONUS);
        }

        movePipes(time);
        moveRewards(time);
        moveBird(time);

		scanEnvironment();

        return true;
    }

	protected abstract void scanEnvironment();

	private boolean isRewardCollected() {
        return rewards.stream()
        		.filter(r -> r.intersects(bird))
        		.peek(r -> r.setVisible(false))
        		.findAny()
        		.isPresent();
    }

    public int getScore() {
        return score.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
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
    protected Optional<PipePair> getNearestPairAheadOfBird() {
    	return pipesPairs.stream()
    			.filter(p -> p.getRightMostX() > bird.getLeftMostX())
        		.sorted()
        		.findFirst();
    }

    public Constants getConstants(){
    	return constants;
    }

    public void setConstants(Constants constants){
    	this.constants = constants;
    	initModel();
    }

}
