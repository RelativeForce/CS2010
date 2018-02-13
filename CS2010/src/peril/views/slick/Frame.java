package peril.views.slick;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * <h3>Basic Description</h3>
 * <p>
 * Holds all the visual elements of the frame of the game. This object also
 * provides operations for retrieving {@link Clickable} objects based on a click
 * at a certain {@link Point} on screen. Each frame that is rendered the current
 * objects drawn on the frame must be cleared using
 * {@link Frame#newFrame(Graphics)}.
 * </p>
 * <h3>Conceptual Description</h3>
 * <p>
 * The frame is broken in to 'planes'. These are {@link List}s of objects that
 * can be drawn without overlapping each other. The reason for doing this is, in
 * a one plane frame, no objects overlap and therefore a click at any
 * {@link Point} on the frame can only intercept one object. Based on this any
 * object that overlaps with an object that is already drawn (found using
 * {@link Region#overlap(Region, Region)}) is placed on the 'plane' above that
 * of the current object.
 * </p>
 * <p>
 * A click at any {@link Point} on a multi-'plane' frame works by processing
 * whether an object was clicked on the 'top' plane using
 * {@link Clickable#isClicked(Point)} and if not checking all the planes below.
 * </p>
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-13
 * @version 1.01.01
 * 
 * @see Clickable
 * @see Point
 * @see Region
 * @see List
 * 
 */
public final class Frame {

	/**
	 * The {@link List} of planes that contain all the {@link Entry}s drawn on
	 * {@link Frame}.
	 */
	private final List<List<Entry>> planes;

	/**
	 * The {@link Graphics} of the current frame.
	 */
	public Graphics g;

	/**
	 * Constructs a new {@link Frame};
	 */
	public Frame() {
		planes = new ArrayList<>();
	}

	/**
	 * Clears the current contents of the {@link Frame} and prepares the
	 * {@link Frame} for drawing objects.
	 * 
	 * @param g
	 *            {@link Graphics} of the new frame.
	 */
	public void newFrame(Graphics g) {
		this.g = g;

		clear();

		// Add the base plane
		final LinkedList<Entry> newPlane = new LinkedList<>();
		planes.add(newPlane);

	}

	/**
	 * Draws a line from on {@link Point} to another.
	 * 
	 * @param a
	 *            {@link Point}
	 * @param b
	 *            {@link Point}
	 */
	public void drawLine(Point a, Point b) {
		g.drawLine(a.x, a.y, b.x, b.y);
	}

	/**
	 * Draws a {@link Clickable} object on screen. The {@link EventListener} defines
	 * the operations that will be performed if certain conditions are met. Also
	 * {@link EventListener#draw(Frame)} defines how composite objects are drawn
	 * (potentially recursively).
	 * 
	 * @param item
	 *            {@link Clickable} object to be drawn on screen.
	 * @param listener
	 *            {@link EventListener} defines the operations that will be
	 *            performed if certain conditions are met.
	 * 
	 * @see EventListener
	 */
	public void draw(Clickable item, EventListener listener) {

		// Add the object to the planes.
		addToPlanes(new Entry(item, listener));
	}

	/**
	 * Draws a {@link Button} on screen.
	 * 
	 * @param button
	 *            {@link Button} to be drawn.
	 */
	public void draw(Button button) {

		// Holds the entry that only processes click operations.
		final Entry entry = new Entry(button, new EventListener() {

			@Override
			public final void mouseHover(Point mouse, int delta) {
				// Do nothing
			}

			@Override
			public final void mouseClick(Point mouse, int mouseButton) {
				button.click();
			}

			@Override
			public final void buttonPress(int Key, Point mouse) {
				// Do nothing
			}

			@Override
			public final void draw(Frame frame) {
				// Draw the button image.
				g.drawImage(button.getImage(), button.getPosition().x, button.getPosition().y);
			}
		});

		addToPlanes(entry);
	}

	/**
	 * Draws an {@link Image} on screen that does not have any operations that will
	 * be performed if certain conditions are met. Can be used in drawing composite
	 * objects.
	 * 
	 * @param image
	 *            {@link Image} to be drawn on screen.
	 * @param x
	 *            The x position on screen.
	 * @param y
	 *            The y position on screen.
	 */
	public void draw(Image image, int x, int y) {
		g.drawImage(image, x, y);
	}

	/**
	 * Draws text on the screen using a specified {@link Font}. Cannot be clicked.
	 * 
	 * @param font
	 *            {@link Font} the text will be drawn in.
	 * @param text
	 *            Text to be drawn on screen.
	 * @param xThe
	 *            x position on screen.
	 * @param y
	 *            The y position on screen.
	 */
	public void draw(Font font, String text, int x, int y) {
		font.draw(g, text, x, y);
	}

	/**
	 * Draws a solid oval on screen.
	 * 
	 * @param x
	 *            The x position on screen.
	 * @param y
	 *            The y position on screen.
	 * @param width
	 *            The width of the oval.
	 * @param height
	 *            The height of the oval.
	 */
	public void fillOval(int x, int y, int width, int height) {
		g.fillOval(x, y, width, height);

	}

	/**
	 * Draws a hollow rectangle on screen.
	 * 
	 * @param x
	 *            The x position on screen.
	 * @param y
	 *            The y position on screen.
	 * @param width
	 *            The width of the rectangle.
	 * @param height
	 *            The height of the rectangle.
	 */
	public void drawRect(int x, int y, int width, int height) {
		g.drawRect(x, y, width, height);
	}

	/**
	 * Draws a solid rectangle on screen.
	 * 
	 * @param x
	 *            The x position on screen.
	 * @param y
	 *            The y position on screen.
	 * @param width
	 *            The width of the rectangle.
	 * @param height
	 *            The height of the rectangle.
	 */
	public void fillRect(int x, int y, int width, int height) {
		g.fillRect(x, y, width, height);
	}

	/**
	 * Assigns a new {@link Color} that geometric objects will be drawn in.
	 * 
	 * @param color
	 *            {@link Color}
	 */
	public void setColor(Color color) {
		g.setColor(color);
	}

	/**
	 * Assigns an new background {@link Color} that will be displayed behind the
	 * objects in the {@link Frame}.
	 * 
	 * @param color
	 *            Background {@link Color}.
	 */
	public void setBackground(Color color) {
		g.setBackground(color);
	}

	/**
	 * Assigns a new line width to the geometric objects will be drawn in.
	 * 
	 * @param width
	 *            Line width >0.
	 */
	public void setLineWidth(float width) {
		g.setLineWidth(width);
	}

	/**
	 * Processes a key press while the mouse is at the specified {@link Point}
	 * position on the screen.
	 * 
	 * @param key
	 *            The key that has been pressed.
	 * @param mousePosition
	 *            {@link Point} position of the mouse on the screen.
	 */
	public void pressButton(int key, Point mousePosition) {

		// Performs the button press operation on every object in the frame.
		planes.forEach(plane -> plane.forEach(entry -> entry.handler.buttonPress(key, mousePosition)));
	}

	/**
	 * Processes a mouse click at a {@link Point} on screen.
	 * 
	 * @param click
	 *            The {@link Point} position of the click.
	 * @param button
	 *            The mouse button that was pressed.
	 * @return Whether or not any objects were clicked or not.
	 */
	public boolean click(Point click, int button) {

		// The object that was clicked.
		final Entry clicked = getClicked(click);

		// If no object was clicked.
		if (clicked == null) {
			return false;
		}

		// Process the click
		clicked.handler.mouseClick(click, button);

		return true;

	}

	/**
	 * Retrieves the current {@link Color} that geometric objects are drawn in.
	 * 
	 * @return {@link Color}
	 */
	public Color getColor() {
		return g.getColor();
	}

	/**
	 * Retrieves the current line width that geometric objects are drawn in.
	 * 
	 * @return Line width
	 */
	public float getLineWidth() {
		return g.getLineWidth();
	}

	/**
	 * Retrieves the {@link Entry} that will be intercepted by a click at the
	 * specified {@link Point} starting from the top plane working downwards.
	 * 
	 * @param mouse
	 *            {@link Point}
	 * @return {@link Entry} that was clicked.
	 */
	private Entry getClicked(Point mouse) {

		// Iterated from the top plane downwards.
		for (int planeIndex = planes.size() - 1; planeIndex >= 0; planeIndex--) {

			// Iterate over every entry in the current plain.
			for (Entry entry : planes.get(planeIndex)) {

				// If the entry is clicked return it.
				if (entry.item.isClicked(mouse)) {
					return entry;
				}
			}
		}

		// If no element was clicked.
		return null;
	}

	/**
	 * Adds an specified {@link Entry} to the {@link Frame#planes} and then draws
	 * it.
	 * 
	 * @param entry
	 *            {@link Entry} to be drawn.
	 */
	private void addToPlanes(Entry entry) {

		boolean placed = false;

		// Iterate over each plane starting at top and working to the bottom.
		for (int planeIndex = planes.size() - 1; planeIndex >= 0; planeIndex--) {

			// Whether or not the entry has collided with anything on the current plane.
			boolean collided = false;

			// Holds the current plane.
			final List<Entry> plane = planes.get(planeIndex);

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

		// If the entry was not placed in any of the planes place it on the base plane.
		if (!placed) {
			planes.get(0).add(entry);
		}

		// Draw the entry.
		entry.handler.draw(this);

	}

	/**
	 * Clears all the {@link Entry}s and planes from this {@link Frame}.
	 */
	private void clear() {

		// If there are planes clear them.
		if (!planes.isEmpty()) {
			planes.clear();
		}
	}

	/**
	 * Holds a {@link Clickable} and {@link EventListener} pair that will be stored
	 * in the planes.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @since 2018-02-13
	 * @version 1.01.01
	 *
	 */
	private final class Entry {

		/**
		 * The {@link Clickable} object that is displayed on the {@link Frame}.
		 */
		public final Clickable item;

		/**
		 * The {@link EventListener} that defines the operations that this {@link Entry}
		 * will perform.
		 */
		public final EventListener handler;

		/**
		 * Constructs a new {@link Entry};
		 * 
		 * @param item
		 *            The {@link Clickable} object that is displayed on the
		 *            {@link Frame}.
		 * @param handler
		 *            The {@link EventListener} that defines the operations that this
		 *            {@link Entry} will perform.
		 */
		public Entry(Clickable item, EventListener handler) {
			this.item = item;
			this.handler = handler;
		}

	}

}
