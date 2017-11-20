package peril.helpers;

import java.io.File;

import peril.Challenge;
import peril.Game;
import peril.io.SaveFile;
import peril.io.MapWriter;
import peril.io.fileParsers.AssetReader;
import peril.io.fileParsers.ChallengeReader;
import peril.io.fileReaders.MusicReader;
import peril.ui.states.gameStates.CoreGameState;

/**
 * This is a helper class of {@link Game} which holds all the input output class
 * instances for the {@link Game}.
 * 
 * @author Joshua_Eddy
 *
 */
public class IOHelper {

	/**
	 * The {@link MusicReader} for this {@link Game}.
	 */
	public final MusicReader musicHelper;

	/**
	 * The {@link AssetReader} that loads all the {@link CoreGameState} states
	 * buttons into the game from memory.
	 */
	public final AssetReader gameLoader;

	/**
	 * The {@link AssetReader} that loads all the game visuals from memory.
	 */
	public final AssetReader mainMenuLoader;

	/**
	 * {@link ChallengeReader} that loads the {@link Challenge}s from memory.
	 */
	public final ChallengeReader challengeLoader;

	private final String mapsDirectory;

	/**
	 * Constructs a new {@link IOHelper}.
	 * 
	 * @param gameLoader
	 *            The {@link AssetReader} that loads all the {@link CoreGameState}
	 *            states buttons into the game from memory.
	 * @param musicHelper
	 *            The {@link MusicReader} for this {@link Game}.
	 * @param mainMenuLoader
	 *            The {@link AssetReader} that loads all the game visuals from
	 *            memory.
	 * @param challengeReader
	 *            {@link ChallengeReader} that loads the {@link Challenge}s from
	 *            memory.
	 * @param mapsDirectory
	 *            The directory containing all the map folders.
	 */
	public IOHelper(String mapsDirectory, AssetReader gameLoader, MusicReader musicHelper, AssetReader mainMenuLoader,
			ChallengeReader challengeReader) {
		this.challengeLoader = challengeReader;
		this.mainMenuLoader = mainMenuLoader;
		this.gameLoader = gameLoader;
		this.musicHelper = musicHelper;
		this.mapsDirectory = mapsDirectory;
	}

	/**
	 * Saves the {@link Board} from the {@link Game} to a specified
	 * {@link SaveFile}.
	 * 
	 * @param game
	 *            {@link Game}
	 * @param file
	 *            {@link SaveFile}
	 */
	public void saveBoard(Game game, SaveFile file) {
		new MapWriter(game, mapsDirectory + File.separatorChar + game.board.getName(), file).write();
	}

}
