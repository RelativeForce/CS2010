package peril.views.slick.components.menus;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import peril.GameController;
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
 * @since 2018-02-25
 * @version 1.01.01
 * 
 * @see Clickable
 * @see Container
 * @see Component
 *
 */
public abstract class Menu extends Clickable implements Container, Component {

	/**
	 * The {@link Game} that this {@link Menu} is a part of.
	 */
	protected final GameController game;

	/**
	 * The {@link SlickGame} this {@link Menu} is apart of.
	 */
	protected final SlickGame slick;

	/**
	 * The {@link List} of {@link Button}s on this {@link Menu}.
	 */
	private final Map<String, Button> buttons;

	/**
	 * The {@link List} of {@link Viewable}s on this {@link Menu}.
	 */
	private final List<Viewable> images;

	/**
	 * The string representation of this {@link Menu}.
	 */
	private final String name;

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
	 *            The {@link GameController} that allows this {@link Menu} to query
	 *            the state of the game.
	 * @param region
	 *            The {@link Region} that denotes this {@link Menu} on screen.
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
	 * Moves all the visual components in the object that extends {@link Menu} along
	 * a specified {@link Point} vector.
	 * 
	 * @param vector
	 *           The {@link Point} vector to move the components by.
	 */
	public abstract void moveComponents(Point vector);

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
	 * screen. The {@link Buttons} are drawn on top of {@link Viewable}s.
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

	/**
	 * Sets the state of the {@link Menu}.
	 * 
	 * @param state
	 *            The visibility of state of the {@link Menu}.
	 */
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

		final Point current = button.getPosition();

		final Point menuPosition = this.getPosition();

		button.setPosition(new Point(current.x + menuPosition.x, current.y + menuPosition.y));

		buttons.put(button.id, button);
	}

	/**
	 * Sets the {@link Point} position of this {@link Menu} and moves all the
	 * components relative to this new {@link Point}.
	 */
	@Override
	public void setPosition(Point position) {

		final Point current = super.getPosition();

		final Point vector = new Point(position.x - current.x, position.y - current.y);

		super.setPosition(position);

		moveComponents(vector);

		moveViewables(buttons.values(), vector);

		moveViewables(images, vector);

	}

	/**
	 * Adds a {@link Viewable} to this {@link Menu}.
	 */
	@Override
	public void addImage(Viewable image) {

		final Point current = image.getPosition();

		final Point menuPosition = this.getPosition();

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
	 * Retrieves whether this menu is visible of not.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Moves a {@link Collection} of {@link Viewable}s along a specified
	 * {@link Point} vector.
	 * 
	 * @param vector
	 *            The {@link Point} vector to move the specified {@link Viewable}s
	 *            by.
	 * @param viewables
	 *            The {@link Collection} of {@link Viewable} objects to be moved by
	 *            the {@link Point} vector.
	 */
	private void moveViewables(Collection<? extends Viewable> viewables, Point vector) {
		viewables.forEach(image -> {
			final int x = image.getPosition().x + vector.x;
			final int y = image.getPosition().y + vector.y;
			image.setPosition(new Point(x, y));
		});
	}

}
