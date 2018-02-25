package peril.views.slick.components.menus;

import peril.views.slick.io.ImageReader;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import peril.controllers.GameController;
import peril.helpers.PointHelper;
import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLinkState;
import peril.model.states.ModelState;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.board.SlickUnit;
import peril.views.slick.components.Component;
import peril.views.slick.components.VisualList;
import peril.views.slick.states.gameStates.CoreGameState;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Encapsulates the behaviour of a {@link Menu} that allows the user to upgrade
 * the selected {@link ModelCountry} and upgrade {@link ModelUnit}s for points.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 * 
 * @since 2018-02-25
 * @version 1.01.01
 *
 * @see Menu
 * @see UnitTrader
 */
public final class UpgradeMenu extends Menu {

	/**
	 * Holds the name of this {@link UpgradeMenu}.
	 */
	public static final String NAME = "Upgrade Menu";

	/**
	 * The width that the {@link Image} of the selected {@link ModelCountry} will be
	 * scaled to.
	 */
	private static final int COUNTRY_WIDTH = 200;

	/**
	 * The width of the {@link UpgradeMenu}.
	 */
	private static final int WIDTH = 800;

	/**
	 * The height of the {@link UpgradeMenu}.
	 */
	private static final int HEIGHT = 600;

	/**
	 * The {@link Font} for the points of the current {@link ModelPlayer}.
	 */
	private final Font pointFont;

	/**
	 * The {@link Font} that displays the name of the selected {@link ModelCountry}.
	 */
	private final Font countryFont;

	/**
	 * The {@link Font} that displays titles.
	 */
	private final Font titleFont;

	/**
	 * The string name of the block link {@link Button}.
	 */
	private final String blockButton;

	/**
	 * The {@link VisualList} that contains all of the neighbours of the currently
	 * selected {@link ModelCountry} that can have their links blocked.
	 */
	private final VisualList<ModelCountry> neighbours;

	/**
	 * The {@link EventListener} that defines the operations for when the
	 * {@link #neighbours} {@link VisualList} is added to the current {@link Frame}.
	 */
	private final EventListener neighbourListener;

	/**
	 * The {@link List} of {@link UnitTrader}s that allow the user to upgrade the
	 * {@link ModelArmy} of the selected {@link ModelCountry}.
	 */
	private final List<UnitTrader> traders;

	/**
	 * The {@link Font} that shows the cost of upgrading a {@link ModelUnit}.
	 */
	private final Font armyFont;

	/**
	 * The {@link Font} that displays the cost of purchases.
	 */
	private final Font costFont;

	/**
	 * The selected {@link ModelCountry} that is being upgraded.
	 */
	private ModelCountry selected;

	/**
	 * The {@link Image} of the {@link SlickPlayer}s icon.
	 */
	private Image player;

	/**
	 * The scaled {@link Image} of the {@link #selected} {@link ModelCountry}.
	 */
	private Image country;

	/**
	 * Whether the {@link ModelCountry} has some links that can be blocked.
	 */
	private boolean hasBlockableLinks;

