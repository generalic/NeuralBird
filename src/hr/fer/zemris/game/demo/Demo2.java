package hr.fer.zemris.game.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.network.GeneticProgram;
import hr.fer.zemris.network.NeuralNetwork;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Demo2 extends Application {

	private Timeline gameLoop;
	private boolean paused = false;

	@Override
	public void start(Stage primaryStage) throws Exception {

		GameModel model = new GameModel();
		NeuralNetwork network = new GeneticProgram().train();
		model.addEnvironmentListener(network);

		Scene scene = model.getScene();

		primaryStage.setScene(scene);
		primaryStage.show();

		gameLoop = new Timeline(new KeyFrame(Duration.millis(1000 / 30), e -> {
			model.update(1);
		}));
		gameLoop.setCycleCount(Animation.INDEFINITE);
		gameLoop.play();

		scene.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.B)) {
				if(!paused) {
					gameLoop.pause();
				} else {
					gameLoop.play();
				}
				paused ^= true;
			} else {
				model.jumpBird();
			}
		});

	}

	public static void main(String[] args) {
		launch(args);
	}

	public NeuralNetwork deserialisation() {
		Path p = Paths.get("weights.ser");
		NeuralNetwork best = null;
		try(
			InputStream settingsIn = Files.newInputStream(p);
			ObjectInputStream in = new ObjectInputStream(settingsIn);
		) {
			best = (NeuralNetwork) in.readObject();
			System.out.println("Successfully deserialized.");
		} catch (IOException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return best;
	}

}
