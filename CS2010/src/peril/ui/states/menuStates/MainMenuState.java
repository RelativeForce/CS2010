package peril.ui.states.menuStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.ui.states.InteractiveState;
import peril.ui.states.gameStates.CombatState;

public class MainMenuState extends InteractiveState {

	/**
	 * The ID of this {@link MainMenuState}.
	 */
	private static final int ID = 0;

	/**
	 * The name of a specific {@link CombatState}.
	 */
	private static final String STATE_NAME = "Main Menu";

	protected MainMenuState(Game game) {
		super(game, STATE_NAME);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		// Draw player name and set the text color to the player's color
		g.setColor(getGame().getCurrentPlayer().getColor());
		g.drawString(getGame().getCurrentPlayer().toString(), 5, 20);

	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void parseClick(int button, Point click) {
	}

	@Override
	public void parseButton(int key, char c) {
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
	}

}
