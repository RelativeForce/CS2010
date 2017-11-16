package peril.ui.components.menus;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Point;
import peril.ui.Button;
import peril.ui.Container;
import peril.ui.components.Clickable;
import peril.ui.components.Region;
import peril.ui.components.Viewable;
import peril.ui.states.InteractiveState;

/**
 * Encapsulates the behaviour of a menu that can be displayed in a
 * {@link InteractiveState}. This also implements {@link Container} and extends
 * {@link Clickable}.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 *
 */
public abstract class Menu extends Clickable implements Container {

	/**
	 * A list of {@link Button}s on the {@link Menu}.
	 */
	private List<Button> buttons;

	private List<Viewable> images;

	/**
	 * 
	 */
	private String name;

	private Game game;

	public boolean visible;

	public Menu(String name, Game game, Region region) {
		super(region);
		this.buttons = new LinkedList<>();
		this.images = new LinkedList<>();
		this.name = name;
		this.game = game;
		this.visible = false;
	}

	protected Game getGame() {
		return game;
	}

	public abstract void init();

	public abstract void parseClick(Point click);

	public void draw(Graphics g) {

		g.fillRect(getPosition().x, getPosition().y, getWidth(), getHeight());
		images.forEach(image->g.drawImage(image.getImage(), image.getPosition().x, image.getPosition().y));
		buttons.forEach(button -> g.drawImage(button.getImage(), button.getPosition().x, button.getPosition().y));
		
	}

	@Override
	public void addButton(Button button) {

		Point current = button.getPosition();

		Point menuPosition = this.getPosition();

		button.setPosition(new Point(current.x + menuPosition.x, current.y + menuPosition.y));

		buttons.add(button);
	}

	@Override
	public void setPosition(Point position) {

		Point current = super.getPosition();

		Point vector = new Point(position.x - current.x, position.y - current.y);

		moveComponents(vector);

		moveButtons(vector);
		
		moveImages(vector);

		super.setPosition(position);

	}

	public abstract void moveComponents(Point vector);

	@Override
	public void addImage(Viewable image) {
		images.add(image);
	}

	@Override
	public boolean clickedButton(Point click) {
		// Iterate through every button on the
		for (Button b : buttons) {
			if (b.isClicked(click)) {
				b.click();
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

	private void moveImages(Point vector) {
		images.forEach(image -> image
				.setPosition(new Point(image.getPosition().x + vector.x, image.getPosition().y + vector.y)));
	}

	private void moveButtons(Point vector) {
		buttons.forEach(button -> button
				.setPosition(new Point(button.getPosition().x + vector.x, button.getPosition().y + vector.y)));
	}
}
