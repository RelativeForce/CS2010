package peril.model.board.links;

import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;

/**
 * Encapsulates the behaviour of a type of link between two
 * {@link ModelCountry}s used by {@link ModelLink}.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.03
 * @since 2018-03-16
 *
 */
public enum ModelLinkState {

	/**
	 * A {@link ModelLinkState} that is completely open and does not effect units
	 * transferring over it.
	 */
	OPEN("Open") {

		@Override
		public boolean canTransfer(ModelUnit unit) {
			return true;
		}

		@Override
		public void transfer(ModelUnit unit, ModelCountry origin, ModelCountry destination) {

			// Move the unit.
			origin.getArmy().remove(unit);
			destination.getArmy().add(unit);
		}
	},
	/**
	 * A {@link ModelLinkState} that prevents any units from being transfered across
	 * it.
	 */
	BLOCKADE("Blockade") {

		@Override
		public boolean canTransfer(ModelUnit unit) {
			return false;
		}

		@Override
		public void transfer(ModelUnit unit, ModelCountry origin, ModelCountry destination) {
			throw new IllegalStateException(name + " cannot transfer units.");
		}
	};

	/**
	 * The name of this {@link ModelLinkState}.
	 */
	public final String name;

	/**
	 * Constructs a new {@link ModelLinkState}.
	 * 
	 * @param name
	 *            The name of this {@link ModelLinkState}.
	 */
	private ModelLinkState(String name) {
		this.name = name;

	}

	/**
	 * Retrieves the {@link ModelLinkState} with the specified name.
	 * 
	 * @param name
	 *            The name of a {@link ModelLinkState}.
	 * @return The {@link ModelLinkState} with the specified name.
	 */
	public static ModelLinkState get(String name) {

		// Iterate over all the model link states.
		for (ModelLinkState linkState : values()) {
			if (linkState.name.equals(name)) {
				return linkState;
			}
		}

		return null;

	}

	/**
	 * Retrieves whether or not a {@link ModelUnit} can be transfered along this
	 * {@link ModelLinkState} between the two {@link ModelCountry}s.
	 * 
	 * @param unit
	 *           The {@link ModelUnit} to be transfered.
	 * @return boolean Retrieves whether or not a {@link ModelUnit} can be
	 *         transfered.
	 */
	public abstract boolean canTransfer(ModelUnit unit);

	/**
	 * Performs the transfer operation on the {@link ModelUnit}.
	 * 
	 * @param unit
	 *            {@link ModelUnit}
	 * @param origin
	 *            {@link ModelCountry}
	 * @param destination
	 *            {@link ModelCountry}
	 */
	public abstract void transfer(ModelUnit unit, ModelCountry origin, ModelCountry destination);

}
