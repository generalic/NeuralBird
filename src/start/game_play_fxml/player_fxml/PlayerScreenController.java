package start.game_play_fxml.player_fxml;

import javafx.scene.Scene;
import start.game_play_fxml.AbstractFXMLController;

public class PlayerScreenController extends AbstractFXMLController {

	@Override
	protected void resetScreen(Scene scene) {
		new PlayerScreen(scene);
	}

}