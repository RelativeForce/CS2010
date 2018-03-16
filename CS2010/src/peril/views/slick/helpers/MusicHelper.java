package peril.views.slick.helpers;

import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;

import peril.views.slick.SlickGame;

/**
 * Handles the music directory and reading {@link Music} from the directory.
 * This also serves as the {@link MusicListener} that handles all the
 * {@link SlickGame} music events.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-17
 * @version 1.01.01
 * 
 * @see MusicListener
 * @see SlickGame
 *
 */
public final class MusicHelper implements MusicListener {

	/**
	 * The {@link SlickGame} this {@link MusicHelper} is a part of.
	 */
	private final SlickGame game;

	/**
	 * The path to the directory containing the {@link Music} files.
	 */
	private final String directory;

	/**
	 * Constructs a new {@link MusicHelper}.
	 * 
	 * @param slickGame
	 *            The {@link SlickGame} this {@link MusicHelper} is a part of.
	 * @param directory
	 *            The path to the directory containing the {@link Music} files.
	 */
	public MusicHelper(SlickGame slickGame, String directory) {
		this.game = slickGame;
		this.directory = directory;
	}

	/**
	 * When the currently playing {@link Music} has finished, start the
	 * {@link Music} of the {@link SlickGame#getCurrentState()}.
	 */
	@Override
	public void musicEnded(Music previousMusic) {

		final Music stateMusic = game.getCurrentState().getMusic();

		// If the state has music, play it.
		if (stateMusic != null) {
			stateMusic.play();
		}

	}

	/**
	 * When the {@link Music} playing in this game has changed.
	 */
	@Override
	public void musicSwapped(Music music1, Music music2) {
		// Do nothing
	}

	/**
	 * Reads a specified .ogg file from the music directory that this
	 * {@link MusicHelper} stores.
	 * 
	 * @param fileName
	 *            The file name of the music. OMIT file extension!
	 * @return {@link Music}
	 */
	public Music read(String fileName) {

		Music music = null;

		// Attempt to read the music file.
		try {
			music = new Music(directory + fileName + ".ogg");

			// Add this as music listener.
			music.addListener(this);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return music;
	}

}
