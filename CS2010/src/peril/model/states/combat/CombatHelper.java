package peril.model.states.combat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.stream.Collectors;

import peril.controllers.GameController;
import peril.helpers.PointHelper;
import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;

/**
 * Helps the game by performing a round of combat denoted by a
 * {@link CombatRound} passed to {@link #fight(CombatRound)}. The results of
 * this round of combat can are {@link #view}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-09
 * @version 1.01.06
 *
 * @see Observable
 * @see CombatRound
 * @see CombatView
 * @see ModelSquad
 * @see ModelSquadMember
 */
public final class CombatHelper extends Observable {

	/**
	 * The maximum size of an attacking squad.
	 */
	public final static int MAX_ATTACK_SQUAD_SIZE = 3;

	/**
	 * The maximum size of an defending squad.
	 */
	public final static int MAX_DEFEND_SQUAD_SIZE = 2;

	/**
	 * The {@link CombatView} that denotes the results of the previous round of
	 * combat.
	 */
	public CombatView view;

	/**
	 * The {@link Random} that generated the dice rolls.
	 */
	private final Random random;

	/**
	 * The {@link GameController} that allows this {@link CombatHelper} to query the
	 * state of the game.
	 */
	private final GameController game;

	/**
	 * Constructs an new {@link CombatHelper}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows this {@link CombatHelper}
	 *            to query the state of the game.
	 */
	public CombatHelper(GameController game) {
		this.random = new Random();
		this.game = game;
	}

	/**
	 * Performs a round of combat between the {@link ModelCountry}s and
	 * {@link ModelSquad}s defined by the parameter {@link CombatRound}. The results
	 * of this {@link CombatRound} will be stored in {@link #view} until the
	 * {@link CombatHelper} is {@link #clear()} or the next
	 * {@link #fight(CombatRound)}.
	 * 
	 * @param round
	 *            The {@link CombatRound} specifying the details of this fight.
	 */
	public void fight(CombatRound round) {

		// Check the countries
		if (round.attacker.getRuler().equals(round.defender.getRuler())) {
			throw new IllegalArgumentException("The two countries can be ruler by the same player.");
		}

		// Check the attacking squad.
		final int attackSquadSize = round.attackerSquad.getAliveUnits();
		if (attackSquadSize > MAX_ATTACK_SQUAD_SIZE || attackSquadSize == 0) {
			throw new IllegalArgumentException(attackSquadSize + " is not a valid attacking squad size.");
		}

		// Check the defending squad.
		final int defendSquadSize = round.defenderSquad.getAliveUnits();
		if (defendSquadSize > MAX_DEFEND_SQUAD_SIZE || defendSquadSize == 0) {
			throw new IllegalArgumentException(defendSquadSize + " is not a valid defending squad size.");
		}

		// Get the dice rolls for the attackers and defenders.
		final Integer[] attackerDiceRolls = getDiceRolls(attackSquadSize);
		final Integer[] defenderDiceRolls = getDiceRolls(defendSquadSize);

		final ModelPlayer attackingPlayer = round.attacker.getRuler();
		final ModelPlayer defendingPlayer = round.defender.getRuler();

		// Compare the dice that were rolled.
		compareDiceRolls(round, attackerDiceRolls, defenderDiceRolls);

		// Process the transition of country ownership.
		processPostFight(round, attackingPlayer, defendingPlayer);

		// Update the view
		this.view = new CombatView(round, attackerDiceRolls, defenderDiceRolls);

		setChanged();
		notifyObservers(this.view);

	}

	/**
	 * Clears this {@link CombatHelper}.
	 */
	public void clear() {
		this.view = null;
	}

	/**
	 * Retrieves the total number of alive {@link ModelUnit}s when the specified
	 * {@link ModelArmy} and the {@link ModelSquad}.
	 * 
	 * @param army
	 *            The {@link ModelArmy} to be combined.
	 * @param squad
	 *            The {@link ModelSquad} to be combined.
	 * @return The total number of alive {@link ModelUnit}s.
	 */
	public int getTotalAliveUnits(ModelArmy army, ModelSquad squad) {
		return army.getNumberOfUnits() + squad.getAliveUnits();
	}

