package peril;

import java.util.function.Consumer;

import peril.ui.UserInterface;

/**
 * Encapsulates a task that is passed form the {@link UserInterface} to the
 * background {@link Game} thread. This exists solely as a wrapper for a
 * task.<br>
 * <br>
 * 
 * Example: Parsing an input using the {@link Game} background thread where
 * 'game' is the {@link UserInterface}'s local instance of a {@link Game}.<br>
 * <br>
 * <code>
 * Action&lt;Game&gt; action = new Action&lt;Game&gt;(<br>
 * &nbsp game,<br>
 * &nbsp game->game.parseInput(clickPosition)<br>);<br>
 * game.addAction(action);<br>
 * while(!action.isDone()){<br>
 * &nbsp // spin a waiting wheel<br>
 * }
 * </code>
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
