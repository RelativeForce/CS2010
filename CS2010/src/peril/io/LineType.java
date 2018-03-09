package peril.io;

/**
 * 
 * The strings that start the lines in a map files.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-09
 * @version 1.01.01
 * 
 * @see MapWriter
 *
 */
public enum LineType {

	/**
	 * A country line.
	 */
	COUNTRY("Country"),
	/**
	 * A unit line.
	 */
	UNIT("Unit"),
	/**
	 * A link line.
	 */
	LINK("Link"),
	/**
	 * A continent line.
	 */
	CONTINENT("Continent"),
	/**
	 * A state line.
	 */
	STATE("State"),
	/**
	 * A player line.
	 */
	PLAYER("Player"),
	/**
	 * The army size challenge line.
	 */
	ARMY_SIZE("ArmySize"),
	/**
	 * The countries owned challenge line.
	 */
	COUNTRIES_OWNED("CountriesOwned"),
	/**
	 * A units killed challenge line.
	 */
	UNITS_KILLED("UnitsKilled");

	/**
	 * The string that starts this {@link LineType}.
	 */
	public final String text;

	/**
	 * Constructs a new {@link LineType}.
	 * 
	 * @param text
	 *            The string that starts this {@link LineType}.
	 */
	private LineType(String text) {
		this.text = text;
	}

}
