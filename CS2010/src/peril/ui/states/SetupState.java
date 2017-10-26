package peril.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.ui.UserInterface;

public class SetupState extends CoreGameState {

	public SetupState(Game game, UserInterface ui) {
		super(game, ui);
		stateName = "Setup";
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
