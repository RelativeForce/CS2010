package peril.views.slick.components.menus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Random;
import java.util.function.Consumer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import peril.concurrent.Action;
import peril.controllers.GameController;
import peril.helpers.PointHelper;
import peril.helpers.UnitHelper;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.model.ModelPlayer;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.board.SlickUnit;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.views.slick.io.ImageReader;
import peril.views.slick.util.Button;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Encapsulates all the game combat logic.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 *
 */
public class WarMenu extends Menu {

	/**
	 * The name of this {@link Menu}.
	 */
	public final static String NAME = "War Menu";

	private final static int MAX_ATTACK_SQUAD_SIZE = 3;

	private final static int MAX_DEFEND_SQUAD_SIZE = 2;

	/**
	 * The {@link Dice} displaying dice interactions on screen.
	 */
	private final Dice dice;

	/**
	 * {@link Random} object for the random number generator.
	 * 
	 */
	private final Random random;

	/**
	 * The {@link Font} for the text of the heading.
	 */
	private final Font headingFont;

	/**
	 * The {@link Font} for the text to display the {@link ModelArmy}s sizes.
	 */
	private final Font textFont;

	/**
	 * The {@link Font} for the text to display the name of the
	 * {@link ModelCountry}s.
	 */
	private final Font countryFont;

	/**
	 * The {@link Font} for the text to display the results of the attack on a;
	 * neighbouring {@link ModelCountry}.
	 * 
	 */
	private final Font resultFont;

	private final Font armyFont;

	private final Font yourArmyFont;

	/**
	 * The id of the {@link Button} that will be clicked to attack enemy
	 * {@link ModelCountry} .
	 * 
	 */
	private final String attackButton;

	private final Consumer<SlickUnit> poolClick;

	private final CombatHelper combat;

	private final Squad attackingSquad;

	private final Squad defendingSquad;

	/**
	 * The {@link ModelCountry} of the attacking {@link ModelPlayer}.
	 */
	private SlickCountry attacker;

	/**
	 * The {@link ModelCountry} of the defending {@link ModelPlayer}.
	 */
	private SlickCountry defender;

	/**
	 * The {@link ModelPlayer} that is ruling the attacking {@link ModelCountry}.
	 */
	private SlickPlayer attackingRuler;

	/**
	 * The {@link ModelPlayer} that is ruling the defending {@link ModelCountry}.
	 */
	private SlickPlayer enemyRuler;

	/**
	 * Constructs a new {@link WarMenu}.
	 * 
	 */
	public WarMenu(Point position, GameController game) {
		super(NAME, game, new Region(800, 600, position));

		this.random = new Random();
		this.headingFont = new Font("Arial", Color.red, 30);
		this.textFont = new Font("Arial", Color.red, 40);
		this.countryFont = new Font("Arial", Color.black, 37);
		this.resultFont = new Font("Arial", Color.black, 30);
		this.armyFont = new Font("Arial", Color.red, 50);
		this.dice = new Dice();
		this.attackButton = "war";
		this.yourArmyFont = new Font("Arial", Color.black, 25);

		final Point attackTop = new Point(this.getPosition().x + (this.getWidth() / 4) - SlickUnit.WIDTH,
				this.getPosition().y + 250);

		this.attackingSquad = new Squad(attackTop, true, MAX_ATTACK_SQUAD_SIZE);

		final Point defendTop = new Point(this.getPosition().x + ((this.getWidth() * 3) / 4),
				this.getPosition().y + 250);

		this.defendingSquad = new Squad(defendTop, false, MAX_DEFEND_SQUAD_SIZE);

		this.poolClick = new Consumer<SlickUnit>() {

			@Override
			public void accept(SlickUnit unit) {

				if (attacker.model.getArmy().getNumberOfUnits() > 1) {
					attackingSquad.moveToSquad(unit.model, attacker.model.getArmy());
					getButton(attackButton).show();
				}
			}
		};

		this.combat = new CombatHelper();
	}

	/**
	 * Initialises all the Visual Elements of {@link WarMenu}.
	 */
	public void init() {

		headingFont.init();
		textFont.init();
		countryFont.init();
		resultFont.init();
		dice.init();
		armyFont.init();
		yourArmyFont.init();

	}

