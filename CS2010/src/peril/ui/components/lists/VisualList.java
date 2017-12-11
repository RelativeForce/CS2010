package peril.ui.components.lists;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Point;
import peril.ui.Clickable;
import peril.ui.Font;
import peril.ui.Region;
import peril.ui.components.Component;
import peril.ui.states.InteractiveState;

/**
 * Encapsulates the behaviours a {@link List} of {@link Element}s that can be
 * displayed on screen and clicked on screen.
 * 
 * @author Joshua_Eddy
 *
 * @param <T>
 *            The type of the {@link Element#get()}
 */
public class VisualList<T> extends Clickable implements Component {

	/**
	 * The <code>int</code> width of the {@link VisualList}.
	 */
	private int width;

	/**
	 * The <code>int</code> height of the {@link VisualList}.
	 */
	private int height;

	/**
	 * The <code>int</code> number of {@link Element}s that are displayed in the box
	 * at once in the {@link VisualList}.
	 */
	private int elementsInView;

	/**
	 * The {@link Font} of the text representation of the {@link Element}s.
	 */
	private Font font;

	/**
	 * {@link List} of {@link Element}s that are displayed in this
	 * {@link VisualList}.
	 */
	private List<Element> elements;

	/**
	 * The {@link Element} that is currently selected in the {@link VisualList}.
	 */
	private Element selected;

	/**
	 * The index of the element at the top of the {@link VisualList}.
	 */
	private int topElementIndex;

	/**
	 * The number of pixels that the {@link Element}s will be displayed from the
	 * edge of the left wall.
	 */
	private int padding;

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
		elements = new LinkedList<>();
		selected = null;
		this.width = width;
		this.height = height;
		this.elementsInView = elementsInView;
		this.padding = padding;
		this.topElementIndex = 0;
	}

	/**
	 * Add an {@link Element} to the {@link VisualList}.
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
	 * @param index
	 */
	public void setSelected(int index) {
		if(index < 0 || index >= elements.size()) {
			throw new IllegalArgumentException(index + " is not a valid index.");
		}
		
		selected = elements.get(index);
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
	 *            {@link Point}
	 */
	public boolean click(Point click) {

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
	 * @param g
	 *            {@link Graphics}
	 */
	public void draw(Graphics g) {

		g.setColor(Color.white);

		int x = this.getPosition().x;
		int y = this.getPosition().y;

		// Draws the background menu box.
		g.fillRect(x, y, getWidth(), getHeight());

		// Draw the map names in the game.
		for (Element element : elements) {

			int index = elements.indexOf(element);

			if (index >= topElementIndex && index < topElementIndex + elementsInView) {

				element.setPosition(new Point(x, y));
				element.draw(g, font, padding);
				y += height;
			}
		}

		// Highlights the selected map
		if (selected != null) {

			int selectedIndex = elements.indexOf(selected);

			if (selectedIndex >= topElementIndex && selectedIndex < topElementIndex + elementsInView) {
				g.drawImage(selected.getImage(), selected.getPosition().x, selected.getPosition().y);
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
	 * 
	 * An element of the {@link VisualList} that can be displayed on screen using
	 * {@link Element#draw(Graphics, Font)}. This element wraps &lt;T&gt;.
	 * 
	 * @author Joshua_Eddy
	 *
	 */
	private class Element extends Clickable {

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
		 */
		public Element(String text, T payload, Point position, int width, int height) {
			super(new Region(width, height, position));
			this.payload = payload;
			this.text = text;
		}

		/**
		 * Retrieves the {@link Element#payload}.
		 * 
		 * @return {@link T}
		 */
		public T get() {
			return payload;
		}

		/**
		 * Initialises the {@link Element}.
		 * 
		 * @param x
		 *            <code>int</code> x coordinate of the {@link Element} on screen.
		 * @param y
		 *            <code>int</code> y coordinate of the {@link Element} on screen.
		 * @param width
		 *            <code>int</code> width of the {@link Element} on screen.
		 * @param height
		 *            <code>int</code> height of the {@link Element} on screen.
		 */
		public void init() {
			setImage(getRegion().getPosition(), getRegion().convert(Color.yellow));
		}

		/**
		 * Draws the name of the {@link Element} on screen.
		 * 
		 * @param g
		 *            {@link Graphics}
		 * @param font
		 *            {@link Font}
		 * @param padding
		 *            The number of pixels to the left of this {@link Element}s
		 *            {@link Point} position this will be displayed.
		 */
		public void draw(Graphics g, Font font, int padding) {
			font.draw(g, text, this.getPosition().x + padding, this.getPosition().y);
		}
	}

}
