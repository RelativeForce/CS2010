package peril.helpers;

import java.io.File;

import peril.controllers.Directory;

/**
 * Provides the file paths to all the game assets. Realises {@link Directory}.
 * 
 * @author Joshua_Eddy
 * 
 * @see Directory
 * @version 1.01.01
 * @since 2018-02-06
 *
 */
public final class DirectoryHelper implements Directory {

	/**
	 * The path to the assets directory.
	 */
	private final String base;

	/**
	 * The name of the user interface folder.
	 */
	private static final String UI = "ui";

	/**
	 * The name of the maps folder.
	 */
	private static final String MAPS = "maps";

	/**
	 * The name of the music folder.
	 */
	private static final String MUSIC = "music";

	/**
	 * The name of the folder that contains the dice images nested inside the user
	 * interface folder.
	 */
	private static final String DICE = "dice";

	/**
	 * The name of the folder that contains the button images nested inside the user
	 * interface folder.
	 */
	private static final String BUTTONS = "buttons";

	/**
	 * The name of the folder that contains the unit images nested inside the user
	 * interface folder.
	 */
	private static final String UNITS = "units";

	/**
	 * The name of the folder containing the players images nested inside the user
	 * interface folder.
	 */
	private static final String PLAYERS = "players";

	/**
	 * The name of the folder containing the hazard icons nested inside the user
	 * interface folder.
	 */
	private static final String HAZARDS = "hazards";

	/**
	 * Constructs a new {@link DirectoryHelper}.
	 * 
	 * @param assetsPath
	 *            The path to the assets directory.
	 */
	public DirectoryHelper(String assetsPath) {
		base = assetsPath;
	}

	@Override
	public String getUIPath() {
		return base + fileSeperator() + UI + fileSeperator();
	}

	@Override
	public String getMapsPath() {
		return base + fileSeperator() + MAPS + fileSeperator();
	}

	@Override
	public String getMusicPath() {
		return base + fileSeperator() + MUSIC + fileSeperator();
	}

	@Override
	public String getDicePath() {
		return getUIPath() + DICE + fileSeperator();
	}

	@Override
	public String getButtonsPath() {
		return getUIPath() + BUTTONS + fileSeperator();
	}

	@Override
	public String getUnitsPath() {
		return getUIPath() + UNITS + fileSeperator();
	}

	@Override
	public String getPlayersPath() {
		return getUIPath() + PLAYERS + fileSeperator();
	}

	@Override
	public String getHazardsPath() {
		return getUIPath() + HAZARDS + fileSeperator();
	}

	@Override
	public String asMapPath(String mapName) {
		return getMapsPath() + mapName + fileSeperator();
	}

	/**
	 * Retrieves the system specific file separator character ('/' on windows).
	 * 
	 * @return File separator character
	 */
	private char fileSeperator() {
		return File.separatorChar;
	}

}
