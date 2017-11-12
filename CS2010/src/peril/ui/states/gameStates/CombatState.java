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
 * Encapsulates the behaviour of the 'Combat' state of the game. In this state
 * the {@link Game#getCurrentPlayer()} chooses which of their {@link Country}s
 * they will attack other {@link Country}s with.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 *
 */
public class CombatState extends CoreGameState {

	/**
	 * The name of a specific {@link CombatState}.
	 */
	private static final String STATE_NAME = "Combat";

	/**
	 * The target {@link Country} for the
	 * {@link CoreGameState#getHighlightedCountry()}.
	 */
	private Country enemyCounrty;

	/**
	 * Whether or not {@link CombatState} is currently after or before a country has
	 * been conquered.
	 */
	private boolean isPostCombat;

	/**
	 * Constructs a new {@link CombatState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link CombatState}.
	 * @param id
	 *            The ID of this {@link CombatState}
	 */
	public CombatState(Game game, int id) {
		super(game, STATE_NAME, id);
		enemyCounrty = null;
		isPostCombat = false;
	}

	/**
	 * Sets the state if this {@link CombatState} to after a {@link Country} has
	 * been conquered.
	 */
	public void setPostCombat() {
		isPostCombat = true;
	}

	/**
	 * Sets the state if this {@link CombatState} to before a {@link Country} has
	 * been conquered.
	 */
	public void setPreCombat() {
		isPostCombat = false;
	}

	/**
	 * {@link CoreGameState#clickButton(Point)} if that returns false then
	 * {@link CoreGameState#clickBoard(Point)}.
	 */
	@Override
	public void parseClick(int button, Point click) {
		// If the states buttons were not clicked click the board.
		if (!clickButton(click)) {
			clickBoard(click);
		}
	}

	/**
	 * If there is a highlighted enemy country remove the highlight effect on that
	 * as well as the primary country.
	 */
	@Override
	public void unhighlightCountry(Country country) {

		// Unhighlight both highlighted countries when this method is called from a
		// external class.
		super.unhighlightCountry(enemyCounrty);

		enemyCounrty = null;

		super.unhighlightCountry(country);
	}

	/**
	 * Highlights a specified {@link Country}. If there is a friendly
	 * {@link Country} already highlighted then this will check if the
	 * {@link Country} is a valid target and allow highlighting accordingly.
	 */
	@Override
	public void highlightCountry(Country country) {

		if (isPostCombat) {
			highlightCountryPostCombat(country);
		} else {
			highlightCounrtyPreCombat(country);
		}

	}

	/**
	 * If a {@link Player} has not conquered a new {@link Country} since they last
	 * clicked a {@link Country}.
	 * 
	 * @param country
	 *            {@link Country} that was clicked.
	 */
	private void highlightCounrtyPreCombat(Country country) {

		// If the country is null then set the primary highlighted as null and
		// unhighlight the current enemy country.
		if (country != null) {

			// Holds the current player
			Player player = getGame().getCurrentPlayer();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			processCountry(country, player, ruler);

		} else {
			super.unhighlightCountry(enemyCounrty);
			enemyCounrty = null;
			super.unhighlightCountry(super.getHighlightedCountry());
			super.highlightCountry(country);
		}
	}

	/**
	 * If a {@link Player} has not conquered a new {@link Country} since they last
	 * clicked a {@link Country}. Remove the highlight effect on both the primary
	 * and secondary country.
	 * 
	 * @param country
	 */
	private void highlightCountryPostCombat(Country country) {

		super.unhighlightCountry(country);
		enemyCounrty = null;
		super.unhighlightCountry(getHighlightedCountry());
		super.highlightCountry(null);

		setPreCombat();
	}

	/**
	 * Displays
	 * {@link CoreGameState#render(GameContainer, StateBasedGame, Graphics) } then
	 * the current {@link Player}s name and then the enemy {@link Country}.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		// Draw player name and set the text color to the player's color
		g.setColor(getGame().getCurrentPlayer().getColor());
		g.drawString(getGame().getCurrentPlayer().toString(), 5, 20);

		if (enemyCounrty != null) {
			g.drawImage(enemyCounrty.getImage(), enemyCounrty.getPosition().x, enemyCounrty.getPosition().y);
		}

	}

	/**
	 * Processes a button press on this {@link CombatState}.
	 */
	@Override
	public void parseButton(int key, char c) {
		// Do nothing
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
		if (getHighlightedCountry() != null && !player.equals(ruler)) {

			// if the country is a neighbour of the primary highlighted country then it is a
			// valid target.
			if (getHighlightedCountry().isNeighbour(country)) {

				System.out.println("A valid target");
				unhighlightCountry(enemyCounrty);
				enemyCounrty = country;
				enemyCounrty.setImage(enemyCounrty.getRegion().getPosition(),
						enemyCounrty.getRegion().convert(Color.yellow));

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
			unhighlightCountry(enemyCounrty);
			super.unhighlightCountry(super.getHighlightedCountry());
			super.highlightCountry(country);
		}
	}

	/**
	 * Retrieves the {@link Country} the
	 * {@link CoreGameState#getHighlightedCountry()} will sent troops too when
	 * attacking.
	 * 
	 * @return {@link Country}
	 */
	public Country getEnemyCountry() {
		return enemyCounrty;
	}
}
