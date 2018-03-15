package peril.model.board.links;

import java.util.Observable;

import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;

/**
 * 
 * Encapsulates the behaviour of a link between two {@link ModelCountry}s. This
 * link can have multiple states.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.03
 * @since 2018-03-15
 * 
 * @see Observable
 * @see ModelLinkState
 *
 */
public final class ModelLink extends Observable {

	/**
	 * Holds the default {@link ModelLinkState} that this {@link ModelLink} will
	 * return too once the {@link ModelLink#duration} has expired.
	 */
	private final ModelLinkState defaultState;

	/**
	 * Holds the current {@link ModelLinkState} of this {@link ModelLink}.
	 */
	private ModelLinkState current;

	/**
	 * Holds the remaining number of rounds that the current {@link ModelLink} has
	 * before it returns to the {@link ModelLink#defaultState}.
	 */
	private int duration;

	/**
	 * Constructs a new {@link ModelLink}.
	 * 
	 * @param defaultState
	 *            The default {@link ModelLinkState} that this {@link ModelLink}
	 *            will return too once the {@link ModelLink#duration} has expired.
	 */
	public ModelLink(ModelLinkState defaultState) {
		this.defaultState = defaultState;
		this.duration = 0;
		this.current = defaultState;
	}

	/**
	 * Assigns a new {@link ModelLinkState} to this {@link ModelLink}.
	 * 
	 * @param state
	 *            The new {@link ModelLinkState}.
	 * @param duration
	 *            The remaining number of rounds that the current {@link ModelLink}
	 *            has before it returns to the {@link ModelLink#defaultState}.
	 */
	public void setState(ModelLinkState state, int duration) {

		if (duration <= 0) {
			throw new IllegalArgumentException(
					"The duration of " + state.name + " cannot be less than or equal to zero.");
		}

		if (state == null) {
			throw new NullPointerException("State cannot be null.");
		}

		this.duration = (state == defaultState) ? 0 : duration;
		this.current = state;

		setChanged();
		notifyObservers();
	}

	/**
	 * Retrieves whether of not this {@link ModelLink} is in its default
	 * {@link ModelLinkState}.
	 * 
	 * @return
	 */
	public boolean isDefault() {
		return current.name.equals(defaultState.name);
	}

	/**
	 * Elapses a round.
	 */
	public void elapse() {

		// If the link is in its default state
		if (!isDefault()) {
			duration--;

			if (duration <= 0) {
				returnToDefault();
			}
		}
	}

	/**
	 * Returns this {@link ModelLink} to its default {@link ModelLinkState}.
	 */
	public void returnToDefault() {
		this.current = defaultState;
		this.duration = 0;

		setChanged();
		notifyObservers();
	}

	/**
	 * Retrieves whether or not a {@link ModelUnit} can be transfered along this
	 * {@link ModelLinkState} between the two {@link ModelCountry}s.
	 * 
	 * @param unit
	 *            {@link ModelUnit}
	 * @param origin
	 *            {@link ModelCountry}
	 * @param destination
	 *            {@link ModelCountry}
	 * @return Whether or not a {@link ModelUnit} can be transfered along this
	 *         {@link ModelLinkState} between the two {@link ModelCountry}s.
	 */
	public boolean canTransfer(ModelUnit unit, ModelCountry origin, ModelCountry destination) {
		return current.canTransfer(unit, origin, destination);
	}

	/**
	 * Retrieves the remaining duration of the current {@link ModelLinkState}.
	 * 
	 * @return The duration of the current {@link ModelLinkState}.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Retrieves the current {@link ModelLinkState} of this {@link ModelLink}.
	 * 
	 * @return {@link ModelLinkState}
	 */
	public ModelLinkState getState() {
		return current;
	}

	/**
	 * Retrieves the default {@link ModelLinkState} of this {@link ModelLink}.
	 * 
	 * @return The default {@link ModelLinkState}.
	 */
	public ModelLinkState getDefaultState() {
		return defaultState;
	}

	/**
	 * Transfers a unit between two countries.
	 * 
	 * @param unit
	 *            {@link ModelUnit} that will be transfered across the link.
	 * @param origin
	 *            {@link ModelCountry}
	 * @param destination
	 *            {@link ModelCountry}
	 * @return Whether or not the {@link ModelUnit} was transfered or not.
	 */
	public boolean transferBetween(ModelUnit unit, ModelCountry origin, ModelCountry destination) {

		// Check the model countries border each other.
		if (!origin.isNeighbour(destination) && !destination.isNeighbour(origin)) {
			throw new IllegalArgumentException("Parameter countries must be neighbours of each other");
		}

		// If the unit can be transfered across the link return false
		if (!current.canTransfer(unit, origin, destination)) {
			return false;
		}

		// Process the transfer
		current.transferBetween(unit, origin, destination);

		return true;

	}
}
