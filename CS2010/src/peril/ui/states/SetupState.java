package peril.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;

public class SetupState extends CoreGameState {

	/**
	 * The ID of this {@link SetupState}
	 */
	private static final int ID = 1;
	
	public SetupState(Game game) {
		super(game);
		stateName = "Setup";
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void parseClick(int button, Point click) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parseButton(int key, char c) {
		// TODO Auto-generated method stub
		
	}
}
