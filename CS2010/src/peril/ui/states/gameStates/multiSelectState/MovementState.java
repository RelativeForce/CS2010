package peril.ui.states.gameStates.multiSelectState;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.board.Country;
import peril.ui.states.gameStates.CoreGameState;

/**
 * Encapsulates the behaviour of the 'Movement' state of the game. In this state
 * the {@link Game#getCurrentPlayer()} chooses which of their {@link Country}s
 * they will move units to another {@link Country}.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 *
 */
public class MovementState extends MultiSelectState {

	/**
	 * The name of a specific {@link MovementState}.
	 */
	private static final String STATE_NAME = "Movement";

	/**
	 * Constructs a new {@link MovementState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link MovementState}.
	 * @param id
	 *            The ID of this {@link MovementState}.
	 * 
	 */
	public MovementState(Game game, int id) {
		super(game, STATE_NAME, id);
	}

	/**
	 * Highlights a specified {@link Country} if it is friendly or a
	 * {@link Country#getNeighbours()} of the
	 * {@link CoreGameState#getHighlightedCountry()}..
	 */
	@Override
	public void highlightCountry(Country country) {

		// If the country is null then set the primary highlighted as null and
		// unhighlight the current enemy country.
		if (country != null) {

			// Holds the current player
			Player player = getGame().getCurrentPlayer();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			processCountry(country, player, ruler);

		} else {
			super.unhighlightCountry(super.getSecondaryHightlightedCounrty());
			super.setSecondaryCountry(null);
			super.unhighlightCountry(super.getHighlightedCountry());
			super.highlightCountry(country);
		}

	}

	/**
	 * Render the {@link MovementState}. 
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);
		super.drawPlayerName(g);
	}

	/**
	 * Retrieves the {@link Country} the
	 * {@link CoreGameState#getHighlightedCountry()} will sent troops too when
	 * fortify is clicked.
	 * 
	 * @return {@link Counrty}
	 */
	public Country getTargetCountry() {
		return super.getSecondaryHightlightedCounrty();
	}

	/**
	 * Processes whether a {@link Country} is a valid target for the
	 * {@link CoreGameState#getHighlightedCountry()} to attack. This is based on the
	 * {@link Player} ruler and the {@link Player} ({@link Game#getCurrentPlayer()})
	 * 
	 * @param country
	 *            {@link Country}
	 * @param player
	 *            {@link Player}
	 * @param ruler
	 *            {@link Player}
	 */
	protected void processCountry(Country country, Player player, Player ruler) {

		// If there is a primary friendly country and the target is not null and the
		// ruler of the country is not the player.
		if (getHighlightedCountry() != null && player.equals(ruler)) {

			// if the country is a neighbour of the primary highlighted country then it is a
			// valid target.
			if (getHighlightedCountry().isNeighbour(country)) {

				System.out.println("A valid target");

				super.unhighlightCountry(super.getSecondaryHightlightedCounrty());

				country.setImage(country.getRegion().getPosition(), country.getRegion().convert(Color.yellow));

				super.setSecondaryCountry(country);

			} else {
				// DO NOTHING
			}

		}
		// If the country clicked is to be the new primary country but is not ruler by
		// the player
		else if (!player.equals(ruler)) {
			// DO NOTHING
		}
		// If the country clicked is to be the new primary country and is owned by the
		// player.
		else {
			super.unhighlightCountry(super.getSecondaryHightlightedCounrty());
			super.highlightCountry(country);
		}
	}
}
