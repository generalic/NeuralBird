package start.game_play_fxml;

import javafx.animation.Transition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

import java.io.IOException;

/**
 * Created by generalic on 7.1.2016..
 */
public abstract class AbstractFXMLScreen {

	public AbstractFXMLScreen(Scene scene, Transition transition) {
		FXMLLoader fxmlLoader = createFXMLLoader();

		Pane root = getRoot(fxmlLoader);

		double width = Screen.getPrimary().getVisualBounds().getWidth();
		double height = Screen.getPrimary().getVisualBounds().getHeight();
		root.setPrefWidth(width);
		root.setPrefHeight(height + 40);

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
