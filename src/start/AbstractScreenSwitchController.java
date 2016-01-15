package start;

import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import start.game_play_fxml.IScreenController;

public abstract class AbstractScreenSwitchController implements IScreenController {

	@FXML
	public Button backButton;

	public static Duration zoomInDuration = Duration.seconds(0.5);
	public static final Duration defaultZoomInDuration = Duration.seconds(0.5);

	public static Duration pauseDuration = Duration.millis(100);
	public static final Duration defaultPauseDuration = Duration.millis(100);

	protected void switchScreen(Scene scene, Pane root) {
		Group group = (Group) scene.getRoot();
		Node menuPane = group.getChildren().get(0);

		ScaleTransition zoomInTransition = new ScaleTransition(zoomInDuration, menuPane);
		zoomInTransition.setFromX(1);
		zoomInTransition.setFromY(1);
		zoomInTransition.setToX(5);
		zoomInTransition.setToY(5);
		zoomInTransition.setInterpolator(Interpolator.LINEAR);

		ScaleTransition zoomOutTransition = new ScaleTransition(Duration.seconds(0.5), root);
		zoomOutTransition.setFromX(5);
		zoomOutTransition.setFromY(5);
		zoomOutTransition.setToX(1);
		zoomOutTransition.setToY(1);
		zoomOutTransition.setInterpolator(Interpolator.LINEAR);

		PauseTransition pauseTransition = new PauseTransition(pauseDuration);
		pauseTransition.setOnFinished(e -> {
			group.getChildren().forEach(c -> c.setVisible(false));
			group.getChildren().add(root);
		});
		SequentialTransition transitions = new SequentialTransition(zoomInTransition, pauseTransition, zoomOutTransition);
		transitions.play();

//		Group group = (Group) scene.getRoot();
//		group.getChildren().forEach(c -> c.setVisible(false));
//		group.getChildren().add(root);

//		FadeTransition transition = new FadeTransition(Duration.seconds(2), group);
//		transition.setFromValue(0);
//		transition.setToValue(1);
//		transition.setInterpolator(Interpolator.LINEAR);
//		transition.play();

//		ScaleTransition transition = new ScaleTransition(Duration.seconds(1), group);
//		transition.setFromX(6);
//		transition.setFromY(9);
//		transition.setToX(1);
//		transition.setToY(1);
//		transition.setInterpolator(Interpolator.EASE_OUT);
//		transition.play();

//		ScaleTransition transition = new ScaleTransition(Duration.millis(500), root);
//		transition.setFromX(1);
//		transition.setFromY(1);
//		transition.setToX(2);
//		transition.setToY(2);
//		transition.setAutoReverse(true);
//		transition.setCycleCount(2);

//		ScaleTransition transition = new ScaleTransition(Duration.seconds(1), root);
//		transition.setFromX(2);
//		transition.setFromY(2);
//		transition.setToX(1);
//		transition.setToY(1);
//		transition.setInterpolator(Interpolator.EASE_OUT);
//		transition.play();

		backButton.setOnAction(e -> {
			zoomInTransition.setDuration(defaultZoomInDuration);
			pauseTransition.setDuration(defaultPauseDuration);

			zoomInTransition.setNode(root);
			zoomOutTransition.setNode(menuPane);
			zoomOutTransition.setDuration(Duration.millis(200));

			pauseTransition.setOnFinished(event -> {
				group.getChildren().remove(root);
				group.getChildren().forEach(c -> c.setVisible(true));
			});

			transitions.play();

//			group.getChildren().remove(root);
//			group.getChildren().forEach(c -> c.setVisible(true));
//
//			ScaleTransition backTransition = new ScaleTransition(Duration.millis(500), menuPane);
//			backTransition.setFromX(15);
//			backTransition.setFromY(15);
//			backTransition.setToX(1);
//			backTransition.setToY(1);
//			backTransition.play();
		});
	}

}
