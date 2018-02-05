package peril.controllers;

public interface Directory {

	/**
	 * The file path string to the UI components folder.
	 * 
	 * @return file path/
	 */
	String getUIPath();

	/**
	 * The file path string to the maps folder.
	 * 
	 * @return file path/
	 */
	String getMapsPath();
	
	/**
	 * Returns a String that is the path to the music assets.
	 * 
	 * @return String path to the music assets.
	 */
	String getMusicPath();
	
	String getDicePath();

	String getButtonsPath();
	
	String getUnitsPath();
	
	String getPlayersPath();
	
	String getHazardsPath();
	
	String asMapPath(String mapName);
	
}
