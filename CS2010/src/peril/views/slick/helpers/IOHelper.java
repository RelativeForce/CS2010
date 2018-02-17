package peril.views.slick.helpers;

import java.util.Set;

import peril.controllers.GameController;
import peril.io.SaveFile;
import peril.io.MapWriter;
import peril.views.slick.Container;
import peril.views.slick.SlickGame;
import peril.views.slick.io.AssetReader;

/**
 * This is a helper class of {@link SlickGame} which holds all the input/output
 * objects.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-17
 * @version 1.01.01
 *
 * @see AssetReader
 * @see SlickGame
 */
public final class IOHelper {

	/**
	 * The {@link AssetReader} that loads all the {@link InteractiveState}'s assets
	 * into the game from memory.
	 */
	public final AssetReader gameLoader;

	/**
	 * The {@link AssetReader} that loads all the main menu game from memory.
	 */
	public final AssetReader mainMenuLoader;

	/**
	 * Constructs a new {@link IOHelper}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows the {@link IOHelper} to
	 *            interact with the game.
	 * @param containers
	 *            The {@link Container}s that the {@link AssetReader}s may add elements
	 *            to.
	 */
	public IOHelper(GameController game, Set<Container> containers) {
		this.mainMenuLoader = new AssetReader(containers, "menu.txt", game);
		this.gameLoader = new AssetReader(containers, "game.txt", game);
	}

	/**
	 * Saves the {@link ModelBoard} from the {@link Game} to a specified
	 * {@link SaveFile}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows the {@link IOHelper} to
	 *            interact with the game.
	 * @param file
	 *            The {@link SaveFile} that the game will be saved to.
	 */
	public void saveBoard(GameController game, SaveFile file) {
		new MapWriter(game, file).write();
	}

}
