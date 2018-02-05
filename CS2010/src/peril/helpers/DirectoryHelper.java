package peril.helpers;

import java.io.File;

import peril.controllers.Directory;

/**
 * Helps the {@link Game} by holding the directory paths of the various asset
 * types.
 * 
 * @author Joshua_Eddy
 *
 */
public final class DirectoryHelper implements Directory {

	private final String base;

	private static final String UI = "ui";

	private static final String MAPS = "maps";

	private static final String MUSIC = "music";

	private static final String DICE = "dice";

	private static final String BUTTONS = "buttons";

	private static final String UNITS = "units";

	private static final String PLAYERS = "players";

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
	
	private char fileSeperator() {
		return File.separatorChar;
	}

}
