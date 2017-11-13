package peril.ui.states.menuStates;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Point;
import peril.ui.Button;
import peril.ui.visual.Clickable;
import peril.ui.visual.Element;
import peril.ui.visual.Font;
import peril.ui.visual.Region;
import peril.ui.visual.VisualList;

/**
 * Encapsulates the behaviour of a Pause Menu
 * 
 * @author Ezekiel_Trinidad
 *
 */
public class PauseMenu extends Clickable {

	// click some button which opens the pause menu
	// when button is clicked, game.getCurrenState().showPauseMenu()

	/**
	 * Whether the screen is paused or not
	 * 
	 */
	private boolean isPaused;

	private VisualList<Toggle> toggleMusic;

	private enum Toggle {
		YES(true), NO(false);

		public final boolean toggle;

		private Toggle(boolean toggle) {
			this.toggle = toggle;
		}
	}

	private int x;
	private int y;

	private Font musicFont;
	private Font textFont;

	/**
	 * A list of {@link Button}s on the {@link PauseMenu}.
	 */
	private List<Button> buttons;

	/**
	 * Constructs a {@link PauseMenu}
	 * 
	 */
	public PauseMenu(int x, int y) {
		buttons = new LinkedList<>();
		this.x = x;
		this.y = y;
		toggleMusic = new VisualList<>(x + 125, y + 50, 50, 20, 2, 5);
		toggleMusic.add(new Element<Toggle>("YES", Toggle.YES));
		toggleMusic.add(new Element<Toggle>("NO", Toggle.NO));
		Region r = new Region(300, 300, new Point(x, y));
		setRegion(r);
	}

	/**
	 * Draws something
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		Region r = getRegion();
		if (r != null) {
			g.setColor(Color.black);
			g.fillRect(r.getPosition().x, r.getPosition().y, r.getWidth(), r.getHeight());
			String pause = "PAUSE";
			String music = "Toggle: Music";
			textFont.draw(g, pause, x - (textFont.getWidth(pause) / 2) + (r.getWidth() / 2), y + 10);
			musicFont.draw(g, music, x + 30 - (musicFont.getWidth(music) / 2), y + 50);
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
			for (Button b : buttons) {
				if (b.isClicked(click)) {
					b.click();
				}
			}
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

}
