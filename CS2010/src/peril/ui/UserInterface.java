package peril.ui;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The object that allows the user to interact with the system.
 * 
 * @author Joshua_Eddy
 * @see JFrame
 */

public class UserInterface extends StateBasedGame {

	public static void main(String[] args) {
		try {
			AppGameContainer agc = new AppGameContainer(new UserInterface());
			agc.setDisplayMode(800, 500, false);
			agc.setTargetFrameRate(60);
			agc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	public UserInterface() {
		super("PERIL: A Turn Based Strategy Game");

	}

	public void initStatesList(GameContainer gc) throws SlickException {
		addState(new CoreGameState());
	}

}
