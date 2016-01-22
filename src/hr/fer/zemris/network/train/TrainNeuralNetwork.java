package hr.fer.zemris.network.train;

import hr.fer.zemris.game.model.GameModelAI;
import hr.fer.zemris.game.model.GameModelAITrainable;
import hr.fer.zemris.network.GeneticProgram;
import hr.fer.zemris.network.NeuralNetwork;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TrainNeuralNetwork extends Application {

	private Timeline gameLoop;
	private boolean paused = false;

	@Override
	public void start(Stage primaryStage) throws Exception {
		GameModelAI model = new GameModelAITrainable();
		model.traceableProperty().set(true);
		NeuralNetwork network = new GeneticProgram().train();
		serialization(network);
		
		model.addEnvironmentListener(network);

		Scene scene = new Scene(model.getGamePane(), 1000, 600);

		primaryStage.setScene(scene);
		primaryStage.show();

		gameLoop = new Timeline(new KeyFrame(Duration.millis(1000 / 30), e -> {
			model.update(1);
		}));
		gameLoop.setCycleCount(Animation.INDEFINITE);
		gameLoop.play();
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
	
	void serialization(NeuralNetwork network) {
        
        Path p = Paths.get("weights.ser");
        
        try (OutputStream networkOut = Files.newOutputStream(p);
                ObjectOutputStream out = new ObjectOutputStream(networkOut);) {
            out.writeObject(network);
            System.out.printf("Serialized data is saved in " + p.toAbsolutePath());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
    }

}
