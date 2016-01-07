package start.game_play_fxml;

import hr.fer.zemris.game.model.GameModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import start.engine.GameEngine;

import java.io.IOException;

/**
 * Created by generalic on 7.1.2016..
 */
public abstract class AbstractFXMLScreen {

	public AbstractFXMLScreen(Scene scene) {
		FXMLLoader fxmlLoader = createFXMLLoader();

		Pane root = getRoot(fxmlLoader);

		IScreenController controller = fxmlLoader.getController();
		GameEngine engine = new GameEngine(createGameModel());

		controller.initScreen(scene, root, engine);

		scene.setOnKeyPressed(engine.getEventHandler());
		engine.runGame();

//		Parent prevRoot = scene.getRoot();
//		controller.exitButton.setOnAction(e -> scene.setRoot(prevRoot));
//		scene.setRoot(root);
	}

	private Pane getRoot(FXMLLoader fxmlLoader) {
		try {
			Pane root = fxmlLoader.load();
			return root;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private FXMLLoader createFXMLLoader() {
		return new FXMLLoader(getClass().getResource(getFXMLFileName()));
	}

	protected abstract String getFXMLFileName();

	protected abstract GameModel createGameModel();

}
