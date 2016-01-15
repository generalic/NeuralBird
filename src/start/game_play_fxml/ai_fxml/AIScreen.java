package start.game_play_fxml.ai_fxml;

/**
 * Created by generalic on 4.1.2016..
 */

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

	private static final String FXML_FILE_NAME = "ai_screen.fxml";

	private static final String WEIGHTS_PATH = "weights.ser";

	protected static NeuralNetwork network;

	static {
		deserialisation();
	}

	public AIScreen(Scene scene) {
		super(scene);
	}

	@Override
	protected String getFXMLFileName() {
		return FXML_FILE_NAME;
	}

	private static void deserialisation() {
		Path path = Paths.get(WEIGHTS_PATH);

		try(
			InputStream settingsIn = Files.newInputStream(path);
			ObjectInputStream in = new ObjectInputStream(settingsIn);
		) {
			network = (NeuralNetwork) in.readObject();
			System.err.println("Successfully deserialized.");
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

}
