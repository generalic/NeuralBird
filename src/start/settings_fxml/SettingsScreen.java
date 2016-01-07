package start.settings_fxml;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SettingsScreen {


    public SettingsScreen(Scene scene) {
		FXMLLoader fxmlLoader = createFXMLLoader();

		Pane root = getRoot(fxmlLoader);

		SettingsScreenController controller = fxmlLoader.getController();
		controller.initScreen(scene, root);
    }

	private FXMLLoader createFXMLLoader() {
		return new FXMLLoader(getClass().getResource("settings_screen.fxml"));
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


}
