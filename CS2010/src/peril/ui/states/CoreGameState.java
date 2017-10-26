package peril.ui.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import peril.Game;
import peril.Point;
import peril.board.Board;
import peril.ui.UIEventHandler;
import peril.ui.UserInterface;
import peril.ui.VisualRepresentation;

/**
 * @author Joseph_Rolli
 */

public abstract class CoreGameState extends BasicGameState {

	/**
	 * Holds the name of a specific {@link CoreGameState}
	 */
	protected String stateName;
	private Game game;
	private Image board;
	private final UIEventHandler eventHandler;
	private int x;
	private int y;

	protected CoreGameState(Game game, UserInterface ui) {
		this.game = game;
		this.eventHandler = new UIEventHandler(ui, game);

	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		System.out.println("Entering gamestate:" + Integer.toString(getID()));
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gc.setUpdateOnlyWhenVisible(true);
		gc.getInput().addKeyListener(eventHandler);
		gc.getInput().addMouseListener(eventHandler);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		Board b = game.getBoard();

		if (b != null) {

			VisualRepresentation vr = b.getVisual();

			if (vr != null) {

				board = vr.getImage();

				if (board != null) {

					g.drawImage(board, 0, 0);
				}
			}
		} else {

			game.init();

		}

		g.drawString(stateName, 5, 50);
		g.drawString("X" + x + "Y" + y, 700, 700);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (gc.getInput().isMouseButtonDown(0)) {
			x = gc.getInput().getMouseX();
			y = gc.getInput().getMouseY();
		}
	}

	public String getStateName() {
		return stateName;
	}

}
