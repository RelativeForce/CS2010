package peril.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;

public class MovementState extends CoreGameState {

	/**
	 * The ID of this {@link MovementState}.
	 */
	private static final int ID = 4;

	public MovementState(Game game) {
		super(game);
		stateName = "Movement";
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

	@Override
	public void parseClick(int button, Point click) {
		// If the states buttons were not clicked click the board.
		if (!clickButton(click)) {
			clickBoard(click);
		}
	}

	@Override
	public void parseButton(int key, char c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		// Draw player name and set the text color to the player's color
		g.setColor(game.getCurrentPlayer().getColor());
		g.drawString(game.getCurrentPlayer().toString(), 5, 20);
		
	}
}
