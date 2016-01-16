package start;

import javafx.animation.*;
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

	protected void switchScreen(Scene scene, Pane root, Transition transition) {
		Group group = (Group) scene.getRoot();
		Node menuPane = group.getChildren().get(0);

		transition.setOnFinished(e -> {
			group.getChildren().forEach(c -> c.setVisible(false));
			group.getChildren().add(root);
		});

		PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));

		ScaleTransition zoomOutTransition = new ScaleTransition(Duration.seconds(0.5), root);
		zoomOutTransition.setFromX(5);
		zoomOutTransition.setFromY(5);
		zoomOutTransition.setToX(1);
		zoomOutTransition.setToY(1);
		zoomOutTransition.setInterpolator(Interpolator.LINEAR);

		SequentialTransition transitions = new SequentialTransition(transition, pauseTransition, zoomOutTransition);
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
			ScaleTransition zoomInTransition = new ScaleTransition(Duration.seconds(0.5), root);
			zoomInTransition.setFromX(1);
			zoomInTransition.setFromY(1);
			zoomInTransition.setToX(5);
			zoomInTransition.setToY(5);
			zoomInTransition.setInterpolator(Interpolator.LINEAR);
			zoomInTransition.setOnFinished(event -> {
				group.getChildren().remove(root);
				group.getChildren().forEach(c -> c.setVisible(true));
			});

			zoomOutTransition.setNode(menuPane);
			zoomOutTransition.setDuration(Duration.millis(200));

			new SequentialTransition(zoomInTransition, pauseTransition, zoomOutTransition).play();

//			transitions.play();

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
