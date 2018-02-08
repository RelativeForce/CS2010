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
import peril.helpers.UnitHelper;
import peril.views.slick.Button;
import peril.views.slick.Font;
import peril.views.slick.Frame;
import peril.views.slick.Point;
import peril.model.ModelPlayer;
import peril.views.slick.Region;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.board.SlickUnit;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.views.slick.io.ImageReader;

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

	/**
	 * The id of the {@link Button} that will be clicked to attack enemy
	 * {@link ModelCountry} .
	 * 
	 */
	private final String attackButton;

	/**
	 * The {@link ModelCountry} of the attacking {@link ModelPlayer}.
	 */
	private SlickCountry attacker;

	/**
	 * The {@link ModelCountry} of the defending {@link ModelPlayer}.
	 */
	private SlickCountry enemy;

	/**
	 * The {@link ModelPlayer} that is ruling the attacking {@link ModelCountry}.
	 */
	private SlickPlayer attackingRuler;

	/**
	 * The {@link ModelPlayer} that is ruling the defending {@link ModelCountry}.
	 */
	private SlickPlayer enemyRuler;

	private Frame frame;

	private final Consumer<SlickUnit> poolClick;

	private final Consumer<SquadMember> squadClick;

	private final List<SquadMember> attackingSquad;

	/**
	 * Constructs a new {@link WarMenu}.
	 * 
	 */
	public WarMenu(Point position, GameController game) {
		super(NAME, game, new Region(600, 600, position));

		this.random = new Random();
		this.headingFont = new Font("Arial", Color.red, 28);
		this.textFont = new Font("Arial", Color.red, 40);
		this.countryFont = new Font("Arial", Color.black, 20);
		this.resultFont = new Font("Arial", Color.black, 15);
		this.armyFont = new Font("Arial", Color.cyan, 25);
		this.dice = new Dice();
		this.attackButton = "war";
		this.attackingSquad = new LinkedList<>();

		poolClick = new Consumer<SlickUnit>() {

			@Override
			public void accept(SlickUnit unit) {
				moveToAttackSquad(unit.model);
			}
		};

		squadClick = new Consumer<SquadMember>() {

			@Override
			public void accept(SquadMember unit) {
				returnUnitToAttackingArmy(unit);
			}
		};

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

	}

	private void returnUnitToAttackingArmy(SquadMember member) {

		attackingSquad.remove(member);

		if (member.isAlive) {
			attacker.model.getArmy().add(member.unit.model);
		}

		// If all the alive units have been removed hide the attack button and clear the
		// dice.
		if (getAliveUnits(attackingSquad) == 0) {
			getButton(attackButton).hide();
			dice.clear();
		}

	}

	private void moveToAttackSquad(ModelUnit unit) {

		removeDeadUnits(attackingSquad);

		if (attackingSquad.size() <= MAX_ATTACK_SQUAD_SIZE - 1 && attacker.model.getArmy().getNumberOfUnits() > 1) {
			attacker.model.getArmy().remove(unit);
			attackingSquad.add(new SquadMember(slick.modelView.getVisual(unit), true));
		}

		getButton(attackButton).show();

	}

	private void removeDeadUnits(List<SquadMember> squad) {

		List<SquadMember> toRemove = new LinkedList<>();

		squad.forEach(member -> {
			if (!member.isAlive) {
				toRemove.add(member);
			}
		});

		toRemove.forEach(member -> squad.remove(member));

	}

	public void returnSquadToArmy(List<SquadMember> squad, ModelArmy army) {
		squad.forEach(member -> {
			if (member.isAlive) {
				army.add(member.unit.model);
			}
		});
		clearSquad(attackingSquad);
		getButton(attackButton).hide();
	}

	private void clearSquad(List<SquadMember> squad) {
		if (!squad.isEmpty()) {
			squad.clear();
		}
	}

	/**
	 * Draws the {@link War Menu} on the screen.
	 */
	public void draw(Graphics g) {

		super.draw(g);

		if (isVisible()) {

			frame = new Frame(g);

			final int size = attackingSquad.size() + attacker.model.getArmy().getNumberOfUnits();

			// Attacker has failed to conquer country
			if (size == 1) {
				failedConquer();
			}
			// Attacker has conquered country
			else if (attacker.model.getRuler().equals(enemy.model.getRuler())) {
				succesfulConquer();
			}
			// Normal Combat
			else {
				drawNormalCombat();
			}

			// drawArmySizes(g);

		}
	}

	private void drawNormalCombat() {
		Point position = new Point(this.getPosition().x + (this.getWidth() / 4) - SlickUnit.WIDTH,
				this.getPosition().y + 250);

		drawSquad(attackingSquad, position);

		drawPlayer(enemyRuler, (getWidth() / 4));

		drawPlayer(attackingRuler, -(getWidth() / 4));

		drawTitle();

		drawArmySizes();

		dice.draw();

		drawArmyPool(frame, new Point(getPosition().x + (getWidth() / 2), getPosition().y + getHeight() - 120));
	}

	private void drawArmyPool(Frame frame, Point position) {

		ModelArmy model = attacker.model.getArmy();

		int x = position.x - ((model.getVarietyOfUnits() - 1) * 50) - (SlickUnit.WIDTH / 2);
		int y = position.y - (SlickUnit.HEIGHT / 2);

		frame.draw(countryFont, "Your Army:", position.x - (countryFont.getWidth("Your Army:") / 2),
				y - (countryFont.getHeight() * 2));

		// For each unit in the army
		for (ModelUnit current : model) {

			// Holds the visual version of the unit.
			final SlickUnit unit = slick.modelView.getVisual(current);

			final Button unitButton = new Button(new Point(x, y), unit.getImage(), new Action<>(unit, poolClick),
					"squadClick");

			final int numberOfCurrent = model.getNumberOf(unit.model);

			final String number = Integer.toString(numberOfCurrent);

			final int fontX = x + (SlickUnit.WIDTH / 2) - (armyFont.getWidth(number) / 2);
			final int fontY = y + (SlickUnit.HEIGHT / 2) - (armyFont.getHeight() / 2);

			frame.draw(unitButton);
			frame.draw(armyFont, number, fontX, fontY);

			x += 50;

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
		enemy = slick.modelView.getVisual(game.getAttack().getSelected(1));

		enemyRuler = slick.modelView.getVisual(enemy.model.getRuler());
		attackingRuler = slick.modelView.getVisual(attacker.model.getRuler());

	}

	private void drawSquad(List<SquadMember> squad, Point topUnit) {

		int x = topUnit.x;
		int y = topUnit.y;

		// Draw each member in the squad
		for (SquadMember member : squad) {

			// Draw the unit
			frame.draw(new Button(new Point(x, y), member.unit.getImage(), new Action<>(member, squadClick), "squad"));

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

	/**
	 * Hides this {@link WarMenu}.
	 */
	@Override
	public void hide() {
		super.hide();

		if (attacker != null) {
			returnSquadToArmy(attackingSquad, attacker.model.getArmy());
		}

		dice.clear();

		getButton(attackButton).hide();

	}

	/**
	 * Processes a click at a {@link Point} position on this {@link WarMenu}.
	 */
	public void parseClick(Point click) {

		if (!frame.click(click)) {
			clickedButton(click);
		}

	}

	/**
	 * Processes the attack between 2 highlighted {@link ModelCountry}s and see if
	 * they are eligible to 'fight'.
	 */
	public void attack() {
		// If there is two countries highlighted
		if (attacker != null && enemy != null) {

			ModelPlayer attackingPlayer = attacker.model.getRuler();
			ModelPlayer defendingPlayer = enemy.model.getRuler();

			// If the army of the primary highlighted country is larger that 1 unit in size
			if (attacker.model.getArmy().getNumberOfUnits() + getAliveUnits(attackingSquad) > 1) {

				// Remove the dead attacking units.
				removeDeadUnits(attackingSquad);

				int squadSize = attackingSquad.size();

				// Execute the combat
				fight(attacker, enemy, squadSize);

				// If the country has been conquered
				if (attacker.model.getRuler().equals(enemy.model.getRuler())) {

					// If there is a defending player
					if (defendingPlayer != null) {

						defendingPlayer.setCountriesRuled(defendingPlayer.getCountriesRuled() - 1);

						// If the player has no countries they have lost.
						if (defendingPlayer.getCountriesRuled() == 0) {

							game.setLoser(defendingPlayer);
							game.checkWinner();
						}
					}

					game.getAttack().deselectAll();

					attackingPlayer.setCountriesRuled(attackingPlayer.getCountriesRuled() + 1);

					game.checkContinentRulership();

					game.checkChallenges();

				} else {

					// If the attacking army is not large enough to attack again.
					if (attacker.model.getArmy().getNumberOfUnits() + getAliveUnits(attackingSquad) == 1) {
						game.getAttack().deselectAll();
						getButton(attackButton).hide();
					}
				}

			}
		}

		// Cant attack any more hide the war menu.
		if (game.getAttack().getPrimary() == null || game.getAttack().getSecondary() == null) {
			game.getAttack().deselectAll();
		}
	}

	private int getAliveUnits(List<SquadMember> squad) {
		int numberOfUnits = 0;

		for (SquadMember member : squad) {
			numberOfUnits += member.isAlive ? 1 : 0;
		}
		return numberOfUnits;
	}

	/**
	 * Moves all the visual components of the {@link WarMenu} along a specified
	 * {@link Point} vector.
	 */
	@Override
	public void moveComponents(Point vector) {

	}

	/**
	 * Clears the elements of and hides, the {@link WarMenu}.
	 */
	public void clear() {

		this.hide();

		attackingRuler = null;
		enemy = null;
		attacker = null;
		enemyRuler = null;

	}

	/**
	 * Selects the highest number of dice possible for the current state of the
	 * {@link WarMenu}.
	 */
	public void selectMaxUnits() {

		ModelArmy army = attacker.model.getArmy();

		ModelUnit unit = army.getStrongestUnit();

		while (attackingSquad.size() < MAX_ATTACK_SQUAD_SIZE && army.getNumberOfUnits() > 1) {

			if (army.hasUnit(unit)) {
				moveToAttackSquad(unit);
			} else {
				unit = army.getStrongestUnit();
			}
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

		// Holds the dice roles.
		Integer[] rolls = new Integer[numberOfRolls];

		// Initialise dice rolls for the attacking army
		for (int rollIndex = 0; rollIndex < numberOfRolls; rollIndex++) {
			rolls[rollIndex] = random.nextInt(6) + 1;
		}

		// Sort the dice roles into descending order.
		Arrays.sort(rolls, Collections.reverseOrder());

		return rolls;
	}

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
	private void fight(SlickCountry attacking, SlickCountry defending, int attackSquadSize) {

		// Check parameter
		if (attackSquadSize > 3 || attackSquadSize < 0) {
			throw new IllegalArgumentException(
					"The attacker cannot attact with more that 3 or less than 3 units at a time.");
		}

		// Get the dice rolls for the attackers and defenders.
		Integer[] attackerDiceRolls = getDiceRolls(attackSquadSize);

		final int defendingArmySize = defending.model.getArmy().getNumberOfUnits();

		Integer[] defenderDiceRolls = getDiceRolls(
				defendingArmySize > MAX_DEFEND_SQUAD_SIZE ? MAX_DEFEND_SQUAD_SIZE : defendingArmySize);

		// The position of the top attacker dice
		final int attackX = this.getPosition().x + (this.getWidth() / 4);

		// The position of the top defender dice
		final int defendX = this.getPosition().x + ((this.getWidth() * 3) / 4);

		final int y = this.getPosition().y + 250;

		// Display the dice that we rolled
		dice.set(attackerDiceRolls, defenderDiceRolls, new Point(attackX, y), new Point(defendX, y));

		// Compare the dice that were rolled.
		compareDiceRolls(attackerDiceRolls, defenderDiceRolls, attacking, defending);

	}

	/**
	 * Displays text when a {@link ModelPlayer} fails to conquer a
	 * {@link ModelCountry} they are attacking.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void failedConquer() {
		String failure = "has insufficient units to attack.";

		frame.draw(countryFont, attacker.model.getName(),
				getPosition().x + (getWidth() / 2) - (countryFont.getWidth(attacker.model.getName()) / 2),
				getPosition().y + (getHeight() / 2) - 30);

		frame.draw(resultFont, failure, getPosition().x + (getWidth() / 2) - (resultFont.getWidth(failure) / 2),
				getPosition().y + (getHeight() / 2));

		drawPlayer(attackingRuler, -(getWidth() / 4));
		drawPlayer(enemyRuler, (getWidth() / 4));

		drawTitle();

	}

	/**
	 * Displays text when a {@link ModelPlayer} successfully conquers a
	 * {@link ModelCountry} they are attacking.
	 * 
	 * @param g
	 *            {@link Graphics}.
	 */
	private void succesfulConquer() {

		// Draw the attacking country's name
		frame.draw(countryFont, attacker.model.getName(),
				getPosition().x + (getWidth() / 2) - (countryFont.getWidth(attacker.model.getName()) / 2),
				getPosition().y + (getHeight() / 2) - 30);

		// Draw text
		String success = "has conquered";
		frame.draw(resultFont, success, getPosition().x + (getWidth() / 2) - (resultFont.getWidth(success) / 2),
				getPosition().y + (getHeight() / 2));

		// Draw the defending country's name
		frame.draw(countryFont, enemy.model.getName(),
				getPosition().x + (getWidth() / 2) - (countryFont.getWidth(enemy.model.getName()) / 2),
				getPosition().y + (getHeight() / 2) + 30);

		drawPlayer(attackingRuler, -(getWidth() / 4));
		drawPlayer(enemyRuler, (getWidth() / 4));

		drawTitle();

	}

	/**
	 * Draws the army sizes for each {@link ModelArmy} belonging to the attacking
	 * and defending {@link ModelCountry}s.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawArmySizes() {

		final int yOffset = 190;
		
		String attackingArmy = Integer
				.toString(attacker.model.getArmy().getStrength() + getSquadStrength(attackingSquad));

		frame.draw(textFont, attackingArmy, getPosition().x + (getWidth() / 4) - (textFont.getWidth(attackingArmy) / 2),
				getPosition().y + yOffset);

		String enemyArmy = "" + enemy.model.getArmy().getStrength();

		frame.draw(textFont, enemyArmy, getPosition().x + ((getWidth() * 3) / 4) - (textFont.getWidth(enemyArmy) / 2),
				getPosition().y + yOffset);

	}

	private int getSquadStrength(List<SquadMember> squad) {
		int strength = 0;

		for (SquadMember member : squad) {
			strength += member.isAlive ? member.unit.model.strength : 0;
		}

		return strength;
	}

	/**
	 * Draws the names of the {@link ModelPlayer}s whose {@link ModelCountry}s are
	 * attacking and defending.
	 * 
	 * @param frame2
	 *            {@link Graphics}
	 */
	private void drawTitle() {
		
		final int yOffset = 170;
		
		String vs = "VS";
		String attackerStr = attacker.model.getName();
		String enemyStr = enemy.model.getName();

		int centreX = getPosition().x + (getWidth() / 2);
		int vsX = centreX - (headingFont.getWidth(vs) / 2);

		int attackerX = centreX - (getWidth() / 4) - (countryFont.getWidth(attackerStr) / 2);
		int enemyX = centreX + (getWidth() / 4) - (countryFont.getWidth(enemyStr) / 2);

		frame.draw(headingFont, vs, vsX, getPosition().y + yOffset);
		frame.draw(countryFont, attackerStr, attackerX, getPosition().y + yOffset);
		frame.draw(countryFont, enemyStr, enemyX, getPosition().y + yOffset);

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
	private void drawPlayer(SlickPlayer player, int offset) {

		if (player == null) {
			return;
		}

		int centreX = getPosition().x + (getWidth() / 2);
		int x = centreX + offset - (player.getWidth() / 2);
		frame.draw(player.getImage(), x, this.getPosition().y + 120);
	}

	/**
	 * Compares the attackers dice roles from the defenders rolls and removes units
	 * from the attacking country and defending country appropriately.
	 * 
	 * @param attackerDiceRolls
	 * @param defenderDiceRolls
	 * @param attacking
	 *            {@link Counrty}
	 * @param defending
	 *            {@link Counrty}
	 */
	private void compareDiceRolls(Integer[] attackerDiceRolls, Integer[] defenderDiceRolls, SlickCountry attacking,
			SlickCountry defending) {

		ModelArmy attackingArmy = attacking.model.getArmy();
		ModelArmy defendingArmy = defending.model.getArmy();

		ModelPlayer defender = defending.model.getRuler();
		ModelPlayer attacker = attacking.model.getRuler();

		// Get the size of the smaller set of dice.
		int diceToCheck = attackerDiceRolls.length >= defenderDiceRolls.length ? defenderDiceRolls.length
				: attackerDiceRolls.length;

		// Compare each attacking dice roll against the defending dice roll
		for (int i = 0; i < diceToCheck; i++) {

			ModelUnit attackingUnit = attackingSquad.get(i).unit.model;
			ModelUnit defendingUnit = UnitHelper.getInstance().getWeakest();

			/*
			 * If the attackers dice is higher than the deffender's remove one unit from the
			 * defender's army and vice versa.
			 */
			if (attackerDiceRolls[i] > defenderDiceRolls[i]) {

				// If the army of the defending country is of size on then this victory will
				// conquer the country. Otherwise just kill unit from the defending army.
				if (attackingUnit.strength >= defendingArmy.getStrength()) {
					defending.model.setRuler(attacker);
					enemyRuler = attackingRuler;
					defendingArmy.setWeakest();
					attacker.totalArmy.add(UnitHelper.getInstance().getWeakest());
					getButton(attackButton).hide();
					break;
				} else {
					defendingArmy.remove(attackingUnit);
				}

				if (defender != null) {
					defender.totalArmy.remove(attackingUnit);
				}

			}
			// Attacker has lost the attack
			else {

				boolean removedFromSquad = false;

				// If the enemy units damage can be removed from the squad remove it from the
				// squad
				for (SquadMember member : attackingSquad) {
					if (!removedFromSquad && member.unit.model.strength == defendingUnit.strength) {
						removedFromSquad = true;
						member.isAlive = false;
					}
				}

				// If the enemy unti's damage was not taken from the squad return the squad to
				// the army and then remove the damage from the army.
				if (!removedFromSquad) {

					returnSquadToArmy(attackingSquad, attackingArmy);
					attackingArmy.remove(defendingUnit);

				}

				if (getAliveUnits(attackingSquad) + attackingArmy.getNumberOfUnits() == 1) {
					getButton(attackButton).hide();
				}
				
				attacker.totalArmy.remove(defendingUnit);

			}
		}

	}

	/**
	 * Handles displaying the dice on the screen.
	 * 
	 * @author Joshua_Eddy
	 *
	 */
	private final class Dice {

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
				display.put(new Point(attackX, attackY), defaultDice.get(roll));
				attackY += SlickUnit.HEIGHT;
			}

			for (Integer roll : defenderDiceRolls) {
				display.put(new Point(defendX, defendY), defaultDice.get(roll));
				defendY += SlickUnit.HEIGHT;
			}

			int boxWidth = 10;

			Region box = new Region(boxWidth, SlickUnit.HEIGHT, new Point(0, 0));

			Image redBox = box.convert(Color.red, 255);

			Image greenBox = box.convert(Color.green, 255);

			// Get the size of the smaller set of dice.
			int diceToCheck = attackerDiceRolls.length >= defenderDiceRolls.length ? defenderDiceRolls.length
					: attackerDiceRolls.length;

			// Compare each attacking dice roll against the defending dice roll
			for (int i = 0; i < diceToCheck; i++) {

				boolean attackerWon = attackerDiceRolls[i] > defenderDiceRolls[i];

				// Display defender box
				Point defend = new Point(defendTop.x - boxWidth - 3, defendTop.y + (i * (SlickUnit.WIDTH + 2)));
				display.put(defend, attackerWon ? redBox : greenBox);

				// Display attacker box
				Point attack = new Point(attackTop.x + SlickUnit.WIDTH + 3, attackTop.y + (i * (SlickUnit.WIDTH + 2)));
				display.put(attack, attackerWon ? greenBox : redBox);
			}

		}

		/**
		 * Initialises this {@link Dice}.
		 */
		public void init() {
			for (int i = 1; i <= 6; i++) {
				this.defaultDice.put(i, ImageReader.getImage(game.getDirectory().getDicePath() + "dice" + i + ".png")
						.getScaledCopy(SlickUnit.WIDTH, SlickUnit.HEIGHT));
			}
		}

		/**
		 * Draws this {@link Dice}.
		 */
		public void draw() {
			display.forEach((position, dice) -> frame.draw(dice, position.x, position.y));
		}

		/**
		 * Clears the dice in this {@link Dice}
		 */
		public void clear() {
			display.clear();
		}

	}

	private final class SquadMember {

		public final SlickUnit unit;

		public boolean isAlive;

		public SquadMember(SlickUnit unit, boolean isAlive) {

			this.unit = unit;
			this.isAlive = isAlive;

		}

	}
}
