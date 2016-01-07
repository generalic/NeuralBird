package start.game_play_fxml.ai_fxml;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import start.engine.GameEngine;
import start.game_play_fxml.AbstractFXMLController;

public class AIScreenController extends AbstractFXMLController {

	@FXML
	private Slider fpsSlider;

	@Override
	public void initScreen(Scene scene, Pane root, GameEngine engine) {
		super.initScreen(scene, root, engine);
		bindFpsSlider(engine.getGameLoop());
	}

	@Override
	protected void resetScreen(Scene scene) {
		new AIScreen(scene);
	}

	private void bindFpsSlider(Timeline gameLoop) {
		fpsSlider.valueProperty().bindBidirectional(gameLoop.rateProperty());
	}

}