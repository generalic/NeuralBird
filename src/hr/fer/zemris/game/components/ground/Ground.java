package hr.fer.zemris.game.components.ground;

import hr.fer.zemris.game.components.IComponent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by jcular on 1/8/16.
 */
public class Ground extends ImageView implements IComponent {

    public Ground(double xPosition, double yPosition){
        Image groundImage = new Image(Ground.class.getResource("ground.png").toExternalForm());
        setImage(groundImage);

        setX(xPosition);
        setY(yPosition);
    }

    @Override
    public double getRightMostX() {
        return getBoundsInParent().getMaxX();
    }

    @Override
    public double getLeftMostX() {
       return getBoundsInParent().getMinX();
    }

    @Override
    public void translate(double dx) {
        setX(getX() - dx);
    }

}
