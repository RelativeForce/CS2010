package peril.model.board.links;

import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;

/**
 * Encapsulates the behaviour of a type of link between two
 * {@link ModelCountry}s used by {@link ModelLink}.
 * 
 * @author Joshua_Eddy
 *
 */
public enum ModelLinkState {

	/**
	 * A {@link ModelLinkState} that is completely open and does not effect units
	 * transferring over it.
	 */
	OPEN("Open") {

		@Override
		public boolean canTransfer(ModelUnit unit, ModelCountry origin, ModelCountry destination) {
			return true;
		}

		@Override
		public void transferBetween(ModelUnit unit, ModelCountry origin, ModelCountry destination) {

			// Move the unit.
			origin.getArmy().remove(unit.strength);
			destination.getArmy().add(unit.strength);
		}
	},
	/**
	 * A {@link ModelLinkState} that prevents any units from being transfered across
	 * it.
	 */
	BLOCKADE("Blockade") {

		@Override
		public boolean canTransfer(ModelUnit unit, ModelCountry origin, ModelCountry destination) {
			return false;
		}

		@Override
		public void transferBetween(ModelUnit unit, ModelCountry origin, ModelCountry destination) {
			throw new IllegalStateException(name + " cannot transfer units.");
		}
	},
	/**
	 * A {@link ModelLinkState} that allows units to move between two friendly
	 * countries freely but if the origin is an enemy of the destination then the
	 * strength of the transfered unit is reduced by 40%.
	 */
	FORTIFIED("Fortified") {

		@Override
		public boolean canTransfer(ModelUnit unit, ModelCountry origin, ModelCountry destination) {
			return true;
		}

		@Override
		public void transferBetween(ModelUnit unit, ModelCountry origin, ModelCountry destination) {

			if (origin.getRuler() == destination.getRuler()) {

				// Move the unit.
				origin.getArmy().remove(unit.strength);
				destination.getArmy().add(unit.strength);
			} else {

				// Add Only move 60% of the unit
				origin.getArmy().remove(unit.strength);
				destination.getArmy().add((unit.strength * 10) / 6);

			}

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
	 * Retrieves whether or not a {@link ModelUnit} can be transfered along this
	 * {@link ModelLinkState} between the two {@link ModelCountry}s.
	 * 
	 * @param unit
	 *            {@link ModelUnit}
	 * @param origin
	 *            {@link ModelCountry}
	 * @param destination
	 *            {@link ModelCountry}
	 * @return boolean
	 */
	public abstract boolean canTransfer(ModelUnit unit, ModelCountry origin, ModelCountry destination);

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
	public abstract void transferBetween(ModelUnit unit, ModelCountry origin, ModelCountry destination);

}
