package peril.views.slick.components;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import peril.views.slick.Frame;
import peril.views.slick.states.InteractiveState;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Encapsulates the behaviours a {@link List} of {@link Element}s that can be
 * displayed on screen and clicked on screen.
 * 
 * @author Joshua_Eddy
 *
 * @param <T>
 *            The type of this lists elements.
 * 
 * @since 2018-03-08
 * @version 1.01.02
 * 
 * @see Clickable
 * @see Component
 */
public final class VisualList<T> extends Clickable implements Component {

	/**
	 * The <code>int</code> width of the {@link VisualList}.
	 */
	private final int width;

	/**
	 * The <code>int</code> height of the {@link VisualList}.
	 */
	private final int height;

	/**
	 * The <code>int</code> number of {@link Element}s that are displayed in the box
	 * at once in the {@link VisualList}.
	 */
	private final int elementsInView;

	/**
	 * {@link List} of {@link Element}s that are displayed in this
	 * {@link VisualList}.
	 */
	private final List<Element> elements;

	/**
	 * The number of pixels that the {@link Element}s will be displayed from the
	 * edge of the left wall.
	 */
	private final int padding;

	/**
	 * Whether or not this {@link VisualList} is visible or not.
	 */
	private boolean visible;

	/**
	 * The {@link Font} of the text representation of the {@link Element}s.
	 */
	private Font font;

	/**
	 * The {@link Element} that is currently selected in the {@link VisualList}.
	 */
	private Element selected;

	/**
	 * The index of the element at the top of the {@link VisualList}.
	 */
	private int topElementIndex;

	/**
	 * Constructs a new {@link VisualList}.
	 * 
	 * @param position
	 *            The {@link Point} position of the {@link VisualList}.
	 * @param width
	 *            The <code>int</code> width of the {@link VisualList}.
	 * @param height
	 *            The <code>int</code> height of the {@link VisualList}.
	 * @param elementsInView
	 *            The <code>int</code> number of {@link Element}s that are displayed
	 *            in the box at once in the {@link VisualList}.
	 * @param padding
	 *            The number of pixels that the {@link Element}s will be displayed
	 *            from the edge of the left wall.
	 */
	public VisualList(Point position, int width, int height, int elementsInView, int padding) {
		super(new Region(width, height * elementsInView, position));
		this.elements = new LinkedList<>();
		this.selected = null;
		this.width = width;
		this.height = height;
		this.elementsInView = elementsInView;
		this.padding = padding;
		this.topElementIndex = 0;
		this.visible = true;
	}

	/**
	 * Add an {@link Element} to the {@link VisualList}.
	 * 
	 * @param text
	 *            The text to represent the {@link Element}.
	 * 
	 * @param payload
	 *            {@link Element}
	 */
	public void add(String text, T payload) {

		Element element = new Element(text, payload,
				new Point(getPosition().x, getPosition().y + (elements.size() * height)), width, height);

		// If this is the first element the set it as the selected.
		if (elements.isEmpty()) {
			selected = element;
		}

		elements.add(element);
	}

	/**
	 * Set the {@link Point} position of this {@link VisualList}.
	 */
	@Override
	public void setPosition(Point position) {

		Point change = new Point(position.x - this.getPosition().x, position.y - this.getPosition().y);

		elements.forEach(element -> element
				.setPosition(new Point(element.getPosition().x + change.x, element.getPosition().y + change.y)));

		super.setPosition(position);
	}

	/**
	 * Retrieves the selected {@link Element} from the {@link VisualList}.
	 * 
	 * @return {@link Element}
	 */
	public T getSelected() {
		return selected.get();
	}

	/**
	 * Sets the selected {@link Element}
	 * 
	 * @param index
	 *            The new index of the selected element.
	 */
	public void setSelected(int index) {
		if (index < 0 || index >= elements.size()) {
			throw new IllegalArgumentException(index + " is not a valid index.");
		}

		selected = elements.get(index);
	}

	/**
	 * Sets the selected {@link Element}
	 * 
	 * @param selected
	 *            The new selected {@link T} element in the {@link VisualList}.
	 */
	public void setSelected(T selected) {

		/*
		 * Iterate through all the elements and if the payload of the current element is
		 * equal to the specified T then set that element as the selected.
		 */
		for (Element e : elements) {
			if (e.payload.equals(selected)) {
				this.selected = e;
				return;
			}
		}

		throw new IllegalArgumentException("Element is not present in this visual list");

	}

	/**
	 * Sets the selected {@link Element} by text;
	 * 
	 * @param text
	 *            The text of the new selected {@link T} element in the
	 *            {@link VisualList}.
	 */
	public void setSelected(String text) {

		/*
		 * Iterate through all the elements and if the payload of the current element is
		 * equal to the specified T then set that element as the selected.
		 */
		for (Element e : elements) {
			if (e.text.equals(text)) {
				this.selected = e;
				return;
			}
		}

		throw new IllegalArgumentException("Element is not present in this visual list");
	}

