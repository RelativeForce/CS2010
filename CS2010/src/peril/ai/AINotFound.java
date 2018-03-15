package peril.ai;

/**
 * A {@link RuntimeException} that is caused by attempting to load an AI that
 * does not exist.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-03-18
 *
 */
public final class AINotFound extends RuntimeException {

	/**
	 * A unique identify for this sub type of {@link RuntimeException}.
	 */
	private static final long serialVersionUID = 53250525159311217L;

	/**
	 * Constructs a new {@link AINotFound} {@link RuntimeException} with the default
	 * message.
	 */
	public AINotFound() {
		super("AI not found.");
	}

	/**
	 * Constructs a new {@link AINotFound} {@link RuntimeException} with a specified
	 * message.
	 * 
	 * @param message
	 *            The message for this {@link AINotFound} exception.
	 */
	public AINotFound(String message) {
		super(message);
	}

}
