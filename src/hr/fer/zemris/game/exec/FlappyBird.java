package hr.fer.zemris.game.exec;

import java.util.Optional;
import java.util.function.Consumer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FlappyBird extends Application {
    
    private static final double WIDTH = 1000.0;
    private static final double HEIGHT = 600.0;
    private static final double BUTTON_WIDTH = WIDTH / 3.0;
    private static final double BUTTON_HEIGHT = HEIGHT / 6.0;
    private static final Paint DEFAULT_HIGHLIGHT_PAINT = Color.DARKGRAY;
    private static final Paint DEFAULT_CLICK_PAINT = Color.SLATEGRAY;
    private static final Paint DEFAULT_PAINT = Color.BLACK;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        Group group = new Group();
        
        Rectangle playGame = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        Rectangle playGameAI = new Rectangle(BUTTON_WIDTH, 2.5 * BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        Rectangle exit = new Rectangle(BUTTON_WIDTH, 4.0 * BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        Label playGameText = new Label("I will play");
        Label playGameAIText = new Label("Let A.I. play");
        Label exitText = new Label("Exit");
        Font font = new Font(playGameText.getFont().getFamily(), 30);
        
        playGameText.setFont(font);
        playGameAIText.setFont(font);
        exitText.setFont(font);
        playGameText.setTextFill(Color.WHITE);
        playGameAIText.setTextFill(Color.WHITE);
        exitText.setTextFill(Color.WHITE);
        group.getChildren().addAll(playGame, playGameAI, exit, playGameText, playGameAIText, exitText);
        
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        playGameText.relocate(BUTTON_WIDTH + (BUTTON_WIDTH - playGameText.getWidth()) / 2.0,
                BUTTON_HEIGHT + (BUTTON_HEIGHT - playGameText.getHeight()) / 2.0);
        playGameAIText.relocate(BUTTON_WIDTH + (BUTTON_WIDTH - playGameAIText.getWidth()) / 2.0,
                2.5 * BUTTON_HEIGHT + (BUTTON_HEIGHT - playGameAIText.getHeight()) / 2.0);
        exitText.relocate(BUTTON_WIDTH + (BUTTON_WIDTH - exitText.getWidth()) / 2.0,
                4.0 * BUTTON_HEIGHT + (BUTTON_HEIGHT - exitText.getHeight()) / 2.0);
                
        setMouseEvents(playGame, playGameText, new PlayGame(group, scene));
        setMouseEvents(playGameAI, playGameAIText, new PlayGameAI(group, scene));
        setMouseEvents(exit, exitText, () -> {
            Platform.exit();
        });
    }
    
    private static void setMouseEvents(Rectangle rectangle, Label label, Runnable clickAction) {
        
        setMouseEnteredEvent(rectangle, label);
        setMouseExitedEvent(rectangle, label);
        setMousseClickedEvent(rectangle, label, clickAction);
    }
    
    private static void setMousseClickedEvent(Rectangle rectangle, Label label, Runnable action) {
        
        setMouseEventOfType(rectangle, rectangle::setOnMouseClicked, DEFAULT_CLICK_PAINT, Optional.of(action));
        setMouseEventOfType(rectangle, label::setOnMouseClicked, DEFAULT_CLICK_PAINT, Optional.of(action));
    }
    
    private static void setMouseEnteredEvent(Rectangle rectangle, Label label) {
        
        setMouseEventOfType(rectangle, rectangle::setOnMouseEntered, DEFAULT_HIGHLIGHT_PAINT, Optional.empty());
        setMouseEventOfType(rectangle, label::setOnMouseEntered, DEFAULT_HIGHLIGHT_PAINT, Optional.empty());
    }
    
    private static void setMouseExitedEvent(Rectangle rectangle, Label label) {
        
        setMouseEventOfType(rectangle, rectangle::setOnMouseExited, DEFAULT_PAINT, Optional.empty());
        setMouseEventOfType(rectangle, label::setOnMouseExited, DEFAULT_PAINT, Optional.empty());
    }
    
    private static void setMouseEventOfType(Rectangle rectangle, Consumer<EventHandler<? super MouseEvent>> event,
            Paint paint, Optional<Runnable> aditionalAction) {
            
        event.accept(e -> {
            rectangle.setFill(paint);
            
            if (aditionalAction.isPresent()) {
                aditionalAction.get().run();
            }
        });
    }
    
    public static void main(String[] args) {
        
        launch(args);
    }
}
