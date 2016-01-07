package start;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import start.game_play_fxml.ai_fxml.AIScreen;
import start.game_play_fxml.player_fxml.PlayerScreen;

public class MainMenuController {

	@FXML
	public Button playButton;

	@FXML
	public Button playAIButton;

	@FXML
	public Button settingsButton;

	@FXML
	public Button quitButton;

	private Scene scene;

	@FXML
	public void runGamePlayer(ActionEvent event) {
		new PlayerScreen(scene);
	}

	@FXML
	public void runGameAI(ActionEvent event) {
		new AIScreen(scene);
	}

	@FXML
	public void openSettings(ActionEvent event) {

	}

	@FXML
	public void quitGame(ActionEvent event) {
		Platform.exit();
	}

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
		ScaleTransition transition = new ScaleTransition(Duration.millis(50), button);
		transition.setToX(1.03);
		transition.setToY(1.03);
		transition.play();
	}

	private void buttonMouseExited(Button button) {
		ScaleTransition transition = new ScaleTransition(Duration.millis(50), button);
		transition.setToX(1);
		transition.setToY(1);
		transition.play();
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

}
