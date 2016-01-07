package start.game_play_fxml.player_fxml;

/**
 * Created by generalic on 4.1.2016..
 */

import hr.fer.zemris.game.environment.Constants;
import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.game.model.GameModelPlayer;
import hr.fer.zemris.network.NeuralNetwork;
import javafx.scene.Scene;
import start.game_play_fxml.AbstractFXMLScreen;

public class PlayerScreen extends AbstractFXMLScreen {

	private NeuralNetwork network;
	private Constants constants;

	private static final String FXML_FILE_NAME = "player_screen.fxml";

	public PlayerScreen(Scene scene) {
		super(scene);
	}

	@Override
	protected String getFXMLFileName() {
		return FXML_FILE_NAME;
	}

	@Override
	protected GameModel createGameModel() {
		return new GameModelPlayer();
	}

}
