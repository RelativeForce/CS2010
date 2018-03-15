package peril.model.combat;

import peril.model.board.ModelCountry;

/**
 * Denotes a round of combat that can be processed by the {@link CombatHelper}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-11
 * @version 1.01.04
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
	 * Constructs a new {@link CombatRound}.<br>
	 * <br>
	 * 
	 * 
	 * 
	 * @param attacker
	 *            The {@link ModelCountry} that will attack the {@link #defender}
	 *            {@link ModelCountry}.
	 * @param defender
	 *            The {@link ModelCountry} that will defend against the
	 *            {@link #attacker} {@link ModelCountry}.
	 * @param attackerSquad
	 *            The {@link ModelSquad} that will attack the
	 *            {@link #defenderSquad}.
	 * @param defenderSquad
	 *            The {@link ModelSquad} that will defend against the
	 *            {@link #defenderSquad}.
	 */
	public CombatRound(ModelCountry attacker, ModelCountry defender, ModelSquad attackerSquad,
			ModelSquad defenderSquad) {

		this.attacker = attacker;
		this.defender = defender;
		this.attackerSquad = attackerSquad;
		this.defenderSquad = defenderSquad;

		checkState();
	}

	/**
	 * Checks that the following rules still remain true for the state of this
	 * {@link CombatRound}. If there is anything about this {@link CombatRound} that
	 * is not valid a appropriate exception will be thrown.
	 * 
	 * Key Aspects:
	 * <ul>
	 * <li>The attacker and defender must be ruled by different players.</li>
	 * <li>The attacker and defender must be neighbours with a valid link between
	 * them that allows an attack.</li>
	 * <li>The attacker and defender must be different countries.</li>
	 * <li>The attacker and defender squads must be different squads.</li>
	 * <li>The attacker and defender squads must have a valid number of alive units
	 * in them.</li>
	 * <li>The attacker and defender squads must not be empty.</li>
	 * </ul>
	 */
	public void checkState() {

		// Check the countries aren't ruled by the same player.
		if (attacker.getRuler().equals(defender.getRuler())) {
			throw new IllegalArgumentException("The two countries can not be ruler by the same player.");
		}

		// Check the countries aren't the same country
		if (attacker.equals(defender)) {
			throw new IllegalArgumentException("The two countries can not be the same country.");
		}

		// Check the squads aren't the same.
		if (attackerSquad.equals(defenderSquad)) {
			throw new IllegalArgumentException("The two squads can not be the same squad.");
		}

		// Check the attacking squad.
		final int attackSquadSize = attackerSquad.getAliveUnits();
		if (attackSquadSize > CombatHelper.MAX_ATTACK_SQUAD_SIZE || attackSquadSize == 0) {
			throw new IllegalArgumentException(attackSquadSize + " is not a valid attacking squad size.");
		}

		// Check the defending squad.
		final int defendSquadSize = defenderSquad.getAliveUnits();
		if (defendSquadSize > CombatHelper.MAX_DEFEND_SQUAD_SIZE || defendSquadSize == 0) {
			throw new IllegalArgumentException(defendSquadSize + " is not a valid defending squad size.");
		}

	}
}
