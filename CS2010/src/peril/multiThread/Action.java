package peril.multiThread;

import java.util.function.Consumer;

/**
 * Encapsulates a function and object pair that can be executed at a later time.
 * This can be used as a for of pseudo functional programming architecture.
 * 
 * 
 * @author Joshua_Eddy
 *
 * @param <T>
 *            The type of element that the {@link Consumer} inside
 *            <code>this</code> {@link Action} will perform its task on.
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
