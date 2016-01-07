package start.settings_fxml;

import hr.fer.zemris.game.environment.Constants;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

public class SettingsScreenController {

	@FXML
	public Slider pipeWidthSlider;

	@FXML
	public Button backButton;

	public void initScreen(Scene scene, Pane root) {
		  initBindings();

		  Group group = (Group) scene.getRoot();
		  group.getChildren().forEach(c -> c.setVisible(false));
		  group.getChildren().add(root);

		  backButton.setOnAction(e -> {
			  group.getChildren().remove(root);
			  group.getChildren().forEach(c -> c.setVisible(true));
		  });
	}

	private void initBindings() {
		pipeWidthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			Constants.PlayerConst.PIPE_WIDTH = newValue.doubleValue();
		});
	 }


}
