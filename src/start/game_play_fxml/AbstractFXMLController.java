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
import javafx.scene.input.KeyCode;
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
		transTransition.setInterpolator(Interpolator.EASE_OUT);
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
				transition.setInterpolator(Interpolator.EASE_OUT);
				transition.play();
			}
		});
	}

	private void hideScoreLabel() {
		TranslateTransition transition = new TranslateTransition(Duration.millis(300), scoreLabel);
		transition.setToY(-root.getPrefHeight());
		transition.setInterpolator(Interpolator.EASE_OUT);
		transition.play();
	}

	private void bindScoreLabels(IntegerProperty scoreProperty) {
		scoreLabel.textProperty().bind(new SimpleStringProperty("SCORE: ").concat(scoreProperty.asString()));
		endScoreLabel.textProperty().bind(scoreLabel.textProperty());
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

//		FadeTransition transition = new FadeTransition(Duration.seconds(2), group);
//		transition.setFromValue(0);
//		transition.setToValue(1);
//		transition.setInterpolator(Interpolator.LINEAR);
//		transition.play();

//		ScaleTransition transition = new ScaleTransition(Duration.seconds(1), group);
//		transition.setFromX(6);
//		transition.setFromY(9);
//		transition.setToX(1);
//		transition.setToY(1);
//		transition.setInterpolator(Interpolator.EASE_OUT);
//		transition.play();

		ScaleTransition transition = new ScaleTransition(Duration.seconds(1), root);
		transition.setFromX(2);
		transition.setFromY(2);
		transition.setToX(1);
		transition.setToY(1);
		transition.setInterpolator(Interpolator.EASE_OUT);
		transition.play();


		backButton.setOnAction(e -> {
			group.getChildren().remove(root);
			group.getChildren().forEach(c -> c.setVisible(true));

			ScaleTransition backTransition = new ScaleTransition(Duration.millis(200), group);
			backTransition.setFromX(15);
			backTransition.setFromY(15);
			backTransition.setToX(1);
			backTransition.setToY(1);
			backTransition.play();
		});

		restartButton.setOnAction(e -> {
			group.getChildren().remove(root);
			group.getChildren().forEach(c -> c.setVisible(true));
			resetScreen(scene);
		});
	}
	
	
	protected abstract void resetScreen(Scene scene);
}