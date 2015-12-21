package hr.fer.zemris.game.exec;

import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.game.model.GameModelLazy;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FlappyBird extends Application {

    private Timeline gameLoop;
    private GameModel model;
    private Group menuGroup;
    private Group modelGroup;
    private Group sceneGroup;
    private VBox verticalContainer;
    private Button playButton;
    private Button playAIButton;
    private Button exitButton;
    private Button optionsButton;
    private Button resetButton;
    private Scene scene;
    private boolean started;

    @Override
    public void start(Stage primaryStage) throws Exception {

        setButtons();

        model = new GameModelLazy();
        menuGroup = new Group();
        verticalContainer = new VBox();
        started = true;
 
        initGameLoop();

        gameLoop.stop();

        verticalContainer.getChildren().addAll(playButton, playAIButton, optionsButton, exitButton);

        menuGroup.getChildren().add(verticalContainer);
        sceneGroup = new Group();
        sceneGroup.getChildren().addAll(menuGroup);

        scene = new Scene(sceneGroup, 1000, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initGameLoop() {
        gameLoop = new Timeline(new KeyFrame(Duration.millis(1000 / 30), e -> {
            if (!model.update(1)) {
                gameLoop.stop();
                resetButton.setVisible(true);
            }
        }));
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }

    private void setButtons() {

        playButton = new Button("PLAY");
        playAIButton = new Button("PLAY AI");
        optionsButton = new Button("OPTIONS");
        exitButton = new Button("EXIT");
        resetButton = new Button("RESET");

        playButton.setOnAction(this::runGame);
        playAIButton.setOnAction(this::runGame);
        exitButton.setOnAction(e-> System.exit(0));
        resetButton.setOnAction(e -> {
            sceneGroup.getChildren().removeAll(modelGroup);
            model.reset();
            menuGroup.setVisible(true);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void runGame(ActionEvent event) {

        resetButton.setVisible(false);
        menuGroup.setVisible(false);
        modelGroup = model.getGroup();
        modelGroup.getChildren().add(resetButton);

        sceneGroup.getChildren().addAll(modelGroup);



        gameLoop.play();

        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.B)) {
//                if (!paused) {
//                    gameLoop.pause();
//                } else {
//                    gameLoop.play();
//                }
//                paused ^= true;
            } else {
                model.jumpBird();
            }
        });
    }
}
