package hr.fer.zemris.game.demo;

import hr.fer.zemris.game.model.GameModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Demo extends Application {

    private Timeline gameLoop;
    private boolean paused = false;

    @Override
    public void start(Stage primaryStage) throws Exception {

        GameModel model = new GameModel();
        Scene scene = model.getScene();

        primaryStage.setScene(scene);
        primaryStage.show();

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

    public static void main(String[] args) {

        launch(args);
    }

}
