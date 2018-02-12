package peril.views.slick;

import java.util.ArrayList;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Holds all the visual elements of the frame.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Frame {

	private final ArrayList<LinkedList<Entry>> planes;

	public final Graphics g;

	public Frame(Graphics g) {
		planes = new ArrayList<>();

		// Add the base plane
		final LinkedList<Entry> newPlane = new LinkedList<>();
		planes.add(newPlane);

		this.g = g;
	}

	public void drawLine(Point a, Point b) {
		g.drawLine(a.x, a.y, b.x, b.y);
	}

	public void draw(Clickable item, EventListener listener) {
		addToPlane(new Entry(item, listener));
	}

	public void draw(Button button) {

		final Entry entry = new Entry(button, new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {
				button.click();
			}

			@Override
			public void buttonPress(int Key, Point mouse) {
				// Do nothing
			}

			@Override
			public void draw(Frame frame) {
				g.drawImage(button.getImage(), button.getPosition().x, button.getPosition().y);
			}
		});

		addToPlane(entry);
	}

	public void draw(Image image, int x, int y) {
		g.drawImage(image, x, y);
	}

	public void draw(Font font, String text, int x, int y) {
		font.draw(g, text, x, y);
	}

	public void setColor(Color color) {
		g.setColor(color);
	}

	public void setBackground(Color color) {
		g.setBackground(color);
	}

	public void setLineWidth(float width) {
		g.setLineWidth(width);
	}

	public boolean click(Point click, int button) {

		boolean clickProcessed = false;

		for (int planeIndex = planes.size() - 1; planeIndex >= 0; planeIndex--) {

			for (Entry entry : planes.get(planeIndex)) {

				if (entry.item.isClicked(click)) {
					entry.handler.mouseClick(click, button);
					clickProcessed = true;
					break;
				}
			}

			if (clickProcessed) {
				break;
			}
		}

		return clickProcessed;

	}

	private void addToPlane(Entry entry) {

		boolean placed = false;

		/**
		 * Iterate over each plane starting at top and working to the bottom.
		 */
		for (int planeIndex = planes.size() - 1; planeIndex >= 0; planeIndex--) {

			// Whether or not the entry has collided with anything on the current plane.
			boolean collided = false;

			// Holds the current plane.
			final LinkedList<Entry> plane = planes.get(planeIndex);

			// Iterate through every element on the current plane and check if the entry
			// collides with them.
			for (Entry planeEntry : plane) {

				if (Region.overlap(entry.item.getRegion(), planeEntry.item.getRegion())) {
					collided = true;
					break;
				}

			}

			// If the entry collided with the current plane then place it on the plane
			// above.
			if (collided) {

				// If the current plane is the top plane add another plane.
				if (planeIndex == planes.size() - 1) {
					final LinkedList<Entry> newPlane = new LinkedList<>();
					planes.add(newPlane);
				}

				// Add the entry to the plane above the current one.
				planes.get(planeIndex + 1).add(entry);
				placed = true;
			}
		}

		if (!placed) {
			planes.get(0).add(entry);
		}

		entry.handler.draw(this);

	}

	public void fillRect(int x, int y, int width, int height) {
		g.fillRect(x, y, width, height);
	}

	public Color getColor() {
		return g.getColor();
	}

	public float getLineWidth() {
		return g.getLineWidth();
	}

	public void fillOval(int x, int y, int width, int height) {
		g.fillOval(x, y, width, height);

	}

	public void drawRect(int x, int y, int width, int height) {
		g.drawRect(x, y, width, height);
	}

	private final class Entry {

		public final Clickable item;

		public final EventListener handler;

		public Entry(Clickable item, EventListener handler) {
			this.item = item;
			this.handler = handler;
		}

	}

}
