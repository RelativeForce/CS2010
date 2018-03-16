package peril.ai.api;

import java.util.Map;
import java.util.Set;

/**
 * Provides information about the board which consists of many
 * {@link Continent}s and {@link Country}s. This board cannot be altered.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-02-06
 *
 */
public interface Board {

	/**
	 * Retrieves all the {@link Continent}s on this {@link Board}.
	 * 
	 * @return {@link Map}&lt;{@link String},? extends {@link Continent}&gt; where
	 *         the key is the name of the {@link Continent}.
	 */
	Map<String, ? extends Continent> getContinents();

	/**
	 * Retrieves the number of {@link Country}s on this {@link Board}.
	 * 
	 * @return number of {@link Country}s
	 */
	int getNumberOfCountries();

	/**
	 * Retrieves all the {@link Country}s on this {@link Board}.
	 * 
	 * @return {@link Set}&lt;? extends {@link Country}&gt;
	 */
	Set<? extends Country> getCountries();
}
