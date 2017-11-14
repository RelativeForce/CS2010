package peril.ui.states.menuStates;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Point;
import peril.ui.Button;
import peril.ui.ButtonContainer;
import peril.ui.components.Clickable;
import peril.ui.components.Element;
import peril.ui.components.Font;
import peril.ui.components.Region;
import peril.ui.components.VisualList;

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

	/**
	 * A list of {@link Button}s on the {@link PauseMenu}.
	 */
	private List<Button> buttons;

	/**
	 * Constructs a {@link PauseMenu}
	 * 
	 */
	public PauseMenu(Point position) {
		buttons = new LinkedList<>();
		toggleMusic = new VisualList<>(position.x + 125, position.y + 50, 50, 20, 2, 5);
		toggleMusic.add(new Element<Toggle>("YES", Toggle.YES));
		toggleMusic.add(new Element<Toggle>("NO", Toggle.NO));
		Region r = new Region(300, 300, position);
		setRegion(r);
	}

	/**
	 * Draws the {@link PauseMenu} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	public void draw(Graphics g) {
		
		Region r = getRegion();
		
		if (r != null) {
			
			Point position = r.getPosition();
			
			g.setColor(Color.black);
			g.fillRect(position.x, position.y, r.getWidth(), r.getHeight());
			
			String pause = "PAUSE";
			String music = "Toggle Music:";
			
			textFont.draw(g, pause, position.x - (textFont.getWidth(pause) / 2) + (r.getWidth() / 2), position.y + 10);
			musicFont.draw(g, music, position.x + 30 - (musicFont.getWidth(music) / 2), position.y + 50);
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
		YES(true), NO(false);

		public final boolean toggle;

		private Toggle(boolean toggle) {
			this.toggle = toggle;
		}
	}
}
