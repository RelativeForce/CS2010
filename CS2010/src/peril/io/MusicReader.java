package peril.io;

import java.io.File;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import peril.Game;

/**
 * A wrapper class for handling the music directory and reading {@link Music}
 * from the directory.
 * 
 * @author Joshua_Eddy
 *
 */
public class MusicReader {

	/**
	 * The path to the music directory.
	 */
	private String musicDirectory;

	/**
	 * The {@link Game} this {@link MusicReader} is a part of.
	 */
	private Game game;

	/**
	 * Constructs a new {@link MusicReader}.
	 * 
	 * @param musicDirectory
	 *            The path to the music directory.
	 * @param game
	 *            The {@link Game} this {@link MusicReader} is a part of.
	 */
	public MusicReader(String musicDirectory, Game game) {
		this.musicDirectory = musicDirectory;
		this.game = game;
	}

	/**
	 * Reads a specified .ogg file from the music directory that this
	 * {@link MusicReader} stores.
	 * 
	 * @param fileName
	 *            The file name of the music. OMIT file extension!
	 * @return {@link Music}
	 */
	public Music read(String fileName) {
		Music music = null;
		// Attempt to read the music file.
		try {
			music = new Music(musicDirectory + File.separatorChar + fileName + ".ogg");
			music.addListener(game);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return music;
	}
}