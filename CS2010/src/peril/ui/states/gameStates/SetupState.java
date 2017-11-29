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
import peril.ui.components.TextField;

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

	private final TextField about;

	private boolean showAbout;

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
		about = new TextField(400, 240, new Point(130, 0));
		super.addComponent(about);
		showAbout = false;
	}

	/**
	 * Enters this {@link SetupState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);

		about.clear();

		about.addText("This is the 'Set Up' state. In this phase of the "
				+ "game the players select which players own which countries at the start of the game.");

		about.addText("To set a player as the ruler of a country, simply click on the country then press "
				+ "the number button that coresponds to the number of the player."
				+ " For example: '1' corresponds to 'Player 1'. 'Space' makes the country neutral.");

		about.addText("Click 'Auto' to randomly assign an equal number of countries to each player.");

		about.addText("Once you have assigned the countries as you desire click 'Play' to start the game.");
	}

	/**
	 * Renders this {@link SetupState}.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		drawImages(g);
		drawButtons(g);

		if (showAbout)
			about.draw(g);

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

	public void showAbout() {
		showAbout = true;
	}

	public void hideAbout() {
		showAbout = false;
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
				if (getGame().players.isPlaying(1)) {
					swapRuler(highlighted, getGame().players.get(1));
				}
				break;
			case Input.KEY_2:
				if (getGame().players.isPlaying(2)) {
					swapRuler(highlighted, getGame().players.get(2));
				}
				break;
			case Input.KEY_3:
				if (getGame().players.isPlaying(3)) {
					swapRuler(highlighted, getGame().players.get(3));
				}
				break;
			case Input.KEY_4:
				if (getGame().players.isPlaying(4)) {
					swapRuler(highlighted, getGame().players.get(4));
				}
				break;
			case Input.KEY_SPACE:
				swapRuler(highlighted, null);
				break;
			}
		}

		if (key == Input.KEY_H) {
			showAbout();
		}
		if (key == Input.KEY_ESCAPE) {
			hideAbout();
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
