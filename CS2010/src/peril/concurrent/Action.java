package peril.concurrent;

import java.util.function.Consumer;

/**
 * Encapsulates a function and object pair that can be executed at a later time.
 * This can be used as a form of pseudo functional programming architecture.
 * 
 * 
 * @author Joshua_Eddy
 *
 * @param <T>
 *            The type of element that the {@link Consumer} inside
 *            <code>this</code> action will perform its task on.
 * @version 1.01.01
 * @since 2018-02-06
 */
public final class Action<T> {

	/**
	 * Whether the {@link Action} is complete or not. The {@link Action} can be
	 * performed multiple times so this should be flagged <code>true</code> upon
	 * first completion of the {@link Action#task}.
	 */
	private volatile boolean done;

	/**
	 * The task this {@link Action} will perform.
	 */
	private final Consumer<T> task;

	/**
	 * The {@link T} that {@link Action#task} is performed on.
	 */
	private final T object;

	/**
	 * Constructs a new {@link Action} using a function.
	 * 
	 * @param task
	 *            {@link Consumer}.
	 * @param object
	 *            The object that this action will be performed on.
	 */
	public Action(T object, Consumer<T> task) {

		// Check parameters
		if (object == null) {
			throw new NullPointerException("The object cannot be null");
		} else if (task == null) {
			throw new NullPointerException("The task cannot be null");
		}

		// Assign the task as initially incomplete.
		this.done = false;
		this.task = task;
		this.object = object;
	}

	/**
	 * Checks if the {@link Action} is complete or not.
	 * 
	 * @return <code>boolean</code> completion state of {@link Action#task}.
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * Performs {@link Action#task}. When the action is finished
	 * {@link Action#isDone()} will return <code>true</code>.
	 */
	public void execute() {

		// Execute the task on the object
		task.accept(object);

		// Assign this task as complete.
		done = true;
	}

}
