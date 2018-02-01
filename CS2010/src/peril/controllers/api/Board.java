package peril.controllers.api;

import java.util.Map;
import java.util.Set;

/**
 * Provides information about the board which consists of many
 * {@link Continent}s and {@link Country}s.
 * 
 * @author Joshua_Eddy
 *
 */
public interface Board {

	/**
	 * Retrieves all the {@link Continent}s on this {@link Board}.
	 * 
	 * @return
	 */
	Map<String, ? extends Continent> getContinents();

	/**
	 * Retrieves the number of {@link Country}s on this {@link Board}.
	 * 
	 * @return
	 */
	int getNumberOfCountries();

	/**
	 * Retrieves all the {@link Country}s on this {@link Board}.
	 * 
	 * @return
	 */
	Set<? extends Country> getCountries();
}
