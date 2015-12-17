package hr.fer.zemris.game.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GameModelLazy extends GameModel {

    public BooleanProperty started;

    public GameModelLazy() {
        super();
        started = new SimpleBooleanProperty(false);
        started.bind(jump);
    }

    @Override
	public boolean update(int time) {
    	if(!started.getValue()) {
    		bird.updateFrame();
    		return false;
    	} else {
    		started.unbind();
    	}
    	return super.update(time);
    }

}
