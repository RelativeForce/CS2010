package peril.ui.components.menus;

import java.util.List;

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
public class ChallengeMenu extends Menu {

	private static final String NAME = "Challenge Menu";

	private List<Challenge> challenges;

	private TextField text;

	public ChallengeMenu(Point position, Game game) {
		super(NAME, game, new Region(300, 300, position));
		this.challenges = game.players.challenges;
		int paddingX = (getWidth() / 12);
		int paddingY = (getHeight() / 8);
		this.text = new TextField(getWidth() - (paddingX * 2), getHeight() - (paddingY * 2),
				new Point(getPosition().x + paddingX, getPosition().y + paddingY));
	}

	@Override
	public void init() {
		text.init();
		refreshChallenges();
	}

	@Override
	public void draw(Graphics g) {

		if (!isVisible()) {
			return;
		}

		super.draw(g);

		g.setColor(Color.white);
		g.drawString("Challenges", getPosition().x + 25, getPosition().y + 10);

		text.draw(g);

	}

	@Override
	public void parseClick(Point click) {
		super.clickedButton(click);
	}

	@Override
	public void moveComponents(Point vector) {
		text.setPosition(new Point(text.getPosition().x + vector.x, text.getPosition().y + vector.y));
	}

	public void refreshChallenges() {

		text.clear();

		challenges.forEach(challenge -> text.addText(challenge.toString()));

	}

}
