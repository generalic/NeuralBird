package hr.fer.zemris.game.model;

import hr.fer.zemris.game.components.IComponent;
import hr.fer.zemris.game.components.bird.Bird;
import hr.fer.zemris.game.components.ground.Ground;
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
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

import java.util.*;

public abstract class GameModel {

    public Dimension2D dimension = new Dimension2D(1024, 700);

	protected Dimension2D gameDimension = new Dimension2D(dimension.getWidth(), dimension.getHeight() - dimension.getHeight() / 8);

	{
		Rectangle2D bounds = Screen.getPrimary().getBounds();
		dimension = new Dimension2D(bounds.getWidth(), bounds.getHeight());
		gameDimension = new Dimension2D(dimension.getWidth(), dimension.getHeight() - dimension.getHeight() / 8);
	}

    protected Random random = RandomProvider.get();

    protected Bird bird;

	protected BooleanProperty jump = new SimpleBooleanProperty(false);

	protected Constants constants;

    protected LinkedList<PipePair> pipesPairs = new LinkedList<>();

	protected PipePair nearestPipePair;

	protected PipePair lastPassed;

    protected LinkedList<Reward> rewards = new LinkedList<>();

    protected LinkedList<Ground> grounds = new LinkedList<>();

	protected Group group = new Group();

	protected IntegerProperty score = new SimpleIntegerProperty(0);

	private IntegerProperty numberOfPassedPipes = new SimpleIntegerProperty(0);

	protected BooleanProperty traceable = new SimpleBooleanProperty(false);

	public GameModel() {
        initModel();
    }

    private void initModel() {
    	constants = Constants.currentConstants;
    	initaliseBird();
        initialiseEnvironment();
        lastPassed = getNearestPairAheadOfBird().get();
    }

	public Pane getGamePane() {
        group.getChildren().add(bird);
        group.getChildren().addAll(pipesPairs);
        group.getChildren().addAll(rewards);
        group.getChildren().addAll(grounds);

        Pane gamePane = new Pane(group);
        gamePane.setPrefWidth(dimension.getWidth());
        gamePane.setPrefHeight(dimension.getHeight());

        return gamePane;
    }

    public void reset() {
        group.getChildren().clear();
        pipesPairs.clear();
        rewards.clear();

        initModel();
    }

	protected void initaliseBird() {
		this.bird = new Bird(gameDimension.getWidth() / 3, gameDimension.getHeight() / 2);
	}

    protected void initialiseEnvironment() {
		setupPipesAndRewards();
		setupGround();
    }

	private void setupPipesAndRewards() {
		double nextPipeX = gameDimension.getWidth() + constants.INITIAL_PIPE_OFFSET.get();
		double nextRewardCenterX = nextPipeX + constants.PIPE_WIDTH.get() + constants.PIPE_GAP_X.get() / 2;
		for (int i = 0; i < constants.NUMBER_OF_PIPES.get(); i++) {
			nextPipeX = initialisePipePair(nextPipeX);
			nextRewardCenterX = initialiseReward(nextRewardCenterX);
		}
	}

	protected void setupGround() {
		double nextGroundX = 0;
		for (int i = 0; i < constants.NUMBER_OF_GROUNDS.get(); i++) {
			nextGroundX = initialiseGround(nextGroundX);
		}
	}

	private abstract class AbstractInitialiser<T extends IComponent> {

        private List<T> components;

        public AbstractInitialiser(List<T> components) {
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
        return new AbstractInitialiser<PipePair>(pipesPairs) {

            @Override
            protected PipePair createComponent(double nextComponentX) {
                return new PipePair(nextPipeX, constants.PIPE_GAP_Y.get(), constants.PIPE_WIDTH.get(), gameDimension.getHeight());
            }

            @Override
            protected double calculateOffset(PipePair component) {
                return component.getRightMostX() + constants.PIPE_GAP_X.get();
            }

        }.initialiseComponent(nextPipeX);
    }

    private double initialiseReward(double nextRewardCenterX) {
        return new AbstractInitialiser<Reward>(rewards) {

            @Override
            protected Reward createComponent(double nextComponentX) {
                return new Reward(nextRewardCenterX, gameDimension.getHeight());
            }

            @Override
            protected double calculateOffset(Reward component) {
                return component.getCenterX() + constants.REWARD_GAP_X.get();
            }

        }.initialiseComponent(nextRewardCenterX);
    }

