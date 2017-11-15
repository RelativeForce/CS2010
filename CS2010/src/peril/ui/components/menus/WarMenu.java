package peril.ui.components.menus;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Army;
import peril.board.Country;
import peril.ui.Button;
import peril.ui.components.Font;
import peril.ui.components.Region;
import peril.ui.components.VisualList;

/**
 * Encapsulates all the game combat logic.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 *
 */
public class WarMenu extends Menu {

	/**
	 * {@link Random} object for the random number generator
	 * 
	 */
	private Random random;

	private VisualList<Integer> squadSizes;

	private List<Button> buttons;

	private Font textFont;

	public boolean visible;

	/**
	 * Constructs a new {@link WarMenu}.
	 * 
	 */
	public WarMenu(Point position, Game game) {
		super("War", game, new Region(300, 300, position));
		random = new Random();

		squadSizes = new VisualList<>(position.x + 100, position.y + 10, 20, 20, 3, 5);
		squadSizes.add("1", 1);
		squadSizes.add("2", 2);
		squadSizes.add("3", 3);

		buttons = new LinkedList<>();
		visible = false;
	}

	/**
	 * When a {@link Player} clicks on another {@link Country} to fight. This pits 2
	 * {@link Army}'s against each other. This emulates 1 turn of combat between 2
	 * {@link Player}s.
	 * 
	 * @param attacking
	 *            This is the {@link Country} that the {@link Player} uses to attack
	 *            a {@link Country}.
	 * @param defending
	 *            This is the {@link Country} that defend against the
	 *            {@link Player}'s attacking {@link Army}.
	 * @param atkSquadSize
	 *            Amount of units (dice) the attacking {@link Army} wants to pit
	 *            against the defending {@link Army}
	 */
	public void fight(Country attacking, Country defending, int atkSquadSize) {

		Army attackingArmy = attacking.getArmy();
		Army defendingArmy = defending.getArmy();

		Player defender = defending.getRuler();
		Player attacker = attacking.getRuler();

		// Check parameter
		if (atkSquadSize > 3 || atkSquadSize < 0) {
			throw new IllegalArgumentException(
					"The attacker cannot attact with more that 3 or less than 3 units at a time.");
		}

		// Get the dice rolls for the attackers and defenders.
		int[] attackerDiceRolls = getDiceRolls(atkSquadSize);
		int[] defenderDiceRolls = getDiceRolls(defendingArmy.getSize() > 1 ? 2 : 1);

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

				if (defendingArmy.getSize() == 1) {
					defending.setRuler(attacker);
					attacker.setTotalArmySize(attacker.getTotalArmySize() + 1);
				} else {
					defendingArmy.setSize(defendingArmy.getSize() - 1);
				}
				defender.setTotalArmySize(defender.getTotalArmySize() - 1);

			} else {

				if (attackingArmy.getSize() == 1) {
					attacking.setRuler(defender);
					defender.setTotalArmySize(defender.getTotalArmySize() + 1);
				} else {
					attackingArmy.setSize(attackingArmy.getSize() - 1);
				}
				attacker.setTotalArmySize(attacker.getTotalArmySize() - 1);

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
	private int[] getDiceRolls(int numberOfRolls) {

		// Holds the dice roles.
		int[] rolls = new int[numberOfRolls];

		// initialise dice rolls for the attacking army
		for (int rollIndex = 0; rollIndex < numberOfRolls; rollIndex++) {
			rolls[rollIndex] = random.nextInt(6) + 1;
		}

		// Sort the dice roles into ascending order.
		Arrays.sort(rolls);

		return rolls;
	}

	public void init() {
		squadSizes.init();
		textFont = new Font("Arial", Color.cyan, 20);
		squadSizes.setFont(textFont);
	}

	public void draw(Graphics g) {

		if (visible) {
			g.setColor(Color.black);
			g.fillRect(getPosition().x, getPosition().y, getWidth(), getHeight());

			squadSizes.draw(g);
			textFont.draw(g, "ATTACK", getPosition().x, getPosition().y);
		}
	}

	public void parseClick(Point click) {
		if (!squadSizes.click(click)) {
			clickedButton(click);
		}
	}

	@Override
	public void addButton(Button button) {
		buttons.add(button);
	}

	@Override
	public List<Button> getButtons() {
		return buttons;
	}

	@Override
	public boolean clickedButton(Point click) {
		for (Button b : buttons) {
			if (b.isClicked(click)) {
				b.click();
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "War";
	}

	@Override
	public void moveComponents(Point vector) {

		squadSizes.setPosition(new Point(squadSizes.getPosition().x + vector.x, squadSizes.getPosition().y + vector.y));

	}

}
