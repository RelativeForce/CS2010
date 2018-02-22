package peril.helpers.combat;

import java.util.Observable;

import peril.model.board.ModelUnit;

public final class ModelSquadMember extends Observable {

	public final ModelUnit unit;

	public boolean isAlive;

	public ModelSquadMember(ModelUnit unit, boolean isAlive) {
		this.unit = unit;
		this.isAlive = isAlive;
	}

}
