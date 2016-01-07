package start.engine;

import hr.fer.zemris.game.model.GameModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Created by generalic on 6.1.2016..
 */
public class GameEngine {

	private GameModel model;

	private Node gameNode;

	private Timeline gameLoop;

	private BooleanProperty gameOverProperty;

	public GameEngine(GameModel model) {
		this.model = model;
		gameNode = getGameNode(model.getGroup());
		this.gameOverProperty = new SimpleBooleanProperty();
		initGameLoop();
		gameOverProperty.addListener((observable, oldValue, newValue) -> {
			if(!newValue) {
				gameLoop.stop();
			}
		});
	}

	private void initGameLoop() {
		gameLoop = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			gameOverProperty.set(model.update(1));
		}));
		gameLoop.setRate(25);
		gameLoop.setCycleCount(Animation.INDEFINITE);
	}

	private boolean paused = false;

	public void runGame() {
		gameLoop.play();
	}

	public EventHandler<KeyEvent> getEventHandler() {
		return e -> {
			if (e.getCode().equals(KeyCode.B)) {
				if (!paused) {
					gameLoop.pause();
				} else {
					gameLoop.play();
				}
				paused ^= true;
			} else {
				model.jumpBird();
			}
		};
	}

	public GameModel getGameModel() {
		return model;
	}

	public Node getGameNode() {
		return gameNode;
	}

	public Timeline getGameLoop() {
		return gameLoop;
	}

	public boolean getGameOverProperty() {
		return gameOverProperty.get();
	}

	public BooleanProperty gameOverPropertyProperty() {
		return gameOverProperty;
	}

	private static Pane getGameNode(Group modelGroup) {
		Pane gameWorld = new Pane(modelGroup);
		String image = GameEngine.class.getResource("backgroundPicture.jpg").toExternalForm();

		gameWorld.setStyle(
				"-fx-background-image: url('" + image + "'); " +
				"-fx-background-size: cover; " +
				"-fx-background-repeat: stretch; " +
				"-fx-background-size: 1000 600;"
		);

		return gameWorld;
	}

}
