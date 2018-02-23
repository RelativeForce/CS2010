package peril.model.states.combat;

import peril.model.board.ModelCountry;

/**
 * Denotes a round of combat that can be processed by the {@link CombatHelper}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-23
 * @version 1.01.02
 * 
 * @see CombatHelper
 * @see ModelSquad
 * @see ModelCountry
 *
 */
public final class CombatRound {

	/**
	 * The {@link ModelCountry} that will attack the {@link #defender}
	 * {@link ModelCountry}.
	 */
	public final ModelCountry attacker;

	/**
	 * The {@link ModelCountry} that will defend against the {@link #attacker}
	 * {@link ModelCountry}.
	 */
	public final ModelCountry defender;

	/**
	 * The {@link ModelSquad} that will attack the {@link #defenderSquad}.
	 */
	public final ModelSquad attackerSquad;

	/**
	 * The {@link ModelSquad} that will defend against the {@link #defenderSquad}.
	 */
	public final ModelSquad defenderSquad;

	/**
	 * Constructs a new {@link CombatRound}.
	 * 
	 * @param attacker
	 *            The {@link ModelCountry} that will attack the {@link #defender}
	 *            {@link ModelCountry}.
	 * @param defending
	 *            The {@link ModelCountry} that will defend against the
	 *            {@link #attacker} {@link ModelCountry}.
	 * @param attackerSquad
	 *            The {@link ModelSquad} that will attack the
	 *            {@link #defenderSquad}.
	 * @param defenderSquad
	 *            The {@link ModelSquad} that will defend against the
	 *            {@link #defenderSquad}.
	 */
	public CombatRound(ModelCountry attacker, ModelCountry defending, ModelSquad attackerSquad,
			ModelSquad defenderSquad) {

		this.attacker = attacker;
		this.defender = defending;
		this.attackerSquad = attackerSquad;
		this.defenderSquad = defenderSquad;
	}

}
