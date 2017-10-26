package peril.board;

/**
 * Encapsulates the behaviours of a collection of units. This will be aggregated
 * by the {@link CombatHandler} and composes the {@link Country}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Army {

	/**
	 * The size of the army.
	 */
	private int size;

	/**
	 * Constructs a new empty {@link Army}.
	 */
	public Army() {
		setSize(0);
	}

	/**
	 * Constructs a new {@link Army} of a specified size.
	 * @param size The size of the {@link Army}. Must be greater than zero.
	 */
	public Army(int size) {
		setSize(size);
	}
	
	/**
	 * Assigns a new size to this {@link Army}.
	 * @param size The new size of the {@link Army}. Must be greater than zero.
	 */
	public void setSize(int size){
		if (size < 0) {
			throw new IllegalArgumentException("Size must be greater than zero");
		}	
		this.size = size;
	}
	
	/**
	 * Retrieves the size of the {@link Army}.
	 * @return 
	 */
	public int getSize(){
		return size;
	}
}
