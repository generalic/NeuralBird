package start.game_play_fxml;

import javafx.animation.Transition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by generalic on 7.1.2016..
 */
public abstract class AbstractFXMLScreen {

	public AbstractFXMLScreen(Scene scene, Transition transition) {
		FXMLLoader fxmlLoader = createFXMLLoader();

		Pane root = getRoot(fxmlLoader);

		IScreenController controller = fxmlLoader.getController();
		controller.initScreen(scene, root, transition);
	}

	private FXMLLoader createFXMLLoader() {
		return new FXMLLoader(getClass().getResource(getFXMLFileName()));
	}

	protected static Pane getRoot(FXMLLoader fxmlLoader) {
		try {
			Pane root = fxmlLoader.load();
			return root;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected abstract String getFXMLFileName();

}
