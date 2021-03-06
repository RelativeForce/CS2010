package peril.views.slick.components.menus;

import peril.Challenge;
import peril.GameController;
import peril.views.slick.Frame;
import peril.views.slick.components.TextField;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Displays the {@link Challenge}s of the game to the user.
 * 
 * @author Joshua_Eddy
 *
 * @since 2018-03-14
 * @version 1.01.03
 *
 * @see Menu
 * @see Challenge
 */
public final class ChallengeMenu extends Menu {

	/**
	 * The string name of the {@link ChallengeMenu}.
	 */
	public static final String NAME = "Challenge Menu";

	/**
	 * The {@link TextField} that shows the challenges on screen.
	 */
	private final TextField text;

	/**
	 * The horizontal padding distance around the boarder of the
	 * {@link ChallengeMenu}.
	 */
	private final int paddingX;

	/**
	 * The vertical padding distance around the boarder of the
	 * {@link ChallengeMenu}.
	 */
	private final int paddingY;

	/**
	 * Constructs a new {@link ChallengeMenu}.
	 * 
	 * @param position
	 *            The {@link Point} position of the {@link ChallengeMenu}
	 * @param game
	 *            The {@link GameController} that allows this {@link ChallengeMenu}
	 *            to query the state of the game.
	 */
	public ChallengeMenu(Point position, GameController game) {
		super(NAME, game, new Region(600, 600, position));

		this.paddingX = (getWidth() / 11);
		this.paddingY = (getHeight() / 6);
		this.text = new TextField(getWidth() - (paddingX * 2),
				new Point(getPosition().x + paddingX, getPosition().y + paddingY));
		this.text.hideBackground();
	}

	/**
	 * Initialises this {@link ChallengeMenu}.
	 */
	@Override
	public void init() {
		text.init();
		refreshChallenges();
	}

	/**
	 * Draws this {@link ChallengeMenu}.
	 */
	@Override
	public void draw(Frame frame) {

		// If the menu is invisible don't draw anything
		if (!isVisible()) {
			return;
		}

		super.draw(frame);

		text.draw(frame);

	}

	/**
	 * Moves all the components of this {@link ChallengeMenu} along a {@link Point}
	 * vector.
	 */
	@Override
	public void moveComponents(Point vector) {
		text.setPosition(new Point(text.getPosition().x + vector.x, text.getPosition().y + vector.y));
	}

	/**
	 * Refreshes the list of {@link Challenge}s displayed by this
	 * {@link ChallengeMenu}.
	 */
	public void refreshChallenges() {

		text.clear();

		// List all the challenges
		game.getChallenges().forEach(
				challenge -> text.addText(challenge.toString() + " - Reward: " + challenge.reward + " Points"));

	}

}
