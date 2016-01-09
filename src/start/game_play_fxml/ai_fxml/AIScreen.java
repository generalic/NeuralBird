package start.game_play_fxml.ai_fxml;

/**
 * Created by generalic on 4.1.2016..
 */

import hr.fer.zemris.game.environment.Constants;
import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.game.model.GameModelAI;
import hr.fer.zemris.network.NeuralNetwork;
import javafx.scene.Scene;
import start.game_play_fxml.AbstractFXMLScreen;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AIScreen extends AbstractFXMLScreen {

	private NeuralNetwork network;
	private Constants constants;

	private static final String FXML_FILE_NAME = "ai_screen.fxml";

	public AIScreen(Scene scene) {
		super(scene);
	}

	@Override
	protected String getFXMLFileName() {
		return FXML_FILE_NAME;
	}

	@Override
	protected GameModel createGameModel() {
		deserialisation();
		Constants.AIConstants = constants;
		GameModelAI model = new GameModelAI();
		model.addEnvironmentListener(network);
		return model;
	}

	private void deserialisation() {
		Path p = Paths.get("weights.ser");

		try(
			InputStream settingsIn = Files.newInputStream(p);
			ObjectInputStream in = new ObjectInputStream(settingsIn);
		) {
			network = (NeuralNetwork) in.readObject();
			constants = (Constants) in.readObject();
			System.err.println("Successfully deserialized.");
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

}
