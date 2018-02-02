package peril.views.slick.components.menus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import peril.controllers.GameController;
import peril.helpers.UnitHelper;
import peril.views.slick.Button;
import peril.views.slick.Font;
import peril.views.slick.Point;
import peril.model.ModelPlayer;
import peril.views.slick.Region;
import peril.views.slick.Viewable;
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

	private final List<SlickUnit> attackingSquad;

	private final List<SlickUnit> defendingSquad;

	private Point squadTop;

	private Point armyPosition;

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
	private SlickPlayer player;

	/**
	 * The {@link ModelPlayer} that is ruling the defending {@link ModelCountry}.
	 */
	private SlickPlayer ruler;

	/**
	 * Constructs a new {@link WarMenu}.
	 * 
	 */
	public WarMenu(Point position, GameController game) {
		super(NAME, game, new Region(300, 400, position));

		this.random = new Random();
		this.headingFont = new Font("Arial", Color.red, 28);
		this.textFont = new Font("Arial", Color.red, 40);
		this.countryFont = new Font("Arial", Color.black, 20);
		this.resultFont = new Font("Arial", Color.black, 15);
		this.dice = new Dice();
		this.attackButton = "war";
		this.attackingSquad = new LinkedList<>();
		this.defendingSquad = new LinkedList<>();

		final int x = getPosition().x + (getWidth() / 2) - 25;
		final int y = getPosition().y - 100 + getHeight();
		this.squadTop = new Point(this.getPosition().x + (this.getWidth() / 4), this.getPosition().y + 200);
		this.armyPosition = new Point(x, y);

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

	}

	/**
	 * Draws the {@link War Menu} on the screen.
	 */
	public void draw(Graphics g) {

		super.draw(g);

		if (isVisible()) {
			
			rePositionAttackSquad();

			// Attacker has failed to conquer country
			if (attacker.model.getArmy().getStrength() == 1) {
				failedConquer(g);
			}
			// Attacker has conquered country
			else if (attacker.model.getRuler().equals(enemy.model.getRuler())) {
				succesfulConquer(g);
			}
			// Normal Combat
			else {
				normalCombat(g);
			}

			drawPlayer(g, player, -(getWidth() / 4));

			drawTitle(g);

			drawArmySizes(g);

		}
	}

	/**
	 * Adds an {@link Viewable} image to this {@link WarMenu}
	 */
	@Override
	public void addImage(Viewable image) {
		super.addImage(image);

		image.scale(getWidth(), getHeight());
		image.setPosition(getPosition());

	}

	/**
	 * Makes this {@link WarMenu} visible along with its attack {@link Button}.
	 */
	@Override
	public void show() {
		super.show();

		getButton(attackButton).show();

		attacker = slick.modelView.getVisual(game.getAttack().getSelected(0));
		enemy = slick.modelView.getVisual(game.getAttack().getSelected(1));

		ruler = slick.modelView.getVisual(enemy.model.getRuler());
		player = slick.modelView.getVisual(attacker.model.getRuler());

		defineDefendingSquad();

	}

	/**
	 * Hides this {@link WarMenu}.
	 */
	@Override
	public void hide() {
		super.hide();
		getButton(attackButton).hide();
		dice.clear();
	}

	/**
	 * Processes a click at a {@link Point} position on this {@link WarMenu}.
	 */
	public void parseClick(Point click) {

		if (slick.modelView.getVisual(attacker.model.getArmy()).isClicked(click, armyPosition, slick.modelView)) {

		} else {

			boolean squadClicked = false;

			// Reposition the squad so the click detection works.
			rePositionAttackSquad();

			SlickUnit toRemove = null;

			for (SlickUnit unit : attackingSquad) {

				if (unit.isClicked(click)) {

					attacker.model.getArmy().add(unit.model);

					toRemove = unit;
					squadClicked = true;
					break;
				}

			}

			if (toRemove != null) {
				attackingSquad.remove(toRemove);
			}

			if (squadClicked) {
				clickedButton(click);
			}

		}

	}

	private void rePositionAttackSquad() {

		int x = squadTop.x;
		int y = squadTop.y;

		for (SlickUnit unit : attackingSquad) {
			unit.setPosition(new Point(x, y));

			y += unit.getHeight() + 2;
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
			if (attacker.model.getArmy().getStrength() > 1) {

				final int squadSize = getSquadSize();

				// If the the attacking army is larger than or equal to the size of the
				// specified squad then attack.
				if (attacker.model.getArmy().getStrength() >= squadSize) {

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
						if (attacker.model.getArmy().getStrength() == 1) {
							game.getAttack().deselectAll();
						}
					}
				}

			}
		}

		// Can't attack any more hide the war menu.
		if (game.getAttack().getPrimary() == null || game.getAttack().getSecondary() == null) {
			game.getAttack().deselectAll();
		}
	}

	/**
	 * Moves all the visual components of the {@link WarMenu} along a specified
	 * {@link Point} vector.
	 */
	@Override
	public void moveComponents(Point vector) {
		final int x = getPosition().x + (getWidth() / 2) - 25;
		final int y = getPosition().y - 100 + getHeight();
		armyPosition = new Point(x, y);
	}

	/**
	 * Clears the elements of and hides, the {@link WarMenu}.
	 */
	public void clear() {
		player = null;
		enemy = null;

		clearSquad();

		attacker = null;
		ruler = null;

		hide();
	}

	private int getSquadSize() {
		return attackingSquad.size();
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
		Integer[] defenderDiceRolls = getDiceRolls(defending.model.getArmy().getStrength() > 1 ? 2 : 1);

		// The position of the top attacker dice
		int attackX = squadTop.x + 55;
		int attackY = squadTop.y;

		// The position of the top defender dice
		int defendX = this.getPosition().x + ((this.getWidth() * 3) / 4);
		int defendY = squadTop.y;

		// Display the dice that we rolled
		dice.set(attackerDiceRolls, defenderDiceRolls, new Point(attackX, attackY), new Point(defendX, defendY),
				attackingSquad.get(0).getHeight());

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
	private void failedConquer(Graphics g) {
		String failure = "has insufficient units to attack.";

		countryFont.draw(g, attacker.model.getName(),
				getPosition().x + (getWidth() / 2) - (countryFont.getWidth(attacker.model.getName()) / 2),
				getPosition().y + (getHeight() / 2) - 30);

		resultFont.draw(g, failure, getPosition().x + (getWidth() / 2) - (resultFont.getWidth(failure) / 2),
				getPosition().y + (getHeight() / 2));

		drawPlayer(g, ruler, (getWidth() / 4));
		getButton(attackButton).hide();
	}

	/**
	 * Displays text when a {@link ModelPlayer} successfully conquers a
	 * {@link ModelCountry} they are attacking.
	 * 
	 * @param g
	 *            {@link Graphics}.
	 */
	private void succesfulConquer(Graphics g) {

		// Draw the attacking country's name
		countryFont.draw(g, attacker.model.getName(),
				getPosition().x + (getWidth() / 2) - (countryFont.getWidth(attacker.model.getName()) / 2),
				getPosition().y + (getHeight() / 2) - 30);

		// Draw text
		String success = "has conquered";
		resultFont.draw(g, success, getPosition().x + (getWidth() / 2) - (resultFont.getWidth(success) / 2),
				getPosition().y + (getHeight() / 2));

		// Draw the defending country's name
		countryFont.draw(g, enemy.model.getName(),
				getPosition().x + (getWidth() / 2) - (countryFont.getWidth(enemy.model.getName()) / 2),
				getPosition().y + (getHeight() / 2) + 30);

		drawPlayer(g, player, (getWidth() / 4));
		getButton(attackButton).hide();
	}

	/**
	 * Displays text detailing the start of an instance of a normal combat phase
	 * ({@link ModelCountry} attacking another {@link ModelCountry}).
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void normalCombat(Graphics g) {
		drawPlayer(g, ruler, (getWidth() / 4));

		slick.modelView.getVisual(attacker.model.getArmy()).drawExpanded(g, armyPosition, ruler, slick.modelView);

		dice.draw(g);
	}

	/**
	 * Draws the army sizes for each {@link ModelArmy} belonging to the attacking
	 * and defending {@link ModelCountry}s.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawArmySizes(Graphics g) {

		String attackingArmy = "" + attacker.model.getArmy().getStrength();

		textFont.draw(g, attackingArmy, getPosition().x + (getWidth() / 4) - (textFont.getWidth(attackingArmy) / 2),
				getPosition().y + 120);

		String enemyArmy = "" + enemy.model.getArmy().getStrength();
		textFont.draw(g, enemyArmy, getPosition().x + ((getWidth() * 3) / 4) - (textFont.getWidth(enemyArmy) / 2),
				getPosition().y + 120);

	}

	/**
	 * Draws the names of the {@link ModelPlayer}s whose {@link ModelCountry}s are
	 * attacking and defending.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawTitle(Graphics g) {
		String vs = "VS";
		String attackerStr = attacker.model.getName();
		String enemyStr = enemy.model.getName();

		int centreX = getPosition().x + (getWidth() / 2);
		int vsX = centreX - (headingFont.getWidth(vs) / 2);

		int attackerX = centreX - (getWidth() / 4) - (countryFont.getWidth(attackerStr) / 2);
		int enemyX = centreX + (getWidth() / 4) - (countryFont.getWidth(enemyStr) / 2);

		headingFont.draw(g, vs, vsX, getPosition().y + 100);
		countryFont.draw(g, attackerStr, attackerX, getPosition().y + 100);
		countryFont.draw(g, enemyStr, enemyX, getPosition().y + 100);
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
	private void drawPlayer(Graphics g, SlickPlayer player, int offset) {

		if (player == null) {
			return;
		}

		int centreX = getPosition().x + (getWidth() / 2);
		int x = centreX + offset - (player.getWidth() / 2);
		g.drawImage(player.getImage(), x, this.getPosition().y + 55);
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

		returnUnitsToArmy(attackingSquad, attacker.model.getArmy());

		ModelArmy attackingArmy = attacking.model.getArmy();
		ModelArmy defendingArmy = defending.model.getArmy();

		ModelPlayer defender = defending.model.getRuler();
		ModelPlayer attacker = attacking.model.getRuler();

		// Get the size of the smaller set of dice.
		int diceToCheck = attackerDiceRolls.length >= defenderDiceRolls.length ? defenderDiceRolls.length
				: attackerDiceRolls.length;

		// Compare each attacking dice roll against the defending dice roll
		for (int i = 0; i < diceToCheck; i++) {
			/*
			 * If the attackers dice is higher than the deffender's remove one unit from the
			 * defender's army and vice versa.
			 */
			if (attackerDiceRolls[i] > defenderDiceRolls[i]) {

				// If the army of the defending country is of size on then this victory will
				// conquer the country. Otherwise just kill one unit from the defending army.
				if (defendingArmy.getStrength() == 1) {
					defending.model.setRuler(attacker);
					attacker.totalArmy.add(UnitHelper.getInstance().getWeakest());
					break;
				} else {
					defendingArmy.remove(attackingSquad.get(i).model.strength);
				}

				if (defender != null) {
					defender.totalArmy.remove(attackingSquad.get(i).model.strength);
				}

			}
			// Attacker has lost the attack
			else {
				attackingArmy.remove(1);
				attacker.totalArmy.remove(1);
			}
		}

	}

	private void defineDefendingSquad() {

		final ModelArmy army = enemy.model.getArmy();
		final int squadSize = army.getNumberOfUnits() > 1 ? 2 : 1;
		final UnitHelper units = UnitHelper.getInstance();

		if (!defendingSquad.isEmpty())
			defendingSquad.clear();

		ModelUnit current = units.getStrongest();

		while (current != null && defendingSquad.size() != squadSize) {

			if (army.hasUnit(current)) {

				for (int index = 0; index < army.getNumberOf(current) && defendingSquad.size() != squadSize; index++) {
					defendingSquad.add(slick.modelView.getVisual(current));
				}
			}

			current = units.getUnitBelow(current);

		}

		// Remove all the squad members from the army.
		defendingSquad.forEach(unit -> enemy.model.getArmy().remove(unit.model));

	}

	private void returnUnitsToArmy(List<SlickUnit> squad, ModelArmy army) {
		squad.forEach((unit) -> army.add(unit.model));
	}

	private void clearSquad() {
		if (!attackingSquad.isEmpty()) {
			attackingSquad.clear();
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
		 * The width of the dice.
		 */
		private static final int diceWidth = 28;

		/**
		 * The height of the dice.
		 */
		private static final int diceHeight = 28;

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
		public void set(Integer[] attackerDiceRolls, Integer[] defenderDiceRolls, Point attackTop, Point defendTop,
				int unitHeight) {

			int defendX = defendTop.x;
			int defendY = defendTop.y;

			int attackX = attackTop.x;
			int attackY = attackTop.y;

			clear();

			for (Integer roll : attackerDiceRolls) {
				display.put(new Point(attackX, attackY), defaultDice.get(roll));
				attackY += unitHeight + 2;
			}

			for (Integer roll : defenderDiceRolls) {
				display.put(new Point(defendX, defendY), defaultDice.get(roll));
				defendY += unitHeight + 2;
			}

			int boxWidth = 10;

			Region box = new Region(boxWidth, unitHeight, new Point(0, 0));

			Image redBox = box.convert(Color.red, 255);

			Image greenBox = box.convert(Color.green, 255);

			// Get the size of the smaller set of dice.
			int diceToCheck = attackerDiceRolls.length >= defenderDiceRolls.length ? defenderDiceRolls.length
					: attackerDiceRolls.length;

			// Compare each attacking dice roll against the defending dice roll
			for (int i = 0; i < diceToCheck; i++) {

				boolean attackerWon = attackerDiceRolls[i] > defenderDiceRolls[i];

				// Display defender box
				Point defend = new Point(defendTop.x - boxWidth - 3, defendTop.y + (i * (diceWidth + 2)));
				display.put(defend, attackerWon ? redBox : greenBox);

				// Display attacker box
				Point attack = new Point(attackTop.x + diceWidth + 3, attackTop.y + (i * (diceWidth + 2)));
				display.put(attack, attackerWon ? greenBox : redBox);
			}

		}

		/**
		 * Initialises this {@link Dice}.
		 */
		public void init() {
			for (int i = 1; i <= 6; i++) {
				this.defaultDice.put(i, ImageReader.getImage(game.getUIPath() + "dice" + i + ".png")
						.getScaledCopy(diceWidth, diceHeight));
			}
		}

		/**
		 * Draws this {@link Dice}.
		 */
		public void draw(Graphics g) {
			display.forEach((position, dice) -> g.drawImage(dice, position.x, position.y));
		}

		/**
		 * Clears the dice in this {@link Dice}
		 */
		public void clear() {
			display.clear();
		}

	}

}
