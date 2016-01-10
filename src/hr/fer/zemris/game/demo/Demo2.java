package hr.fer.zemris.game.demo;

import hr.fer.zemris.game.environment.Constants;
import hr.fer.zemris.game.model.GameModelAITrainable;
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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Demo2 extends Application {

	private Timeline gameLoop;
	private boolean paused = false;

	@Override
	public void start(Stage primaryStage) throws Exception {

		GameModelAITrainable model = new GameModelAITrainable();
		NeuralNetwork network = new GeneticProgram().train();
		serialization(network, model.getConstants());
		
		
		model.addEnvironmentListener(network);

		Scene scene = new Scene(model.getGamePane(), 1000, 600);

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
		Constants constants = null;
		try(
			InputStream settingsIn = Files.newInputStream(p);
			ObjectInputStream in = new ObjectInputStream(settingsIn);
		) {
			best = (NeuralNetwork) in.readObject();
			constants = (Constants) in.readObject();
			System.out.println("Successfully deserialized.");
		} catch (IOException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return best;
	}
	
	void serialization(NeuralNetwork network, Constants constants) {
        
        Path p = Paths.get("weights.ser");
        
        try (OutputStream networkOut = Files.newOutputStream(p);
                ObjectOutputStream out = new ObjectOutputStream(networkOut);) {
            out.writeObject(network);
            out.writeObject(constants);
            System.out.printf("Serialized data is saved in " + p.toAbsolutePath());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
    }

}
