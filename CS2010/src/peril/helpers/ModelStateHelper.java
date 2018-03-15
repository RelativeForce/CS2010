package peril.helpers;

import peril.Game;
import peril.model.ModelPlayer;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.board.ModelArmy;
import peril.model.states.*;
import peril.model.states.combat.Attack;

/**
 * A helper class for {@link Game} this object stores the instances of the
 * various {@link ModelState}s.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-06
 * @version 1.01.01
 *
 */
public class ModelStateHelper {

	/**
	 * The state that displays combat to the user.
	 */
	public final Attack attack;

	/**
	 * The state that will allow the user to set up which {@link ModelPlayer} owns
	 * which {@link ModelCountry}.
	 */
	public final Setup setup;

	/**
	 * The state that allows the {@link ModelPlayer} to distribute their
	 * {@link ModelArmy} to the {@link ModelCountry}s they rule.
	 */
	public final Reinforce reinforce;

	/**
	 * The state which lets the user move {@link ModelArmy}'s {@link ModelUnit}(s)
	 * from one {@link ModelCountry} to another.
	 */
	public final Fortify fortify;

	/**
	 * Constructs a new {@link ModelStateHelper}.
	 * 
	 * @param combat
	 *            The state that displays combat to the user.
	 * @param reinforcement
	 *            The state that allows the {@link ModelPlayer} to distribute their
	 *            {@link ModelArmy} to the {@link ModelCountry}s they rule.
	 * @param setup
	 *            The state that will allow the user to set up which
	 *            {@link ModelPlayer} owns which {@link ModelCountry}.
	 * @param movement
	 *            The state which lets the user move {@link ModelArmy}'s
	 *            {@link ModelUnit}(s) from one {@link ModelCountry} to another.
	 * 
	 */
	public ModelStateHelper(Attack combat, Reinforce reinforcement, Setup setup, Fortify movement) {

		this.attack = combat;
		this.reinforce = reinforcement;
		this.setup = setup;
		this.fortify = movement;

	}

	/**
	 * Clears the selected {@link ModelCountry}s from all the {@link ModelState}s.
	 */
	public void clearAll() {
		setup.deselectAll();
		reinforce.deselectAll();
		attack.deselectAll();
		fortify.deselectAll();
	}

}