	/**
	 * Constructs a new {@link UpgradeMenu}.
	 * 
	 * @param position
	 *            The {@link Point} position of the {@link UpgradeMenu}.
	 * @param game
	 *            The {@link GameController} the {@link UpgradeMenu} is associated
	 *            with.
	 */
	public UpgradeMenu(Point position, GameController game) {
		super(NAME, game, new Region(WIDTH, HEIGHT, position));

		this.pointFont = new Font("Arial", Color.black, 25);
		this.countryFont = new Font("Arial", Color.black, 30);
		this.titleFont = new Font("Arial", Color.black, 40);
		this.costFont = new Font("Arial", Color.black, 25);
		this.armyFont = new Font("Arial", Color.red, 50);
		this.hasBlockableLinks = false;
		this.player = null;
		this.blockButton = "block";

		final Font listFont = new Font("Arial", Color.black, 25);

		final int width = 200;
		final int elementHeight = 28;
		final int x = getPosition().x + ((getWidth() * 2) / 3) - (width / 2);
		final int y = getPosition().y + (getHeight() / 4);

		this.neighbours = new VisualList<>(new Point(x, y), width, elementHeight, 5, 5);
		this.neighbours.setFont(listFont);

		this.traders = new LinkedList<>();

		this.neighbourListener = new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {
				neighbours.click(mouse);
			}

			@Override
			public void draw(Frame frame) {
				neighbours.draw(frame);
			}

			@Override
			public void buttonPress(int key, Point mouse) {
				if (neighbours.isClicked(mouse)) {

					if (key == Input.KEY_UP) {
						neighbours.up();
					} else if (key == Input.KEY_DOWN) {
						neighbours.down();
					}
				}

			}
		};

	}

	/**
	 * Sets this {@link UpgradeMenu} a visible.
	 */
	@Override
	public void show() {
		super.show();

		if (slick.getCurrentState() instanceof CoreGameState) {

			final ModelState state = ((CoreGameState) slick.getCurrentState()).model;

			// Retrieve the selected model country.
			this.selected = state.getSelected(0);

			final SlickPlayer player = slick.modelView.getVisual(selected.getRuler());
			final Image country = slick.modelView.getVisual(selected).getRegion().convert(player.color);
			final float scaleFactor = (float) COUNTRY_WIDTH / country.getWidth();

			this.country = country.getScaledCopy(scaleFactor);
			this.player = slick.modelView.getVisual(selected.getRuler()).getImage();

			populatesUpgrades();

			populateTraders();

		}
	}

	/**
	 * Hides this {@link UpgradeMenu}.
	 */
	@Override
	public void hide() {
		super.hide();

		// If there is a selected country destroy the image
		if (country == null) {

			try {
				country.destroy();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

		country = null;
		selected = null;

	}

	/**
	 * Draws the {@link UpgradeMenu} on screen.
	 * 
	 * @param frame
	 *            The {@link Frame} that will draw the {@link UpgradeMenu} on
	 *            screen.
	 */
	public void draw(Frame frame) {

		super.draw(frame);

		drawCountry(frame);
		drawUpgrades(frame);
		drawPoints(frame);
		drawPlayer(frame);
		drawUnits(frame);

	}

	/**
	 * Initialises all the visual elements off {@link UpgradeMenu}.
	 */
	public void init() {

		pointFont.init();
		countryFont.init();
		titleFont.init();
		neighbours.init();
		costFont.init();
		armyFont.init();

	}

	/**
	 * Moves all the components in this {@link UpgradeMenu}.
	 */
	@Override
	public void moveComponents(Point vector) {

		final Point current = neighbours.getPosition();

		neighbours.setPosition(new Point(current.x + vector.x, current.y + vector.y));

	}

	/**
	 * Blocks the link between the selected {@link ModelCountry} and the currently
	 * selected neighbour {@link ModelCountry}.
	 */
	public void blockLink() {

		final ModelPlayer ruler = selected.getRuler();
		final int currentPoints = ruler.getPoints();

		// If the user has enough points trade up.
		if (currentPoints >= PointHelper.BLOCKADE_COST) {

			final ModelCountry neighbour = neighbours.getSelected();

			ruler.spendPoints(PointHelper.BLOCKADE_COST);

			neighbour.getLinkTo(selected).setState(ModelLinkState.BLOCKADE, 3);

			populatesUpgrades();

		} else {

			final String message = "You have in sufficient points.";

			slick.showToolTip(message, getButton(blockButton).getPosition());
		}

	}

	/**
	 * Creates the {@link List} of {@link UnitTrader}s that
	 */
	private void populateTraders() {

		// Empty the traders.
		if (!traders.isEmpty()) {
			traders.clear();
		}

		final ModelArmy army = selected.getArmy();
		final UnitHelper helper = UnitHelper.getInstance();
		final Point armyPos = getArmyPosition();
		final int interval = SlickUnit.WIDTH - 5;

		boolean isStrongest = true;
		ModelUnit current = helper.getStrongest();
		int x = armyPos.x + interval;
		int y = armyPos.y;

		// Iterate over the units until there is no more.
		while (current != null) {

			// If the army has the current unit
			if (army.hasUnit(current)) {

				// If the current unit is not the strongest unit in the game.
				if (!isStrongest) {
					traders.add(new UnitTrader(current, new Point(x, y)));
					x -= (interval * 2);
				}
			}

			// Move to the unit below.
			isStrongest = false;
			current = helper.getUnitBelow(current);
		}
	}

	/**
	 * Retrieves the {@link Point} position of the {@link #traders} on screen.
	 * 
	 * @return The {@link Point} position.
	 */
	private Point getArmyPosition() {

		final int x = getPosition().x + ((getWidth() * 3) / 4) - (SlickUnit.WIDTH / 2);
		final int y = getPosition().y + ((getHeight() * 4) / 5) - (SlickUnit.HEIGHT / 2) - 10;

		return new Point(x, y);
	}

	/**
	 * Populates the {@link #neighbours} {@link VisualList} with all the
	 * {@link ModelCountry} neighbours of the current {@link ModelCountry} that can
	 * have the links blocked.
	 */
	private void populatesUpgrades() {

		// Clear the list of neighbours
		neighbours.clear();
		hasBlockableLinks = false;

		// Iterate over all the neighbours of the currently selected country.
		for (ModelCountry neighbour : selected.getNeighbours()) {

			// Whether the link is currently open.
			final boolean hasOpenLink = neighbour.getLinkTo(selected).getState() == ModelLinkState.OPEN;

			// Whether it is an enemy country.
			final boolean isEnemyRuled = selected.getRuler() != neighbour.getRuler();

			// If the country is valid add it as a block-able neighbour.
			if (hasOpenLink && isEnemyRuled) {
				neighbours.add(neighbour.getName(), neighbour);
				hasBlockableLinks = true;
			}

		}

		// Set the first as the selected.
		if (hasBlockableLinks) {
			neighbours.setSelected(0);
			neighbours.init();
			getButton(blockButton).show();
		} else {
			getButton(blockButton).hide();
		}
	}

	/**
	 * Draws the {@link UnitTrader}s on screen.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link UpgradeMenu} on screen.
	 */
	private void drawUnits(Frame frame) {

		final Point armyPos = getArmyPosition();
		final ModelArmy army = selected.getArmy();
		final UnitHelper helper = UnitHelper.getInstance();
		final int interval = SlickUnit.WIDTH - 5;
		final int y = armyPos.y;

		int x = armyPos.x;
		int index = 0;
		ModelUnit current = helper.getStrongest();

		// Iterate over all the units.
		while (current != null) {

			// If the current unit is in the army.
			if (army.hasUnit(current)) {

				// Draw the unit icon
				final Image unit = slick.modelView.getVisual(current).getImage();
				frame.draw(unit, x, y);

				// Draw the cost to trade the unit.
				final String text = "[Cost: " + PointHelper.TRADE_UNIT_COST + "]";
				final int costX = x + (SlickUnit.WIDTH / 2) - (costFont.getHeight(text) / 2);
				frame.draw(costFont, text, costX, y - costFont.getHeight(text));

				// Draw the trader
				traders.get(index).draw(frame);

				// Draws the number of the current unit.
				final int numberOfCurrent = army.getNumberOf(current);
				final String number = Integer.toString(numberOfCurrent);
				final int fontX = x + (SlickUnit.WIDTH / 2) - (armyFont.getWidth(number) / 2);
				final int fontY = y + (SlickUnit.HEIGHT / 2) - (armyFont.getHeight() / 2);
				frame.draw(armyFont, number, fontX, fontY);

				index++;
				x -= (interval * 2);
			}

			// Move to the unit below.
			current = helper.getUnitBelow(current);
		}
	}

	/**
	 * Draws the current {@link ModelPlayer} on screen.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link UpgradeMenu} on screen.
	 */
	private void drawPlayer(Frame frame) {
		final int playerX = getPosition().x + (getWidth() / 4) - (player.getWidth() / 2);
		final int playerY = getPosition().y + (getHeight() / 2) - player.getHeight() - 10;
		frame.draw(player, playerX, playerY);
	}

	/**
	 * Draws the possible blockades for this current {@link ModelCountry}.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link UpgradeMenu} on screen.
	 */
	private void drawUpgrades(Frame frame) {

		final String block = "Blockade [Cost: " + PointHelper.BLOCKADE_COST + "]";
		final int x = neighbours.getPosition().x + (neighbours.getWidth() / 2) - (costFont.getWidth(block) / 2);
		final int y = neighbours.getPosition().y - costFont.getHeight(block) - 5;

		// If there are possible blockades.
		if (hasBlockableLinks) {

			frame.draw(costFont, block, x, y);
			frame.draw(neighbours, neighbourListener);
		} else {

			final String text = "No blockades available.";
			frame.draw(costFont, text, x, y);
		}
	}

	/**
	 * Draws the number of points the current {@link ModelPlayer} has.
	 * 
	 * @param frame
	 *            The {@link Frame} the displays the {@link UpgradeMenu} on screen.
	 */
	private void drawPoints(Frame frame) {

		final String points = Integer.toString(selected.getRuler().getPoints());
		final String text = "POINTS";

		final int x = getPosition().x + (getWidth() / 4);
		final int y = getPosition().y + (getHeight() / 4);

		frame.draw(pointFont, text, x - (pointFont.getWidth(text) / 2), y);
		frame.draw(titleFont, points, x - (titleFont.getWidth(points) / 2), y - titleFont.getHeight(points) - 5);
	}

	/**
	 * Draws the currently selected {@link ModelCountry} on screen.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link UpgradeMenu} on screen.
	 */
	private void drawCountry(Frame frame) {

		final int countryX = getPosition().x + (getWidth() / 4);
		final int countryY = getPosition().y + (getHeight() / 2);

		frame.draw(country, countryX - (country.getWidth() / 2), countryY - (country.getHeight() / 2));

		final String countryName = selected.getName();
		final int counrtyNameX = countryX - (countryFont.getWidth(countryName) / 2);
		final int countryNameY = countryY - (countryFont.getHeight(countryName) / 2);

		frame.draw(countryFont, countryName, counrtyNameX, countryNameY);
	}

	/**
	 * A object that when clicked upgrades the specified {@link ModelUnit} to the
	 * {@link ModelUnit} above it in terms of strength.
	 * 
	 * @author Joseph_Rolli, Joshua_Eddy
	 * 
	 * @since 2018-02-18
	 * @version 1.01.01
	 * 
	 * @see Component
	 * @see Clickable
	 *
	 */
	private final class UnitTrader implements Component {

		/**
		 * The file name of the icon used to trade a unit for the unit above.
		 */
		private static final String TRADE_ICON_NAME = "rightButton.png";

		/**
		 * The {@link Clickable} that when clicked attempts the trade the specified unit
		 * up.
		 */
		private final Clickable tradeIcon;

		/**
		 * The {@link EventListener} that handles the operations for this
		 * {@link UnitTrader}.
		 */
		private final EventListener listener;

		/**
		 * Constructs a new {@link UnitTrader}.
		 * 
		 * @param unit
		 *            The {@link ModelUnit} this {@link UnitTrader} will trade up.
		 * @param position
		 *            The position of this {@link UnitTrader}.
		 */
		public UnitTrader(ModelUnit unit, Point position) {

			final Image temp = ImageReader.getImage(game.getDirectory().getButtonsPath() + TRADE_ICON_NAME);

			this.tradeIcon = new Clickable(temp.getScaledCopy(SlickUnit.WIDTH, SlickUnit.HEIGHT));
			this.tradeIcon.setPosition(position);

			this.listener = new EventListener() {

				@Override
				public void mouseHover(Point mouse, int delta) {
					// Do nothing
				}

				@Override
				public void mouseClick(Point mouse, int mouseButton) {

					final ModelArmy army = selected.getArmy();
					final ModelPlayer ruler = selected.getRuler();
					final int currentPoints = ruler.getPoints();

					// If the user has enough points trade up.
					if (currentPoints >= PointHelper.TRADE_UNIT_COST) {

						// If the unit was successfully traded up.
						if (army.tradeUp(unit)) {
							ruler.spendPoints(PointHelper.TRADE_UNIT_COST);
							populateTraders();
						} else {

							// Display the ratio of units.
							final ModelUnit above = UnitHelper.getInstance().getUnitAbove(unit);
							final int aboveStrength = above.strength;
							final int unitStrength = unit.strength;
							final int ratio = aboveStrength / unitStrength;
							final String message = ratio + " " + unit.name + "(s) required.";

							slick.showToolTip(message, tradeIcon.getPosition());

						}

					} else {

						final String message = "You have in sufficient points.";

						slick.showToolTip(message, tradeIcon.getPosition());
					}

				}

				@Override
				public void draw(Frame frame) {
					frame.draw(tradeIcon.getImage(), tradeIcon.getPosition().x, tradeIcon.getPosition().y);
				}

				@Override
				public void buttonPress(int key, Point mouse) {
					// Do nothing
				}
			};
		}

		/**
		 * Draws this {@link UnitTrader} on screen.
		 */
		@Override
		public void draw(Frame frame) {
			frame.draw(tradeIcon, listener);
		}

		/**
		 * Initialises this {@link UnitTrader}.
		 */
		@Override
		public void init() {
			// Do nothing
		}

		/**
		 * Sets the {@link Point} position of this {@link UnitTrader}.
		 */
		@Override
		public void setPosition(Point position) {
			tradeIcon.setPosition(position);

		}

		/**
		 * Retrieves the {@link Point} of the {@link UnitTrader}.
		 */
		@Override
		public Point getPosition() {
			return tradeIcon.getPosition();
		}

	}

}
