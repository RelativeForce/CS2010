package peril.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.board.Country;

public class CombatState extends CoreGameState {

	/**
	 * The ID of this {@link CombatState}
	 */
	private static final int ID = 3;

	private Country enemyCounrty;

	public CombatState(Game game) {
		super(game);
		stateName = "Combat";
	}

	@Override
	public int getID() {
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
	public void highlightCountry(Country country) {
		
		if(country.getRuler() != null ) {
			
			if(!country.getRuler().equals(game.getCurrentPlayer())) {
				
				
				
			}
			
		}
		
		super.highlightCountry(country);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		// Draw player name and set the text color to the player's color
		g.setColor(game.getCurrentPlayer().getColor());
		g.drawString(game.getCurrentPlayer().toString(), 5, 20);

		if (enemyCounrty != null) {
			g.drawImage(enemyCounrty.getImage(), enemyCounrty.getPosition().x, enemyCounrty.getPosition().y);
		}

	}

	@Override
	public void parseButton(int key, char c) {
		// Do nothing
	}
}
