package peril.ui.states.gameStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Country;

/**
 * The state where the user selects which player gets what {@link Country}s.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 * 
 *         {@link CoreGameState}
 *
 */
public class SetupState extends CoreGameState {

	/**
	 * The name of a specific {@link SetupState}.
	 */
	private static final String STATE_NAME = "Setup";

	/**
	 * Constructs a new {@link SetupState}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link SetupState} is a part of.
	 * @param id
	 *            The ID of this {@link SetupState}
	 */
	public SetupState(Game game, int id) {
		super(game, STATE_NAME, id);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);
	}

	@Override
	public void parseClick(int button, Point click) {
		// If the states buttons were not clicked click the board.
		if (!clickButton(click)) {
			clickBoard(click);
		}
	}

	@Override
	public void highlightCountry(Country country) {

		// Unhighlight the current country
		super.unhighlightCountry(super.getHighlightedCountry());

		// Hightlight the new country
		super.highlightCountry(country);

	}

	@Override
	public void parseButton(int key, char c) {

		Country highlighted = getHighlightedCountry();

		if (highlighted != null) {

			switch (key) {
			case Input.KEY_1:
				highlighted.setRuler(Player.PLAYERONE);
				break;
			case Input.KEY_2:
				highlighted.setRuler(Player.PLAYERTWO);
				break;
			case Input.KEY_3:
				highlighted.setRuler(Player.PLAYERTHREE);
				break;
			case Input.KEY_4:
				highlighted.setRuler(Player.PLAYERFOUR);
				break;
			case Input.KEY_SPACE:
				highlighted.setRuler(null);
				break;

			}
		}

	}
}
