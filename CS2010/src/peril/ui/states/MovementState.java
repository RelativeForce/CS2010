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

public class MovementState extends CoreGameState {

	/**
	 * The ID of this {@link MovementState}.
	 */
	private static final int ID = 4;

	/**
	 * The target {@link Country} for the
	 * {@link CoreGameState#getHighlightedCountry()}.
	 */
	private Country targetCounrty;

	public MovementState(Game game) {
		super(game);
		stateName = "Movement";
		targetCounrty = null;
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
			super.unhighlightCountry(targetCounrty);
			targetCounrty = null;
			super.highlightCountry(country);
		}

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
		// DO NOTHING
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		// Draw player name and set the text color to the player's color
		g.setColor(game.getCurrentPlayer().getColor());
		g.drawString(game.getCurrentPlayer().toString(), 5, 20);

	}

	@Override
	public void unhighlightCountry(Country country) {

		// Unhighlight both highlighted countries when this method is called from a
		// external class.
		super.unhighlightCountry(targetCounrty);
		super.unhighlightCountry(country);
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
