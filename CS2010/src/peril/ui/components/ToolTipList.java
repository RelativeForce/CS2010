package peril.ui.components;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Point;

public class ToolTipList {
	
	private Map<String, Delay> toolTips;

	private Point position;

	public ToolTipList(Point position) {
		this.toolTips = new IdentityHashMap<>();
		this.position = position;
	}

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

	public void elapseTime() {

		// Holds the elements to be removed.
		List<String> toRemove = new LinkedList<>();

		toolTips.keySet().forEach(element -> {
			if (toolTips.get(element).hasElapsed()) {
				toRemove.add(element);
			}
		});

		// Remove all the elapsed elements.
		toRemove.forEach(challenge -> toolTips.remove(challenge));
	}

	public void add(String text, long delay) {
		toolTips.put(text, new Delay(delay));
	}

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
		 * The number of {@link Delay#hasElapsed()} executions before this {@link Delay}
		 * has elapsed.
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
		 * @return Whether this {@link Delay} has elapsed or not.
		 */
		public boolean hasElapsed() {
			time--;
			return time == 0;
		}

	}

}