	/**
	 * Draws the {@link War Menu} on the screen.
	 */
	public void draw(Frame frame) {

		super.draw(frame);

		if (!isVisible()) {
			return;
		}

		final int size = attackingSquad.size() + attacker.model.getArmy().getNumberOfUnits();

		// Attacker has failed to conquer country
		if (size == 1) {
			failedConquer(frame);
		}
		// Attacker has conquered country
		else if (attacker.model.getRuler().equals(defender.model.getRuler())) {
			succesfulConquer(frame);
		}
		// Normal Combat
		else {
			drawNormalCombat(frame);
		}

		slick.showToolTip("Click the units below, to select the number of attacking units");

	}

	private void drawNormalCombat(Frame frame) {

		attackingSquad.draw(frame);

		defendingSquad.draw(frame);

		drawPlayer(enemyRuler, (getWidth() / 4), frame);

		drawPlayer(attackingRuler, -(getWidth() / 4), frame);

		drawTitle(frame);

		drawArmySizes(frame);

		dice.draw(frame);

		drawArmyPool(frame, new Point(getPosition().x + (getWidth() / 2), getPosition().y + getHeight() - 120));
	}

	private void drawArmyPool(Frame frame, Point position) {

		ModelArmy model = attacker.model.getArmy();

		int x = position.x - (((model.getVarietyOfUnits() - 1) * SlickUnit.WIDTH) / 2) - (SlickUnit.WIDTH / 2);
		int y = position.y - (SlickUnit.HEIGHT / 2);

		frame.draw(yourArmyFont, "Your Army:", position.x - (yourArmyFont.getWidth("Your Army:") / 2),
				y - (yourArmyFont.getHeight()));

		// For each unit in the army
		for (ModelUnit current : model) {

			// Holds the visual version of the unit.
			final SlickUnit unit = slick.modelView.getVisual(current);

			final Button unitButton = new Button(new Point(x, y), unit.getImage(), new Action<>(unit, poolClick),
					"poolClick");

			final int numberOfCurrent = model.getNumberOf(unit.model);

			final String number = Integer.toString(numberOfCurrent);

			final int fontX = x + (SlickUnit.WIDTH / 2) - (armyFont.getWidth(number) / 2);
			final int fontY = y + (SlickUnit.HEIGHT / 2) - (armyFont.getHeight() / 2);

			frame.draw(unitButton);
			frame.draw(armyFont, number, fontX, fontY);

			x += SlickUnit.WIDTH + 10;

		}

	}

	/**
	 * Makes this {@link WarMenu} visible along with its attack {@link Button}.
	 */
	@Override
	public void show() {
		super.show();

		getButton(attackButton).hide();

		attacker = slick.modelView.getVisual(game.getAttack().getSelected(0));
		defender = slick.modelView.getVisual(game.getAttack().getSelected(1));

		enemyRuler = slick.modelView.getVisual(defender.model.getRuler());
		attackingRuler = slick.modelView.getVisual(attacker.model.getRuler());

		defendingSquad.autoPopulate(defender.model.getArmy());

	}

	/**
	 * Hides this {@link WarMenu}.
	 */
	@Override
	public void hide() {
		super.hide();

		if (attacker != null) {
			attackingSquad.returnSquadToArmy(attacker.model.getArmy());

		}
		if (defender != null) {
			defendingSquad.returnSquadToArmy(defender.model.getArmy());
		}

		dice.clear();

		getButton(attackButton).hide();

	}

	/**
	 * Processes the attack between 2 highlighted {@link ModelCountry}s and see if
	 * they are eligible to 'fight'.
	 */
	public void attack() {

		// If there is two countries highlighted
		if (attacker != null && defender != null) {

			final ModelPlayer attackingPlayer = attacker.model.getRuler();
			final ModelPlayer defendingPlayer = defender.model.getRuler();

			// If the army of the attacker country is larger that 1 unit in size
			if (getTotalAliveUnits(attacker.model.getArmy(), attackingSquad) > 1) {

				// Remove the dead attacking units.
				attackingSquad.removeDeadUnits();

				defendingSquad.autoPopulate(defender.model.getArmy());

				// Execute the combat
				combat.fight(attacker, defender, attackingSquad.getAliveUnits());

				processPostFight(attackingPlayer, defendingPlayer);
			}
		}

		// Cant attack any more hide the war menu.
		if (game.getAttack().getPrimary() == null || game.getAttack().getSecondary() == null) {
			game.getAttack().deselectAll();
		}

	}

