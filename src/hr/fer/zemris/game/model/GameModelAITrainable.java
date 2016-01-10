package hr.fer.zemris.game.model;

import hr.fer.zemris.game.components.IComponent;
import hr.fer.zemris.game.components.pipes.PipePair;
import hr.fer.zemris.game.components.reward.Reward;
import hr.fer.zemris.game.environment.Constants;
import hr.fer.zemris.game.environment.EnvironmentVariables;
import hr.fer.zemris.game.environment.IEnvironmentListener;
import hr.fer.zemris.game.environment.IEnvironmentProvider;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameModelAITrainable extends GameModelAI {

    @Override
    protected void movableGround(int time) {
        //does nothing performance-wise
    }

}
