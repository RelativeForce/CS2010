package peril.helpers;

import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;

import peril.Game;
import peril.ui.states.InteractiveState;

/**
 * Handles the music directory and reading {@link Music}
 * from the directory.
 * 
 * @author Joshua_Eddy
 *
 */
public class MusicHelper implements MusicListener{

	/**
	 * The {@link Game} this {@link MusicHelper} is a part of.
	 */
	private Game game;

	/**
	 * Constructs a new {@link MusicHelper}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link MusicHelper} is a part of.
	 */
	public MusicHelper(Game game) {
		this.game = game;
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
			music = new Music(game.assets.music + fileName + ".ogg");
			music.addListener(this);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return music;
	}
	
	/**
	 * When the currently playing {@link Music} has finished, start the
	 * {@link Music} of the {@link Game#getCurrentState()} if the
	 * {@link InteractiveState} has {@link Music}.
	 */
	@Override
	public void musicEnded(Music previousMusic) {

		Music stateMusic = game.getCurrentState().getMusic();

		if (stateMusic != null) {
			stateMusic.play();
		}

	}

	/**
	 * When the {@link Music} playing in this {@link Game} has changed.
	 */
	@Override
	public void musicSwapped(Music music1, Music music2) {

	}


}
