package peril.ui.components.menus;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Point;
import peril.ui.Button;
import peril.ui.ButtonContainer;
import peril.ui.components.Clickable;

public abstract class Menu extends Clickable implements ButtonContainer {

	/**
	 * A list of {@link Button}s on the {@link PauseMenu}.
	 */
	private List<Button> buttons;

	private String name;

	private Game game;

	public boolean visible;

	public Menu(String name, Game game) {
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
	
	public abstract void draw(Graphics g);

	@Override
	public void addButton(Button button) {
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
