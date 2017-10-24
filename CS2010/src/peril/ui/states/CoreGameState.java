package peril.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.Player;

public class CoreGameState extends BasicGameState {

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		System.out.println("Entering gamestate0!");
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gc.setUpdateOnlyWhenVisible(true);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
	}

	@Override
	public int getID() {
		return 0;
	}
	
	public void displayWinner(Player player) {
		
	}

}


