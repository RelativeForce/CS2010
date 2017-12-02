package peril.ui.components.menus;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Point;
import peril.ui.Button;
import peril.ui.Clickable;
import peril.ui.Container;
import peril.ui.Region;
import peril.ui.Viewable;
import peril.ui.components.Component;
import peril.ui.states.InteractiveState;

/**
 * Encapsulates the behaviour of a menu that can be displayed in a
 * {@link InteractiveState}. This also implements {@link Container} and extends
 * {@link Clickable}.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 *
 */
public abstract class Menu extends Clickable implements Container, Component {

	/**
	 * A {@link List} of {@link Button}s on this {@link Menu}.
	 */
	private List<Button> buttons;

	/**
	 * A {@link List} of {@link Viewable}s on this {@link Menu}.
	 */
	private List<Viewable> images;

	/**
	 * The string representation of this {@link Menu}.
	 */
	private String name;

	/**
	 * The {@link Game} that this {@link Menu} is a part of.
	 */
	private Game game;

	/**
	 * Whether or not this {@link Menu} is visible or not.
	 */
	private boolean visible;

	/**
	 * Constructs a new {@link Menu}. Initially invisible.
	 * 
	 * @param name
	 *            The string representation of this {@link Menu}.
	 * @param game
	 *            The {@link Game} that this {@link Menu} is a part of.
	 * @param region
	 *            {@link Region}
	 */
	public Menu(String name, Game game, Region region) {
		super(region);

		this.buttons = new LinkedList<>();
		this.images = new LinkedList<>();
		this.name = name;
		this.game = game;
		this.visible = false;
	}

	/**
	 * Initialises the visual elements of this {@link Menu}.
	 */
	public abstract void init();

	/**
	 * Processes a mouse click at a specified {@link Point} on the screen.
	 * 
	 * @param click
	 *            {@link Point}
	 */
	public abstract void parseClick(Point click);

	/**
	 * Retrieves whether this menu is visible of not.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Toggles this {@link Menu} between visible and invisible.
	 */
	public void toggleVisibility() {

		// If the menu is visible hide it otherwise show it.
		if (isVisible()) {
			hide();
		} else {
			show();
		}
	}

	/**
	 * Draws this {@link Menu} and all its {@link Button}s and {@link Viewable}s on
	 * screen. {@link Buttons} are drawn on top of {@link Viewable}s.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	public void draw(Graphics g) {
		if (visible) {

			images.forEach(image -> g.drawImage(image.getImage(), image.getPosition().x, image.getPosition().y));
			buttons.forEach(button -> {
				if (button.isVisible()) {
					g.drawImage(button.getImage(), button.getPosition().x, button.getPosition().y);
				}
			});
		}

	}

	/**
	 * Shows this {@link Menu}.
	 */
	public void show() {
		visible = true;
	}

	/**
	 * Hides this {@link Menu}.
	 */
	public void hide() {
		visible = false;
	}

	/**
	 * Adds a {@link Button} to this {@link Menu}.
	 */
	@Override
	public void addButton(Button button) {

		Point current = button.getPosition();

		Point menuPosition = this.getPosition();

		button.setPosition(new Point(current.x + menuPosition.x, current.y + menuPosition.y));

		buttons.add(button);
	}

	/**
	 * Sets the {@link Point} position of this {@link Menu} and moves all the
	 * components relative to this new {@link Point}.
	 */
	@Override
	public void setPosition(Point position) {

		Point current = super.getPosition();

		Point vector = new Point(position.x - current.x, position.y - current.y);

		super.setPosition(position);

		moveComponents(vector);

		moveButtons(vector);

		moveImages(vector);

	}

	/**
	 * Moves all the visual components in the object that extends {@link Menu} along
	 * a specified {@link Point} vector.
	 * 
	 * @param vector
	 *            {@link Point}
	 */
	public abstract void moveComponents(Point vector);

	/**
	 * Adds a {@link Viewable} to this {@link Menu}.
	 */
	@Override
	public void addImage(Viewable image) {

		Point current = image.getPosition();

		Point menuPosition = this.getPosition();

		image.setPosition(new Point(current.x + menuPosition.x, current.y + menuPosition.y));

		images.add(image);
	}

	/**
	 * Processes a click at a specified {@link Point} on the {@link Button}s in the
	 * {@link Menu}.
	 */
	@Override
	public boolean clickedButton(Point click) {

		// Iterate through every button on the menu and is a button is clicked
		for (Button b : buttons) {

			// If the button is clicked process a click on that button.
			if (b.isClicked(click)) {
				b.click();
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the string representation of the {@link Menu}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the {@link Game} that this {@link Menu} is a part of.
	 * 
	 * @return {@link Game}
	 */
	protected final Game getGame() {
		return game;
	}

	/**
	 * Moves all the {@link Viewable}s along a specified {@link Point} vector.
	 * 
	 * @param vector
	 *            {@link Point}
	 */
	private void moveImages(Point vector) {
		images.forEach(image -> image
				.setPosition(new Point(image.getPosition().x + vector.x, image.getPosition().y + vector.y)));
	}

	/**
	 * Moves all the {@link Button}s along a specified {@link Point} vector.
	 * 
	 * @param vector
	 *            {@link Point}
	 */
	private void moveButtons(Point vector) {
		buttons.forEach(button -> button
				.setPosition(new Point(button.getPosition().x + vector.x, button.getPosition().y + vector.y)));
	}
}
