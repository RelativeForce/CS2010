package peril.ui.states.gameStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.ui.states.InteractiveState;
import peril.ui.visual.Font;

/**
 * 
 * The final state of the game where the winner is displayed.
 * 
 * @author Joshua_Eddy
 *
 */
public class EndState extends InteractiveState {

	/**
	 * The name of a specific {@link EndState}.
	 */
	private static final String STATE_NAME = "EndState";

	private Font winnerFont;

	private Player winner;

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

	public void setWinner(Player player) {
		winner = player;
		winnerFont = new Font("Arial", player.getColor(), 50);
	}

	@Override
	public void parseClick(int button, Point click) {
		super.clickButton(click);
	}

	@Override
	public void parseButton(int key, char c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		winnerFont.draw(g, winner.toString() + " WINS!!!!", gc.getWidth() / 2, gc.getHeight() / 2);

	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);
	}

	@Override
	public Music getMusic() {
		return null;
	}
}
