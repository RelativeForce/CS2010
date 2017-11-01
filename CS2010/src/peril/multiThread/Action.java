package peril.multiThread;

import java.util.function.Consumer;

import peril.Game;

/**
 * Encapsulates a task that is passed form the {@link Game} to the
 * background {@link Game} thread. This exists solely as a wrapper for a
 * task.
 * 
 * 
 * @author Joshua_Eddy
 *
 * @param <T>
 *            The type of element that the {@link Consumer} inside
 *            <code>this</code> action will perform its task on.
 */
public class Action<T> {

	/**
	 * Whether the action is complete or not.
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
		task.accept(object);
		done = true;
	}

}
