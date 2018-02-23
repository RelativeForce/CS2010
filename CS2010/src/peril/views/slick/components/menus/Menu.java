package peril.views.slick.components.menus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Graphics;

import peril.controllers.GameController;
import peril.views.slick.Container;
import peril.views.slick.Frame;
import peril.views.slick.SlickGame;
import peril.views.slick.components.Component;
import peril.views.slick.states.InteractiveState;
import peril.views.slick.util.Button;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;
import peril.views.slick.util.Viewable;

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
	private final Map<String, Button> buttons;

	/**
	 * A {@link List} of {@link Viewable}s on this {@link Menu}.
	 */
	private final List<Viewable> images;

	/**
	 * The string representation of this {@link Menu}.
	 */
	private String name;

	/**
	 * The {@link Game} that this {@link Menu} is a part of.
	 */
	protected final GameController game;

	protected final SlickGame slick;

	/**
	 * Whether or not this {@link Menu} is visible or not.
	 */
	private volatile boolean visible;

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
	public Menu(String name, GameController game, Region region) {
		super(region);

		if (!(game.getView() instanceof SlickGame)) {
			throw new IllegalArgumentException("This menu ( " + name + " ) only supports slick 2d.");
		}

		this.slick = (SlickGame) game.getView();
		this.buttons = new HashMap<>();
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
	public void draw(Frame frame) {
		if (visible) {

			images.forEach(image -> frame.draw(image.getImage(), image.getPosition().x, image.getPosition().y));
			buttons.forEach((buttonId, button) -> {
				if (button.isVisible()) {
					frame.draw(button);
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

	public void setVisibility(boolean state) {

		if (state) {
			show();
		} else {
			hide();
		}

	}

	/**
	 * Adds a {@link Button} to this {@link Menu}.
	 */
	@Override
	public void addButton(Button button) {

		Point current = button.getPosition();

		Point menuPosition = this.getPosition();

		button.setPosition(new Point(current.x + menuPosition.x, current.y + menuPosition.y));

		buttons.put(button.id, button);
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
	 * Retrieves a {@link Button} from this {@link Menu} by id.
	 */
	@Override
	public Button getButton(String id) {

		if (!buttons.containsKey(id)) {
			throw new IllegalArgumentException("'" + id + "' button is not in " + name + " menu.");
		}
		return buttons.get(id);

	}

	/**
	 * Returns the string representation of the {@link Menu}
	 */
	@Override
	public String getName() {
		return name;
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
		buttons.forEach((buttonId, button) -> button
				.setPosition(new Point(button.getPosition().x + vector.x, button.getPosition().y + vector.y)));
	}
}
