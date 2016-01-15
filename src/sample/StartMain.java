package sample;

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
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StartMain extends Application {

    private static final double MAX_FPS = 100;
    private static final double MIN_FPS = 1;
    private static final double DEFAULT_FPS = 30;

    private Timeline gameLoop;
    private GameModel model;
    private Group menuGroup;
    private Pane modelGroup;
    private Group sceneGroup;

    private Button resetButton;
    private Scene scene;
    private NeuralNetwork network;

    private DoubleProperty fps;
    private Slider fpsSlider;

    private boolean paused;


    Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = fxmlLoader.load();

        controller = fxmlLoader.getController();

        setButtons();

        menuGroup = new Group(root);

        initGameLoop();
        gameLoop.stop();

        fps = new SimpleDoubleProperty(DEFAULT_FPS);
        fpsSlider = new Slider(MIN_FPS, MAX_FPS, fps.get());
        fps.bind(fpsSlider.valueProperty());
        gameLoop.rateProperty().bind(fps);


        sceneGroup = new Group();
        sceneGroup.getChildren().addAll(menuGroup);

        scene = new Scene(sceneGroup, 1000, 600);

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        primaryStage.show();

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

        resetButton = new Button("RESET");

        controller.playButton.setOnAction(this::runGamePlayer);
        controller.playAIButton.setOnAction(this::runGameAI);
        controller.quitButton.setOnAction(e -> Platform.exit());
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
            System.err.println("Successfully deserialized.");
        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private Pane getGameEnvironment() {
        Pane gameWorld = new Pane(modelGroup);

        String image = StartMain.class.getResource("backgroundPicture.jpg").toExternalForm();

        gameWorld.setStyle(
                "-fx-background-image: url('" + image + "'); " +
                "-fx-background-size: cover; " +
                "-fx-background-repeat: stretch; " +
                "-fx-background-size: 1000 600;"
        );

        return gameWorld;
    }


    private void runGame(ActionEvent event) {
        resetButton.setVisible(false);
        menuGroup.setVisible(false);

        modelGroup = model.getGamePane();
        modelGroup.getChildren().add(new VBox(5, fpsSlider, resetButton));

        sceneGroup.getChildren().addAll(getGameEnvironment());

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
