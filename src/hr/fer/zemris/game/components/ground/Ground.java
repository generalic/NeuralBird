package hr.fer.zemris.game.components.ground;

import hr.fer.zemris.game.physics.Physics;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jcular on 1/8/16.
 */
public class Ground{

    private List<ImageView> groundList;
    private Group groundGroup;
    private double y;

    public Ground(double yPosition){
        y = yPosition;
        groundList = new LinkedList<ImageView>();
        groundGroup = new Group();

        Image groundImage = new Image(Ground.class.getResource("ground.png").toExternalForm());
        groundList.add(new ImageView(groundImage));
        groundList.add(new ImageView(groundImage));

        double xPosition = 0;
        for (ImageView groundImageView : groundList) {
            groundGroup.getChildren().add(groundImageView);

            groundImageView.setX(xPosition);
            groundImageView.setY(yPosition);

            xPosition = xPosition + groundImageView.getBoundsInLocal().getMaxX();
        }
    }

    public double getY() {
        return y;
    }

    public Group getGroundGroup(){
        return groundGroup;
    }

    public void moveGround(int time, double velocity){
        for (int i = 0; i < groundList.size(); i++) {
            groundList.get(i).setX(groundList.get(i).getX() - Physics.calculateShiftX(velocity, time));
            if (groundList.get(i).getBoundsInLocal().getMaxX() < 0) {
                groundList.get(i).setX(groundList.get(i + 1).getBoundsInParent().getMaxX());
                groundList.add(groundList.get(i));
                groundList.remove(i);
            }
        }
    }
}
