package peril;

/**
 * The internal representation of a user of the system. This object will hold
 * all of the details about a users game such as the number of {@link Country}s
 * that user has.
 * 
 * @author Joshua_Eddy
 * @author Ezekiel_Trinidad
 *
 */
public enum Player {
	PLAYER_ONE("Player 1"), PLAYER_TWO("Player 2"), PLAYER_THREE("Player 3"), PLAYER_FOUR("Player 4");
	
	/**
	 * String Representation of the {@link Player}.
	 */
	private final String name;
	
	/**
	 * Constructs a new {@link Player}.
	 * 
	 * @param name String Representation of the {@link Player}.
	 */
	private Player(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
