package hr.fer.zemris.game.model;

import hr.fer.zemris.game.components.pipes.PipePair;
import hr.fer.zemris.game.components.reward.Reward;
import hr.fer.zemris.game.environment.Constants;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GameModelPlayer extends GameModel {

    public BooleanProperty started;

    public GameModelPlayer() {
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
				ground.moveGround(time, constants.PIPES_SPEED_X);
				rewards.forEach(Reward::updateFrame);
				return true;
    	} else {
    		started.unbind();
    	}
    	return super.update(time);
    }

	@Override
	protected void scanEnvironment() {
		PipePair nearestPipePair = getNearestPairAheadOfBird().get();
		if (!nearestPipePair.equals(lastPassed)) {
			score.set(score.get() + constants.PIPE_PASSED_BONUS);
			lastPassed = nearestPipePair;
		}
	}

	@Override
    public void reset() {
        super.reset();
        started.bind(jump);
    }

}
