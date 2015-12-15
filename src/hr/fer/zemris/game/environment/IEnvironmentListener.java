package hr.fer.zemris.game.environment;

public interface IEnvironmentListener {

	void environmentChanged(IEnvironmentProvider provider, EnvironmentVariables variables);

}
