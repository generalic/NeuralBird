package start.game_play_fxml;

import hr.fer.zemris.game.model.GameModel;
import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import start.AbstractScreenSwitchController;
import start.engine.GameEngine;

/**
 * Created by generalic on 7.1.2016..
 */
public abstract class AbstractFXMLController extends AbstractScreenSwitchController {

	@FXML
	private AnchorPane root;

	@FXML
	private Pane gamePane;

	@FXML
	private VBox optionPanel;

	@FXML
	private VBox gameOverVBox;

	@FXML
	private Label scoreLabel;

	@FXML
	private Label endScoreLabel;

	@FXML
	private Button restartButton;

	protected GameEngine engine;

	@FXML
	public void initialize() {
		optionPanel.setTranslateX(-root.getPrefWidth());
		optionPanel.setDisable(true);
//		optionPanel.setVisible(false);

		gameOverVBox.setTranslateY(-root.getPrefHeight());
		gameOverVBox.setDisable(true);
	}

	@FXML
	public void mouseEntered() {
//		Timeline timeline = new Timeline();
//		timeline.getKeyFrames().addAll(
//				new KeyFrame(Duration.millis(300),
//						new KeyValue(vbox.opacityProperty(), 1)
//				)
//		);
//		timeline.play();
	}

	@FXML
	public void mouseExited() {
		TranslateTransition transTransition = new TranslateTransition(Duration.millis(350), optionPanel);
		transTransition.setToX(-optionPanel.getPrefWidth());
		transTransition.setInterpolator(Interpolator.LINEAR);
		transTransition.play();
		transTransition.setOnFinished(e -> optionPanel.setDisable(true));

//		ScaleTransition transition = new ScaleTransition(Duration.seconds(1), optionPanel);
//		transition.setFromX(1);
//		transition.setFromY(1);
//		transition.setToX(0);
//		transition.setToY(0);
//		transition.setInterpolator(Interpolator.EASE_BOTH);
//		transition.play();
//		transition.setOnFinished(e -> optionPanel.setVisible(false));
	}

	@FXML
	public void keyPressed(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ESCAPE)) {
			slideInToolBar();
		}
	}

	@FXML
	public void buttonMouseEntered() {
		ScaleTransition transition = new ScaleTransition(Duration.millis(200), backButton);
		transition.setToX(1.5);
		transition.setToY(1.5);
		transition.play();
	}

	@FXML
	public void buttonMouseExited() {
		ScaleTransition transition = new ScaleTransition(Duration.millis(200), backButton);
		transition.setToX(1);
		transition.setToY(1);
		transition.play();
	}

	@FXML
	public void showToolBar(MouseEvent me) {
		if(me.getSceneX() < 50) {
			slideInToolBar();
		}
	}

	private void slideInToolBar() {
		optionPanel.setDisable(false);
		TranslateTransition transTransition = new TranslateTransition(Duration.millis(350), optionPanel);
		transTransition.setToX(0);
		transTransition.setInterpolator(Interpolator.LINEAR);
		transTransition.play();


//		ScaleTransition transition = new ScaleTransition(Duration.seconds(1), optionPanel);
//		transition.setFromX(3);
//		transition.setFromY(3);
//		transition.setToX(1);
//		transition.setToY(1);
//		transition.setInterpolator(Interpolator.EASE_BOTH);
//		optionPanel.setVisible(true);
//		transition.play();
	}


	private void bindOnGameOverAction(BooleanProperty gameOverProperty) {
		gameOverProperty.addListener((observable, oldValue, newValue) -> {
			if(!newValue) {
				gameOverVBox.setDisable(false);
				hideScoreLabel();
				TranslateTransition transition = new TranslateTransition(Duration.millis(300), gameOverVBox);
				transition.setToY(0);
				transition.setInterpolator(Interpolator.LINEAR);
				transition.play();
			}
		});
	}

	private void hideScoreLabel() {
		TranslateTransition transition = new TranslateTransition(Duration.millis(300), scoreLabel);
		transition.setToY(-root.getPrefHeight());
		transition.setInterpolator(Interpolator.LINEAR);
		transition.play();
	}

	private void bindScoreLabels(IntegerProperty scoreProperty) {
		scoreLabel.textProperty().bind(new SimpleStringProperty("SCORE: ").concat(scoreProperty.asString()));
		endScoreLabel.textProperty().bind(scoreLabel.textProperty());

		scoreLabel.textProperty().addListener(observable -> {
			ScaleTransition transition = new ScaleTransition(Duration.millis(300), scoreLabel);
			transition.setFromX(1);
			transition.setFromY(1);
			transition.setToX(1.5);
			transition.setToY(1.5);
			transition.setInterpolator(Interpolator.LINEAR);
			transition.setAutoReverse(true);
			transition.setCycleCount(2);
			transition.play();
		});
	}

	public void addGameScreen(Node gameNode) {
		gamePane.getChildren().add(gameNode);
	}

	@Override
	public void initScreen(Scene scene, Pane root, Transition transition) {
		this.engine = new GameEngine(createGameModel());

		addGameScreen(engine.getGameNode());
		bindOnGameOverAction(engine.gameOverProperty());
		bindScoreLabels(engine.getGameModel().scoreProperty());

		switchScreen(scene, root, transition);

		Group group = (Group) scene.getRoot();
		restartButton.setOnAction(e -> {
			ScaleTransition zoomInTransition = new ScaleTransition(Duration.seconds(0.5), root);
			zoomInTransition.setFromX(1);
			zoomInTransition.setFromY(1);
			zoomInTransition.setToX(5);
			zoomInTransition.setToY(5);
			zoomInTransition.setInterpolator(Interpolator.LINEAR);
			zoomInTransition.setOnFinished(event -> {
				group.getChildren().remove(root);
				group.getChildren().forEach(c -> c.setVisible(false));
			});

			TranslateTransition clearScreen = new TranslateTransition(Duration.millis(300), gameOverVBox);
			clearScreen.setToY(-root.getPrefWidth());
			clearScreen.setInterpolator(Interpolator.LINEAR);

			resetScreen(scene, new SequentialTransition(clearScreen, zoomInTransition));
		});

		scene.setOnKeyPressed(engine.getEventHandler());
		engine.runGame();
	}

	protected abstract GameModel createGameModel();
	
	protected abstract void resetScreen(Scene scene, Transition transition);

}