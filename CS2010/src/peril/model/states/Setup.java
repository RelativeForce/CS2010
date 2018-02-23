package peril.model.states;

import peril.controllers.GameController;
import peril.helpers.PlayerHelper;
import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;

/**
 * 
 * The logic of the {@link ModelState} where the players decide which
 * {@link ModelPlayer}s rules each {@link ModelCountry}. Specific
 * {@link ModelCountry}s can be selected one at a time.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-23
 * @version 1.01.02
 * 
 * @see ModelState
 *
 */
public final class Setup extends ModelState {

	/**
	 * The name of the {@link Setup} state.
	 */
	private static final String STATE_NAME = "Setup";

	/**
	 * Constructs a new {@link Setup}.
	 */
	public Setup(GameController game) {
		super(STATE_NAME, game);
	}

	/**
	 * During {@link Setup} any country is select-able but only one can be selected
	 * at a time.
	 */
	@Override
	public boolean select(ModelCountry country) {

		deselectAll();
		addSelected(country, 0);

		return true;
	}

	/**
	 * Distributes the all the {@link ModelCountry}s on the {@link ModelBoard}
	 * between all the {@link ModelPlayer}s equally.
	 * 
	 * @param board
	 *            The {@link ModelBoard} that contains all the
	 *            {@link ModelCountry}s.
	 * @param The
	 *            {@link PlayerHelper} that contains all the {@link ModelPlayer}s in
	 *            the game.
	 */
	public void autoDistributeCountries(ModelBoard board, PlayerHelper players) {

		// Reset all the players back to their original state.
		players.forEach(player -> player.reset());

		// Iterate through each country on the board and assign it a player ruler.
		board.forEachCountry(country -> assignPlayer(country, players, board.getNumberOfCountries()));

	}

	/**
	 * Assigns a random {@link ModelPlayer} ruler to a {@link ModelCountry}.
	 * 
	 * @param country
	 *            {@link ModelCountry} that is to be ruled.
	 * @param players
	 *            The {@link PlayerHelper} that contains all the {@link ModelPlayer}
	 *            in the game.
	 * @param numberOfCountries
	 *            The total number of {@link ModelCountry}s on the
	 *            {@link ModelBoard}.
	 */
	private void assignPlayer(ModelCountry country, PlayerHelper players, int numberOfCountries) {

		// Holds whether the country has assigned a player ruler.
		boolean set = false;

		// Iterate until a ruler is selected.
		while (!set) {

			// Holds the number of players in the game.
			final int numberOfPlayers = players.numberOfPlayers();

			// Get the player at the random index.
			final ModelPlayer player = players.getRandomPlayer();

			// Holds the maximum countries this player can own so that all the players and
			// up
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