	/**
	 * Processes the state of the specified {@link CombatRound} after the
	 * {@link #compareDiceRolls(CombatRound, Integer[], Integer[])} has been
	 * performed.
	 * 
	 * @param round
	 *            The {@link CombatRound} to be processed.
	 * @param attackingPlayer
	 *            The {@link ModelPlayer} that started the attack.
	 * @param defendingPlayer
	 *            The {@link ModelPlayer} that defended against the attack.
	 */
	private void processPostFight(CombatRound round, ModelPlayer attackingPlayer, ModelPlayer defendingPlayer) {

		// If the country has been conquered
		if (round.attacker.getRuler().equals(round.defender.getRuler())) {

			// If there is a defending player
			if (defendingPlayer != null) {

				defendingPlayer.setCountriesRuled(defendingPlayer.getCountriesRuled() - 1);

				// If the player has no countries they have lost.
				if (defendingPlayer.getCountriesRuled() == 0) {

					game.setLoser(defendingPlayer);
					game.checkWinner();
				}
			}

			// Increment the statistics appropriately
			attackingPlayer.setCountriesRuled(attackingPlayer.getCountriesRuled() + 1);
			attackingPlayer.setCountriesTaken(attackingPlayer.getCountriesTaken() + 1);
			attackingPlayer.addPoints(PointHelper.CONQUER_REWARD);

			game.checkContinentRulership();
			game.checkChallenges();

		}
	}

	/**
	 * Compares the specified dice rolls and performs the combat on the elements of
	 * the specified {@link CombatRound}.
	 * 
	 * @param round
	 *            The {@link CombatRound} specifying the details of this fight.
	 * @param attackerDiceRolls
	 *            The dice rolls of the attacker.
	 * @param defenderDiceRolls
	 *            The dice rolls of the defender.
	 */
	private void compareDiceRolls(CombatRound round, Integer[] attackerDiceRolls, Integer[] defenderDiceRolls) {

		// Get the size of the smaller set of dice.
		final int diceToCheck = attackerDiceRolls.length >= defenderDiceRolls.length ? defenderDiceRolls.length
				: attackerDiceRolls.length;

		// Copy the squads to these holding lists so the squads can be modified.
		final List<ModelSquadMember> attackingSqd = round.attackerSquad.stream().collect(Collectors.toList());
		final List<ModelSquadMember> defendingSqd = round.defenderSquad.stream().collect(Collectors.toList());

		// The index of the current units that will fight.
		int index = 0;

		// Holds whether the combat is in a valid state to continue. For example combat
		// cannot continue if the defender has no units.
		boolean warEnded = false;

		// Compare each attacking dice roll against the defending dice roll unless
		// finished is flagged.
		while (index < diceToCheck && !warEnded) {

			// The units that will fight
			final ModelUnit attackingUnit = attackingSqd.get(index).unit;
			final ModelUnit defendingUnit = defendingSqd.get(index).unit;

			// Attacker won
			if (attackerDiceRolls[index] > defenderDiceRolls[index]) {

				final ModelPlayer victor = round.attacker.getRuler();
				final ModelPlayer loser = round.defender.getRuler();
				final ModelArmy army = round.defender.getArmy();
				final ModelSquad squad = round.defenderSquad;

				warEnded = processDamage(victor, loser, army, squad, attackingUnit);

				// If the war ended then assign the attacker as the ruler of the defending country.
				if (warEnded) {
					victor.totalArmy.add(UnitHelper.getInstance().getWeakest());
					round.defender.setRuler(victor);
				}
			}
			// Attacker lost
			else {

				final ModelPlayer victor = round.defender.getRuler();
				final ModelPlayer loser = round.attacker.getRuler();
				final ModelArmy army = round.attacker.getArmy();
				final ModelSquad squad = round.attackerSquad;

				warEnded = processDamage(victor, loser, army, squad, defendingUnit);

				// If the war ended add one unit to the losers army.
				if (warEnded) {
					loser.totalArmy.add(UnitHelper.getInstance().getWeakest());
				}
			}

			index++;
		}
	}

