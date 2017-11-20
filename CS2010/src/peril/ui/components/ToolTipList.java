package peril.ui.components;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Point;

/**
 * Encapsulates the behaviours of a list of tool tips that appear on screen for
 * a set amount of time then disappear.
 * 
 * @author Joshua_Eddy
 *
 */
public class ToolTipList {

	/**
	 * Holds the text tool tip that will be displayed to the use and its assigned
	 * delay until it is removed from the users view.
	 */
	private Map<String, Delay> toolTips;

	/**
	 * The {@link Point} position of this {@link ToolTipList}.
	 */
	private Point position;

	/**
	 * Constructs a new {@link ToolTipList}.
	 * 
	 * @param position
	 */
	public ToolTipList(Point position) {
		this.toolTips = new IdentityHashMap<>();
		this.position = position;
	}

	/**
	 * Draws this {@link ToolTipList} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	public void draw(Graphics g) {

		int x = position.x;
		int y = position.y;

		// Draw a box back drop
		g.setColor(Color.lightGray);
		g.fillRect(x, y, 400, toolTips.size() * 20);

		// Add padding
		x += 2;
		y += 2;

		// Draw each challenge on screen.
		g.setColor(Color.black);
		for (String element : toolTips.keySet()) {
			g.drawString(element, x, y);
			y += 15;
		}

	}

	/**
	 * Elapses the specified time for the {@link ToolTipList}.
	 * 
	 * @param delta
	 *            Time passed in milliseconds
	 */
	public void elapseTime(int delta) {

		// Holds the elements to be removed.
		List<String> toRemove = new LinkedList<>();

		toolTips.keySet().forEach(element -> {
			if (toolTips.get(element).hasElapsed(delta)) {
				toRemove.add(element);
			}
		});

		// Remove all the elapsed elements.
		toRemove.forEach(challenge -> toolTips.remove(challenge));
	}

	/**
	 * Adds a tool tip to this {@link ToolTipList}.
	 * 
	 * @param text
	 *            The text that will be displayed to the user.
	 * @param delay
	 *            The amount of time in milliseconds that the new tool tip will be
	 *            display for.
	 */
	public void add(String text, long delay) {
		toolTips.put(text, new Delay(delay));
	}

	/**
	 * Clears all the tool tips from the {@link ToolTipList}.
	 */
	public void clear() {
		toolTips.clear();
	}

	/**
	 * Encapsulates the behaviour of a time delay.
	 * 
	 * @author Joshua_Eddy
	 *
	 */
	private class Delay {

		/**
		 * The time in milliseconds before this {@link Delay} has elapsed.
		 */
		private long time;

		/**
		 * Constructs a new {@link Delay}.
		 * 
		 * @param time
		 *            The number of {@link Delay#hasElapsed()} executions before this
		 *            {@link Delay} has elapsed.
		 */
		public Delay(long time) {
			if (time <= 0) {
				throw new IllegalArgumentException("Delay time cannot be <= zero.");
			}
			this.time = time;
		}

		/**
		 * Reduces {@link Delay#time} and retrieves whether it has elapsed or not.
		 * 
		 * @param delta
		 *            The time that has passed in milliseconds
		 * 
		 * @return Whether this {@link Delay} has elapsed or not.
		 */
		public boolean hasElapsed(int delta) {
			time -= delta;
			return time <= 0;
		}

	}

}
