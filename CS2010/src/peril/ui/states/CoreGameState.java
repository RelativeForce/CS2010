package peril.ui.states;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.board.Board;
import peril.ui.UserInterface;
import peril.ui.VisualRepresentation;

/**
 * @author Joseph_Rolli
 */

public abstract class CoreGameState extends BasicGameState {

	/**
	 * Holds the name of a specific {@link CoreGameState}
	 */
	protected String stateName;
	private Game game;
	private Image board;

	protected CoreGameState(Game game) {
		this.game = game;

	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		System.out.println("Entering gamestate:" + Integer.toString(getID()));
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gc.setUpdateOnlyWhenVisible(true);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		Board b = game.getBoard();

		if (b != null) {

			VisualRepresentation vr = b.getVisual();

			if (vr != null) {

				board = vr.getImage();
				if (board != null) {

					g.drawImage(board, 0, 0);
				}
			}
		} else {

			game.init();

		}

		g.drawString(stateName, 5, 50);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

	}

	public String getStateName() {
		return stateName;
	}

}
