package hr.fer.zemris.game.environment;

public interface IEnvironmentProvider {

	/**
     * Adds the given listener.
     *
     * @param listener listener to add
     */
	void addEnvironmentListener(IEnvironmentListener listener);

	/**
     * Removes the given listener.
     *
     * @param listener listener to remove
     */
	void removeEnvironmentListener(IEnvironmentListener listener);

	/**
     * Gets the translation for the given key.
     *
     * @param key used key
     * @return translation of the key
     */
	void react();

}
