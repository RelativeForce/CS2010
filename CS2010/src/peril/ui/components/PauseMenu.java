package peril.ui.components;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Point;
import peril.ui.Button;
import peril.ui.ButtonContainer;

/**
 * Encapsulates the behaviour of a Pause Menu
 * 
 * @author Ezekiel_Trinidad
 *
 */
public class PauseMenu extends Clickable implements ButtonContainer {

	private final static String NAME = "Pause Menu";

	/**
	 * Whether the screen is paused or not
	 * 
	 */
	private boolean isPaused;

	private VisualList<Toggle> toggleMusic;

	private Font musicFont;
	private Font textFont;

	private Game game;

	/**
	 * A list of {@link Button}s on the {@link PauseMenu}.
	 */
	private List<Button> buttons;

	/**
	 * Constructs a {@link PauseMenu}
	 * 
	 */
	public PauseMenu(Point position, Game game) {
		this.buttons = new LinkedList<>();
		this.game = game;
		setRegion(new Region(300, 300, position));
		this.toggleMusic = new VisualList<>(position.x + 40 + (getWidth() / 2), position.y + 50, 30, 15, 2, 5);
		this.toggleMusic.add(new Element<Toggle>(Toggle.ON.toString, Toggle.ON));
		this.toggleMusic.add(new Element<Toggle>(Toggle.OFF.toString, Toggle.OFF));
	}

	/**
	 * Draws the {@link PauseMenu} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	public void draw(Graphics g) {

		if (getRegion() != null) {

			g.setColor(Color.black);
			g.fillRect(getPosition().x, getPosition().y, getWidth(), getHeight());

			String pause = "PAUSE";
			String music = "Toggle Music:";

			textFont.draw(g, pause, getPosition().x - (textFont.getWidth(pause) / 2) + (getWidth() / 2),
					getPosition().y + 10);
			musicFont.draw(g, music, getPosition().x + (getWidth() / 2) - 90, getPosition().y + 50);
		}

		g.setColor(Color.white);

		// draws the List
		toggleMusic.draw(g);

	}

	/**
	 * Initialises all the visual elements off {@link PauseMenu}
	 * 
	 */
	public void init() {
		textFont = new Font("Calibri", Color.cyan, 20);
		musicFont = new Font("Arial", Color.pink, 10);
		toggleMusic.init();
		toggleMusic.setFont(new Font("Arial", Color.green, 10));
	}

	/**
	 * 
	 * 
	 * @param click
	 * 
	 * @return
	 */
	public void parseClick(Point click) { // Alt-Shift-R (Refactoring)

		if (!toggleMusic.click(click)) {
			clickedButton(click);
		} else {
			game.toggleMusic(toggleMusic.getSelected().get().toggle);
		}
	}

	/**
	 * Adds a {@link Button} to the {@link List} of buttons in the
	 * {@link PauseMenu}.
	 * 
	 * @param button
	 */
	public void addButton(Button button) {
		buttons.add(button);
	}

	public void setPause(boolean isPaused) {
		this.isPaused = isPaused;
	}

	public boolean IsPaused() {
		return this.isPaused;
	}

	@Override
	public List<Button> getButtons() {
		return null;
	}

	@Override
	public boolean clickedButton(Point click) {

		// Iterate through every button on the
		for (Button b : buttons) {
			if (b.isClicked(click)) {
				b.click();
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return NAME;
	}

	private enum Toggle {
		ON("On", true), OFF("Off", false);

		public final boolean toggle;

		public final String toString;

		private Toggle(String toString, boolean toggle) {
			this.toggle = toggle;
			this.toString = toString;
		}
	}
}
