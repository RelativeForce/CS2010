package peril.ui.states.gameStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.java.games.input.Component;
import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Country;

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
	 */
	public SetupState(Game game, int id) {
		super(game, STATE_NAME, id);
	}

	/**
	 * Renders this {@link SetupState}.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);
		super.drawAllLinks(g);
		super.drawArmies(g);
		super.drawImages(g);
		super.drawButtons(g);
		super.drawPopups(g);
		super.drawHelp(g);
		super.drawPauseMenu(g);
		super.drawChallengeMenu(g);

	}

	/**
	 * Parses a button press on this {@link SetupState}.
	 */
	@Override
	public void parseButton(int key, char c, Point mousePosition) {

		Country highlighted = getSelected();

		/*
		 * If the player has highlighted a county then parse the button presses that
		 * change the owner ship of a country.
		 */
		if (highlighted != null) {

			switch (key) {
			case Input.KEY_1:
				if (getGame().players.isPlaying(1)) {
					swapRuler(highlighted, getGame().players.getPlayer(1));
				}
				break;
			case Input.KEY_2:
				if (getGame().players.isPlaying(2)) {
					swapRuler(highlighted, getGame().players.getPlayer(2));
				}
				break;
			case Input.KEY_3:
				if (getGame().players.isPlaying(3)) {
					swapRuler(highlighted, getGame().players.getPlayer(3));
				}
				break;
			case Input.KEY_4:
				if (getGame().players.isPlaying(4)) {
					swapRuler(highlighted, getGame().players.getPlayer(4));
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
	 * During {@link SetupState} any country is select-able.
	 */
	@Override
	public boolean select(Country country) {
		removeHighlight(getSelected());
		addHighlight(country);
		setSelected(country);
		return true;
	}

	/**
	 * Moves all the {@link Component}s of this {@link SetupState}.
	 */
	@Override
	protected void panElements(Point panVector) {
		// No elements to pan.
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
			oldRuler.totalArmy.remove(1);
		}

		// Reassign the ruler of the country.
		country.setRuler(newRuler);

		// If the country has a new ruler increase the number of countries that player
		// owns by one.
		if (newRuler != null) {
			newRuler.setCountriesRuled(newRuler.getCountriesRuled() + 1);
			newRuler.totalArmy.add(1);
		}

	}
}
