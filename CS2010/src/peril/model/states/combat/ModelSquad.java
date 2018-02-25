package peril.model.states.combat;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Stream;

import peril.Update;
import peril.model.board.ModelArmy;
import peril.model.board.ModelUnit;

/**
 * A squad of {@link ModelSquadMember}s that originated from a {@link ModelArmy}
 * and will be used in combat by the {@link CombatHelper}. This cannot exceed
 * its maximum size.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-25
 * @version 1.01.05
 * 
 * @see ModelSquadMember
 * @see ModelSquadMemberState
 * @see CombatHelper
 * @see Observable
 * @see Iterable
 *
 */
public final class ModelSquad extends Observable implements Iterable<ModelSquadMember> {

	/**
	 * The {@link List} of {@link ModelSquadMember}s that make up this
	 * {@link ModelSquad}.
	 */
	private final List<ModelSquadMember> members;

	/**
	 * The maximum size of this {@link ModelSquad}.
	 */
	private final int maxSize;

	/**
	 * Constructs an new empty {@link ModelSquad}.
	 * 
	 * @param maxSize
	 *            The maximum size of this {@link ModelSquad}.
	 */
	public ModelSquad(int maxSize) {
		this.maxSize = maxSize;
		this.members = new LinkedList<>();
	}

	/**
	 * Retrieves the number of {@link ModelSquadMember}s that are alive in this
	 * {@link ModelSquad}.
	 * 
	 * @return The number of alive {@link ModelSquadMember}s.
	 */
	public int getAliveUnits() {
		return (int) members.stream().filter(member -> member.state == ModelSquadMemberState.ALIVE).count();
	}

	/**
	 * The number of {@link ModelSquadMember}s in this {@link ModelSquad} that are
	 * dead or alive.
	 * 
	 * @return The number of {@link ModelSquadMember}s in this {@link ModelSquad}
	 */
	public int size() {
		return members.size();
	}

	/**
	 * Constructs the strongest {@link ModelSquad} from the specified
	 * {@link ModelArmy} leaving the the {@link ModelArmy} with at least the minimum
	 * number of {@link ModelUnit}s specified.
	 * 
	 * @param army
	 *            The source {@link ModelArmy}.
	 * @param minArmySize
	 *            The minimum number of model units to be left in the
	 *            {@link ModelArmy}.
	 */
	public void autoPopulate(ModelArmy army, int minArmySize) {

		returnSquadToArmy(army);
		removeNonActiveUnits();

		ModelUnit unit = army.getStrongestUnit();

		// Iterate over the army until the squad is at the max size of the army is
		// depleted.
		while (size() < maxSize && army.getNumberOfUnits() > minArmySize) {

			if (army.hasUnit(unit)) {
				this.moveToSquad(unit, army);
			} else {
				unit = army.getStrongestUnit();
			}
		}

	}

	/**
	 * Retrieves the combined strength of all the alive {@link ModelSquadMember}s in
	 * this {@link ModelSquad}.
	 * 
	 * @return The combined strength of all the alive {@link ModelSquadMember}s in
	 *         this {@link ModelSquad}
	 */
	public int geStrength() {
		return members.stream().filter(member -> member.state == ModelSquadMemberState.ALIVE)
				.mapToInt(member -> member.unit.strength).sum();
	}

	/**
	 * Attempts to find a {@link ModelSquadMember} with the specified
	 * {@link ModelUnit} in this {@link ModelSquad} and kill it. If no such
	 * {@link ModelUnit} exists in this {@link ModelSquad} this will return false;
	 * 
	 * @param unit
	 *            The {@link ModelUnit} to kill.
	 * @return Whether the specified {@link ModelUnit} was killed our not.
	 */
	public boolean killUnit(ModelUnit unit) {

		// Whether the unit has been killed.
		boolean killed = false;

		// If the enemy units damage can be removed from the squad remove it from the
		// squad
		for (ModelSquadMember member : members) {

			if (!killed && member.unit.strength == unit.strength && member.state == ModelSquadMemberState.ALIVE) {
				killed = true;
				member.state = ModelSquadMemberState.DEAD;

				setChanged();
				notifyObservers(new Update("members", members));
			}
		}

		return killed;

	}

	/**
	 * Returns a specified {@link ModelSquadMember} from this {@link ModelSquad} to
	 * the the specified {@link ModelArmy}.
	 * 
	 * @param army
	 *            The {@link ModelArmy} the {@link ModelUnit} will be returned to.
	 * @param member
	 *            The {@link ModelSquadMember} that will be returned to the
	 *            {@link ModelArmy}.
	 */
	public void returnUnitToArmy(ModelArmy army, ModelSquadMember member) {

		// If the member was alive then add it to the army.
		if (member.state == ModelSquadMemberState.ALIVE) {

			member.state = ModelSquadMemberState.RETURNED;
			army.add(member.unit);
		}
		// If the member is dead or returned then remove it.
		else {
			members.remove(member);
		}

		setChanged();
		notifyObservers(new Update("members", members));
	}

	/**
	 * Moves a specified {@link ModelUnit} from the specified {@link ModelArmy} to
	 * this {@link ModelSquad}.
	 * 
	 * @param unit
	 *            The {@link ModelUnit} to be moved.
	 * @param army
	 *            The source {@link ModelArmy} of the {@link ModelUnit}.
	 */
	public void moveToSquad(ModelUnit unit, ModelArmy army) {

		// Remove the dead units to free up any space.
		removeNonActiveUnits();

		// If there is enough space in the squad attempt to add the unit.
		if (members.size() < maxSize) {
			army.remove(unit);

			final ModelSquadMember member = new ModelSquadMember(unit, ModelSquadMemberState.ALIVE);
			members.add(member);

			setChanged();
			notifyObservers(new Update("members", members));
		}

	}

	/**
	 * Retrieves the {@link Stream} of {@link ModelSquadMember}s from this
	 * {@link ModelSquad}.
	 * 
	 * @return The {@link Stream} of {@link ModelSquadMember}s.
	 */
	public Stream<ModelSquadMember> stream() {
		return members.stream();
	}

	/**
	 * Removes all the {@link ModelSquadMember}s dead or alive from this
	 * {@link ModelSquad} without returning them to their source army.
	 */
	public void clear() {
		if (!members.isEmpty()) {
			members.clear();
		}

		setChanged();
		notifyObservers(new Update("members", members));
	}

	/**
	 * Removes all the {@link ModelSquadMember}s that are not
	 * {@link ModelSquadMemberState#ALIVE} from this {@link ModelSquad}.
	 */
	public void removeNonActiveUnits() {
		members.removeIf(member -> member.state != ModelSquadMemberState.ALIVE);

		setChanged();
		notifyObservers(new Update("members", members));
	}

	/**
	 * Returns all the {@link ModelSquadMember}s from this {@link ModelSquad} to the
	 * Specified {@link ModelArmy}.
	 * 
	 * @param army
	 *            The {@link ModelArmy} the {@link ModelSquadMember}s will be
	 *            returned to.
	 */
	public void returnSquadToArmy(ModelArmy army) {
		members.stream().filter(member -> member.state == ModelSquadMemberState.ALIVE).forEach(member -> {
			army.add(member.unit);
			member.state = ModelSquadMemberState.RETURNED;
		});
	}

	/**
	 * Retrieves the {@link Iterator} that iterates over each
	 * {@link ModelSquadMember} of this {@link ModelSquad}.
	 */
	@Override
	public Iterator<ModelSquadMember> iterator() {
		return members.iterator();
	}

}
