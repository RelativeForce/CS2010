package peril.views.slick.components;

import peril.views.slick.Frame;
import peril.views.slick.Point;

/**
 * Encapsulates the behaviours of a progress bar that can be displayed on
 * screen.
 * 
 * @author Joshua_Eddy
 *
 */
public final class ProgressBar implements Component {

	/**
	 * The progress of this {@link ProgressBar}.
	 */
	private int progress;

	/**
	 * The total number tick this {@link ProgressBar} must perform to be to be
	 * completed.
	 */
	private int total;

	/**
	 * The pixel height of this {@link ProgressBar} on screen.
	 */
	private int height;

	/**
	 * The pixel width of this {@link ProgressBar} on screen.
	 */
	private int width;

	/**
	 * The {@link Point} position of the {@link ProgressBar}.
	 */
	private Point position;

	/**
	 * Constructs a new {@link ProgressBar}.
	 * 
	 * @param position
	 *            The {@link Point} position of the {@link ProgressBar}.
	 * @param width
	 *            The pixel width of this {@link ProgressBar} on screen.
	 * @param height
	 *            The pixel height of this {@link ProgressBar} on screen.
	 */
	public ProgressBar(Point position, int width, int height) {
		this.total = 0;
		this.progress = 0;
		this.height = height;
		this.width = width;
		this.position = position;
	}

	/**
	 * Initialise the {@link ProgressBar}.
	 */
	public void init() {
		// DO NOTHING
	}

	/**
	 * Assigns a new width to this {@link ProgressBar}. This may not be performed
	 * after the {@link ProgressBar} has been drawn.
	 * 
	 * @param width
	 *            of the {@link ProgressBar}
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Assigns a new height to this {@link ProgressBar}. This may not be performed
	 * after the {@link ProgressBar} has been drawn.
	 * 
	 * @param height
	 *            of the {@link ProgressBar}
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Assigns a new {@link Point} position to this {@link ProgressBar}. This may
	 * not be performed after the {@link ProgressBar} has been drawn.
	 * 
	 * @param position
	 *            of the {@link ProgressBar}
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * Resets the {@link ProgressBar} to its starting state.
	 */
	public void reset() {
		progress = 0;
		total = 0;
	}

	/**
	 * Increments the progress of the {@link ProgressBar}.
	 */
	public void increment() {

		if (progress == total) {
			return;
		}

		progress++;
	}

	/**
	 * Retrieves the height of the {@link ProgressBar}.
	 * 
	 * @return int
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * The total number tick this {@link ProgressBar} must perform to be to be
	 * completed by a specified value.
	 * 
	 * @param size
	 */
	public void increaseTotal(int size) {
		total += size;
	}

	/**
	 * Draws the {@link ProgressBar} to screen.
	 */
	public void draw(Frame frame) {

		// Calculate the current progress on screen
		int currentProgress = (progress * width) / total;

		// Draw the empty bar
		frame.drawRect(position.x, position.y, width, height);

		// Draw the progress
		if (currentProgress > 0) {
			frame.fillRect(position.x, position.y, currentProgress, height);
		}

	}

	/**
	 * Retrieves the {@link Point} position of this {@link ProgressBar}.
	 */
	@Override
	public Point getPosition() {
		return position;
	}

}
