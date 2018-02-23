package peril.views.slick.components.menus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import peril.concurrent.Action;
import peril.controllers.GameController;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.model.ModelPlayer;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.board.SlickUnit;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.states.combat.Attack;
import peril.model.states.combat.CombatHelper;
import peril.model.states.combat.CombatRound;
import peril.model.states.combat.ModelSquad;
import peril.model.states.combat.ModelSquadMember;
import peril.views.slick.io.ImageReader;
import peril.views.slick.util.Button;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Displays the {@link CombatHelper} from the {@link Attack} to the user.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 * 
 * @since 2018-02-23
 * @version 1.01.01
 * 
 * @see Menu
 * @see Attack
 *
 */
public final class WarMenu extends Menu {

	/**
	 * The name of this {@link Menu}.
	 */
	public final static String NAME = "War Menu";

	/**
	 * The {@link Attack} that contains the {@link CombatHelper}.
	 */
	private final Attack state;

	/**
	 * The {@link Dice} displaying dice on screen.
	 */
	private final Dice dice;

	/**
	 * The {@link Font} used to draw the number of each type of {@link SlickUnit}.
	 */
	private final Font armyFont;

	/**
	 * The {@link Font} for the text to display the {@link ModelArmy}s sizes.
	 */
	private final Font textFont;

	/**
	 * The {@link Font} for the text to display the results of the attack on a;
	 * neighbouring {@link ModelCountry}.
	 * 
	 */
	private final Font resultFont;

	/**
	 * The {@link Font} for the text of the heading.
	 */
	private final Font headingFont;

	/**
	 * The {@link Font} for the text to display the name of the
	 * {@link ModelCountry}s.
	 */
	private final Font countryFont;

	/**
	 * The {@link Font} used to display the title of the player's army.
	 */
	private final Font yourArmyFont;

	/**
	 * The id of the {@link Button} that will be clicked to attack enemy
	 * {@link ModelCountry} .
	 * 
	 */
	private final String attackButton;

	/**
	 * The {@link Consumer} that defines the operation to perform on a specified
	 * {@link SlickUnit} when it is clicked in the pool of select-able
	 * {@link ModelUnit}.
	 */
	private final Consumer<SlickUnit> poolClick;

	/**
	 * The {@link SlickSquad} that contains the {@link SlickSquadMember} that will
	 * attack the {@link #defendingSquad}.
	 */
	private final SlickSquad attackingSquad;

	/**
	 * The {@link SlickSquad} that contains the {@link SlickSquadMember}s that will
	 * defend against the attack from the {@link #attackingSquad}.
	 */
	private final SlickSquad defendingSquad;

