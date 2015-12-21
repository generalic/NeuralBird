package hr.fer.zemris.game.model;

import hr.fer.zemris.game.components.reward.Reward;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GameModelLazy extends GameModel {

    public BooleanProperty started;

    public GameModelLazy() {
        super();
        this.started = new SimpleBooleanProperty(false);
        started.bind(jump);
    }

    @Override
	public boolean update(int time) {
    	if(!started.getValue()) {
    		bird.updateFrame();
    		rewards.forEach(Reward::updateFrame);
    		return true;
    	} else {
    		started.unbind();
    	}
    	return super.update(time);
    }

    @Override
    public void reset() {
        super.reset();
        started.bind(jump);
    }

}