	private int getTotalAliveUnits(ModelArmy army, Squad squad) {
		return army.getNumberOfUnits() + squad.getAliveUnits();
	}

	private void processPostFight(final ModelPlayer attackingPlayer, final ModelPlayer defendingPlayer) {

		// If the country has been conquered
		if (attacker.model.getRuler().equals(defender.model.getRuler())) {

			enemyRuler = attackingRuler;
			attackingPlayer.totalArmy.add(UnitHelper.getInstance().getWeakest());

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

			game.getAttack().deselectAll();
			game.checkContinentRulership();
			game.checkChallenges();
			getButton(attackButton).hide();

		} else if (getTotalAliveUnits(attacker.model.getArmy(), attackingSquad) == 1) {
			game.getAttack().deselectAll();
			getButton(attackButton).hide();
		}
	}

	/**
	 * Moves all the visual components of the {@link WarMenu} along a specified
	 * {@link Point} vector.
	 */
	@Override
	public void moveComponents(Point vector) {
		attackingSquad.move(vector);
		defendingSquad.move(vector);
	}

	/**
	 * Clears the elements of and hides, the {@link WarMenu}.
	 */
	public void clear() {

		this.hide();

		attackingRuler = null;
		defender = null;
		attacker = null;
		enemyRuler = null;

		attackingSquad.clear();
		defendingSquad.clear();

	}

	/**
	 * Selects the highest number of dice possible for the current state of the
	 * {@link WarMenu}.
	 */
	public void selectMaxUnits() {

		ModelArmy army = attacker.model.getArmy();

		Squad squad = attackingSquad;

		squad.autoPopulate(army);
	}

	/**
	 * Displays text when a {@link ModelPlayer} fails to conquer a
	 * {@link ModelCountry} they are attacking.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void failedConquer(Frame frame) {
		String failure = "has insufficient units to attack.";

		frame.draw(countryFont, attacker.model.getName(),
				getPosition().x + (getWidth() / 2) - (countryFont.getWidth(attacker.model.getName()) / 2),
				getPosition().y + (getHeight() / 2) - 45);

		frame.draw(resultFont, failure, getPosition().x + (getWidth() / 2) - (resultFont.getWidth(failure) / 2),
				getPosition().y + (getHeight() / 2));

		getButton(attackButton).hide();

		drawPlayer(attackingRuler, -(getWidth() / 4), frame);
		drawPlayer(enemyRuler, (getWidth() / 4), frame);

		drawTitle(frame);

	}

	/**
	 * Displays text when a {@link ModelPlayer} successfully conquers a
	 * {@link ModelCountry} they are attacking.
	 * 
	 * @param g
	 *            {@link Graphics}.
	 */
	private void succesfulConquer(Frame frame) {

		// Draw the result text.
		final String success = "has conquered";
		final int resultX = getPosition().x + (getWidth() / 2) - (resultFont.getWidth(success) / 2);
		final int resultY = getPosition().y + (getHeight() / 2);
		frame.draw(resultFont, success, resultX, resultY);

		// Draw the attacking country's name
		final String attackerName = attacker.model.getName();
		final int attackerX = getPosition().x + (getWidth() / 2) - (countryFont.getWidth(attackerName) / 2);
		final int attackerY = getPosition().y + (getHeight() / 2) - ((countryFont.getHeight(attackerName) * 3) / 2);
		frame.draw(countryFont, attackerName, attackerX, attackerY);

		// Draw the defending country's name
		final String conqueredName = defender.model.getName();
		final int conqueredX = getPosition().x + (getWidth() / 2) - (countryFont.getWidth(conqueredName) / 2);
		final int conqueredY = getPosition().y + (getHeight() / 2) + ((countryFont.getHeight(attackerName) * 3) / 2);
		frame.draw(countryFont, conqueredName, conqueredX, conqueredY);

		getButton(attackButton).hide();

		// Draw the player icons
		drawPlayer(attackingRuler, -(getWidth() / 4), frame);
		drawPlayer(enemyRuler, (getWidth() / 4), frame);

		// Draw the country titles
		drawTitle(frame);

	}

