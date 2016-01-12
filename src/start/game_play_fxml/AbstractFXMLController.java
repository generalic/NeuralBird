package start.game_play_fxml;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import start.engine.GameEngine;

/**
 * Created by generalic on 7.1.2016..
 */
public abstract class AbstractFXMLController implements IScreenController {

	@FXML
	private AnchorPane root;

	@FXML
	private Pane gamePane;

	@FXML
	private VBox optionPanel;

	@FXML
	private Button backButton;

	@FXML
	private VBox gameOverVBox;

	@FXML
	private Label scoreLabel;

	@FXML
	private Label endScoreLabel;

	@FXML
	private Button restartButton;

	@FXML
	public void initialize() {
		optionPanel.setTranslateX(-optionPanel.getPrefWidth() - 1);
		optionPanel.setDisable(true);
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
		transTransition.setToX(-optionPanel.getPrefWidth() - 1);
		transTransition.setInterpolator(Interpolator.EASE_OUT);
		transTransition.play();
		transTransition.setOnFinished(e -> optionPanel.setDisable(true));
	}

	@FXML
	public void keyPressed(KeyEvent event) {
//		if(event.getCode().equals(KeyCode.ESCAPE)) {
//			mouseEntered();
//		}
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
			optionPanel.setDisable(false);
			TranslateTransition transTransition = new TranslateTransition(Duration.millis(350), optionPanel);
			transTransition.setToX(0);
			transTransition.setInterpolator(Interpolator.EASE_OUT);
			transTransition.play();
		}
	}

	private void bindOnGameOverAction(BooleanProperty gameOverProperty) {
		gameOverProperty.addListener((observable, oldValue, newValue) -> {
			if(!newValue) {
				gameOverVBox.setDisable(false);
				TranslateTransition transition = new TranslateTransition(Duration.millis(300), gameOverVBox);
				transition.setToY(0);
				transition.setInterpolator(Interpolator.EASE_OUT);
				transition.play();
			}
		});
	}

	private void bindScoreLabels(IntegerProperty scoreProperty) {
		endScoreLabel.textProperty().bind(new SimpleStringProperty("SCORE: ").concat(scoreProperty.asString()));
		scoreLabel.textProperty().bind(new SimpleStringProperty("SCORE: ").concat(scoreProperty.asString()));
	}

	public void addGameScreen(Node gameNode) {
		gamePane.getChildren().add(gameNode);
	}

	public void initScreen(Scene scene, Pane root, GameEngine engine) {
		addGameScreen(engine.getGameNode());
		bindOnGameOverAction(engine.gameOverProperty());
		bindScoreLabels(engine.getGameModel().scoreProperty());

		Group group = (Group) scene.getRoot();
		group.getChildren().forEach(c -> c.setVisible(false));
		group.getChildren().add(root);

		backButton.setOnAction(e -> {
			group.getChildren().remove(root);
			group.getChildren().forEach(c -> c.setVisible(true));
		});

		restartButton.setOnAction(e -> {
			group.getChildren().remove(root);
			group.getChildren().forEach(c -> c.setVisible(true));
			resetScreen(scene);
		});
	}
	
	
	protected abstract void resetScreen(Scene scene);
}