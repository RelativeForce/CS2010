package peril.ui.components;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Point;
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
public class VisualList<T> extends Clickable {

	/**
	 * The <code>int</code> x coordinate of the {@link VisualList}.
	 */
	private int x;

	/**
	 * The <code>int</code> y coordinate of the {@link VisualList}.
	 */
	private int y;

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
	private List<Element<T>> elements;

	/**
	 * The {@link Element} that is currently selected in the {@link VisualList}.
	 */
	private Element<T> selected;

	private int topElementIndex;

	/**
	 * The number of pixels that the {@link Element}s will be displayed from the
	 * edge of the left wall.
	 */
	private int padding;

	/**
	 * Constructs a new {@link VisualList}.
	 * 
	 * @param x
	 *            The <code>int</code> x coordinate of the {@link VisualList}.
	 * @param y
	 *            The <code>int</code> y coordinate of the {@link VisualList}.
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
	public VisualList(int x, int y, int width, int height, int elementsInView, int padding) {

		elements = new LinkedList<>();
		selected = null;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.elementsInView = elementsInView;
		this.padding = padding;
		this.topElementIndex = 0;
		this.setRegion(new Region(width, height * elementsInView, new Point(x, y)));
	}

	/**
	 * Add an {@link Element} to the {@link VisualList}.
	 * 
	 * @param element
	 *            {@link Element}
	 */
	public void add(Element<T> element) {

		// If this is the first element the set it as the selected.
		if (elements.isEmpty()) {
			selected = element;
		}

		elements.add(element);
	}

	/**
	 * Retrieves the selected {@link Element} from the {@link VisualList}.
	 * 
	 * @return {@link Element}
	 */
	public Element<T> getSelected() {
		return selected;
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
		for (Element<T> element : elements) {

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

		// Get the x and y of the list.
		int y = this.y;
		int x = this.x;

		// Assign all the elements to have a vertical list position.
		for (Element<T> element : elements) {
			element.init(x, y, width, height);
			y += height;
		}

		// Set the default font
		font = new Font("Arial", Color.black, 20);
	}

	/**
	 * Draws the {@link VisualList} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	public void draw(Graphics g) {

		Region r = getRegion();

		g.setColor(Color.white);
		// Draws the background menu box.
		g.fillRect(r.getPosition().x, r.getPosition().y, r.getWidth(), r.getHeight());

		int x = this.x;
		int y = this.y;

		// Draw the map names in the game.
		for (Element<T> element : elements) {

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
}
