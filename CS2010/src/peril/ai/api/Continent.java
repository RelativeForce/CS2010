package peril.ai.api;

import java.util.Set;

/**
 * Provides information about a continent on the {@link Board} that consists of
 * many {@link Country}s.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-02-06
 *
 */
public interface Continent {

	/**
	 * Retrieves the {@link Player} owner of this {@link Continent}.
	 * 
	 * @return {@link Player}.
	 */
	Player getOwner();

	/**
	 * Retrieves a {@link Set} of {@link Country}s that make up this
	 * {@link Continent}.
	 * 
	 * @return {@link Set}&lt;? extends {@link Country}&gt;
	 */
	Set<? extends Country> getCountries();

}
