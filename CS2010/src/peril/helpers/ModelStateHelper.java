package peril.helpers;

import peril.Game;
import peril.model.states.Attack;
import peril.model.states.Fortify;
import peril.model.states.Reinforce;
import peril.model.states.Setup;

/**
 * A helper class for {@link Game} this object stores the instances of the
 * various {@link InteractiveState}s of the {@link Game}.
 * 
 * @author Joshua_Eddy
 *
 */
public class ModelStateHelper {

	/**
	 * The state that displays combat to the user. This is heavily couples with
	 * {@link WarMenu}.
	 */
	public final Attack combat;

	/**
	 * The {@link SetupState} that will allow the user to set up which
	 * {@link SlickPlayer} owns which {@link SlickCountry}.
	 */
	public final Setup setup;

	/**
	 * The {@link ReinforcementState} that allows the {@link SlickPlayer} to
	 * distribute their {@link ModelArmy} to the {@link SlickCountry}s they rule.
	 */
	public final Reinforce reinforcement;

	/**
	 * The {@link MovementState} which lets the user move {@link ModelArmy}s from
	 * one {@link SlickCountry} to another.
	 */
	public final Fortify movement;

	/**
	 * Constructs a new {@link ModelStateHelper}.
	 * 
	 * @param combat
	 *            The state that displays combat to the user. This is heavily
	 *            couples with {@link WarMenu}.
	 * @param reinforcement
	 *            The {@link ReinforcementState} that allows the {@link SlickPlayer}
	 *            to distribute their {@link ModelArmy} to the {@link SlickCountry}s
	 *            they rule.
	 * @param setup
	 *            The {@link SetupState} that will allow the user to set up which
	 *            {@link SlickPlayer} owns which {@link SlickCountry}.
	 * @param movement
	 *            The {@link MovementState} which lets the user move
	 *            {@link ModelArmy}s from one {@link SlickCountry} to another.

	 */
	public ModelStateHelper(Attack combat, Reinforce reinforcement, Setup setup, Fortify movement) {

		this.combat = combat;
		this.reinforcement = reinforcement;
		this.setup = setup;
		this.movement = movement;

	}

	public void clearAll() {
		setup.deselectAll();
		reinforcement.deselectAll();
		combat.deselectAll();
		movement.deselectAll();
	}

}
