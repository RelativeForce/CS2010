package peril.model.states.combat;

import peril.controllers.GameController;
import peril.model.ModelPlayer;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLink;
import peril.model.states.ModelState;

/**
 * 
 * The logic of the {@link ModelState} where the current {@link ModelPlayer} can
 * select any of their {@link ModelCountry}s and then a {@link ModelCountry}
 * that is directly linked to the {@link ModelPlayer} that is ruled by an enemy
 * {@link ModelPlayer}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-23
 * @version 1.01.02
 * 
 * @see ModelState
 *
 */
public final class Attack extends ModelState {

	/**
	 * The name of the {@link Attack} state.
	 */
	private static final String STATE_NAME = "Combat";

	/**
	 * Holds the {@link CombatHelper} that will handle combat during the
	 * {@link Attack} state.
	 */
	public final CombatHelper combat;

	/**
	 * Constructs a new {@link Attack}.
	 */
	public Attack() {
		super(STATE_NAME);
		this.combat = new CombatHelper();
	}

	/**
	 * During {@link Attack} the current {@link ModelPlayer} can select any of their
	 * {@link ModelCountry}s and then a {@link ModelCountry} that is directly linked
	 * to the {@link ModelPlayer} that is ruled by an enemy {@link ModelPlayer}.
	 */
	@Override
	public boolean select(ModelCountry country, GameController api) {

		// Whether the country is a valid primary country.
		final boolean validPrimary = selectPrimary(country, api);

		// Whether the country is a valid secondary country.
		final boolean validSecondary = selectSecondary(country);

		// If it a valid primary or secondary country
		if (validPrimary || validSecondary) {

			// If the country is a valid primary then the old primary is de-selected.
			if (validPrimary && !validSecondary) {

				deselectAt(0);
				addSelected(country, 0);
			}
			// Otherwise the current secondary is de-selected and then the country is
			// selected.
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
	 * Retrieves the primary selected {@link ModelCountry}.
	 * 
	 * @return Primary selected {@link ModelCountry}.
	 */
	public ModelCountry getPrimary() {
		return getSelected(0);
	}

	/**
	 * Retrieves the secondary selected {@link ModelCountry}.
	 * 
	 * @return Secondary selected {@link ModelCountry}.
	 */
	public ModelCountry getSecondary() {
		return getSelected(1);
	}

	/**
	 * Retrieves whether or not the secondary {@link ModelCountry} is a valid
	 * targets for the primary {@link ModelCountry}.
	 * 
	 * @param primary
	 *            The primary {@link ModelCountry}.
	 * @param target
	 *            The target {@link ModelCountry}.
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
				if (primary.getArmy().getNumberOfUnits() > 1) {

					// Holds the link between the two countries.
					final ModelLink link = primary.getLinkTo(target);

					// Whether a unit can be transfer between the primary and the target.
					boolean canTranfer = false;

					/*
					 * Iterate over all the units in the army and if any can be transfered across
					 * the link.
					 */
					for (ModelUnit unit : primary.getArmy()) {

						// If the current unit can be transfered.
						if (link.canTransfer(unit, primary, target)) {
							canTranfer = true;
							break;
						}
					}

					// If there is a unit that can be transfered then it is a valid target.
					if (canTranfer) {
						return true;
					}
				}
			}
		}

		// If any of the conditions are not met then the target is not valid.
		return false;
	}

	/**
	 * Retrieves whether or not the specified {@link ModelCountry} could be selected
	 * as the {@link Attack}'s primary {@link ModelCounrty}.
	 * 
	 * @param country
	 *            The {@link ModelCountry} that could be selected.
	 * @param game
	 *            The {@link GameController} that allows this {@link Attack} to
	 *            query the state of the game.
	 * @return Whether or not the specified {@link ModelCountry} could be selected.
	 */
	private boolean selectPrimary(ModelCountry country, GameController game) {

		// If the country is null then it cannot be selected.
		if (country == null) {
			return false;
		}

		// If the country is ruler by the current player then it is valid primary.
		return game.getCurrentModelPlayer().equals(country.getRuler());
	}

	/**
	 * Retrieves whether or not the specified {@link ModelCountry} could be selected
	 * as the {@link Attack}'s secondary {@link ModelCounrty}.
	 * 
	 * @param country
	 *            The {@link ModelCountry} that could be selected.
	 * @return Whether or not the specified {@link ModelCountry} could be selected.
	 */
	private boolean selectSecondary(ModelCountry country) {

		// If the country is null then it cannot be selected.
		if (country == null) {
			return false;
		}

		// If there is a primary selected and the country is a valid target then the
		// country is a valid secondary.
		return getPrimary() != null && isValidTarget(getPrimary(), country);
	}

}
