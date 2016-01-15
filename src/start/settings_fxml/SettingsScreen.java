package start.settings_fxml;

import javafx.scene.Scene;
import start.game_play_fxml.AbstractFXMLScreen;

public class SettingsScreen extends AbstractFXMLScreen {

	private static final String FXML_FILE_NAME = "settings_screen.fxml";

    public SettingsScreen(Scene scene) {
		super(scene);
    }

	@Override
	protected String getFXMLFileName() {
		return  FXML_FILE_NAME;
	}

}
