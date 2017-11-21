package peril.ui.components.menus;

import java.util.Arrays;
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

	private final static String NAME = "War Menu";

	/**
	 * {@link Random} object for the random number generator
	 * 
	 */
	private Random random;

	private VisualList<Integer> squadSizes;

	private Button attackButton;
	
	private Font headingFont;

	private Font textFont;

	private Font countryFont;

	private Font playerFont;

	private Font resultFont;

	private Country attacker;

	private Country enemy;

	/**
	 * 
	 * 
	 */
	private Player player;

	/**
	 * 
	 * 
	 */
	private Player ruler;

	/**
	 * Constructs a new {@link WarMenu}.
	 * 
	 */
	public WarMenu(Point position, Game game) {
		super("War", game, new Region(300, 300, position));

		random = new Random();

		squadSizes = new VisualList<>(position.x + (getWidth() / 2), position.y + 200, 20, 20, 3, 5);
		squadSizes.add("1", 1);
		squadSizes.add("2", 2);
		squadSizes.add("3", 3);
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
	private void fight(Country attacking, Country defending, int atkSquadSize) {

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

				// If the army of the defending country is of size on then this victory will
				// conquer the country. Otherwise just kill one unit from the defending army.
				if (defendingArmy.getSize() == 1) {
					defending.setRuler(attacker);
					attacker.setTotalArmySize(attacker.getTotalArmySize() + 1);
				} else {
					defendingArmy.setSize(defendingArmy.getSize() - 1);
				}

				if (defender != null) {
					defender.setTotalArmySize(defender.getTotalArmySize() - 1);
				}

			}
			// Attacker has lost the attack
			else {
				attackingArmy.setSize(attackingArmy.getSize() - 1);
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
		headingFont = new Font("Arial", Color.red, 20);
		textFont = new Font("Arial", Color.red, 40);
		countryFont = new Font("Arial", Color.cyan, 20);
		playerFont = new Font("Arial", Color.black, 15);
		resultFont = new Font("Arial", Color.white, 20);
		squadSizes.setFont(headingFont);
	}

	public void draw(Graphics g) {
		
		if (isVisible()) {
			g.setColor(Color.black);
			super.draw(g);

			// Attacker has failed to conquer country
			if (attacker.getArmy().getSize() == 1) {
				failedConquer(g);
			}
			// Attacker has conquered country
			else if (attacker.getRuler().equals(enemy.getRuler())) {
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
	
	@Override
	public void addButton(Button button) {
		super.addButton(button);
		if(button.getId().equals("war")) {
			attackButton = button;
		}
	}

	@Override
	public void show() {
		super.show();

		attackButton.show();
		attacker = getGame().states.combat.getHighlightedCountry();
		enemy = getGame().states.combat.getEnemyCountry();

		ruler = enemy.getRuler();
		player = attacker.getRuler();
	}

	public void parseClick(Point click) {
		if (!squadSizes.click(click)) {
			clickedButton(click);
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	public void attack() {
		// If there is two countries highlighted
		if (attacker != null && enemy != null) {

			Player attackingPlayer = attacker.getRuler();
			Player defendingPlayer = enemy.getRuler();

			// If the army of the primary highlighted country is larger that 1 unit in size
			if (attacker.getArmy().getSize() > 1) {

				// Execute the combat
				fight(attacker, enemy, 1);

				// If the country has been conquered
				if (attacker.getRuler().equals(enemy.getRuler())) {

					// If there is a defending player
					if (defendingPlayer != null) {

						defendingPlayer.setCountriesRuled(defendingPlayer.getCountriesRuled() - 1);

						// If the player has no countries they have lost.
						if (defendingPlayer.getCountriesRuled() == 0) {
							getGame().players.setLoser(defendingPlayer);
							getGame().checkWinner();
						}
					}

					attackingPlayer.setCountriesRuled(attackingPlayer.getCountriesRuled() + 1);

					getGame().states.combat.setPostCombat();
					getGame().states.combat.highlightCountry(enemy);

					getGame().checkContinentRulership();

					getGame().players.checkChallenges(attackingPlayer);

				} else {

					// If the attacking army is not large enough to attack again.
					if (attacker.getArmy().getSize() == 1) {
						getGame().states.combat.hideAttackButton();
					}
				}
			} else {
				getGame().states.combat.hideAttackButton();
			}
		} else {
			getGame().states.combat.hideAttackButton();
		}
	}
	
	@Override
	public void moveComponents(Point vector) {

		squadSizes.setPosition(new Point(squadSizes.getPosition().x + vector.x, squadSizes.getPosition().y + vector.y));

	}

	private void failedConquer(Graphics g) {
		String failure = "has insufficient units to attack.";
		
		countryFont.draw(g, attacker.getName(),
				getPosition().x + (getWidth() / 2) - (resultFont.getWidth(attacker.getName()) / 2),
				getPosition().y + (getHeight() / 2) - 30);
		
		resultFont.draw(g, failure, getPosition().x + (getWidth() / 2) - (resultFont.getWidth(failure) / 2),
				getPosition().y + (getHeight() / 2));
		
		drawPlayer(g, ruler, (getWidth() / 4));
		attackButton.hide();
	}
	
	private void succesfulConquer(Graphics g) {
		String success = "has conquered";
		countryFont.draw(g, attacker.getName(),
				getPosition().x + (getWidth() / 2) - (resultFont.getWidth(attacker.getName()) / 2),
				getPosition().y + (getHeight() / 2) - 30);
		
		resultFont.draw(g, success, getPosition().x + (getWidth() / 2) - (resultFont.getWidth(success) / 2),
				getPosition().y + (getHeight() / 2));
		
		countryFont.draw(g, enemy.getName(),
				getPosition().x + (getWidth() / 2) - (resultFont.getWidth(enemy.getName()) / 2),
				getPosition().y + (getHeight() / 2) + 30);
		
		drawPlayer(g, player, (getWidth() / 4));
		attackButton.hide();
	}
	
	private void normalCombat(Graphics g) {
		squadSizes.draw(g);
		drawPlayer(g, ruler, (getWidth() / 4));
		String squadSize = "Attack squad size: ";
		playerFont.setColor(Color.white);
		playerFont.draw(g, squadSize, getPosition().x + (getWidth()/2) - playerFont.getWidth(squadSize), getPosition().y + 200);
	}
	
	private void drawArmySizes(Graphics g) {

		String attackingArmy = "" + attacker.getArmy().getSize();

		textFont.draw(g, attackingArmy, getPosition().x + (getWidth() / 4) - (textFont.getWidth(attackingArmy) / 2),
				getPosition().y + 60);

		String enemyArmy = "" + enemy.getArmy().getSize();
		textFont.draw(g, enemyArmy, getPosition().x + ((getWidth() * 3) / 4) - (textFont.getWidth(enemyArmy) / 2),
				getPosition().y + 60);

	}

	private void drawTitle(Graphics g) {
		String vs = "VS";
		String attackerStr = attacker.getName();
		String enemyStr = enemy.getName();

		int centreX = getPosition().x + (getWidth() / 2);
		int vsX = centreX - (headingFont.getWidth(vs) / 2);

		int attackerX = centreX - (getWidth() / 4) - (countryFont.getWidth(attackerStr) / 2);
		int enemyX = centreX + (getWidth() / 4) - (countryFont.getWidth(enemyStr) / 2);

		headingFont.draw(g, vs, vsX, getPosition().y + 20);
		countryFont.draw(g, attackerStr, attackerX, getPosition().y + 20);
		countryFont.draw(g, enemyStr, enemyX, getPosition().y + 20);
	}

	private void drawPlayer(Graphics g, Player player, int offset) {
		int centreX = getPosition().x + (getWidth() / 2);
		int x = centreX + offset - (playerFont.getWidth(player.toString()) / 2);
		playerFont.setColor(player.getColor());
		playerFont.draw(g, player.toString(), x, getPosition().y + 40);
	}

}
