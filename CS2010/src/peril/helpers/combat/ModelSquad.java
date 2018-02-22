package peril.helpers.combat;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Stream;

import peril.Update;
import peril.model.board.ModelArmy;
import peril.model.board.ModelUnit;

public final class ModelSquad extends Observable {

	private final List<ModelSquadMember> members;

	private final int maxSize;

	public ModelSquad(int maxSize) {

		this.maxSize = maxSize;
		this.members = new LinkedList<>();
	}

	public int getAliveUnits() {
		return (int) members.stream().filter(member -> member.isAlive).count();
	}

	public int size() {
		return members.size();
	}

	public int geStrength() {
		return members.stream().filter(member -> member.isAlive).mapToInt(member -> member.unit.strength).sum();
	}

	public boolean killUnit(ModelUnit unit) {

		boolean removedFromSquad = false;

		// If the enemy units damage can be removed from the squad remove it from the
		// squad
		for (ModelSquadMember member : members) {
			if (!removedFromSquad && member.unit.strength == unit.strength && member.isAlive) {
				removedFromSquad = true;
				member.isAlive = false;
				
				setChanged();
				notifyObservers(new Update("members", members));
			}
		}

		return removedFromSquad;

	}

	public void returnUnitToArmy(ModelArmy army, ModelSquadMember member) {

		members.remove(member);

		if (member.isAlive) {
			army.add(member.unit);
		}
		
		setChanged();
		notifyObservers(new Update("members", members));
	}

	public void moveToSquad(ModelUnit unit, ModelArmy army) {

		removeDeadUnits();

		if (members.size() < maxSize) {
			army.remove(unit);

			final ModelSquadMember member = new ModelSquadMember(unit, true);
			members.add(member);
			
			setChanged();
			notifyObservers(new Update("members", members));
		}

	}

	public Stream<ModelSquadMember> stream() {
		return members.stream();
	}
	
	public void clear() {
		if (!members.isEmpty()) {
			members.clear();
		}
		
		setChanged();
		notifyObservers(new Update("members", members));
	}

	public void removeDeadUnits() {
		members.removeIf(member -> !member.isAlive);
		
		setChanged();
		notifyObservers(new Update("members", members));
	}

	public void returnSquadToArmy(ModelArmy army) {
		members.stream().filter(member -> member.isAlive).forEach(member -> army.add(member.unit));
		clear();
	}

}
