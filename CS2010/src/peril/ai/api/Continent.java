package peril.ai.api;

import java.util.Set;

/**
 * Provides information about a continent on the {@link Board} that consists of
 * many {@link Country}s.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.02
 * @since 2018-03-16
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

	/**
	 * Retrieves maximum percentage of the {@link Unit}s in a {@link Country}'s
	 * {@link Army} that the environmental hazard for this {@link Continent} will
	 * kill if it occurs.
	 * 
	 * @return The maximum percentage the hazard will kill.
	 * 
	 * @see #getHazardFrequency()
	 */
	int getMaxHazardCausalties();

	/**
	 * The percentages chance of the environmental hazard that occurs on this
	 * {@link Continent} of happening.
	 * 
	 * @return The percentage chance that the hazard will occur at the end of a
	 *         round.
	 */
	int getHazardFrequency();

}