	/**
	 * Draws the army sizes for each {@link ModelArmy} belonging to the attacking
	 * and defending {@link ModelCountry}s.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawArmySizes(Frame frame) {

		final int yOffset = 190;

		String attackingArmy = Integer.toString(attacker.model.getArmy().getStrength() + attackingSquad.geStrength());

		frame.draw(textFont, attackingArmy, getPosition().x + (getWidth() / 4) - (textFont.getWidth(attackingArmy) / 2),
				getPosition().y + yOffset);

		String enemyArmy = Integer.toString(defender.model.getArmy().getStrength() + defendingSquad.geStrength());

		frame.draw(textFont, enemyArmy, getPosition().x + ((getWidth() * 3) / 4) - (textFont.getWidth(enemyArmy) / 2),
				getPosition().y + yOffset);

	}

	/**
	 * Draws the names of the {@link ModelPlayer}s whose {@link ModelCountry}s are
	 * attacking and defending.
	 * 
	 * @param frame2
	 *            {@link Graphics}
	 */
	private void drawTitle(Frame frame) {

		final int yOffset = 150;

		int y = getPosition().y + yOffset;

		String vs = "VS";
		String attackerStr = attacker.model.getName();
		String enemyStr = defender.model.getName();

		int centreX = getPosition().x + (getWidth() / 2);
		int vsX = centreX - (headingFont.getWidth(vs) / 2);

		int attackerX = centreX - (getWidth() / 4) - (countryFont.getWidth(attackerStr) / 2);
		int enemyX = centreX + (getWidth() / 4) - (countryFont.getWidth(enemyStr) / 2);

		frame.draw(headingFont, vs, vsX, y);
		frame.draw(countryFont, attackerStr, attackerX, y);
		frame.draw(countryFont, enemyStr, enemyX, y);

	}

	/**
	 * Draws the String representation of a {@link ModelPlayer}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 * @param player
	 *            The {@link ModelPlayer} you want to display
	 * @param offset
	 *            The string offset
	 */
	private void drawPlayer(SlickPlayer player, int offset, Frame frame) {

		if (player == null) {
			return;
		}

		int centreX = getPosition().x + (getWidth() / 2);
		int x = centreX + offset - (player.getWidth() / 2);
		frame.draw(player.getImage(), x, this.getPosition().y + 80);
	}

	private final class CombatHelper {

		/**
		 * This emulates one round of a war between two {@link ModelCountry}s. The
		 * defender will always defend with the maximum number of units available to
		 * them.
		 * 
		 * @param attacking
		 *            This is the {@link ModelCountry} that the {@link ModelPlayer} uses
		 *            to attack a {@link ModelCountry}.
		 * @param defending
		 *            This is the {@link ModelCountry} that defend against the
		 *            {@link ModelPlayer}'s attacking {@link ModelArmy}.
		 * @param attackSquadSize
		 *            Amount of units (dice) the attacking {@link ModelArmy} wants to
		 *            pit against the defending {@link ModelArmy}
		 */
		public void fight(SlickCountry attacking, SlickCountry defending, int attackSquadSize) {

			// Check parameter
			if (attackSquadSize > 3 || attackSquadSize < 0) {
				throw new IllegalArgumentException(
						"The attacker cannot attact with more that 3 or less than 3 units at a time.");
			}

			final int defendingArmySize = defending.model.getArmy().getNumberOfUnits() + defendingSquad.getAliveUnits();

			// Get the dice rolls for the attackers and defenders.
			final Integer[] attackerDiceRolls = getDiceRolls(attackSquadSize);

			final Integer[] defenderDiceRolls = getDiceRolls(
					defendingArmySize > MAX_DEFEND_SQUAD_SIZE ? MAX_DEFEND_SQUAD_SIZE : defendingArmySize);

			// The position of the top attacker dice
			final int attackX = getPosition().x + (getWidth() / 4);

			// The position of the top defender dice
			final int defendX = getPosition().x + ((getWidth() * 3) / 4);

			final int y = getPosition().y + 250;

			// Display the dice that we rolled
			dice.set(attackerDiceRolls, defenderDiceRolls, new Point(attackX, y), new Point(defendX, y));

			// Compare the dice that were rolled.
			compareDiceRolls(attackerDiceRolls, defenderDiceRolls);

		}

