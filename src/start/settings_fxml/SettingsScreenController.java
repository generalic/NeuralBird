package start.settings_fxml;

import hr.fer.zemris.game.environment.Constants;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class SettingsScreenController {

	@FXML
	public Label gravityLabel;

	@FXML
	public Slider gravitySlider;

	@FXML
	public Label pipeXSpeedLabel;

	@FXML
	public Slider pipeXSpeedSlider;

	@FXML
	public Label pipeYSpeedLabel;

	@FXML
	public Slider pipeYSpeedSlider;

	@FXML
	public Label pipeGapXLabel;

	@FXML
	public Slider pipeGapXSlider;

	@FXML
	public Label pipeGapYLabel;

	@FXML
	public Slider pipeGapYSlider;

	@FXML
	public Label pipeWidthLabel;

	@FXML
	public Slider pipeWidthSlider;

	@FXML
	public Label jumpSpeedLabel;

	@FXML
	public Slider jumpSpeedSlider;

	@FXML
	public Label rewardProbabilityLabel;

	@FXML
	public Slider rewardProbabilitySlider;

	@FXML
	public Label godModeLabel;

	@FXML
	public Slider godModeSlider;

	@FXML
	public Button backButton;

	@FXML
	public Button defaultSettingsButton;

	public void initScreen(Scene scene, Pane root) {
		setupBinding();

		Group group = (Group) scene.getRoot();
		group.getChildren().forEach(c -> c.setVisible(false));
		group.getChildren().add(root);

		ScaleTransition transition = new ScaleTransition(Duration.seconds(1), root);
		transition.setFromX(2);
		transition.setFromY(2);
		transition.setToX(1);
		transition.setToY(1);
		transition.setInterpolator(Interpolator.EASE_OUT);
		transition.play();

		backButton.setOnAction(e -> {
			group.getChildren().remove(root);
			group.getChildren().forEach(c -> c.setVisible(true));

			ScaleTransition backTransition = new ScaleTransition(Duration.millis(200), group);
			backTransition.setFromX(15);
			backTransition.setFromY(15);
			backTransition.setToX(1);
			backTransition.setToY(1);
			backTransition.play();
		});
	}

	private void setupBinding() {
		gravityLabel.textProperty().bind(gravitySlider.valueProperty().asString("%.2f"));
		gravitySlider.valueProperty().set(Constants.PlayerConstants.GRAVITY);
		gravitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			Constants.PlayerConstants.GRAVITY = newValue.doubleValue();
		});

		pipeXSpeedLabel.textProperty().bind(pipeXSpeedSlider.valueProperty().asString("%.0f"));
		pipeXSpeedSlider.valueProperty().set(Constants.PlayerConstants.PIPES_SPEED_X);
		pipeXSpeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			Constants.PlayerConstants.PIPES_SPEED_X = newValue.doubleValue();
			Constants.PlayerConstants.REWARD_SPEED_X = Constants.PlayerConstants.PIPES_SPEED_X;
		});

		pipeYSpeedLabel.textProperty().bind(pipeYSpeedSlider.valueProperty().asString("%.0f"));
		pipeYSpeedSlider.valueProperty().set(Constants.PlayerConstants.PIPES_SPEED_Y);
		pipeYSpeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			Constants.PlayerConstants.PIPES_SPEED_Y = newValue.doubleValue();
		});

		pipeGapXLabel.textProperty().bind(pipeGapXSlider.valueProperty().asString("%.0f"));
		pipeGapXSlider.valueProperty().set(Constants.PlayerConstants.PIPE_GAP_X);
		pipeGapXSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			Constants.PlayerConstants.PIPE_GAP_X = newValue.doubleValue();
			Constants.PlayerConstants.REWARD_GAP_X = Constants.PlayerConstants.PIPE_GAP_X + Constants.PlayerConstants.PIPE_WIDTH;
		});

		pipeGapYLabel.textProperty().bind(pipeGapYSlider.valueProperty().asString("%.0f"));
		pipeGapYSlider.valueProperty().set(Constants.PlayerConstants.PIPE_GAP_Y);
		pipeGapYSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			Constants.PlayerConstants.PIPE_GAP_Y = newValue.doubleValue();
		});

		pipeWidthLabel.textProperty().bind(pipeWidthSlider.valueProperty().asString("%.0f"));
		pipeWidthSlider.valueProperty().set(Constants.PlayerConstants.PIPE_WIDTH);
		pipeWidthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			Constants.PlayerConstants.PIPE_WIDTH = newValue.doubleValue();
			Constants.PlayerConstants.REWARD_GAP_X = Constants.PlayerConstants.PIPE_GAP_X + Constants.PlayerConstants.PIPE_WIDTH;
		});

		jumpSpeedLabel.textProperty().bind(jumpSpeedSlider.valueProperty().asString("%.0f"));
		jumpSpeedSlider.valueProperty().set(-Constants.PlayerConstants.JUMP_SPEED);
		jumpSpeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			Constants.PlayerConstants.JUMP_SPEED = -newValue.doubleValue();
		});

		rewardProbabilityLabel.textProperty().bind(rewardProbabilitySlider.valueProperty().asString("%.2f"));
		rewardProbabilitySlider.valueProperty().set(Constants.PlayerConstants.REWARD_PROBABILITY);
		rewardProbabilitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			Constants.PlayerConstants.REWARD_PROBABILITY = newValue.doubleValue();
		});

		godModeLabel.setText(Constants.PlayerConstants.GOD_MODE ? "ON" : "OFF");
		godModeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			godModeLabel.setText(newValue.intValue() == 0 ? "OFF" : "ON");
			Constants.PlayerConstants.GOD_MODE = newValue.intValue() != 0;
		});
		godModeSlider.valueProperty().set(Constants.PlayerConstants.GOD_MODE ? 1 : 0);
	}

}
