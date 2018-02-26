package peril.views.slick.states;

import org.newdawn.slick.Color;
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
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;

/**
 * 
 * The {@link InteractiveState} of the {@link Game} where the instructions of
 * the entire game are displayed.
 * 
 * @author Adrian_Wong
 * 
 * @since 2018-02-26
 * @version 1.01.01
 * 
 * @see InteractiveState
 *
 */

public final class InstructionsState extends InteractiveState {

	/**
	 * The name of a specific {@link InstructionsState}.
	 */
	private static final String NAME = "Instructions";

	private static final int MAX = 20;

	private final ScrollBar scrollBar;

	private final TextField text;

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

		this.scrollBar = new ScrollBar(new Point(MainMenu.WIDTH - 30, 5), 30, MainMenu.HEIGHT - 10, MAX);

		this.text = new TextField(MainMenu.WIDTH - 90, new Point(30, 30));

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

	}

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

		drawImages();

		frame.draw(scrollBar, scrollListener);

		text.draw(frame);

		drawButtons();
	}

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

	private void setTextPosition() {
		text.setPosition(new Point(30, 30 - ((text.getHeight() - MainMenu.HEIGHT + 60) / MAX) * scrollBar.getIndex()));
	}

	/**
	 * Retrieves the {@link Music} of this {@link InstructionsState}.
	 */
	@Override
	public Music getMusic() {
		return null;
	}

}