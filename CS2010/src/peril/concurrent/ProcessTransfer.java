package peril.concurrent;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * A thread safe queue that allows {@link Action}s to be passed from
 * thread to thread. This object uses the singleton pattern and is accessible
 * via {@link ProcessTransfer#getInstane()}.
 * 
 * @author Joshua_Eddy
 * @version 1.01.01
 * @since 2018-02-06
 * @see LinkedTransferQueue
 *
 */
public final class ProcessTransfer {

	/**
	 * The singleton instance of {@link ProcessTransfer}.
	 */
	private final static ProcessTransfer INSTANCE = new ProcessTransfer();

	/**
	 * This {@link Queue} holds instructions stored as objects for the background
	 * thread to execute. This allows for an asynchronous execution resulting is a
	 * responsive user interface.
	 * 
	 * @see Queue
	 * @see LinkedTransferQueue
	 */
	private final Queue<Action<?>> buffer;

	/**
	 * Constructs the single {@link ProcessTransfer}.
	 */
	private ProcessTransfer() {
		this.buffer = new LinkedTransferQueue<>();
	}

	/**
	 * Executes the first {@link Action} in the {@link Queue} of actions to be
	 * executed. This removes the {@link Action} from the {@link Queue}.
	 */
	public void poll() {

		// If the buffer is not empty the execute the first.
		if (!buffer.isEmpty()) {
			buffer.poll().execute();
		}
	}

	/**
	 * Adds a {@link Action} to the {@link ProcessTransfer} {@link Queue}. This
	 * {@link Action} will only be executed when a thread calls
	 * {@link ProcessTransfer#poll()}.
	 * 
	 * @param action
	 *            {@link Action} to perform.
	 */
	public void transfer(Action<?> action) {

		// Null check.
		if (action == null) {
			throw new IllegalArgumentException("Action cannot be null.");
		}

		buffer.add(action);

	}

	/**
	 * Retrieves if the {@link ProcessTransfer} {@link Queue} is empty.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean isEmpty() {
		return buffer.isEmpty();
	}

	/**
	 * Retrieve the singleton instance of {@link ProcessTransfer}.
	 * 
	 * @return {@link ProcessTransfer}
	 */
	public static ProcessTransfer getInstane() {
		return INSTANCE;
	}

}
