package start.game_play_fxml.ai_fxml;/**
 * Created by generalic on 4.1.2016..
 */

import hr.fer.zemris.game.model.GameModelAITrainable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import start.engine.GameEngine;

import java.io.IOException;

public class AIScreen2 extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ai_screen.fxml"));

		Pane root = loader.load();

		AIScreenController controller = loader.getController();
		GameEngine engine = new GameEngine(new GameModelAITrainable());
		controller.addGameScreen(engine.getGameNode());

		engine.runGame();

		Scene scene = new Scene(root);
		scene.setOnKeyPressed(engine.getEventHandler());
		primaryStage.setFullScreen(true);
		primaryStage.setScene(scene);

		primaryStage.setTitle("Translate Animation");
		primaryStage.show();
	}
}
