package peril.ai.api;

import java.util.List;

/**
 * Provides information about a continent on the {@link Board} that consists of
 * many {@link Country}s.
 * 
 * @author Joshua_Eddy
 *
 */
public interface Continent {

	/**
	 * Retrieves the {@link Player} owner of this {@link Continent}.
	 * @return {@link Player}.
	 */
	Player getOwner();

	/**
	 * Retrieves a {@link List} of {@link Country}s that make up this {@link Continent}.
	 * @return
	 */
	List<? extends Country> getCountries();

}
