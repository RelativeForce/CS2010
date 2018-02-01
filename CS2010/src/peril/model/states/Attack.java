package peril.model.states;

import peril.controllers.GameController;
import peril.helpers.UnitHelper;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLink;

public class Attack extends ModelState {

	@Override
	public boolean select(ModelCountry country, GameController api) {

		final boolean selectPrimary = selectPrimary(country, api);
		final boolean selectSecondary = selectSecondary(country);

		// If it a valid primary or secondary country
		if (selectPrimary || selectSecondary) {

			// If the country is a valid primary then the old primary is de-highlighted
			if (selectPrimary && !selectSecondary) {

				deselectAt(0);
				addSelected(country, 0);
			}
			// Otherwise the the secondary is de-highlighted and then the country is
			// highlighted.
			else {
				deselectAt(1);
				addSelected(country, 1);
			}

			return true;
		}

		deselectAll();

		return false;
	}

	/**
	 * The specified {@link ModelCountry} is a valid primary {@link ModelCountry}
	 * if:
	 * <ul>
	 * <li>Is <strong>NOT</strong> null</li>
	 * <li>It is ruled by the current player.</li>
	 * </ul>
	 */
	private boolean selectPrimary(ModelCountry country, GameController api) {
		return country != null && api.getCurrentModelPlayer().equals(country.getRuler());
	}

	/**
	 * The specified {@link ModelCountry} is a valid primary {@link ModelCountry}
	 * if:
	 * <ul>
	 * <li>Is <strong>NOT</strong> null</li>
	 * <li>It is a valid target of the primary {@link ModelCountry}.</li>
	 * </ul>
	 */
	private boolean selectSecondary(ModelCountry country) {

		if (country == null) {
			return false;
		}

		return getPrimary() != null && isValidTarget(getPrimary(), country);
	}

	/**
	 * Retrieves the secondary selected {@link ModelCountry}.
	 * 
	 * @return Primary selected {@link ModelCountry}
	 */
	public ModelCountry getPrimary() {
		return getSelected(0);
	}

	/**
	 * Retrieves the secondary selected {@link ModelCountry}.
	 * 
	 * @return Primary selected {@link ModelCountry}
	 */
	public ModelCountry getSecondary() {
		return getSelected(1);
	}

	/**
	 * Retrieves whether or not the secondary {@link ModelCountry} is a valid
	 * targets for the primary {@link ModelCountry}.
	 * 
	 * @param primary
	 *            is the primary {@link ModelCountry}
	 * @param target
	 *            is the second {@link ModelCountry}
	 * @return <code>boolean</code> if it is a valid target.
	 */
	public boolean isValidTarget(ModelCountry primary, ModelCountry target) {

		// If there is a primary friendly country and the target is not null and the
		// ruler of the country is not the player.
		if (!primary.getRuler().equals(target.getRuler())) {

			// if the country is a neighbour of the primary highlighted country then it is a
			// valid target.
			if (primary.isNeighbour(target)) {

				// If the army size of the primary country is greater than 1.
				if (primary.getArmy().getStrength() > 1) {

					// Hold the immutable function parameters.
					final UnitHelper units = UnitHelper.getInstance();
					final ModelLink link = primary.getLinkTo(target);

					// Whether a unit can be transfer between the primary and the target.
					boolean canTranfer = false;
					
					// The current unit in the iteration
					ModelUnit current = units.getStrongest();

					// While there is a units to iterate over.
					while (current != null) {

						// If the primary country has the current unit.
						if (primary.getArmy().hasUnit(current)) {

							// If the current unit can be transfered.
							if (link.canTransfer(current, primary, target)) {
								canTranfer = true;
								break;
							}
						}
						
						current = units.getUnitBelow(current);
					}

					// If there is a unit that can be transfered.
					if (canTranfer) {
						return true;
					}
				}
			}
		}

		// If any of the conditions are not met then the target is not valid.
		return false;
	}

}
