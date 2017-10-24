package peril.ui.states;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.Player;
import peril.ui.UserInterface;

/**
 * @author Joseph_Rolli
 */

public class CoreGameState extends BasicGameState {

	/**
	 * Holds the name of a specific {@link CoreGameState}
	 */
	private String stateName;

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

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

	}

	@Override
	public int getID() {
		return 0;
	}

	public String getStateName() {
		return stateName;
	}

}
