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
		this.g = g;
	}

	public void drawLine(Point a, Point b) {
		g.drawLine(a.x, a.y, b.x, b.y);
	}

	public void draw(Clickable item, EventListener listener) {
		
	}
	
	public void draw(Button button) {
		addToPlane(button);
		g.drawImage(button.getImage(), button.getPosition().x, button.getPosition().y);
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

	public boolean click(Point click) {

		boolean clickProcessed = false;

		for (int planeIndex = planes.size() - 1; planeIndex >= 0; planeIndex--) {

			for (Entry button : planes.get(planeIndex)) {

				if (button.item.isClicked(click)) {
					button.handler.mouseClick(click);
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
	
	private void addToPlane(Button button) {

		boolean placed = false;

		final Entry entry = new Entry(button, new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse) {
				button.click();
			}

			@Override
			public void buttonPress(int Key, Point mouse) {
				// Do nothing
			}
		});

		for (LinkedList<Entry> plane : planes) {

			boolean collided = false;

			for (Entry planeButton : plane) {

				if (Region.overlap(button.getRegion(), planeButton.item.getRegion())) {
					collided = true;
				}

			}

			if (!collided) {
				placed = true;
				plane.add(entry);

				break;
			}
		}

		if (!placed) {

			final LinkedList<Entry> newPlane = new LinkedList<>();
			newPlane.add(entry);
			planes.add(newPlane);
		}
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