		/**
		 * Compares the attackers dice roles from the defenders rolls and removes units
		 * from the attacking country and defending country appropriately.
		 * 
		 * @param attackerDiceRolls
		 * @param defenderDiceRolls
		 */
		private void compareDiceRolls(Integer[] attackerDiceRolls, Integer[] defenderDiceRolls) {

			// Get the size of the smaller set of dice.
			final int diceToCheck = attackerDiceRolls.length >= defenderDiceRolls.length ? defenderDiceRolls.length
					: attackerDiceRolls.length;

			// Copy the squads to these holding lists so the squads can be modified.
			final LinkedList<SquadMember> attackingSqd = new LinkedList<>(attackingSquad.members);
			final LinkedList<SquadMember> defendingSqd = new LinkedList<>(defendingSquad.members);

			// The index of the current set of current units.
			int unitIndex = 0;

			// Holds whether the combat is in a valid state to continue. For example combat
			// cannot continue if the defender has no units.
			boolean finished = false;

			// Compare each attacking dice roll against the defending dice roll
			while (unitIndex < diceToCheck && !finished) {

				// The units that will fight
				final ModelUnit attackingUnit = attackingSqd.get(unitIndex).unit.model;
				final ModelUnit defendingUnit = defendingSqd.get(unitIndex).unit.model;

				// Attacker won
				if (attackerDiceRolls[unitIndex] > defenderDiceRolls[unitIndex]) {
					finished = attackerWon(defender, attacker, attackingUnit);
				}
				// Attacker lost
				else {
					finished = attackerLost(defender, attacker, defendingUnit);
				}

				unitIndex++;
			}
		}

		private boolean attackerWon(SlickCountry defending, SlickCountry attacking, ModelUnit attackingUnit) {

			final ModelPlayer defender = defending.model.getRuler();
			final ModelPlayer attacker = attacking.model.getRuler();
			final ModelArmy defendingArmy = defending.model.getArmy();
			final int totalStrength = defendingArmy.getStrength() + defendingSquad.geStrength();

			// If the army of the defending country is of size on then this victory will
			// conquer the country. Otherwise just kill unit from the defending army.
			if (attackingUnit.strength >= totalStrength) {
				resetArmyToWeakest(defendingSquad, defendingArmy, defender, attackingUnit);

				defending.model.setRuler(attacker);
				return true;

			} else {
				removeUnitFromArmy(defendingSquad, defendingArmy, defender, attackingUnit);

				// If the defending army was cleared.
				if (defendingArmy.getNumberOfUnits() == 0) {
					defendingArmy.setWeakest();
					defending.model.setRuler(attacker);
					return true;
				}

				return false;
			}
		}

		private boolean attackerLost(SlickCountry defending, SlickCountry attacking, ModelUnit defendingUnit) {

			final ModelPlayer attacker = attacking.model.getRuler();
			final ModelArmy attackingArmy = attacking.model.getArmy();
			final int totalStrength = attackingArmy.getStrength() + attackingSquad.geStrength();

			if (defendingUnit.strength >= totalStrength) {
				resetArmyToWeakest(attackingSquad, attackingArmy, attacker, defendingUnit);
				return true;
			} else {
				removeUnitFromArmy(attackingSquad, attackingArmy, attacker, defendingUnit);

				// If the attacking army was cleared.
				if (attackingArmy.getNumberOfUnits() == 1) {
					attackingArmy.setWeakest();
					return true;
				}

				return false;
			}
		}

		private void resetArmyToWeakest(Squad squad, ModelArmy army, ModelPlayer player, ModelUnit enemyUnit) {
			final ModelUnit weakestUnit = UnitHelper.getInstance().getWeakest();

			squad.returnSquadToArmy(army);

			final int toRemove = enemyUnit.strength - weakestUnit.strength;

			if (player != null) {
				player.totalArmy.remove(toRemove);
			}

			army.setWeakest();

		}

		private void removeUnitFromArmy(Squad squad, ModelArmy army, ModelPlayer player, ModelUnit enemyUnit) {

			boolean removedFromSquad = squad.killUnit(enemyUnit);

			// If the enemy unti's damage was not taken from the squad return the squad to
			// the army and then remove the damage from the army.
			if (!removedFromSquad) {
				squad.returnSquadToArmy(army);
				army.remove(enemyUnit);
			}

			if (player != null) {
				player.totalArmy.remove(enemyUnit);
			}

		}

