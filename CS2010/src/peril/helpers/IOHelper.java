package peril.helpers;

import java.io.File;

import peril.Game;
import peril.io.SaveFile;
import peril.io.MapWriter;
import peril.io.fileParsers.AssetReader;
import peril.io.fileParsers.FileParser;
import peril.io.fileReaders.MusicReader;
import peril.ui.Container;
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
	 * Constructs a new {@link IOHelper}.
	 * 
	 * @param game
	 *            The instance of {@link Game} that this {@link IOHelper} helps.
	 * @param containers
	 *            {@link Container}s that the {@link FileParser}s may add elements
	 *            to.
	 */
	public IOHelper(Game game, Container[] containers) {
		this.mainMenuLoader = new AssetReader(containers, "menu.txt", game);
		this.gameLoader = new AssetReader(containers, "game.txt", game);
		this.musicHelper = new MusicReader(game);
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
		new MapWriter(game, game.assets.maps + File.separatorChar + game.board.getName(), file).write();
	}

}
