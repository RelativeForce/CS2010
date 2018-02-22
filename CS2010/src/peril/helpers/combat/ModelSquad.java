package peril.helpers.combat;

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
 * @since 2018-02-22
 * @version 1.01.01
 * 
 * @see ModelSquadMember
 * @see CombatHelper
 * @see Observable
 *
 */
public final class ModelSquad extends Observable {

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
		return (int) members.stream().filter(member -> member.isAlive).count();
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
	 * Retrieves the combined strength of all the alive {@link ModelSquadMember}s in
	 * this {@link ModelSquad}.
	 * 
	 * @return The combined strength of all the alive {@link ModelSquadMember}s in
	 *         this {@link ModelSquad}
	 */
	public int geStrength() {
		return members.stream().filter(member -> member.isAlive).mapToInt(member -> member.unit.strength).sum();
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

			if (!killed && member.unit.strength == unit.strength && member.isAlive) {
				killed = true;
				member.isAlive = false;

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

		// Remove the member from the squad.
		members.remove(member);

		// If the member was alive then add it to the army.
		if (member.isAlive) {
			army.add(member.unit);
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
		removeDeadUnits();

		// If there is enough space in the squad attempt to add the unit.
		if (members.size() < maxSize) {
			army.remove(unit);

			final ModelSquadMember member = new ModelSquadMember(unit, true);
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
	 * Removes all the dead {@link ModelSquadMember}s from this {@link ModelSquad}.
	 */
	public void removeDeadUnits() {
		members.removeIf(member -> !member.isAlive);

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
		members.stream().filter(member -> member.isAlive).forEach(member -> army.add(member.unit));
		clear();
	}

}
