package peril.model.states;

import java.util.Random;

import peril.controllers.GameController;
import peril.helpers.PlayerHelper;
import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.views.slick.states.gameStates.SetupState;

public class Setup extends ModelState {

	/**
	 * The name of a specific {@link Setup}.
	 */
	private static final String STATE_NAME = "Setup";

	/**
	 * Constructs a new {@link Setup}.
	 */
	public Setup() {
		super(STATE_NAME);
	}

	/**
	 * During {@link SetupState} any country is select-able.
	 */
	public boolean select(ModelCountry country, GameController api) {

		deselectAll();
		addSelected(country, 0);

		return true;
	}

	/**
	 * Distributes the countries between the {@link Game#players} equally.
	 */
	public void autoDistributeCountries(ModelBoard board, PlayerHelper players) {

		players.forEach(player -> {
			player.setCountriesRuled(0);
			player.setContinentsRuled(0);
			player.totalArmy.setStrength(0);
		});

		// Iterate through each country on the board.
		board.forEachCountry(country -> assignPlayer(country, players, board.getNumberOfCountries()));

	}

	/**
	 * Assigns a {@link SlickPlayer} ruler to a {@link SlickCountry} using a
	 * parameter {@link Random}.
	 * 
	 * @param country
	 *            {@link SlickCountry} that is to be ruled.
	 */
	private void assignPlayer(ModelCountry country, PlayerHelper players, int numberOfCountries) {

		// Holds whether the country has assigned a player ruler.
		boolean set = false;

		while (!set) {

			// Holds the number of players in the game.
			int numberOfPlayers = players.numberOfPlayers();

			// Get the player at the random index.
			ModelPlayer player = players.getRandomPlayer();

			// Holds the maximum countries this player can own so that all the players nd up
			// with the same number of countries.
			int maxCountries;

			/*
			 * If the number of countries on this board can be equally divided between the
			 * players then set the max number of countries of that a player can own to the
			 * equal amount for each player. Otherwise set the max number of countries of
			 * that a player can own to one above the normal proportion so the to account
			 * for the left over countries.
			 */
			if (numberOfCountries % numberOfPlayers == 0) {
				maxCountries = numberOfCountries / numberOfPlayers;
			} else {
				maxCountries = numberOfCountries / numberOfPlayers + 1;
			}

			// If the player owns more that their fair share of the maps countries assign
			// the player again.
			if (player.getCountriesRuled() < maxCountries) {
				set = true;
				country.setRuler(player);
				player.setCountriesRuled(player.getCountriesRuled() + 1);
				player.totalArmy.add(UnitHelper.getInstance().getWeakest());
			}

		}

	}

}
