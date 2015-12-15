package hr.fer.zemris.game.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.network.NeuralNetwork;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class PlayGameAI implements Runnable {
    
    private boolean paused = false;
    private Timeline gameLoop;
    private Group group;
    private Scene scene;
    
    public PlayGameAI(Group group, Scene scene) {
        this.group = group;
        this.scene = scene;
    }
    
    @Override
    public void run() {
        
        GameModel model = new GameModel();
        NeuralNetwork network = deserialisation();// new GeneticProgram().train();
        
        group.getChildren().clear();
        
        model.addEnvironmentListener(network);
        model.fillGroup(group);
        
        gameLoop = new Timeline(new KeyFrame(Duration.millis(1000 / 30), e -> {
            model.update(1);
        }));
        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.play();
        
        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.B)) {
                if (!paused) {
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
    
    public NeuralNetwork deserialisation() {
        
        Path p = Paths.get("weights.ser");
        NeuralNetwork best = null;
        try (InputStream settingsIn = Files.newInputStream(p);
                ObjectInputStream in = new ObjectInputStream(settingsIn);) {
            best = (NeuralNetwork) in.readObject();
            System.out.println("Successfully deserialized.");
        } catch (IOException | ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return best;
    }
}