		/**
		 * Gets an <code>int[]</code> of {@link Random#nextInt(int)} with bounds of 1 -
		 * 6
		 * 
		 * @param numberOfRolls
		 *            <code>int</code> number of rolls
		 * @return <code>int[]</code>
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

	/**
	 * Handles displaying the dice on the screen.
	 * 
	 * @author Joshua_Eddy
	 *
	 */
	private final class Dice {

		private static final int WIDTH = SlickUnit.WIDTH - 20;

		private static final int HEIGHT = SlickUnit.HEIGHT - 20;

		/**
		 * Holds the dice that will be displayed on screen.
		 */
		private final Map<Point, Image> display;

		/**
		 * Holds the default set of dice images.
		 */
		private final Map<Integer, Image> defaultDice;

		/**
		 * Constructs a new {@link Dice}.
		 */
		public Dice() {
			this.defaultDice = new HashMap<>();
			this.display = new HashMap<>();
		}

		/**
		 * Adds dice to {@link WarMenu#display} with there designated {@link Point}
		 * position.
		 * 
		 * @param attackerDiceRolls
		 * @param defenderDiceRolls
		 * @param attackTop
		 *            {@link Point} of the top dice for the attacker.
		 * @param defendTop
		 *            {@link Point} of the top dice for the defender.
		 */
		public void set(Integer[] attackerDiceRolls, Integer[] defenderDiceRolls, Point attackTop, Point defendTop) {

			int defendX = defendTop.x;
			int defendY = defendTop.y;

			int attackX = attackTop.x;
			int attackY = attackTop.y;

			clear();

			for (Integer roll : attackerDiceRolls) {
				display.put(new Point(attackX, attackY + 10), defaultDice.get(roll));
				attackY += SlickUnit.HEIGHT;
			}

			for (Integer roll : defenderDiceRolls) {
				display.put(new Point(defendX - WIDTH, defendY + 10), defaultDice.get(roll));
				defendY += SlickUnit.HEIGHT;
			}

			int boxWidth = 10;

			final Region box = new Region(boxWidth, HEIGHT, new Point(0, 0));

			final Image redBox = box.convert(Color.red, 255);

			final Image greenBox = box.convert(Color.green, 255);

			// Get the size of the smaller set of dice.
			final int diceToCheck = attackerDiceRolls.length >= defenderDiceRolls.length ? defenderDiceRolls.length
					: attackerDiceRolls.length;

			// Compare each attacking dice roll against the defending dice roll
			for (int i = 0; i < diceToCheck; i++) {

				final boolean attackerWon = attackerDiceRolls[i] > defenderDiceRolls[i];

				// Display defender box
				final Point defend = new Point(defendTop.x - WIDTH - boxWidth - 3,
						defendTop.y + (i * (SlickUnit.HEIGHT + 2)) + 10);
				display.put(defend, attackerWon ? redBox : greenBox);

				// Display attacker box
				final Point attack = new Point(attackTop.x + WIDTH + 3,
						attackTop.y + (i * (SlickUnit.HEIGHT + 2)) + 10);
				display.put(attack, attackerWon ? greenBox : redBox);
			}

		}

		/**
		 * Initialises this {@link Dice}.
		 */
		public void init() {

			// Iterate over all the values of a dice and import the dice's image.
			for (int index = 1; index <= 6; index++) {

				final Image dice = ImageReader.getImage(game.getDirectory().getDicePath() + "dice" + index + ".png");

				this.defaultDice.put(index, dice.getScaledCopy(WIDTH, HEIGHT));
			}
		}

		/**
		 * Draws this {@link Dice}.
		 */
		public void draw(Frame frame) {
			display.forEach((position, dice) -> frame.draw(dice, position.x, position.y));
		}

		/**
		 * Clears the dice in this {@link Dice}
		 */
		public void clear() {
			display.clear();
		}

	}

	private final class Squad {

		public final List<SquadMember> members;

		private final boolean isUser;

		private final int maxSize;

		private Point squadTop;

		public Squad(Point position, boolean isUser, int maxSize) {
			this.squadTop = position;
			this.isUser = isUser;
			this.maxSize = maxSize;
			this.members = new LinkedList<>();
		}

		public void returnUnitToArmy(ModelArmy army, SquadMember member) {

			members.remove(member);

			if (member.isAlive) {
				army.add(member.unit.model);
			}

			repositionUnits();
		}

