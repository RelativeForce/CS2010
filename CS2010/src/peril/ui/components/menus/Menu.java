package peril.ui.components.menus;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Point;
import peril.ui.Button;
import peril.ui.ButtonContainer;
import peril.ui.components.Clickable;
import peril.ui.components.Region;
import peril.ui.states.InteractiveState;

/**
 * Encapsulates the behaviour of a menu that can be displayed in a
 * {@link InteractiveState}. This also implements {@link ButtonContainer} and
 * extends {@link Clickable}.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 *
 */
public abstract class Menu extends Clickable implements ButtonContainer {

	/**
	 * A list of {@link Button}s on the {@link Menu}.
	 */
	private List<Button> buttons;

	/**
	 * 
	 */
	private String name;

	private Game game;

	public boolean visible;

	public Menu(String name, Game game, Region region) {
		super(region);
		this.buttons = new LinkedList<>();
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
		buttons.forEach(button -> g.drawImage(button.getImage(), button.getPosition().x, button.getPosition().y));
	}

	@Override
	public void addButton(Button button) {
		button.setPosition(new Point(button.getPosition().x + this.getPosition().x,
				button.getPosition().y + this.getPosition().y));
		buttons.add(button);
	}

	@Override
	public List<Button> getButtons() {
		return buttons;
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

}
