package start.game_play_fxml;

import javafx.animation.Transition;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * Created by generalic on 7.1.2016..
 */
public interface IScreenController {

	void initScreen(Scene scene, Pane root, Transition transition);

}
