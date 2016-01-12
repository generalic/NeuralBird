package hr.fer.zemris.game.model;

import hr.fer.zemris.game.components.reward.Reward;
import hr.fer.zemris.game.environment.Constants;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GameModelPlayer extends GameModel {

    public BooleanProperty started;

    public GameModelPlayer() {
		super();
        this.started = new SimpleBooleanProperty(false);
        started.bind(jump);
    }

	@Override
	protected Constants provideConstants() {
		return Constants.PlayerConstants;
	}

    @Override
	public boolean update(int time) {
    	if(!started.getValue()) {
			bird.updateFrame();
			moveGround(time);
			rewards.forEach(Reward::updateFrame);
			return true;
    	} else {
    		started.unbind();
    	}
    	return super.update(time);
    }

	@Override
	protected void scanEnvironment() {}

	@Override
    public void reset() {
        super.reset();
        started.bind(jump);
    }

}
