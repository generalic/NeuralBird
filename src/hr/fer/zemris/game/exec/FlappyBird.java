package hr.fer.zemris.game.exec;

import hr.fer.zemris.game.environment.Constants;
import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.game.model.GameModelAITrainable;
import hr.fer.zemris.game.model.GameModelPlayer;
import hr.fer.zemris.network.NeuralNetwork;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FlappyBird extends Application {

    private static final double MAX_FPS = 100;
	private static final double MIN_FPS = 1;
	private static final double DEFAULT_FPS = 30;

	private Timeline gameLoop;
    private GameModel model;
    private Group menuGroup;
    private Pane modelGroup;
    private Group sceneGroup;
    private VBox verticalContainer;
    private Button playButton;
    private Button playAIButton;
    private Button exitButton;
    private Button optionsButton;
    private Button resetButton;
    private Scene scene;
    private NeuralNetwork network;
    private Constants constants;

    private DoubleProperty fps;
    private Slider fpsSlider;

    private boolean paused;


    @Override
    public void start(Stage primaryStage) throws Exception {
        setButtons();

        menuGroup = new Group();
        verticalContainer = new VBox();

        initGameLoop();
        gameLoop.stop();

        fps = new SimpleDoubleProperty(DEFAULT_FPS);
        fpsSlider = new Slider(MIN_FPS, MAX_FPS, fps.get());
        fps.bind(fpsSlider.valueProperty());
        gameLoop.rateProperty().bind(fps);

        verticalContainer.getChildren().addAll(playButton, playAIButton, optionsButton, exitButton);

        menuGroup.getChildren().add(verticalContainer);
        sceneGroup = new Group();
        sceneGroup.getChildren().addAll(menuGroup);

        setScene();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

	private void setScene() {
		Pane root = new Pane(sceneGroup);
    	String image = FlappyBird.class.getResource("backgroundPicture.jpg").toExternalForm();
    	root.setStyle("-fx-background-image: url('" + image + "'); " +
    	           "-fx-background-size: cover; ");

    	scene = new Scene(root, 1000, 600);
	}

    private void initGameLoop() {
        gameLoop = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
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

        playButton.setOnAction(this::runGamePlayer);
        playAIButton.setOnAction(this::runGameAI);
        exitButton.setOnAction(e -> Platform.exit());
        resetButton.setOnAction(e -> {
            sceneGroup.getChildren().removeAll(modelGroup);
            model.reset();
            menuGroup.setVisible(true);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void runGamePlayer(ActionEvent event) {
        model = new GameModelPlayer();
        runGame(event);
    }

    private void runGameAI(ActionEvent event) {
        deserialisation();

        GameModelAITrainable model = new GameModelAITrainable();
        model.setConstants(constants);
        model.addEnvironmentListener(network);
        this.model = model;

        runGame(event);
    }

    private void deserialisation() {
        Path p = Paths.get("weights.ser");

        try(
            InputStream settingsIn = Files.newInputStream(p);
            ObjectInputStream in = new ObjectInputStream(settingsIn);
        ) {
            network = (NeuralNetwork) in.readObject();
            constants = (Constants) in.readObject();
            System.err.println("Successfully deserialized.");
        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }



    private void runGame(ActionEvent event) {
        resetButton.setVisible(false);
        menuGroup.setVisible(false);

        modelGroup = model.getGamePane();
        modelGroup.getChildren().add(new VBox(5, fpsSlider, resetButton));

        sceneGroup.getChildren().addAll(modelGroup);

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
