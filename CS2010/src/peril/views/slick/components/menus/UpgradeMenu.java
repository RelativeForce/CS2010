package peril.views.slick.components.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import peril.Game;
import peril.controllers.GameController;
import peril.model.board.ModelCountry;
import peril.model.board.links.ModelLinkState;
import peril.model.states.ModelState;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.TextField;
import peril.views.slick.components.VisualList;
import peril.views.slick.states.gameStates.CoreGameState;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Encapsulates the behaviour of a Pause Menu
 * 
 * @author Joseph Rolli
 *
 */
public class UpgradeMenu extends Menu {

	/**
	 * Holds the name of this {@link UpgradeMenu}.
	 */
	public final static String NAME = "Upgrade Menu";

	private static final int COUNTRY_WIDTH = 200;

	private static final int WIDTH = 800;

	private static final int HEIGHT = 600;

	private static final int BLOCK_LINK_COST = 4;

	/**
	 * The padding in the horizontal direction between the edge of the
	 * {@link HelpMenu} and the edge of the {@link TextField}.
	 */
	private static final int PADDING_X = WIDTH / 12;

	/**
	 * The padding in the vertical direction between the edge of the
	 * {@link HelpMenu} and the edge of the {@link TextField}.
	 */
	private static final int PADDING_Y = HEIGHT / 9;

	/**
	 * The {@link Font} for the text of the text of the {@link UpgradeMenu}.
	 */
	private final Font pointFont;

	private final Font countryFont;

	private final Font titleFont;

	private final VisualList<ModelCountry> neighbours;

	private final EventListener neighbourListener;

	private ModelCountry selected;

	private Image player;

	private Image country;

	private boolean hasUpgrades;

	private Font blockFont;

	/**
	 * Constructs a new {@link UpgradeMenu}.
	 * 
	 * @param position
	 *            {@link Point} position of the {@link UpgradeMenu}.
	 * @param game
	 *            The {@link Game} the {@link UpgradeMenu} is associated with.
	 */
	public UpgradeMenu(Point position, GameController game) {
		super(NAME, game, new Region(800, 600, position));

		this.pointFont = new Font("Arial", Color.black, 25);
		this.countryFont = new Font("Arial", Color.black, 30);
		this.titleFont = new Font("Arial", Color.black, 40);
		this.blockFont = new Font("Arial", Color.black, 25);
		this.hasUpgrades = false;
		this.player = null;

		final Font listFont = new Font("Arial", Color.black, 25);

		final int width = 200;
		final int elementHeight = 28;
		final int x = getPosition().x + ((getWidth() * 2) / 3) - (width / 2);
		final int y = getPosition().y + (getHeight() / 4);

		neighbours = new VisualList<>(new Point(x, y), width, elementHeight, 5, 5);
		neighbours.setFont(listFont);

		neighbourListener = new EventListener() {

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

			this.selected = state.getSelected(0);

			final SlickPlayer player = slick.modelView.getVisual(selected.getRuler());
			final Image country = slick.modelView.getVisual(selected).getRegion().convert(player.color);
			final float scaleFactor = (float) COUNTRY_WIDTH / country.getWidth();

			this.country = country.getScaledCopy(scaleFactor);

			this.player = slick.modelView.getVisual(selected.getRuler()).getImage();

			populatesUpgrades();

		}

	}

	private void populatesUpgrades() {

		neighbours.clear();

		int validUpgrades = 0;

		for (ModelCountry neighbour : selected.getNeighbours()) {

			final boolean hasOpenLink = neighbour.getLinkTo(selected).getState() == ModelLinkState.OPEN;
			final boolean isEnemyRuled = selected.getRuler() != neighbour.getRuler();

			final boolean isValid = hasOpenLink && isEnemyRuled;

			if (isValid) {
				neighbours.add(neighbour.getName(), neighbour);
				validUpgrades++;
			}

		}

		// Set the first as the selected.
		if (validUpgrades > 0) {
			neighbours.setSelected(0);
			hasUpgrades = true;
			neighbours.init();
		}
	}

	@Override
	public void hide() {
		super.hide();

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
	 *            {@link Graphics}
	 */
	public void draw(Frame frame) {

		super.draw(frame);

		drawBackground(frame);

		drawTitle(frame);

		drawCountry(frame);

		drawUpgrades(frame);

		drawPoints(frame);

		drawPlayer(frame);

	}

	private void drawPlayer(Frame frame) {
		final int playerX = getPosition().x + (getWidth() / 4) - (player.getWidth() / 2);
		final int playerY = getPosition().y + (getHeight() / 2) - player.getHeight() - 10;
		frame.draw(player, playerX, playerY);
	}

	private void drawUpgrades(Frame frame) {
		if (hasUpgrades) {

			final String block = "Block link [Cost: " + BLOCK_LINK_COST + "]";
			final int x = neighbours.getPosition().x + (neighbours.getWidth() / 2) - (blockFont.getWidth(block) / 2);
			final int y = neighbours.getPosition().y - blockFont.getHeight(block) - 5;

			frame.draw(blockFont, block, x, y);

			frame.draw(neighbours, neighbourListener);
		} else {
			frame.draw(countryFont, "This country has no upgrades avalible.", 500, 400);
		}
	}

	private void drawPoints(Frame frame) {

		final String points = Integer.toString(selected.getRuler().getPoints());
		final String text = "POINTS";

		final int x = getPosition().x + (getWidth() / 4);
		final int y = getPosition().y + (getHeight() / 4);

		frame.draw(pointFont, text, x - (pointFont.getWidth(text) / 2), y);
		frame.draw(titleFont, points, x - (titleFont.getWidth(points) / 2), y - titleFont.getHeight(points) - 5);
	}

	private void drawCountry(Frame frame) {

		final int countryX = getPosition().x + (getWidth() / 4);
		final int countryY = getPosition().y + (getHeight() / 2);

		frame.draw(country, countryX - (country.getWidth() / 2), countryY - (country.getHeight() / 2));

		final String countryName = selected.getName();
		final int counrtyNameX = countryX - (countryFont.getWidth(countryName) / 2);
		final int countryNameY = countryY - (countryFont.getHeight(countryName) / 2);

		frame.draw(countryFont, countryName, counrtyNameX, countryNameY);
	}

	private void drawBackground(Frame frame) {
		frame.setColor(Color.lightGray);

		final int boxX = getPosition().x + PADDING_X;
		final int boxY = getPosition().y + PADDING_Y;
		final int boxWidth = WIDTH - (2 * PADDING_X);
		final int boxHeight = HEIGHT - (2 * PADDING_Y);

		frame.fillRect(boxX, boxY, boxWidth, boxHeight);
	}

	private void drawTitle(Frame frame) {
		final String title = "UPGRADE";
		final int titleX = getPosition().x + (getWidth() / 2) - (titleFont.getWidth(title) / 2);
		final int titleY = getPosition().y + titleFont.getHeight(title);

		frame.draw(titleFont, title, titleX, titleY);
	}

	/**
	 * Initialises all the visual elements off {@link UpgradeMenu}.
	 */
	public void init() {

		pointFont.init();
		countryFont.init();
		titleFont.init();
		neighbours.init();
		blockFont.init();

	}

	/**
	 * Process a click.
	 */
	public void parseClick(Point click) {

	}

	/**
	 * Moves all the components in this {@link UpgradeMenu}.
	 */
	@Override
	public void moveComponents(Point vector) {

		final Point current = neighbours.getPosition();

		neighbours.setPosition(new Point(current.x + vector.x, current.y + vector.y));

	}

}
