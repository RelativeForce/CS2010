package peril.ui;

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

	/**
	 * The instance of the {@link Game} that this {@link UIEventHandler} assists.
	 */
	private Game game;

	/**
	 * Constructs an new {@link UIEventHandler};
	 * 
	 * @param game
	 *            {@link UIEventHandler#game}
	 */
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

		// Holds the position of the click
		Point click = new Point(x, y);

		// If the states buttons were not clicked click the board.
		if (!clickButton(click)) {
			clickBoard(click);
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

		switch (key) {
		case Input.KEY_1:
			highlighted.setRuler(Player.PLAYERONE);
			break;
		case Input.KEY_2:
			highlighted.setRuler(Player.PLAYERTWO);
			break;
		case Input.KEY_3:
			highlighted.setRuler(Player.PLAYERTHREE);
			break;
		case Input.KEY_4:
			highlighted.setRuler(Player.PLAYERFOUR);
			break;
		case Input.KEY_SPACE:
			highlighted.setRuler(null);
			break;
		case Input.KEY_N:
			game.nextPlayer();
			break;

		}

	}

	@Override
	public void keyReleased(int key, char c) {
	}

	/**
	 * Simulate a click a specified {@link Point} and check if any of the
	 * {@link Clickable} {@link Button}s are intersected by the {@link Point}.
	 * 
	 * @param click
	 *            {@link Point}
	 * @return whether any {@link Button} was intersected by the {@link Point}.
	 */
	private boolean clickButton(Point click) {

		// Iterate through all the buttons in the current state.
		for (Clickable element : ((CoreGameState) game.getCurrentState()).getClickableElements()) {

			// If the click is in the current element
			if (element.isClicked(click)) {

				// If the element is a button
				if (element instanceof Button) {

					// Click the button
					((Button) element).click();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Simulates a click at a {@link Point} on the {@link Board} and highlights the
	 * {@link Country} that clicked.
	 * 
	 * @param click
	 *            {@link Point}
	 */
	private void clickBoard(Point click) {

		// Holds the game board
		Board board = game.getBoard();

		// If there is a game board
		if (board != null) {

			// Get the country that is clicked.
			Country clickedCountry = board.getCountry(click);

			// Get the currently highlighted country
			Country highlighted = ((CoreGameState) game.getCurrentState()).getHighlightedCountry();

			// If a country was clicked
			if (clickedCountry != null) {

				// Highlight the country that was clicked
				clickedCountry.setImage(clickedCountry.getRegion().getPosition(),
						clickedCountry.getRegion().convert(Color.yellow));

				// If the currently highlighted country and the clicked country are different
				// unhighlight the current country.
				if (highlighted != null && !clickedCountry.equals(highlighted)) {
					unhighlight(highlighted);
				}

				// Highlight the clicked country
				game.highlight(clickedCountry);
				System.out.println(clickedCountry.getName());
			} else {
				unhighlight(highlighted);
				game.highlight(null);
			}
		}
	}

	/**
	 * Removes the highlight colouring effect on a {@link Country}.
	 * 
	 * @param toUnhighlight
	 *            {@link Country}
	 */
	private void unhighlight(Country toUnhighlight) {

		// If there is a country to unhighlight
		if (toUnhighlight != null) {

			// Holds the position of the country
			Point position = toUnhighlight.getRegion().getPosition();

			// Holds the region of the country
			Region region = toUnhighlight.getRegion();

			// Holds the ruler of the country
			Player ruler = toUnhighlight.getRuler();

			// If there is a ruler then return the colour of the country to that of the
			// ruler. Otherwise remove the highlight effect.
			if (ruler != null) {
				toUnhighlight.setImage(position, region.convert(ruler.getColor()));
			} else {
				toUnhighlight.setImage(null, null);
			}

		}

	}
}
