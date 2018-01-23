package peril.views.slick.helpers;

import peril.Game;
import peril.io.SaveFile;
import peril.io.MapWriter;
import peril.io.fileParsers.FileParser;
import peril.views.slick.AssetReader;
import peril.views.slick.Container;

/**
 * This is a helper class of {@link Game} which holds all the input output class
 * instances for the {@link Game}.
 * 
 * @author Joshua_Eddy
 *
 */
public class IOHelper {

	/**
	 * The {@link AssetReader} that loads all the {@link InteractiveState}'s
	 * assets into the game from memory.
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
	}

	/**
	 * Saves the {@link ModelBoard} from the {@link Game} to a specified
	 * {@link SaveFile}.
	 * 
	 * @param game
	 *            {@link Game}
	 * @param file
	 *            {@link SaveFile}
	 */
	public void saveBoard(Game game, SaveFile file) {
		new MapWriter(game, game.assets.maps + game.board.getName(), file).write();
	}

}