		public boolean killUnit(ModelUnit unit) {

			boolean removedFromSquad = false;

			// If the enemy units damage can be removed from the squad remove it from the
			// squad
			for (SquadMember member : members) {
				if (!removedFromSquad && member.unit.model.strength == unit.strength && member.isAlive) {
					removedFromSquad = true;
					member.isAlive = false;
				}
			}

			return removedFromSquad;

		}

		public void autoPopulate(ModelArmy army) {

			removeDeadUnits();
			returnSquadToArmy(army);

			ModelUnit unit = army.getStrongestUnit();

			while (size() < maxSize && army.getNumberOfUnits() > (isUser ? 1 : 0)) {

				if (army.hasUnit(unit)) {
					this.moveToSquad(unit, army);
				} else {
					unit = army.getStrongestUnit();
				}
			}

		}

		public int getAliveUnits() {
			int numberOfUnits = 0;

			for (SquadMember member : members) {
				numberOfUnits += member.isAlive ? 1 : 0;
			}
			return numberOfUnits;
		}

		public int size() {
			return members.size();
		}

		public void moveToSquad(ModelUnit unit, ModelArmy army) {

			removeDeadUnits();

			if (members.size() < maxSize) {
				army.remove(unit);

				SquadMember member = new SquadMember(slick.modelView.getVisual(unit), true);
				member.setPosition(new Point(squadTop.x, squadTop.y + (members.size() * SlickUnit.HEIGHT)));

				members.add(member);
			}

		}

		public void draw(Frame frame) {

			int x = squadTop.x;
			int y = squadTop.y;

			// Draw each member in the squad
			for (SquadMember member : members) {

				// Draw the unit

				frame.draw(member, new EventListener() {

					@Override
					public void mouseHover(Point mouse, int delta) {
						// Do nothing
					}

					@Override
					public void mouseClick(Point mouse, int mouseButton) {

						if (isUser) {
							attackingSquad.returnUnitToArmy(attacker.model.getArmy(), member);

							// If all the alive units have been removed hide the attack button and clear the
							// dice.
							if (attackingSquad.getAliveUnits() == 0) {
								getButton(attackButton).hide();
								dice.clear();
							}
						}
					}

					@Override
					public void draw(Frame frame) {
						frame.draw(member.getImage(), member.getPosition().x, member.getPosition().y);
					}

					@Override
					public void buttonPress(int key, Point mouse) {
						// Do Nothing

					}
				});

				// If the member is dead then draw a cross over it.
				if (!member.isAlive) {

					final int left = x;
					final int right = x + SlickUnit.WIDTH;
					final int top = y;
					final int bottom = y + SlickUnit.HEIGHT;

					frame.setColor(Color.red);
					frame.drawLine(new Point(left, top), new Point(right, bottom));
					frame.drawLine(new Point(left, bottom), new Point(right, top));

				}

				y += SlickUnit.HEIGHT;
			}
		}

		public void clear() {
			if (!members.isEmpty()) {
				members.clear();
			}
		}

		public void move(Point vector) {

			int x = squadTop.x + vector.x;
			int y = squadTop.y + vector.y;

			squadTop = new Point(x, y);
		}

		public void removeDeadUnits() {

			members.stream().filter(member -> !member.isAlive).forEach(member -> members.remove(member));

			repositionUnits();

		}

		private void repositionUnits() {

			// Reposition all the alive units.
			int index = 0;
			for (SquadMember member : members) {
				member.setPosition(new Point(squadTop.x, squadTop.y + (index * SlickUnit.HEIGHT)));
				index++;
			}

		}

		public int geStrength() {
			int strength = 0;

			for (SquadMember member : members) {
				strength += member.isAlive ? member.unit.model.strength : 0;
			}

			return strength;
		}

		public void returnSquadToArmy(ModelArmy army) {
			members.forEach(member -> {
				if (member.isAlive) {
					army.add(member.unit.model);
				}
			});
			clear();
		}

	}

	private final class SquadMember extends Clickable {

		public final SlickUnit unit;

		public boolean isAlive;

		public SquadMember(SlickUnit unit, boolean isAlive) {

			this.unit = unit;
			this.isAlive = isAlive;

			replaceImage(unit.getImage());

		}

	}
}