	protected double initialiseGround(double nextGroundX) {
        return new AbstractInitialiser<Ground>(grounds) {

            @Override
            protected Ground createComponent(double nextComponentX) {
                return new Ground(nextGroundX, gameDimension.getHeight());
            }

            @Override
            protected double calculateOffset(Ground component) {
                return component.getRightMostX();
            }

        }.initialiseComponent(nextGroundX);
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
			if(Objects.isNull(first)) {
				return;
			}
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
                double shiftX = Physics.calculateShiftX(constants.PIPES_SPEED_X.get(), time);
                component.translatePair(shiftX);
                double shiftY = Physics.calculateShiftX(constants.PIPES_SPEED_Y.get(), time);
                component.setPairYPosition(shiftY);
            }

            @Override
            protected void putFirstBehindLast(PipePair first, PipePair last) {
                first.setPairXPosition(last.getRightMostX() + constants.PIPE_GAP_X.get());
                first.randomizeYPositions();
            }

        }.move(time);
    }

    private void moveRewards(int time) {
        new AbstractMover<Reward>(rewards) {

            @Override
            protected void translate(Reward component) {
                double shiftX = Physics.calculateShiftX(constants.REWARD_SPEED_X.get(), time);
                component.translateReward(shiftX);
                component.updateFrame();
            }

            @Override
            protected void putFirstBehindLast(Reward first, Reward last) {
                first.setCenterX(last.getCenterX() + constants.REWARD_GAP_X.get());
                first.randomizeYPosition();
                first.setVisible(random.nextDouble() < constants.REWARD_PROBABILITY.get());
            }

        }.move(time);
    }

    protected void moveGround(int time) {
        new AbstractMover<Ground>(grounds) {

            @Override
            protected void translate(Ground component) {
                double shiftX = Physics.calculateShiftX(constants.PIPES_SPEED_X.get(), time);
                component.translate(shiftX);
            }

            @Override
            protected void putFirstBehindLast(Ground first, Ground last) {
                first.setX(last.getRightMostX());
            }

        }.move(time);
    }

    private void moveBird(int time) {
        if (jump.get()) {
            double shiftY = Physics.calculateShiftY(constants.JUMP_SPEED.negate().get(), time);
            bird.setCurrentVelocity(constants.JUMP_SPEED.negate().get());
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
        if (!constants.GOD_MODE.get() && checkCollisions()) {
            return false;
        }

		refreshScore();

        movePipes(time);
        moveRewards(time);
        moveBird(time);
		moveGround(time);

		scanEnvironment();

        return true;
    }

	private void refreshScore() {
		if (isRewardCollected()) {
			score.set(score.get() + constants.REWARD_COLLECTED_BONUS.get());
		}

		nearestPipePair = getNearestPairAheadOfBird().get();
		if (!nearestPipePair.equals(lastPassed)) {
			score.set(score.get() + constants.PIPE_PASSED_BONUS.get());
			numberOfPassedPipes.set(numberOfPassedPipes.get() + 1);
			lastPassed = nearestPipePair;
		}
	}
	
	protected abstract void scanEnvironment();

	private boolean isRewardCollected() {
        return rewards.stream()
        		.filter(r -> r.intersects(bird))
        		.peek(r -> r.setVisible(false))
        		.findAny()
        		.isPresent();
    }

	private boolean checkCollisions() {
        boolean intersection = pipesPairs.stream()
        		.filter(p -> p.intersects(bird))
        		.findAny()
        		.isPresent();
        return intersection || isBirdOutOfBounds();
    }

    private boolean isBirdOutOfBounds() {
		Bounds birdBounds = bird.getBoundsInParent();
        return birdBounds.getMaxY() > gameDimension.getHeight() || birdBounds.getMinY() < 0;
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

	public void jumpBird() {
		jump.set(true);
	}

	public BooleanProperty jumpProperty() {
		return jump;
	}

	public int getScore() {
		return score.get();
	}

	public IntegerProperty scoreProperty() {
		return score;
	}

	public int getNumberOfPassedPipes() {
		return numberOfPassedPipes.get();
	}

	public BooleanProperty traceableProperty() {
		return traceable;
	}

}
