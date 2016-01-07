package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class Controller {

	@FXML
	public Button playButton;

	@FXML
	public Button playAIButton;

	@FXML
	public Button settingsButton;

	@FXML
	public Button quitButton;

	@FXML
	public void playMouseEntered(MouseEvent me) {
		buttonMouseEntered(playButton);
	}

	@FXML
	public void playMouseExited(MouseEvent me) {
		buttonMouseExited(playButton);
	}

	@FXML
	public void playAIMouseEntered(MouseEvent me) {
		buttonMouseEntered(playAIButton);
	}

	@FXML
	public void playAIMouseExited(MouseEvent me) {
		buttonMouseExited(playAIButton);
	}

	@FXML
	public void settingsMouseEntered(MouseEvent me) {
		buttonMouseEntered(settingsButton);
	}

	@FXML
	public void settingsMouseExited(MouseEvent me) {
		buttonMouseExited(settingsButton);
	}

	@FXML
	public void quitMouseEntered(MouseEvent me) {
		buttonMouseEntered(quitButton);
	}

	@FXML
	public void quitMouseExited(MouseEvent me) {
		buttonMouseExited(quitButton);
	}

	private void buttonMouseEntered(Button button) {
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.millis(200.0d),
						new KeyValue(button.scaleXProperty(), 1.03),
						new KeyValue(button.scaleYProperty(), 1.03)
				)
		);
		timeline.play();
	}

	private void buttonMouseExited(Button button) {
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.millis(200.0d),
						new KeyValue(button.scaleXProperty(), 1),
						new KeyValue(button.scaleYProperty(), 1)
				)
		);
		timeline.play();
	}

}
