package hr.fer.zemris.game.demo;

import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.game.model.GameModelPlayer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DemoLazy extends Application {

    private Timeline gameLoop;
    private boolean paused;
    private boolean gameOver;
    private GameModel model;

    private Scene scene;

    private Stage window;

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//    	this.window = primaryStage;
//
//    	gameLoop = new Timeline(new KeyFrame(Duration.millis(1000 / 10), e -> {
//            System.out.println("bok");
//            System.out.println(Thread.currentThread());
//        }));
//        gameLoop.setCycleCount(Animation.INDEFINITE);
//        gameLoop.play();
//
//        for(int i = 0; i < 50; i++) {
//        	System.out.println(i);
//        	System.out.println(Thread.currentThread());
//        }
//
//        System.out.println("EROOOOOOOOOOOOOOOOOOR");
//
//
////        initGame(primaryStage);
//    }
    
    

    EventHandler<KeyEvent> action = e -> {
    	if (e.getCode().equals(KeyCode.B)) {
            if (!paused) {
                gameLoop.pause();
            } else {
                gameLoop.play();
            }
            paused ^= true;
        } else if(e.getCode().equals(KeyCode.SPACE)) {
        	if(gameOver) {
        		gameLoop.stop();
        		initGame();
        		startGame();
        	}
        } else {
            model.jumpBird();
        }
    };

    @Override
    public void start(Stage primaryStage) throws Exception {
    	this.window = primaryStage;

    	initGame();
    	startGame();


    }

    private void initGame() {
    	this.model = new GameModelPlayer();
    	this.paused = false;
    	this.gameOver = false;
    	Pane root = new Pane(model.getGroup());
    	String image = DemoLazy.class.getResource("backgroundPicture.jpg").toExternalForm();
    	root.setStyle("-fx-background-image: url('" + image + "'); " +
    	           "-fx-background-size: cover; ");
    	this.scene = new Scene(root, 1000, 600);
    	scene.setOnKeyPressed(action);
    	this.gameLoop = new Timeline(new KeyFrame(Duration.millis(1000 / 30), e -> {
            gameOver = !model.update(1);
        }));
        gameLoop.setCycleCount(Animation.INDEFINITE);
    	window.setScene(scene);
        window.show();
    }


    private void startGame() {
        gameLoop.play();
    }

	public static void main(String[] args) {
        launch(args);
    }

}
