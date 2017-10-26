package peril.ui;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

import peril.board.Board;
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
	
	public UIEventHandler(UserInterface ui) {
		if(ui == null)throw new IllegalArgumentException("UserInterface cannot be null");
		this.ui = ui;
	}
	
	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAcceptingInput() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInput(Input input) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(int button, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(int change) {
		// TODO Auto-generated method stub

	}

	/**
	 * Converts a {@link Point} of the mouse into a {@link Point} relative to
	 * the {@link Board}.
	 * 
	 * @param mouse
	 *            {@link Point} of the mouse in the {@link UserInterface}.
	 */
	private void convertToBoardPosition(Point mouse) {

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
