package start.settings_fxml;

import javafx.animation.Transition;
import javafx.scene.Scene;
import start.game_play_fxml.AbstractFXMLScreen;

public class SettingsScreen extends AbstractFXMLScreen {

	private static final String FXML_FILE_NAME = "settings_screen.fxml";

    public SettingsScreen(Scene scene, Transition transition) {
		super(scene, transition);
    }

	@Override
	protected String getFXMLFileName() {
		return  FXML_FILE_NAME;
	}

}
