package hr.fer.zemris.game.demo;

import hr.fer.zemris.game.environment.Constants;
import hr.fer.zemris.game.model.GameModelAI;
import hr.fer.zemris.network.NeuralNetwork;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Demo3 extends Application {

	private Timeline gameLoop;
	private boolean paused = false;
	private NeuralNetwork network;
	private Constants constants;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		GameModelAI model = new GameModelAI();
		deserialisation();
		model.setConstants(constants);
		
		
		model.addEnvironmentListener(network);

		Scene scene = new Scene(model.getGroup(), 1000, 600);

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

	public void deserialisation() {
		Path p = Paths.get("weights.ser");

		try(
			InputStream settingsIn = Files.newInputStream(p);
			ObjectInputStream in = new ObjectInputStream(settingsIn);
		) {
			network = (NeuralNetwork) in.readObject();
			constants = (Constants) in.readObject();
			System.out.println("Successfully deserialized.");
		} catch (IOException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	

}
