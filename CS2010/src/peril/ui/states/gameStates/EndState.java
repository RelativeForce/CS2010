package peril.ui.states.gameStates;

import java.util.LinkedList;

import org.newdawn.slick.Color;
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

	private LinkedList<Player> podium;

	private Font loserFont;

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
		podium = new LinkedList<>();
	}

	public void addPlayerToPodium(Player player) {
		podium.push(player);
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

		drawPodium(g, gc.getWidth(), gc.getHeight());
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);
		winnerFont = new Font("Arial", Color.yellow, 50);
		loserFont = new Font("Arial", Color.red, 30);
	}

	@Override
	public Music getMusic() {
		return null;
	}

	private void drawPodium(Graphics g, int width, int height) {
		int position = 1;

		int y = height / 3;

		for (Player player : podium) {

			if (position == 1) {
				winnerFont.draw(g, position + ". " + player.toString(), (width / 3) - 20, y);
			} else {
				loserFont.draw(g, position + ". " + player.toString(), width / 3, y);
			}

			y += 60;
			position++;
		}

	}

}
