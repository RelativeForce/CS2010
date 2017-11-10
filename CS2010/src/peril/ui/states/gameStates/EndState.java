package peril.ui.states.gameStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;

/**
 * 
 * The final state of the game where the winner is displayed.
 * 
 * @author Joshua_Eddy
 *
 */
public class EndState extends CoreGameState {

	/**
	 * The name of a specific {@link EndState}.
	 */
	private static final String STATE_NAME = "EndState";

	/**
	 * Constructs a new {@link EndState}.
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param id
	 *            The id of this {@link EndState}.
	 */
	public EndState(Game game, int id) {
		super(game, STATE_NAME, id);
	}

	public void displayWinner(Player player) {

	}

	@Override
	public void parseClick(int button, Point click) {
		// TODO Auto-generated method stub

	}

	@Override
	public void parseButton(int key, char c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		// Draw player name and set the text color to the player's color
		g.setColor(getGame().getCurrentPlayer().getColor());
		g.drawString(getGame().getCurrentPlayer().toString(), 5, 20);

	}
}
