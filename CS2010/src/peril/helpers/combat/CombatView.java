package peril.helpers.combat;

public class CombatView {

	public final Integer[] attackerDiceRolls;

	public final Integer[] defenderDiceRolls;

	public final CombatRound round;

	public CombatView(CombatRound round, Integer[] attackerDiceRolls, Integer[] defenderDiceRolls) {
		this.round = round;
		this.attackerDiceRolls = attackerDiceRolls;
		this.defenderDiceRolls = defenderDiceRolls;
	}

}
