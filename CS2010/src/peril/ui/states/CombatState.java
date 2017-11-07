package peril.ui.states;

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
	 * The ID of this {@link CombatState}
	 */
	private static final int ID = 3;

	/**
	 * The target {@link Country} for the
	 * {@link CoreGameState#getHighlightedCountry()}.
	 */
	private Country enemyCounrty;

	/**
	 * Constructs a new {@link CombatState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link CoreGameState}.
	 */
	public CombatState(Game game) {
		super(game);
		stateName = "Combat";
		enemyCounrty = null;
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
	public void unhighlightCountry(Country country) {

		// Unhighlight both highlighted countries when this method is called from a
		// external class.
		super.unhighlightCountry(enemyCounrty);
		super.unhighlightCountry(country);
	}

	@Override
	public void highlightCountry(Country country) {

		// If the country is null then set the primary highlighted as null and
		// unhighlight the current enemy country.
		if (country != null) {

			// Holds the current player
			Player player = game.getCurrentPlayer();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			processCountry(country, player, ruler);

		} else {
			unhighlightCountry(enemyCounrty);
			enemyCounrty = null;
			super.highlightCountry(country);
		}

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

			if (ruler != null) {

				// if the country is a neighbour of the primary highlighted country then it is a
				// valid target.
				if (getHighlightedCountry().isNeighbour(country)) {

					System.out.println("A valid target");
					unhighlightCountry(enemyCounrty);
					enemyCounrty = country;
					enemyCounrty.setImage(enemyCounrty.getRegion().getPosition(),
							enemyCounrty.getRegion().convert(Color.black));

				} else {
					// DO NOTHING
				}
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
			super.highlightCountry(country);
		}
	}
}
