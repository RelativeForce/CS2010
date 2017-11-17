package peril.ui.states.gameStates;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.board.Country;
import peril.ui.components.menus.PauseMenu;

/**
 * Encapsulates the behaviour of the Reinforcement {@link CoreGameState} where
 * the {@link Player} places their units from
 * {@link Player#getDistributableArmySize()} on their {@link Country}s.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 *
 */
public final class ReinforcementState extends CoreGameState {

	/**
	 * The name of a specific {@link ReinforcementState}.
	 */
	private static final String STATE_NAME = "Reinforcement";

	/**
	 * Constructs a new {@link ReinforcementState}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link ReinforcementState} is a part of.
	 * @param id
	 *            The ID of this {@link ReinforcementState}
	 * @param pauseMenu
	 *            The {@link PauseMenu} for this {@link ReinforcementState}.
	 */
	public ReinforcementState(Game game, int id, PauseMenu pauseMenu) {
		super(game, STATE_NAME, id, pauseMenu);
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

			if (player.equals(ruler)) {
				super.highlightCountry(country);
			}

		} else {
			super.highlightCountry(country);
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		super.drawPlayerName(g);
		
		drawImages(g);
		drawButtons(g);

		// Set the text color to magenta
		g.setColor(Color.black);

		// Draw player name
		g.drawString("Units: " + getGame().getCurrentPlayer().getDistributableArmySize(), 5, 35);
	}
}
