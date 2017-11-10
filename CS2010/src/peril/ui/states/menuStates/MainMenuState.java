package peril.ui.states.menuStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.ui.states.InteractiveState;
import peril.ui.states.gameStates.CombatState;

/**
 * Encapsulates the behaviour of the main menu.
 * 
 * @author Joshua_Eddy
 * 
 * @see InteractiveState
 *
 */
public class MainMenuState extends InteractiveState {

	/**
	 * The name of a specific {@link CombatState}.
	 */
	private static final String STATE_NAME = "Main Menu";

	/**
	 * Constructs a new {@link MainMenuState}
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param id
	 *            The ID of this {@link MainMenuState}.
	 */
	public MainMenuState(Game game, int id) {
		super(game, STATE_NAME, id);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

	}

	@Override
	public void parseClick(int button, Point click) {
	}

	@Override
	public void parseButton(int key, char c) {
		if (key == Input.KEY_SPACE) {
			getGame().enterState(getGame().setup.getID());
		}
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
	}

}
