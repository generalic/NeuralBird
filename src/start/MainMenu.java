package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainMenu extends Application {

	private Stage stage;
	private Scene scene;

	private double dragAnchorX;
	private double dragAnchorY;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
		Parent root = fxmlLoader.load();

		MainMenuController controller = fxmlLoader.getController();

		this.stage = primaryStage;
		this.scene = new Scene(new Group(root), 1100, 700);
		controller.setScene(scene);

		setupScreenDragging();

		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.show();
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
