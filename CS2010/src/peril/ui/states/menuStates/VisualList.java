package peril.ui.states.menuStates;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;

import peril.Point;
import peril.ui.states.Font;

public class VisualList<T> {

	private int x;
	private int y;
	private int width;
	private int height;
	private int elementsInView;
	private Font font;

	private List<Element<T>> elements;

	private Element<T> selected;

	public VisualList(int x, int y, int width, int height, int elementsInView) {

		elements = new LinkedList<>();
		selected = null;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.elementsInView = elementsInView;
	}

	public void add(Element<T> element) {
		elements.add(element);
	}

	public Element<T> getSelected() {
		return selected;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public void click(Point click) {

		for (Element<T> element : elements) {

			if (element.isClicked(click)) {
				selected = element;
				break;
			}
		}
	}

	public void init() {

		int y = this.y;
		int x = this.x;

		for (Element<T> element : elements) {
			element.init(x, y, width, height);
			y += height;
		}
	}

	public void draw(Graphics g) {

		// Draws the background menu box.
		g.fillRect(x, y, width, (elementsInView * height));

		// Highlights the selected map
		if (selected != null) {
			g.drawImage(selected.getImage(), selected.getPosition().x, selected.getPosition().y);
		}

		// Draw the map names in the game.
		elements.forEach(element -> element.draw(g, font));
	}

	public void up() {
		if (selected != null) {
			int index = elements.indexOf(selected);

			if (index > 0) {
				selected = elements.get(index - 1);
			}
		}
	}

	public void down() {
		if (selected != null) {
			int index = elements.indexOf(selected);

			if (index < elements.size() - 1) {
				selected = elements.get(index + 1);
			}
		}
	}
}
