package peril.ui;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

import peril.board.Board;
import peril.board.Country;
import peril.ui.states.CoreGameState;
import peril.ui.visual.Clickable;
import peril.ui.visual.Region;
import peril.Game;
import peril.Player;
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

	private Game game;

	public UIEventHandler(Game game) {
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

		boolean clickedButton = false;
		List<Clickable> elements = ((CoreGameState) game.getCurrentState()).getClickableElements();

		Point click = new Point(x, y);

		for (Clickable element : elements) {
			if (element.isClicked(click)) {
				if (element instanceof Button) {
					((Button) element).click();
					clickedButton = true;
				}
			}
		}

		if (!clickedButton) {
			
			Board board = game.getBoard();
			
			if (board != null) {
				
				Country clickedCountry = board.getCountry(new Point(x, y));

				Country highlighted = ((CoreGameState) game.getCurrentState()).getHighlightedCountry();

				if (clickedCountry != null && highlighted != null && !clickedCountry.equals(highlighted)) {

					Point position = highlighted.getRegion().getPosition();
					Region region = highlighted.getRegion();
					Player ruler = highlighted.getRuler();

					if (ruler != null) {
						highlighted.setImage(position, region.convert(ruler.getColor()));
					} else {
						highlighted.setImage(null, null);
					}

				}

				if (clickedCountry != null) {
					clickedCountry.setImage(clickedCountry.getRegion().getPosition(), clickedCountry.getRegion().convert(Color.yellow));
					game.highlight(clickedCountry);
					System.out.println(clickedCountry.getName());
				} else {
					game.highlight(null);
				}
			}
		}

	}

	@Override
	public void mouseReleased(int button, int x, int y) {

	}

	@Override
	public void mouseWheelMoved(int change) {
	}

	@Override
	public void keyPressed(int key, char c) {

		Country highlighted = ((CoreGameState) game.getCurrentState()).getHighlightedCountry();
		if (key == Input.KEY_1) {
			highlighted.setRuler(Player.PLAYERONE);
		} else if (key == Input.KEY_2) {
			highlighted.setRuler(Player.PLAYERTWO);
		} else if (key == Input.KEY_3) {
			highlighted.setRuler(Player.PLAYERTHREE);
		} else if (key == Input.KEY_4) {
			highlighted.setRuler(Player.PLAYERFOUR);
		}

	}

	@Override
	public void keyReleased(int key, char c) {
		// TODO Auto-generated method stub

	}
}
