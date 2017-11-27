package peril.ui.states.gameStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Country;
import peril.ui.components.menus.PauseMenu;

/**
 * The state where the user selects which player gets what {@link Country}s.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 * 
 *         {@link CoreGameState}
 *
 */
public final class SetupState extends CoreGameState {

	/**
	 * The name of a specific {@link SetupState}.
	 */
	private static final String STATE_NAME = "Setup";

	/**
	 * Constructs a new {@link SetupState}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link SetupState} is a part of.
	 * @param id
	 *            The ID of this {@link SetupState}
	 * @param pauseMenu
	 *            The {@link PauseMenu} for this {@link SetupState}.
	 */
	public SetupState(Game game, int id, PauseMenu pauseMenu) {
		super(game, STATE_NAME, id, pauseMenu);
	}

	/**
	 * Renders this {@link SetupState}.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		drawImages(g);
		drawButtons(g);

		drawPauseMenu(g);

	}

	/**
	 * Handles highlighting a {@link Country} on the {@link SetupState}.
	 */
	@Override
	public void highlightCountry(Country country) {

		// Unhighlight the current country
		super.removeHighlightFrom(super.getHighlightedCountry());

		// Highlight the new country
		super.highlightCountry(country);

	}

	/**
	 * Parses a button press on this {@link SetupState}.
	 */
	@Override
	public void parseButton(int key, char c, Point mousePosition) {

		Country highlighted = getHighlightedCountry();

		/*
		 * If the player has highlighted a county then parse the button presses that
		 * change the owner ship of a country.
		 */
		if (highlighted != null) {

			switch (key) {
			case Input.KEY_1:
				if (getGame().players.isPlaying(Player.ONE)) {
					swapRuler(highlighted, Player.ONE);
				}
				break;
			case Input.KEY_2:
				if (getGame().players.isPlaying(Player.TWO)) {
					swapRuler(highlighted, Player.TWO);
				}
				break;
			case Input.KEY_3:
				if (getGame().players.isPlaying(Player.THREE)) {
					swapRuler(highlighted, Player.THREE);
				}
				break;
			case Input.KEY_4:
				if (getGame().players.isPlaying(Player.FOUR)) {
					swapRuler(highlighted, Player.FOUR);
				}
				break;
			case Input.KEY_SPACE:
				swapRuler(highlighted, null);
				break;
			}
		}

		super.parseButton(key, c, mousePosition);

	}

	/**
	 * Swaps the {@link Player} ruler of a specified {@link Country} for a new
	 * {@link player}.
	 * 
	 * @param country
	 *            {@link Country}
	 * @param newRuler
	 *            {@link Player} new ruler of the {@link Country}.
	 */
	private void swapRuler(Country country, Player newRuler) {

		// Holds the old ruler of the country
		Player oldRuler = country.getRuler();

		// If the country has a ruler reduce the number of countries that player owns by
		// one.
		if (oldRuler != null) {
			oldRuler.setCountriesRuled(oldRuler.getCountriesRuled() - 1);
			oldRuler.setTotalArmySize(oldRuler.getTotalArmySize() - 1);
		}

		// Reassign the ruler of the country.
		country.setRuler(newRuler);

		// If the country has a new ruler increase the number of countries that player
		// owns by one.
		if (newRuler != null) {
			newRuler.setCountriesRuled(newRuler.getCountriesRuled() + 1);
			newRuler.setTotalArmySize(newRuler.getTotalArmySize() + 1);
		}

	}
}
