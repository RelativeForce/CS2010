package peril.ui.components.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Challenge;
import peril.Game;
import peril.Point;
import peril.ui.Region;
import peril.ui.components.TextField;

/**
 * Displays the {@link Challenge}s of the {@link Game} to the user.
 * 
 * @author Joshua_Eddy
 *
 */
public final class ChallengeMenu extends Menu {

	/**
	 * The string name of the {@link ChallengeMenu}.
	 */
	private static final String NAME = "Challenge Menu";

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
	 *            The {@link Game} this {@link ChallengeMenu} is a part of.
	 */
	public ChallengeMenu(Point position, Game game) {
		super(NAME, game, new Region(350, 300, position));
		this.paddingX = (getWidth() / 12);
		this.paddingY = (getHeight() / 8);
		this.text = new TextField(getWidth() - (paddingX * 2), getHeight() - (paddingY * 2),
				new Point(getPosition().x + paddingX, getPosition().y + paddingY));
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
	public void draw(Graphics g) {

		// If the menu is invisible don't draw anything
		if (!isVisible()) {
			return;
		}

		super.draw(g);

		g.setColor(Color.white);
		g.drawString("Challenges", getPosition().x + paddingX, getPosition().y + 10);

		text.draw(g);

	}

	/**
	 * Processes a click at a {@link Point} position on this {@link ChallengeMenu}.
	 */
	@Override
	public void parseClick(Point click) {
		super.clickedButton(click);
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
		getGame().players.challenges
				.forEach(challenge -> text.addText(challenge.toString() + " - Reward: " + challenge.reward + " Units"));

	}

}