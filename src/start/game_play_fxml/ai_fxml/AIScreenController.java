package start.game_play_fxml.ai_fxml;

import hr.fer.zemris.game.model.GameModelAI;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import start.engine.GameEngine;
import start.game_play_fxml.AbstractFXMLController;

public class AIScreenController extends AbstractFXMLController {

	@FXML
	private Slider fpsSlider;

	@FXML
	private ToggleButton traceButton;

	@FXML
	private Circle inputIndicator;

	@Override
	public void initScreen(Scene scene, Pane root, GameEngine engine) {
		super.initScreen(scene, root, engine);

		GameModelAI gameModel = (GameModelAI) engine.getGameModel();
		traceButton.selectedProperty().bindBidirectional(gameModel.traceableProperty());
		inputIndicator.visibleProperty().bind(gameModel.jumpProperty());

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