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
 * Encapsulates the behaviour of the 'Movement' state of the game. In this state
 * the {@link Game#getCurrentPlayer()} chooses which of their {@link Country}s
 * they will move units to another {@link Country}.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 *
 */
public class MovementState extends CoreGameState {

	/**
	 * The name of a specific {@link MovementState}.
	 */
	private static final String STATE_NAME = "Movement";

	/**
	 * The target {@link Country} for the
	 * {@link CoreGameState#getHighlightedCountry()}.
	 */
	private Country targetCounrty;

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
		targetCounrty = null;
	}

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
			super.unhighlightCountry(targetCounrty);
			targetCounrty = null;
			super.unhighlightCountry(super.getHighlightedCountry());
			super.highlightCountry(country);
		}

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
		// DO NOTHING
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		// Draw player name and set the text colour to the player's colour
		g.setColor(getGame().getCurrentPlayer().getColor());
		g.drawString(getGame().getCurrentPlayer().toString(), 5, 20);

	}

	@Override
	public void unhighlightCountry(Country country) {

		// Unhighlight both highlighted countries when this method is called from a
		// external class.
		super.unhighlightCountry(targetCounrty);

		targetCounrty = null;

		super.unhighlightCountry(country);

	}

	/**
	 * Retrieves the {@link Country} the
	 * {@link CoreGameState#getHighlightedCountry()} will sent troops too when
	 * fortify is clicked.
	 * 
	 * @return {@link Counrty}
	 */
	public Country getTargetCountry() {
		return targetCounrty;
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
	private void processCountry(Country country, Player player, Player ruler) {

		// If there is a primary friendly country and the target is not null and the
		// ruler of the country is not the player.
		if (getHighlightedCountry() != null && player.equals(ruler)) {

			// if the country is a neighbour of the primary highlighted country then it is a
			// valid target.
			if (getHighlightedCountry().isNeighbour(country)) {

				System.out.println("A valid target");
				super.unhighlightCountry(targetCounrty);
				targetCounrty = country;
				targetCounrty.setImage(targetCounrty.getRegion().getPosition(),
						targetCounrty.getRegion().convert(Color.yellow));

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
			super.unhighlightCountry(targetCounrty);
			super.highlightCountry(country);
		}
	}
}
