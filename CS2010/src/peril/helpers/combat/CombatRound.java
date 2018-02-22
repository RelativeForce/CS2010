package peril.helpers.combat;

import peril.model.board.ModelCountry;

public class CombatRound {

	public final ModelCountry attacking;
	public final ModelCountry defending;
	public final ModelSquad attackerSquad;
	public final ModelSquad defenderSquad;

	public CombatRound(ModelCountry attacking, ModelCountry defending) {

		this.attacking = attacking;
		this.defending = defending;
		this.attackerSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE);
		this.defenderSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);
	}

}
