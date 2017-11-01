package peril.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import peril.Game;
import peril.board.Board;
import peril.board.Country;

/**
 * @author Joseph_Rolli, Joshua_Eddy
 */

public abstract class CoreGameState extends BasicGameState {

	/**
	 * Holds the name of a specific {@link CoreGameState}.
	 */
	protected String stateName;
	protected Game game;
	private int x;
	private int y;

	private Country higlightedCountry;

	protected CoreGameState(Game game) {
		this.game = game;
		this.higlightedCountry = null;
	}

	public void highlight(Country country) {
		higlightedCountry = country;
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

		if (b != null && b.hasImage()) {
			g.drawImage(b.getImage(), 0, 0);
		} else {
			game.loadAssets();
		}

		if (higlightedCountry != null ) {
			
			Image c = higlightedCountry.getImage();
			
			if(c != null)g.drawImage(c, higlightedCountry.getPosition().x,
					higlightedCountry.getPosition().y);
		}

		g.drawString(stateName, 5, 50);
		g.drawString("X" + x + " Y" + y, 700, 700);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (gc.getInput().isMouseButtonDown(0)) {
			x = gc.getInput().getMouseX();
			y = gc.getInput().getMouseY();
		}
	}

	public String getStateName() {
		return stateName;
	}

}
