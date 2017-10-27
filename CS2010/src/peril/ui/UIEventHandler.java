package peril.ui;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

import peril.board.Board;
import peril.board.Country;
import peril.Game;
import peril.Point;

/**
 * Handles all the interactions that the user will cause using the
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy, Adrian_Wong
 *
 * @see MouseListener
 */
public class UIEventHandler implements MouseListener, KeyListener {

	private UserInterface ui;
	private Game game;

	public UIEventHandler(UserInterface ui, Game game) {
		if (ui == null)
			throw new IllegalArgumentException("UserInterface cannot be null");
		this.ui = ui;
		this.game = game;
	}

	@Override
	public void inputEnded() {

	}

	@Override
	public void inputStarted() {

	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void setInput(Input input) {

	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {

	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {

	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {

	}

	@Override
	public void mousePressed(int button, int x, int y) {

		Board b = game.getBoard();
		if (b != null) {
			Country c = b.getCountry(new Point(x, y));
			if (c != null) {
				ui.highlight(c);
				System.out.println(c.getName());
			} else {
				ui.highlight(null);
			}
		}

	}

	@Override
	public void mouseReleased(int button, int x, int y) {

	}

	@Override
	public void mouseWheelMoved(int change) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int key, char c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int key, char c) {
		// TODO Auto-generated method stub

	}

	// TODO private Button getButtonByClick(){}

	// TODO private void executeButtonPress(Button button){}

}