	/**
	 * Constructs a new {@link WarMenu}.
	 * 
	 * @param position
	 *            The initial {@link Point} position of the {@link WarMenu}.
	 * @param game
	 *            The {@link GameController} that allows this {@link WarMenu} to
	 *            query the state of the game.
	 */
	public WarMenu(Point position, GameController game) {
		super(NAME, game, new Region(800, 600, position));

		this.state = game.getAttack();
		this.dice = new Dice();
		this.attackButton = "war";

		// Define the fonts used in the war menu
		this.armyFont = new Font("Arial", Color.red, 50);
		this.textFont = new Font("Arial", Color.red, 40);
		this.resultFont = new Font("Arial", Color.black, 30);
		this.countryFont = new Font("Arial", Color.black, 37);
		this.headingFont = new Font("Arial", Color.red, 30);
		this.yourArmyFont = new Font("Arial", Color.black, 25);

		// Define the point whether the attack squad will be displayed.
		final int attackX = this.getPosition().x + (this.getWidth() / 4) - SlickUnit.WIDTH;
		final int attackY = this.getPosition().y + 250;
		final Point attackTop = new Point(attackX, attackY);

		// Define the point whether the defend squad will be displayed.
		final int defendX = this.getPosition().x + ((this.getWidth() * 3) / 4);
		final int defendY = this.getPosition().y + 250;
		final Point defendTop = new Point(defendX, defendY);

		// Define the squads
		this.attackingSquad = new SlickSquad(attackTop, true, new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE));
		this.defendingSquad = new SlickSquad(defendTop, false, new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE));

		this.poolClick = new Consumer<SlickUnit>() {

			@Override
			public void accept(SlickUnit unit) {

				// If there is more that one unit in the attacking army
				if (state.getPrimary().getArmy().getNumberOfUnits() > 1) {
					attackingSquad.model.moveToSquad(unit.model, state.getPrimary().getArmy());
					getButton(attackButton).show();
				}
			}
		};

	}

	/**
	 * Initialises all the visual elements of {@link WarMenu}.
	 */
	@Override
	public void init() {

		dice.init();
		textFont.init();
		armyFont.init();
		resultFont.init();
		headingFont.init();
		countryFont.init();
		yourArmyFont.init();

	}

	/**
	 * Draws the {@link War Menu} on the screen.
	 */
	@Override
	public void draw(Frame frame) {

		super.draw(frame);

		if (!isVisible()) {
			return;
		}

		final int size = attackingSquad.model.size() + state.getPrimary().getArmy().getNumberOfUnits();

		// Attacker has failed to conquer country
		if (size == 1) {
			failedConquer(frame);
		}
		// Attacker has conquered country
		else if (state.getPrimary().getRuler().equals(state.getSecondary().getRuler())) {
			succesfulConquer(frame);
		}
		// Normal Combat
		else {
			drawNormalCombat(frame);
		}

		slick.showToolTip("Click the units below, to select the number of attacking units");

	}

	/**
	 * Makes this {@link WarMenu} visible along with its attack {@link Button}.
	 */
	@Override
	public void show() {
		super.show();

		getButton(attackButton).hide();
		defendingSquad.autoPopulate(state.getSecondary().getArmy());

	}

	/**
	 * Hides this {@link WarMenu}.
	 */
	@Override
	public void hide() {
		super.hide();

		if (state.getPrimary() != null) {
			attackingSquad.model.returnSquadToArmy(state.getPrimary().getArmy());

		}
		if (state.getSecondary() != null) {
			defendingSquad.model.returnSquadToArmy(state.getSecondary().getArmy());
		}

		dice.clear();

		getButton(attackButton).hide();

	}

	/**
	 * Causes the {@link CombatHelper} to process a {@link CombatRound} with the
	 * {@link ModelSquad}s currently in the {@link WarMenu}.
	 */
	public void attack() {

		// If there is two countries highlighted
		if (state.numberOfSelected() == 2) {

			final ModelCountry attacker = state.getPrimary();
			final ModelCountry defender = state.getSecondary();
			final CombatRound round = new CombatRound(attacker, defender, attackingSquad.model, defendingSquad.model);

			// If the army of the attacker country is larger that 1 unit in size
			if (state.combat.getTotalAliveUnits(attacker.getArmy(), attackingSquad.model) > 1) {

				// Remove the dead attacking units.
				attackingSquad.model.removeDeadUnits();

				defendingSquad.autoPopulate(defender.getArmy());

				// Execute the combat
				state.combat.fight(round);

				// The position of the top attacker dice
				final int attackX = getPosition().x + (getWidth() / 4);

				// The position of the top defender dice
				final int defendX = getPosition().x + ((getWidth() * 3) / 4);

				final int y = getPosition().y + 250;

				// Display the dice that we rolled
				dice.set(state.combat.view.attackerDiceRolls, state.combat.view.defenderDiceRolls,
						new Point(attackX, y), new Point(defendX, y));

				if (state.combat.getTotalAliveUnits(attacker.getArmy(), attackingSquad.model) > 1) {
					getButton(attackButton).show();
				} else {
					getButton(attackButton).hide();
				}

			}
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
	 * Clears the elements of and hides the {@link WarMenu}.
	 */
	public void clear() {

		this.hide();

		attackingSquad.model.clear();
		defendingSquad.model.clear();

	}

	/**
	 * Selects the {@link ModelUnit}s to be {@link SlickSquadMember}s of the
	 * {@link #attackingSquad} that will result in the strongest possible
	 * {@link #attackingSquad}.
	 */
	public void selectMaxUnits() {

		final ModelArmy army = state.getPrimary().getArmy();

		attackingSquad.autoPopulate(army);

	}

	/**
	 * Draws the normal state of combat where both countries are valid for combat.
	 * 
	 * @param frame
	 *            The {@link Frame} that will display this normal combat state.
	 */
	private void drawNormalCombat(Frame frame) {

		attackingSquad.draw(frame);

		defendingSquad.draw(frame);

		drawPlayer(state.getSecondary().getRuler(), (getWidth() / 4), frame);

		drawPlayer(state.getPrimary().getRuler(), -(getWidth() / 4), frame);

		drawTitle(frame);

		drawArmyStrengths(frame);

		dice.draw(frame);

		drawArmyPool(frame, new Point(getPosition().x + (getWidth() / 2), getPosition().y + getHeight() - 120));
	}

	/**
	 * Draws the attackers army so that they can select the units to add to the
	 * attacking squad.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link WarMenu}.
	 * @param position
	 *            The position of the army pool.
	 */
	private void drawArmyPool(Frame frame, Point position) {

		final ModelArmy model = state.getPrimary().getArmy();

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
	 * Draws the {@link WarMenu} where the attacking country has failed to conquer
	 * the other country.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link WarMenu}.
	 */
	private void failedConquer(Frame frame) {
		String failure = "has insufficient units to attack.";

		frame.draw(countryFont, state.getPrimary().getName(),
				getPosition().x + (getWidth() / 2) - (countryFont.getWidth(state.getPrimary().getName()) / 2),
				getPosition().y + (getHeight() / 2) - 45);

		frame.draw(resultFont, failure, getPosition().x + (getWidth() / 2) - (resultFont.getWidth(failure) / 2),
				getPosition().y + (getHeight() / 2));

		getButton(attackButton).hide();

		drawPlayer(state.getPrimary().getRuler(), -(getWidth() / 4), frame);
		drawPlayer(state.getSecondary().getRuler(), (getWidth() / 4), frame);

		drawTitle(frame);

	}

	/**
	 * Draws the {@link WarMenu} where the attacking country has conquered the other
	 * country.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link WarMenu}.
	 */
	private void succesfulConquer(Frame frame) {

		// Draw the result text.
		final String success = "has conquered";
		final int resultX = getPosition().x + (getWidth() / 2) - (resultFont.getWidth(success) / 2);
		final int resultY = getPosition().y + (getHeight() / 2);
		frame.draw(resultFont, success, resultX, resultY);

		// Draw the attacking country's name
		final String attackerName = state.getPrimary().getName();
		final int attackerX = getPosition().x + (getWidth() / 2) - (countryFont.getWidth(attackerName) / 2);
		final int attackerY = getPosition().y + (getHeight() / 2) - ((countryFont.getHeight(attackerName) * 3) / 2);
		frame.draw(countryFont, attackerName, attackerX, attackerY);

		// Draw the defending country's name
		final String conqueredName = state.getSecondary().getName();
		final int conqueredX = getPosition().x + (getWidth() / 2) - (countryFont.getWidth(conqueredName) / 2);
		final int conqueredY = getPosition().y + (getHeight() / 2) + ((countryFont.getHeight(attackerName) * 3) / 2);
		frame.draw(countryFont, conqueredName, conqueredX, conqueredY);

		// Draw the player icons
		drawPlayer(state.getPrimary().getRuler(), -(getWidth() / 4), frame);
		drawPlayer(state.getSecondary().getRuler(), (getWidth() / 4), frame);

		// Draw the country titles
		drawTitle(frame);

	}

	/**
	 * Draws the army strength for each {@link ModelArmy} belonging to the attacking
	 * and defending {@link ModelCountry}s.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link WarMenu}.
	 */
	private void drawArmyStrengths(Frame frame) {

		final int yOffset = 190;

		// The strengths of both armies
		final int attack = state.getPrimary().getArmy().getStrength() + attackingSquad.model.geStrength();
		final int defend = state.getSecondary().getArmy().getStrength() + defendingSquad.model.geStrength();

		// The strengths as strings
		final String attackStr = Integer.toString(attack);
		final String defendStr = Integer.toString(defend);

		// Draw the attack strength
		final int attackX = getPosition().x + (getWidth() / 4) - (textFont.getWidth(attackStr) / 2);
		final int attackY = getPosition().y + yOffset;
		frame.draw(textFont, attackStr, attackX, attackY);

		// Draw the defend strength
		final int defendX = getPosition().x + ((getWidth() * 3) / 4) - (textFont.getWidth(defendStr) / 2);
		final int defendY = getPosition().y + yOffset;
		frame.draw(textFont, defendStr, defendX, defendY);

	}

	/**
	 * Draws the names of the {@link ModelPlayer}s whose {@link ModelCountry}s are
	 * attacking and defending.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link WarMenu}.
	 */
	private void drawTitle(Frame frame) {

		final int y = getPosition().y + 150;

		final String vs = "VS";
		final String attackerStr = state.getPrimary().getName();
		final String enemyStr = state.getSecondary().getName();

		// The centre of the menu
		final int centreX = getPosition().x + (getWidth() / 2);

		final int vsX = centreX - (headingFont.getWidth(vs) / 2);
		final int attackerX = centreX - (getWidth() / 4) - (countryFont.getWidth(attackerStr) / 2);
		final int enemyX = centreX + (getWidth() / 4) - (countryFont.getWidth(enemyStr) / 2);

		frame.draw(headingFont, vs, vsX, y);
		frame.draw(countryFont, attackerStr, attackerX, y);
		frame.draw(countryFont, enemyStr, enemyX, y);

	}

	/**
	 * Draws the visual of a {@link ModelPlayer}.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link WarMenu}.
	 * @param player
	 *            The {@link ModelPlayer} you want to display
	 * @param offset
	 *            The string offset
	 */
	private void drawPlayer(ModelPlayer player, int offset, Frame frame) {

		if (player == null) {
			return;
		}

		final int centreX = getPosition().x + (getWidth() / 2);

		final SlickPlayer slickPlayer = slick.modelView.getVisual(player);

		final int x = centreX + offset - (slickPlayer.getWidth() / 2);
		final int y = this.getPosition().y + 80;
		frame.draw(slickPlayer.getImage(), x, y);
	}

	/**
	 * Handles displaying the dice on the screen.
	 * 
	 * @author Joshua_Eddy
	 *
	 * @since 2018-02-23
	 * @version 1.01.01
	 *
	 * @see Image
	 *
	 */
	private final class Dice {

		/**
		 * The width of a dice.
		 */
		private static final int WIDTH = SlickUnit.WIDTH - 20;
		/**
		 * The height of a dice
		 */
		private static final int HEIGHT = SlickUnit.HEIGHT - 20;

		/**
		 * The width of the box that displays which dice wins.
		 */
		private static final int BOX_WIDTH = 10;

		/**
		 * Holds the dice that will be displayed on screen.
		 */
		private final Map<Point, Image> display;

		/**
		 * Holds the default set of dice images.
		 */
		private final Map<Integer, Image> defaultDice;

		/**
		 * The {@link Image} of a red box.
		 */
		private final Image redBox;

		/**
		 * The {@link Image} of a green box.
		 */
		private final Image greenBox;

		/**
		 * Constructs a new {@link Dice}.
		 */
		public Dice() {
			this.defaultDice = new HashMap<>();
			this.display = new HashMap<>();

			final Region box = new Region(BOX_WIDTH, HEIGHT, new Point(0, 0));

			this.redBox = box.convert(Color.red, 255);
			this.greenBox = box.convert(Color.green, 255);
		}

		/**
		 * Adds dice to {@link WarMenu} with there designated {@link Point} position.
		 * 
		 * @param attackerDiceRolls
		 *            The values of the dice to be displayed.
		 * @param defenderDiceRolls
		 *            The values of the dice to be displayed.
		 * @param attackTop
		 *            The {@link Point} of the top dice for the attacker.
		 * @param defendTop
		 *            The {@link Point} of the top dice for the defender.
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

			// Get the size of the smaller set of dice.
			final int diceToCheck = attackerDiceRolls.length >= defenderDiceRolls.length ? defenderDiceRolls.length
					: attackerDiceRolls.length;

			// Compare each attacking dice roll against the defending dice roll
			for (int i = 0; i < diceToCheck; i++) {

				final boolean attackerWon = attackerDiceRolls[i] > defenderDiceRolls[i];

				// Display defender box
				final Point defend = new Point(defendTop.x - WIDTH - BOX_WIDTH - 3,
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
		 * 
		 * @param frame
		 *            The {@link Frame} that displays the {@link WarMenu}.
		 */
		public void draw(Frame frame) {
			display.forEach((position, dice) -> frame.draw(dice, position.x, position.y));
		}

		/**
		 * Clears the dice in this {@link Dice}
		 */
		public void clear() {
			if (!display.isEmpty()) {
				display.clear();
			}
		}

	}

	/**
	 * Displays a {@link ModelSquad} to the user.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @since 2018-02-23
	 * @version 1.01.01
	 * 
	 * @see Observer
	 * @see SlickSquadMember
	 *
	 */
	private final class SlickSquad implements Observer {

		/**
		 * Whether of not this {@link SlickSquad} is the user's.
		 */
		private final boolean isUser;

		/**
		 * The {@link ModelSquad} this {@link SlickSquad} displays.
		 */
		private final ModelSquad model;

		/**
		 * The {@link List} of {@link SlickSquadMember}s that make up this
		 * {@link SlickSquad}.
		 */
		private final List<SlickSquadMember> members;

		/**
		 * The position of this {@link SlickSquad}.
		 */
		private Point position;

		/**
		 * Constructs a new {@link SlickSquad}.
		 * 
		 * @param position
		 *            The position of this {@link SlickSquad}.
		 * @param isUser
		 *            Whether of not this {@link SlickSquad} is the user's.
		 * @param model
		 *            The {@link ModelSquad} this {@link SlickSquad} displays.
		 */
		public SlickSquad(Point position, boolean isUser, ModelSquad model) {
			this.position = position;
			this.isUser = isUser;
			this.model = model;
			this.model.addObserver(this);
			this.members = new LinkedList<>();
		}

		/**
		 * Draws this {@link SlickSquad} on the specified {@link Frame}.
		 * 
		 * @param frame
		 *            The {@link Frame} that displays the {@link SlickSquad} to the
		 *            user.
		 */
		public void draw(Frame frame) {

			int x = position.x;
			int y = position.y;

			// Draw each member in the squad
			for (SlickSquadMember member : members) {

				// Draw the unit
				frame.draw(member, new EventListener() {

					@Override
					public void mouseHover(Point mouse, int delta) {
						// Do nothing
					}

					@Override
					public void mouseClick(Point mouse, int mouseButton) {

						if (isUser) {
							attackingSquad.model.returnUnitToArmy(state.getPrimary().getArmy(), member.model);

							// If all the alive units have been removed hide the attack button and clear the
							// dice.
							if (attackingSquad.model.getAliveUnits() == 0) {
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
				if (!member.model.isAlive) {

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
		 * Moves this {@link SlickSquad} by the specified {@link Point} vector.
		 * 
		 * @param vector
		 *            The {@link Point} vector.
		 */
		public void move(Point vector) {

			final int x = position.x + vector.x;
			final int y = position.y + vector.y;

			this.position = new Point(x, y);

			repositionUnits();
		}

		/**
		 * Performs {@link ModelSquad#autoPopulate(ModelArmy, int)} distinguishing
		 * between the minimum army size based on whether this {@link SlickSquad} is the
		 * user's or not.
		 * 
		 * @param army
		 *            The source {@link ModelArmy}.
		 */
		public void autoPopulate(ModelArmy army) {
			model.autoPopulate(army, isUser ? 1 : 0);
		}

		/**
		 * Changes the positions of all the {@link SlickSquadMember}s in this
		 * {@link SlickSquad} based on {@link #position}.
		 */
		private void repositionUnits() {

			// Reposition all the alive units.
			int index = 0;
			for (SlickSquadMember member : members) {
				member.setPosition(new Point(position.x, position.y + (index * SlickUnit.HEIGHT)));
				index++;
			}

		}

		/**
		 * Updates this {@link SlickSquad}.
		 */
		@Override
		public void update(Observable o, Object arg) {
			
			if (!members.isEmpty()) {
				members.clear();
			}

			model.forEach(member -> members.add(new SlickSquadMember(member)));

			repositionUnits();
		}

	}

	/**
	 * 
	 * Displays a {@link ModelSquadMember} to the user.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @since 2018-02-23
	 * @version 1.01.01
	 * 
	 * @see Clickable
	 * @see ModelSquadMember
	 *
	 */
	private final class SlickSquadMember extends Clickable {

		/**
		 * The {@link ModelSquadMember} this {@link SlickSquadMember} displays.
		 */
		public final ModelSquadMember model;

		/**
		 * Constructs a new {@link SlickSquadMember}.
		 * 
		 * @param model
		 *            The {@link ModelSquadMember} this {@link SlickSquadMember}
		 *            displays.
		 */
		public SlickSquadMember(ModelSquadMember model) {
			this.model = model;

			// Set this members image as the units image.
			replaceImage(slick.modelView.getVisual(model.unit).getImage());
		}

	}

}
