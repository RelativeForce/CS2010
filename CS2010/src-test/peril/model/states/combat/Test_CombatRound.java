package peril.model.states.combat;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import peril.ai.AI;
import peril.helpers.UnitHelper;
import peril.model.ModelColor;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLink;
import peril.model.board.links.ModelLinkState;
import peril.model.combat.CombatHelper;
import peril.model.combat.CombatRound;
import peril.model.combat.ModelSquad;

/**
 * Tests that {@link CombatRound} is constructed properly and only with valid
 * parameters.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.03
 * @since 2018-03-12
 * 
 * @see CombatRound
 *
 */
public final class Test_CombatRound {

	/**
	 * The {@link List} of {@link ModelUnit}s that will make up a standard
	 * {@link ModelArmy}.
	 */
	private List<ModelUnit> testArmyUnits;

	/**
	 * Sets up the {@link #testArmyUnits} that will be used by every test.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		final ModelUnit testUnit1 = new ModelUnit("testUnit1", 1, "na");

		UnitHelper.getInstance().clear();
		UnitHelper.getInstance().addUnit(testUnit1);

		// The basic units for a standard army.
		this.testArmyUnits = new LinkedList<>();
		this.testArmyUnits.add(testUnit1);
		this.testArmyUnits.add(testUnit1);
		this.testArmyUnits.add(testUnit1);
		this.testArmyUnits.add(testUnit1);
		this.testArmyUnits.add(testUnit1);

	}

	/**
	 * Tests that the
	 * {@link CombatRound#CombatRound(ModelCountry, ModelCountry, ModelSquad, ModelSquad)}
	 * performs properly when given valid parameters.
	 */
	@Test
	public void test_normal() {

		// Define the players that will rule both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer player2 = new ModelPlayer(2, AI.USER);

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Set the countries as linked.
		country1.addNeighbour(country2, new ModelLink(ModelLinkState.OPEN));
		country2.addNeighbour(country1, new ModelLink(ModelLinkState.OPEN));

		// Assign the rulers.
		country1.setRuler(player1);
		country2.setRuler(player2);

		// Set the players as owning one country
		player1.setCountriesRuled(1);
		player2.setCountriesRuled(1);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Add the units to the players total armies.
		player1.totalArmy.add(testArmyUnits);
		player2.totalArmy.add(testArmyUnits);

		// Define the squads
		final ModelSquad attackSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE);
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);

		// Auto populate the squads
		attackSquad.autoPopulate(country1.getArmy(), 1);
		defendSquad.autoPopulate(country2.getArmy(), 0);

		// Constructs the combat round with no exceptions.
		new CombatRound(country1, country2, attackSquad, defendSquad);

	}

	/**
	 * Tests that
	 * {@link CombatRound#CombatRound(ModelCountry, ModelCountry, ModelSquad, ModelSquad)}
	 * throws an {@link IllegalArgumentException} if empty {@link ModelSquad}s are
	 * used for the combat.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_emptySquads() {

		// Define the players that will rule both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer player2 = new ModelPlayer(2, AI.USER);

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Set the countries as linked.
		country1.addNeighbour(country2, new ModelLink(ModelLinkState.OPEN));
		country2.addNeighbour(country1, new ModelLink(ModelLinkState.OPEN));

		// Assign the rulers.
		country1.setRuler(player1);
		country2.setRuler(player2);

		// Set the players as owning one country
		player1.setCountriesRuled(1);
		player2.setCountriesRuled(1);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Add the units to the players total armies.
		player1.totalArmy.add(testArmyUnits);
		player2.totalArmy.add(testArmyUnits);

		// Define the squads
		final ModelSquad attackSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE);
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);

		// This should throw an illegal argument exception as the countries are ruled by
		// the same player.
		new CombatRound(country1, country2, attackSquad, defendSquad);

	}

	/**
	 * Tests that
	 * {@link CombatRound#CombatRound(ModelCountry, ModelCountry, ModelSquad, ModelSquad)}
	 * throws an {@link IllegalArgumentException} when the proposed
	 * {@link CombatRound} has the attacking squad and defending squad are the same
	 * squad.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_sameSquad() {

		// Define the players that will rule both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer player2 = new ModelPlayer(2, AI.USER);

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Set the countries as linked.
		country1.addNeighbour(country2, new ModelLink(ModelLinkState.OPEN));
		country2.addNeighbour(country1, new ModelLink(ModelLinkState.OPEN));

		// Assign the rulers.
		country1.setRuler(player1);
		country2.setRuler(player2);

		// Set the players as owning one country
		player1.setCountriesRuled(1);
		player2.setCountriesRuled(1);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Add the units to the players total armies.
		player1.totalArmy.add(testArmyUnits);
		player2.totalArmy.add(testArmyUnits);

		// Define the squads
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);

		// Auto populate the squads
		defendSquad.autoPopulate(country2.getArmy(), 1);

		// This should throw an illegal argument exception as the countries are ruled by
		// the same player.
		new CombatRound(country1, country2, defendSquad, defendSquad);

	}

	/**
	 * Tests that
	 * {@link CombatRound#CombatRound(ModelCountry, ModelCountry, ModelSquad, ModelSquad)}
	 * throws an {@link IllegalArgumentException} if the countries fighting are
	 * ruled by the same player.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_friendlyCountrys() {

		// The player that rules both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Set the countries as linked.
		country1.addNeighbour(country2, new ModelLink(ModelLinkState.OPEN));
		country2.addNeighbour(country1, new ModelLink(ModelLinkState.OPEN));

		// Assign the rulers
		country1.setRuler(player1);
		country2.setRuler(player1);

		// Set the player as owning two country
		player1.setCountriesRuled(2);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Add the units to the players total armies.
		player1.totalArmy.add(testArmyUnits);

		// Define the squads
		final ModelSquad attackSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE);
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);

		// Auto populate the squads
		attackSquad.autoPopulate(country1.getArmy(), 1);
		defendSquad.autoPopulate(country2.getArmy(), 0);

		// This should throw an illegal argument exception as the squads are empty
		new CombatRound(country1, country2, attackSquad, defendSquad);

	}

	/**
	 * Tests that
	 * {@link CombatRound#CombatRound(ModelCountry, ModelCountry, ModelSquad, ModelSquad)}
	 * throws an {@link IllegalArgumentException} if the countries fighting are the
	 * same country.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_sameCountry() {

		// The player that rules both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);

		// Define the single country.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));

		// Assign the rulers
		country1.setRuler(player1);

		// Set the player as owning one country
		player1.setCountriesRuled(1);

		// Build the armies
		country1.getArmy().add(testArmyUnits);

		// Add the units to the players total armies.
		player1.totalArmy.add(testArmyUnits);

		// Define the squads
		final ModelSquad attackSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE);
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);

		// Auto populate the squads
		attackSquad.autoPopulate(country1.getArmy(), 1);
		defendSquad.autoPopulate(country1.getArmy(), 1);

		// This should throw an illegal argument exception as the countries are the
		// same.
		new CombatRound(country1, country1, attackSquad, defendSquad);

	}

	/**
	 * Tests that
	 * {@link CombatRound#CombatRound(ModelCountry, ModelCountry, ModelSquad, ModelSquad)}
	 * throws an {@link IllegalArgumentException} if the attacking squad is to
	 * large.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_attackSquad_toBig() {

		// Define the players that will rule both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer player2 = new ModelPlayer(2, AI.USER);

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Set the countries as linked.
		country1.addNeighbour(country2, new ModelLink(ModelLinkState.OPEN));
		country2.addNeighbour(country1, new ModelLink(ModelLinkState.OPEN));

		// Assign the rulers.
		country1.setRuler(player1);
		country2.setRuler(player2);

		// Set the players as owning one country
		player1.setCountriesRuled(1);
		player2.setCountriesRuled(1);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Add the units to the players total armies.
		player1.totalArmy.add(testArmyUnits);
		player2.totalArmy.add(testArmyUnits);

		// Define the squads
		final ModelSquad attackSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE + 1);
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);

		// Auto populate the squads
		attackSquad.autoPopulate(country1.getArmy(), 1);
		defendSquad.autoPopulate(country2.getArmy(), 0);

		// This should throw an illegal argument exception as the attackSquad is 1 unit
		// to big.
		new CombatRound(country1, country2, attackSquad, defendSquad);

	}

	/**
	 * Tests that
	 * {@link CombatRound#CombatRound(ModelCountry, ModelCountry, ModelSquad, ModelSquad)}
	 * throws an {@link IllegalArgumentException} if the defend squad is to large.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_defendSquad_toBig() {

		// Define the players that will rule both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer player2 = new ModelPlayer(2, AI.USER);

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Set the countries as linked.
		country1.addNeighbour(country2, new ModelLink(ModelLinkState.OPEN));
		country2.addNeighbour(country1, new ModelLink(ModelLinkState.OPEN));

		// Assign the rulers.
		country1.setRuler(player1);
		country2.setRuler(player2);

		// Set the players as owning one country
		player1.setCountriesRuled(1);
		player2.setCountriesRuled(1);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Add the units to the players total armies.
		player1.totalArmy.add(testArmyUnits);
		player2.totalArmy.add(testArmyUnits);

		// Define the squads
		final ModelSquad attackSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE);
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE + 1);

		// Auto populate the squads
		attackSquad.autoPopulate(country1.getArmy(), 1);
		defendSquad.autoPopulate(country2.getArmy(), 0);

		// This should throw an illegal argument exception as the attackSquad is 1 unit
		// to big.
		new CombatRound(country1, country2, attackSquad, defendSquad);

	}

}
