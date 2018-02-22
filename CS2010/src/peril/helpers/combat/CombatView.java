package peril.helpers.combat;

/**
 * 
 * Denotes the results of a {@link CombatRound} being processed by the
 * {@link CombatHelper}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 1.01.01
 * @version 1.01.01
 * 
 * @see CombatHelper
 * @see CombatRound
 *
 */
public class CombatView {

	/**
	 * The dice rolls that the attacker had.
	 */
	public final Integer[] attackerDiceRolls;

	/**
	 * The dice rolls that the defender had.
	 */
	public final Integer[] defenderDiceRolls;

	/**
	 * The {@link CombatRound} that was processed.
	 */
	public final CombatRound round;

	/**
	 * Constructs an new {@link CombatView}.
	 * 
	 * @param round
	 *            The {@link CombatRound} that was processed.
	 * @param attackerDiceRolls
	 *            The dice rolls that the attacker had.
	 * @param defenderDiceRolls
	 *            The dice rolls that the defender had.
	 */
	public CombatView(CombatRound round, Integer[] attackerDiceRolls, Integer[] defenderDiceRolls) {
		this.round = round;
		this.attackerDiceRolls = attackerDiceRolls;
		this.defenderDiceRolls = defenderDiceRolls;
	}

}
