package peril;

/**
 * 
 * Provides the path to the game asset folders. All paths end in the system
 * specific path separator character. ('/' on windows).
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.03
 * @since 2018-02-18
 *
 */
public interface Directory {

	/**
	 * The file path string to the folder containing the UI images.
	 * 
	 * @return UI images file path
	 */
	String getUIPath();

	/**
	 * The file path string to the folder which contains the map folder(s). Each sub
	 * folder has the same name as the map it contains.
	 * 
	 * @return Maps file path
	 */
	String getMapsPath();

	/**
	 * The file path string to the music folder.
	 * 
	 * @return Music file path
	 */
	String getMusicPath();

	/**
	 * The file path string to the folder containing the dice images.
	 * 
	 * @return Dice file path
	 */
	String getDicePath();

	/**
	 * The file path to the folder containing the button images.
	 * 
	 * @return Buttons file path
	 */
	String getButtonsPath();

	/**
	 * The file path to the folder containing the unit icons.
	 * 
	 * @return Units file path
	 */
	String getUnitsPath();

	/**
	 * The file path to the folder containing the player images.
	 * 
	 * @return Player file path
	 */
	String getPlayersPath();

	/**
	 * The file path to the folder containing the hazard icons.
	 * 
	 * @return Hazards file path
	 */
	String getHazardsPath();

	/**
	 * Uses the specified map name to construct the file path to the folder that
	 * contains the assets of specified map.
	 * 
	 * @param mapName
	 *            Name of the map.
	 * @return Map file path
	 */
	String asMapPath(String mapName);

	/**
	 * The file path to the folder containing the splash page frames.
	 * 
	 * @return Splash file path
	 */
	String getSplashPath();

	/**
	 * The file path to the folder containing the text files.
	 * @return Text file path
	 */
	String getTextPath();
}
