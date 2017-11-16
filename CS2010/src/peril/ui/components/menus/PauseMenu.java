package peril.ui.components.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Point;
import peril.ui.components.Font;
import peril.ui.components.Region;
import peril.ui.components.VisualList;

/**
 * Encapsulates the behaviour of a Pause Menu
 * 
 * @author Ezekiel_Trinidad
 *
 */
public class PauseMenu extends Menu {

	private final static String NAME = "Pause Menu";

	/**
	 * {@link VisualList} of buttons to toggle the Music on and off.
	 * 
	 */
	private VisualList<Toggle> toggleMusic;

	/**
	 * {@link Font} for the text of the music option.
	 */
	private Font musicFont;

	/**
	 * {@link Font} for the text of the {@link PauseMenu}.
	 */
	private Font textFont;

	/**
	 * Constructs a new {@link PauseMenu}.
	 * 
	 * @param position
	 *            {@link Point} position of the {@link PauseMenu}.
	 * @param game
	 *            The {@link Game} the {@link PauseMenu} is associated with.
	 */
	public PauseMenu(Point position, Game game) {
		super(NAME, game, new Region(300, 300, position));
		this.toggleMusic = new VisualList<>(position.x + (getWidth() / 2), position.y + 50, 30, 15, 2, 5);
		this.toggleMusic.add(Toggle.ON.toString, Toggle.ON);
		this.toggleMusic.add(Toggle.OFF.toString, Toggle.OFF);
	}

	/**
	 * Draws the {@link PauseMenu} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	public void draw(Graphics g) {

		if (visible) {

			g.setColor(Color.black);
			super.draw(g);

			String pause = "PAUSE";
			String music = "Toggle Music:";

			textFont.draw(g, pause, getPosition().x - (textFont.getWidth(pause) / 2) + (getWidth() / 2),
					getPosition().y + 10);

			musicFont.draw(g, music, getPosition().x + (getWidth() / 2) - (textFont.getWidth(music) / 2) - 15,
					getPosition().y + 50);

			g.setColor(Color.white);

			// draws the List
			toggleMusic.draw(g);
		}

	}

	/**
	 * Initialises all the visual elements off {@link PauseMenu}.
	 */
	public void init() {
		textFont = new Font("Calibri", Color.cyan, 20);
		musicFont = new Font("Arial", Color.pink, 10);
		toggleMusic.init();
		toggleMusic.setFont(new Font("Arial", Color.green, 10));
	}

	/**
	 * Processed the click on the toggle music buttons.
	 */
	public void parseClick(Point click) {

		if (!toggleMusic.click(click)) {
			clickedButton(click);
		} else {
			getGame().toggleMusic(toggleMusic.getSelected().toggle);
		}
	}

	/**
	 * Holds "ON" and "OFF" which has boolean value assigned to them.
	 * 
	 * @author Ezekiel_Trinidad, Joshua_Eddy
	 *
	 */
	private enum Toggle {

		ON("On", true), OFF("Off", false);

		public final boolean toggle;

		public final String toString;

		private Toggle(String toString, boolean toggle) {
			this.toggle = toggle;
			this.toString = toString;
		}
	}

	@Override
	public void moveComponents(Point vector) {

		toggleMusic
				.setPosition(new Point(toggleMusic.getPosition().x + vector.x, toggleMusic.getPosition().y + vector.y));

	}
}
