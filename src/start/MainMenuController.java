package start;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import start.game_play_fxml.ai_fxml.AIScreen;
import start.game_play_fxml.player_fxml.PlayerScreen;
import start.settings_fxml.SettingsScreen;

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
		new PlayerScreen(scene, getTransition());
	}

	@FXML
	public void runGameAI(ActionEvent event) {
		new AIScreen(scene, getTransition());
	}

	@FXML
	public void openSettings(ActionEvent event) {
		new SettingsScreen(scene, getTransition());
	}

	private Transition getTransition() {
		Group group = (Group) scene.getRoot();
		Node menuPane = group.getChildren().get(0);

		ScaleTransition zoomInTransition = new ScaleTransition(Duration.seconds(0.5), menuPane);
		zoomInTransition.setFromX(1);
		zoomInTransition.setFromY(1);
		zoomInTransition.setToX(5);
		zoomInTransition.setToY(5);
		zoomInTransition.setInterpolator(Interpolator.LINEAR);

		return zoomInTransition;
	}

	@FXML
	public void quitGame(ActionEvent event) {
		final double duration = 0.8;

		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), scene.getRoot());
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);

		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(duration), scene.getRoot());
		scaleTransition.setFromX(1);
		scaleTransition.setFromY(1);
		scaleTransition.setToX(0);
		scaleTransition.setToY(0);

		ParallelTransition transitions = new ParallelTransition(fadeTransition, scaleTransition);
		transitions.setOnFinished(e -> Platform.exit());
		transitions.play();
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
