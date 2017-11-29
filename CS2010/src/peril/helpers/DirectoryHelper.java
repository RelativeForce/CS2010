package peril.helpers;

import java.io.File;

/**
 * Helps the {@link Game} by holding the directory paths of the various asset
 * types.
 * 
 * @author Joshua_Eddy
 *
 */
public class DirectoryHelper {

	/**
	 * The path to the ui folder.
	 */
	public final String ui;

	/**
	 * The path to the maps folder.
	 */
	public final String maps;

	/**
	 * The path to the music folder.
	 */
	public final String music;

	/**
	 * Constructs a new {@link DirectoryHelper}.
	 * 
	 * @param assetsPath
	 *            The path to the assets directory.
	 */
	public DirectoryHelper(String assetsPath) {

		// Set the directory paths for the different types of game assets.
		maps = new StringBuilder(assetsPath).append(File.separatorChar).append("maps").toString();
		music = new StringBuilder(assetsPath).append(File.separatorChar).append("music").toString();
		ui = new StringBuilder(assetsPath).append(File.separatorChar).append("ui").toString();

	}

}
