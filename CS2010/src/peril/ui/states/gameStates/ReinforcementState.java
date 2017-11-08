package peril.ui.states.gameStates;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Country;

/**
 * Encapsulates the behaviour of the Reinforcement {@link CoreGameState} where
 * the {@link Player} places their units from
 * {@link Player#getDistributableArmySize()} on their {@link Country}s.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 *
 */
public class ReinforcementState extends CoreGameState {

	/**
	 * The ID of this {@link ReinforcementState}
	 */
	private static final int ID = 2;
	
	/**
	 * The name of a specific {@link ReinforcementState}.
	 */
	private static final String STATE_NAME = "Reinforcement";

	public ReinforcementState(Game game) {
		super(game, STATE_NAME);
	}
	
	@Override
	public void highlightCountry(Country country) {

		super.unhighlightCountry(super.getHighlightedCountry());
		
		// If the country is null then set the primary highlighted as null and
		// unhighlight the current enemy country.
		if (country != null) {

			// Holds the current player
			Player player = getGame().getCurrentPlayer();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			if(player.equals(ruler)) {
				super.highlightCountry(country);
			}

		} else {
			super.highlightCountry(country);
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		// Draw player name and set the text color to the player's color
		g.setColor(getGame().getCurrentPlayer().getColor());
		g.drawString(getGame().getCurrentPlayer().toString(), 5, 20);
		
		// Set the text color to magenta
		g.setColor(Color.black);

		// Draw player name
		g.drawString("Units: " + getGame().getCurrentPlayer().getDistributableArmySize(), 5, 35);
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
	public void parseButton(int key, char c) {
				
	}
}
