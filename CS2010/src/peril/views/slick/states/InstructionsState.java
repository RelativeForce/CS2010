package peril.views.slick.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.controllers.GameController;
import peril.io.TextFileReader;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.components.ScrollBar;
import peril.views.slick.components.TextField;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.util.Point;

/**
 * 
 * The {@link InteractiveState} of the game where the instructions of the entire
 * game are displayed.
 * 
 * @author Adrian_Wong, Joshua_Eddy
 * 
 * @since 2018-02-27
 * @version 1.01.04
 * 
 * @see InteractiveState
 *
 */
public final class InstructionsState extends InteractiveState {

	/**
	 * The name of a specific {@link InstructionsState}.
	 */
	private static final String NAME = "Instructions";

	/**
	 * The max index of the {@link #scrollBar}.
	 */
	private static final int MAX = 20;

	/**
	 * The {@link ScrollBar} that allows the user
	 */
	private final ScrollBar scrollBar;

	/**
	 * The {@link TextField} that displays the instructions to the user.
	 */
	private final TextField text;

	/**
	 * The {@link EventListener} that defines the operations for drawing the
	 * {@link #scrollBar}.
	 */
	private final EventListener scrollListener;

	/**
	 * Constructs a new {@link InstructionsState}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows this
	 *            {@link InstructionsState} to query the state of the game.
	 * @param id
	 *            The id of this {@link InstructionsState}.
	 */
	public InstructionsState(GameController game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);
		
		final String sliderImagePath = game.getDirectory().getUIPath() + "slider.png";

		this.scrollBar = new ScrollBar(new Point(MainMenu.WIDTH - 50, 53), 30, MainMenu.HEIGHT - 111, MAX, sliderImagePath);

		this.text = new TextField(MainMenu.WIDTH - 105, new Point(46, 53));

		super.addComponent(scrollBar);
		super.addComponent(text);

		this.scrollListener = new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {

			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {
				scrollBar.click(mouse);
				setTextPosition();
			}

			@Override
			public void draw(Frame frame) {
				scrollBar.draw(frame);
			}

			@Override
			public void buttonPress(int key, Point mouse) {

			}
		};

	}

	/**
	 * Initialise the visual elements of the {@link InstructionsState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		for (String line : TextFileReader.scanFile(game.getDirectory().getTextPath(), "instructions.txt")) {
			text.addText(line);
		}

		scrollBar.hideNumbers();

	}

	/**
	 * Updates the {@link InstructionsState}.
	 */
	@Override
	public void update(GameContainer gc, int delta, Frame frame) {

		// final Point mouse = new Point(gc.getInput().getAbsoluteMouseX(),
		// gc.getInput().getAbsoluteMouseY());
		// scrollBar.click(mouse);

	}

	/**
	 * Render the {@link InstructionsState}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {
		
		text.draw(frame);
		
		drawImages();

		frame.draw(scrollBar, scrollListener);

		drawButtons();
	}

	/**
	 * Processes a button press on this {@link InstructionsState}.
	 */
	@Override
	public void parseButton(int key, Point mousePosition) {
		super.parseButton(key, mousePosition);

		switch (key) {

		case Input.KEY_UP:
			scrollBar.up();
			setTextPosition();
			break;
		case Input.KEY_DOWN:
			scrollBar.down();
			setTextPosition();
			break;

		}

	}

	/**
	 * Retrieves the {@link Music} of this {@link InstructionsState}.
	 */
	@Override
	public Music getMusic() {
		return null;
	}

	/**
	 * Updates the position of the {@link #text} based on the index of the
	 * {@link #scrollBar}.
	 */
	private void setTextPosition() {
		text.setPosition(new Point(text.getPosition().x, 50 - ((text.getHeight() - MainMenu.HEIGHT + 100) / MAX) * scrollBar.getIndex()));
	}

}
