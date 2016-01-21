package start;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MainMenu extends Application {

	private static final String FXML_FILE_NAME = "main_menu.fxml";

	private Stage stage;
	private Scene scene;

	private double dragAnchorX;
	private double dragAnchorY;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
		Parent root = fxmlLoader.load();

		MainMenuController controller = fxmlLoader.getController();

		this.stage = primaryStage;
		this.scene = new Scene(new Group(root), 1024, 700);
		controller.setScene(scene);

		setupScreenDragging();

		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.show();

//		final double duration = 0.6;
//
//		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), scene.getRoot());
//		fadeTransition.setFromValue(0);
//		fadeTransition.setToValue(1);
//
//		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(duration), scene.getRoot());
//		scaleTransition.setFromX(1.3);
//		scaleTransition.setFromY(1.3);
//		scaleTransition.setToX(1);
//		scaleTransition.setToY(1);
//
//		ParallelTransition transitions = new ParallelTransition(fadeTransition, scaleTransition);
//		transitions.setInterpolator(Interpolator.LINEAR);
//		transitions.play();

		final double duration = 0.8;


		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), scene.getRoot());
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);

		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(duration), scene.getRoot());
		scaleTransition.setFromX(0);
		scaleTransition.setFromY(0);
		scaleTransition.setToX(1);
		scaleTransition.setToY(1);

		ParallelTransition transitions = new ParallelTransition(fadeTransition, scaleTransition);
		transitions.play();
	}

	private void setupScreenDragging() {
		scene.setOnMousePressed((MouseEvent me) -> {
			dragAnchorX = me.getScreenX() - stage.getX();
			dragAnchorY = me.getScreenY() - stage.getY();
		});

		//when screen is dragged, translate it accordingly
		scene.setOnMouseDragged((MouseEvent me) -> {
			stage.setX(me.getScreenX() - dragAnchorX);
			stage.setY(me.getScreenY() - dragAnchorY);
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
