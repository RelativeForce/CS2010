package peril.model.states.combat;

import peril.model.board.ModelCountry;

/**
 * Denotes a round of combat that can be processed by the {@link CombatHelper}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-22
 * @version 1.01.01
 * 
 * @see CombatHelper
 * @see ModelSquad
 * @see ModelCountry
 *
 */
public final class CombatRound {

	/**
	 * The {@link ModelCountry} that will attack the {@link #defending}
	 * {@link ModelCountry}.
	 */
	public final ModelCountry attacking;

	/**
	 * The {@link ModelCountry} that will defend against the {@link #attacking}
	 * {@link ModelCountry}.
	 */
	public final ModelCountry defending;

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
	 * @param attacking
	 *            The {@link ModelCountry} that will attack the {@link #defending}
	 *            {@link ModelCountry}.
	 * @param defending
	 *            The {@link ModelCountry} that will defend against the
	 *            {@link #attacking} {@link ModelCountry}.
	 */
	public CombatRound(ModelCountry attacking, ModelCountry defending) {

		this.attacking = attacking;
		this.defending = defending;
		this.attackerSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE);
		this.defenderSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);
	}

}
