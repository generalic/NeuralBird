package hr.fer.zemris.game.environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.geometry.Dimension2D;

public class EnvironmentSettings implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8736054514542076180L;

	public final int NUMBER_OF_PIPES = 5;
	public final double PIPES_SPEED_X = 10;
    public final double PIPES_SPEED_Y = 5;
    public final double JUMP_SPEED = -27;
    public final double PIPE_GAP_X = 300;
    public final double PIPE_GAP_Y = 200;


    public final double PIPE_WIDTH = 20;


    public final double INITIAL_PIPE_OFFSET = 100;

    public final double REWARD_GAP_X = PIPE_GAP_X + PIPE_WIDTH;

    public final double WIDTH = 1000;
    public final double HEIGHT = 600;

    public Dimension2D getDimension() {
    	return new Dimension2D(WIDTH, HEIGHT);
    }

	public static void main(String[] args) {
		EnvironmentSettings s = new EnvironmentSettings();
		Path p = Paths.get("F:/settings" + s.PIPE_WIDTH + ".ser");

//		try(
//			OutputStream settingsOut = Files.newOutputStream(p);
//			ObjectOutputStream out = new ObjectOutputStream(settingsOut);
//		) {
//			out.writeObject(s);
//			System.out.printf("Serialized data is saved in " + p);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		EnvironmentSettings ss = null;
		try(
				InputStream settingsIn = Files.newInputStream(p);
				ObjectInputStream in = new ObjectInputStream(settingsIn);
			) {
				ss = (EnvironmentSettings) in.readObject();
				System.out.println("Successfully deserialized.");
		} catch (IOException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(ss.PIPE_WIDTH);
	}

}
