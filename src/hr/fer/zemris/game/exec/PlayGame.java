package hr.fer.zemris.game.exec;

import hr.fer.zemris.game.model.GameModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class PlayGame implements Runnable {
    
    private boolean paused = false;
    private Timeline gameLoop;
    private Group group;
    private Scene scene;
    
    public PlayGame(Group group, Scene scene) {
        this.group = group;
        this.scene = scene;
    }
    
    @Override
    public void run() {
        
        GameModel model = new GameModel();
        
        group.getChildren().clear();
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
}
