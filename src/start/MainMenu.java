package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainMenu extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
		Parent root = fxmlLoader.load();

		MainMenuController controller = fxmlLoader.getController();

		Scene scene = new Scene(new Group(root), 1100, 700);
		controller.setScene(scene);


		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.show();

	}


	public static void main(String[] args) {
		launch(args);
	}
}
