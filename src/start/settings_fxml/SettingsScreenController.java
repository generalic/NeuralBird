package start.settings_fxml;

import hr.fer.zemris.game.environment.Constants;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

public class SettingsScreenController {

	@FXML
	public Label pipeWidthLabel;

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
		pipeWidthLabel.textProperty().bind(new SimpleStringProperty("PIPE WIDTH: ").concat(pipeWidthSlider.valueProperty().asString("%.0f")));
		pipeWidthSlider.valueProperty().set(Constants.PlayerConstants.PIPE_WIDTH);
		pipeWidthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			Constants.PlayerConstants.PIPE_WIDTH = newValue.doubleValue();
			Constants.PlayerConstants.REWARD_GAP_X = Constants.PlayerConstants.PIPE_GAP_X + Constants.PlayerConstants.PIPE_WIDTH;
		});
	}

}
