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

	OPEN("Open"){
		
		
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
		
		
		
	};
	
	public final String name;
	
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
	abstract boolean canTransfer(ModelUnit unit, ModelCountry origin, ModelCountry destination);

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
	abstract void transferBetween(ModelUnit unit, ModelCountry origin, ModelCountry destination);

}
