package start.game_play_fxml.ai_fxml;

import hr.fer.zemris.game.model.GameModelAI;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import start.engine.GameEngine;
import start.game_play_fxml.AbstractFXMLController;

public class AIScreenController extends AbstractFXMLController {

	@FXML
	private Slider fpsSlider;

	@FXML
	private StackPane inputIndicator;

	@FXML
	private Circle inputOn;

	@FXML
	private VBox optionPanel;


	@Override
	public void initScreen(Scene scene, Pane root, GameEngine engine) {
		super.initScreen(scene, root, engine);

		GameModelAI gameModel = (GameModelAI) engine.getGameModel();

		iOSToggleButton toggleButton = new iOSToggleButton();
		gameModel.traceableProperty().bind(toggleButton.switchedOnProperty());
		optionPanel.getChildren().add(1, toggleButton);

		inputIndicator.visibleProperty().bind(gameModel.traceableProperty());
		inputIndicator.visibleProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue) {
				ScaleTransition transition = new ScaleTransition(Duration.millis(500), root);
				transition.setFromX(1);
				transition.setFromY(1);
				transition.setToX(2);
				transition.setToY(2);
				transition.setAutoReverse(true);
				transition.setCycleCount(2);
			}
		});
		inputOn.visibleProperty().bind(gameModel.jumpProperty());

		setupBinding();
		bindFpsSlider(engine.getGameLoop());
	}



	@Override
	protected void resetScreen(Scene scene) {
		new AIScreen(scene);
	}

	private void setupBinding() {
	}

	private void bindFpsSlider(Timeline gameLoop) {
		fpsSlider.valueProperty().bindBidirectional(gameLoop.rateProperty());
	}

}