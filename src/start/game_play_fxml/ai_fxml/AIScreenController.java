package start.game_play_fxml.ai_fxml;

import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.game.model.GameModelAI;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
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
	public void initScreen(Scene scene, Pane root) {
		super.initScreen(scene, root);
		setupBinding();
	}

	@Override
	protected GameModel createGameModel() {
		GameModelAI model = new GameModelAI();
		model.addEnvironmentListener(AIScreen.network);
		return model;
	}

	@Override
	protected void resetScreen(Scene scene) {
		new AIScreen(scene);
	}

	private void setupBinding() {
		GameModelAI gameModel = (GameModelAI) engine.getGameModel();

		iOSToggleButton toggleButton = new iOSToggleButton();
		gameModel.traceableProperty().bind(toggleButton.switchedOnProperty());
		optionPanel.getChildren().add(1, toggleButton);

		inputIndicator.visibleProperty().bind(gameModel.traceableProperty());
		inputOn.visibleProperty().bind(gameModel.jumpProperty());

		fpsSlider.valueProperty().bindBidirectional(engine.getGameLoop().rateProperty());
	}

}