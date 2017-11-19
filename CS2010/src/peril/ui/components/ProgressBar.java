package peril.ui.components;

import org.newdawn.slick.Graphics;

import peril.Point;

/**
 * Encapsulates the behaviours of a progress bar that can be desipayed on
 * screen.
 * 
 * @author Joshua_Eddy
 *
 */
public final class ProgressBar {

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
	 */
	public ProgressBar() {
		this.total = 0;
		this.progress = 0;
	}

	/**
	 * Initialise the {@link ProgressBar}.
	 * 
	 * @param position
	 *            The {@link Point} position of the {@link ProgressBar}.
	 * @param width
	 *            The pixel width of this {@link ProgressBar} on screen.
	 * @param height
	 *            The pixel height of this {@link ProgressBar} on screen.
	 */
	public void init(Point position, int width, int height) {
		this.height = height;
		this.width = width;
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
		progress++;
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
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {

		// Calculate the current progress on screen
		int currentProgress = (int) (progress * (((double) width) / total));

		// Draw the empty bar
		g.drawRect(position.x, position.y, width, height);

		// Draw the progress
		if (currentProgress > 0) {
			g.fillRect(position.x, position.y, currentProgress, height);
		}

	}

}