	/**
	 * Sets the {@link Font} for the {@link VisualList}.
	 * 
	 * @param font
	 *            {@link Font}
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Selected the {@link Element} that is being clicked from this
	 * {@link VisualList}.
	 * 
	 * @param click
	 *            The {@link Point} click.
	 * 
	 * @return <code>boolean</code> Return if click is true or false.
	 */
	public boolean click(Point click) {

		if (!visible) {
			return false;
		}

		// Iterate through all the elements in this list
		for (Element element : elements) {

			// If the current element is clicked selected and return true.
			if (element.isClicked(click)) {
				selected = element;
				return true;
			}
		}

		// If not element is clicked return false.
		return false;
	}

	/**
	 * Initialises this {@link VisualList} this should be called during or after the
	 * {@link InteractiveState#init(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame)}
	 */
	public void init() {

		elements.forEach(element -> element.init());

		if (font == null) {
			// Set the default font
			font = new Font("Arial", Color.black, 20);
		}

		font.init();

	}

	/**
	 * Draws the {@link VisualList} on screen.
	 * 
	 * @param frame
	 *            The {@link Frame} that draws this {@link VisualList} on screen.
	 */
	public void draw(Frame frame) {

		if (!visible) {
			return;
		}

		frame.setColor(Color.white);

		final int x = this.getPosition().x;
		int y = this.getPosition().y;

		// Draws the background menu box.
		frame.fillRect(x, y, getWidth(), getHeight());

		// Draw the map names in the game.
		for (Element element : elements) {

			final int index = elements.indexOf(element);

			if (index >= topElementIndex && index < topElementIndex + elementsInView) {

				element.setPosition(new Point(x, y));
				element.draw(frame, font, padding);
				y += height;
			}
		}

		// Highlights the selected map
		if (selected != null) {

			final int selectedIndex = elements.indexOf(selected);

			if (selectedIndex >= topElementIndex && selectedIndex < topElementIndex + elementsInView) {
				frame.draw(selected.getImage(), selected.getPosition().x, selected.getPosition().y);
			}

		}

	}

	/**
	 * Selects the {@link Element} above the currently selected {@link Element} in
	 * the {@link VisualList}.
	 */
	public void up() {

		// If there is a element selected
		if (selected != null) {

			// Get the index of the selected item
			int index = elements.indexOf(selected);

			// If the selected is not at the top of the list select the element above it.
			if (index > 0) {
				index--;
				selected = elements.get(index);
			}

			if (index < topElementIndex) {
				topElementIndex--;
			}
		}
	}

	/**
	 * Selects the {@link Element} below the currently selected {@link Element} in
	 * the {@link VisualList}.
	 */
	public void down() {

		// If there is a element selected
		if (selected != null) {

			// Get the index of the selected item
			int index = elements.indexOf(selected);

			// If the selected is not at the bottom of the list select the element below it.
			if (index < elements.size() - 1) {
				index++;
				selected = elements.get(index);
			}

			if (index >= topElementIndex + elementsInView) {
				topElementIndex++;
			}
		}

	}

	/**
	 * Clears the {@link VisualList} of all its elements.
	 */
	public void clear() {
		elements.clear();
		selected = null;
	}

	/**
	 * Shows this {@link VisualList}
	 */
	public void show() {
		visible = true;
	}

	/**
	 * Hides this {@link VisualList}.
	 */
	public void hide() {
		visible = false;
	}

	/**
	 * Retrieves whether or not the {@link VisualList} is visible or not.
	 * 
	 * @return visibility
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * 
	 * An element of the {@link VisualList} that can be displayed on screen using
	 * {@link Element#draw(Frame, Font, int)}. This element wraps &lt;T&gt;.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @since 2018-02-23
	 * @version 1.01.01
	 * 
	 * @see Clickable
	 *
	 */
	private final class Element extends Clickable {

		/**
		 * The {@link T} that will be returned if this {@link Element} is selected.
		 */
		private T payload;

		/**
		 * The text representation of the {@link Element}.
		 */
		private String text;

		/**
		 * Constructs a new {@link Element}.
		 * 
		 * @param payload
		 *            The {@link T} that will be returned if this {@link Element} is
		 *            selected.
		 * @param text
		 *            The text representation of the {@link Element}.
		 * @param position
		 *            The position of this {@link Element}.
		 * @param width
		 *            The width of this {@link Element}.
		 * @param height
		 *            The height of this {@link Element}.
		 */
		public Element(String text, T payload, Point position, int width, int height) {
			super(new Region(width, height, position));
			this.payload = payload;
			this.text = text;
		}

		/**
		 * Retrieves the {@link Element#payload}.
		 * 
		 * @return The {@link T} pay load of this {@link Element}.
		 */
		public T get() {
			return payload;
		}

		/**
		 * Initialises the {@link Element}.
		 */
		public void init() {
			replaceImage(getRegion().convert(Color.yellow));
		}

		/**
		 * Draws the name of the {@link Element} on screen.
		 * 
		 * @param frame
		 *            The {@link Frame} that draws this {@link Element} on screen.
		 * @param font
		 *            The {@link Font} that the element will be drawn in.
		 * @param padding
		 *            The number of pixels to the left of this {@link Element}s
		 *            {@link Point} position this will be displayed.
		 */
		public void draw(Frame frame, Font font, int padding) {
			frame.draw(font, text, this.getPosition().x + padding, this.getPosition().y);
		}
	}

}