	/**
	 * Processes a fight with the specified victor and loser. The strength of the
	 * specified {@link ModelUnit} is dealt as damage to the specified
	 * {@link ModelArmy} and {@link ModelSquad}.
	 * 
	 * @param victor
	 *            The {@link ModelPlayer} that rules the {@link ModelCountry} that
	 *            has won this round of the war.
	 * @param loser
	 *            The {@link ModelPlayer} that rules the {@link ModelCountry} that
	 *            has lost this round of the war.
	 * @param army
	 *            The army from the {@link ModelCountry} that has lost this round of
	 *            the war.
	 * @param squad
	 *            The squad that was defending the {@link ModelCountry} that has
	 *            lost this round of the war.
	 * @param unit
	 *            The unit that will damage the attacker {@link ModelArmy} and
	 *            {@link ModelSquad}.
	 * @return Whether this {@link ModelUnit}'s damage has caused the war to end.
	 */
	private boolean processDamage(ModelPlayer victor, ModelPlayer loser, ModelArmy army, ModelSquad squad,
			ModelUnit unit) {

		// The number of units in the squad and army before the units damage was dealt.
		final int preCombatUnitCount = army.getNumberOfUnits() + squad.getAliveUnits();

		// Remove the unit from the army or squad.
		removeUnitFromArmy(squad, army, unit);

		// The number of units in the squad and army after the units damage was dealt.
		final int postCombatUnitCount = army.getNumberOfUnits() + squad.getAliveUnits();

		// Remove the units from the losers total army.
		if (loser != null) {
			loser.totalArmy.remove(unit.strength);
		}

		// Add the change in units to the victors units killed.
		victor.addUnitsKilled(preCombatUnitCount - postCombatUnitCount);

		// If the army and squad weakened to the point they can no longer attack.
		if (postCombatUnitCount == 0) {

			// Reset the army to its weakest.
			army.setWeakest();

			// The war has ended.
			return true;
		}

		// The war can continue.
		return false;
	}

	/**
	 * First attempts to remove the specified enemy {@link ModelUnit} from the
	 * specified {@link ModelSquad} and if that is not possible if returns the
	 * {@link ModelSquadMember}s back to the specified {@link ModelArmy} then remove
	 * the enemy {@link ModelUnit} from the {@link ModelArmy}.
	 * 
	 * @param squad
	 *            The {@link ModelSquad} that will be returned to the specified
	 *            {@link ModelArmy}.
	 * @param army
	 *            The {@link ModelArmy} that the {@link ModelSquad} will be returned
	 *            to if the {@link ModelUnit} is not removed from it.
	 * @param enemyUnit
	 *            The {@link ModelUnit} that will be removed from the
	 *            {@link ModelSquad} or {@link ModelArmy}.
	 */
	private void removeUnitFromArmy(ModelSquad squad, ModelArmy army, ModelUnit enemyUnit) {

		final boolean removedFromSquad = squad.killUnit(enemyUnit);

		// If the enemy unti's damage was not taken from the squad return the squad to
		// the army and then remove the damage from the army.
		if (!removedFromSquad) {
			squad.returnSquadToArmy(army);
			army.remove(enemyUnit);
		}
	}

	/**
	 * Retrieves a {@link Integer}[] of random six sided dice rolls.
	 * 
	 * @param numberOfRolls
	 *            The {@link Integer}[] number of rolls
	 * @return The {@link Integer}[] dice rolls.
	 */
	private Integer[] getDiceRolls(int numberOfRolls) {

		final int maxDice = 6;

		// Holds the dice roles.
		final Integer[] rolls = new Integer[numberOfRolls];

		// Initialise dice rolls for the attacking army
		for (int rollIndex = 0; rollIndex < numberOfRolls; rollIndex++) {
			rolls[rollIndex] = random.nextInt(maxDice) + 1;
		}

		// Sort the dice roles into descending order.
		Arrays.sort(rolls, Collections.reverseOrder());

		return rolls;
	}

}
