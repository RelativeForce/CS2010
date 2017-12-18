package peril.ui.states;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.ai.AI;
import peril.ai.Monkey;
import peril.helpers.PlayerHelper;
import peril.ui.Font;
import peril.ui.components.lists.VisualList;
import peril.ui.components.menus.HelpMenu;

public class PlayerSelection extends InteractiveState {

	private static final String NAME = "Select";

	private final Map<String, AI> allAI;

	private final Map<Integer, VisualList<AI>> selectors;

	private final String playButton;

	/**
	 * The {@link VisualList} of the number of {@link Player}s that can be in the
	 * {@link Game}.
	 */
	private VisualList<Integer> players;

	public PlayerSelection(Game game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);

		this.playButton = "play";
		this.allAI = new IdentityHashMap<>();
		this.selectors = new HashMap<>();
		this.players = new VisualList<>(new Point(100, 100), 20, 24, 3, 5);

		players.setFont(new Font("Arial", Color.black, 19));

		addAllAI();
		addSelectors();

		super.addComponent(players);

	}

	/**
	 * Enter the {@link PlayerSelection} state.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);

		players.setPosition(new Point((gc.getWidth() / 6) - players.getWidth() - 10, gc.getHeight() / 2));

		// Set the positions and visibility of the selectors.
		positionSelectors(gc);
		confingureSelectors();

		// Set the position of the play button.
		getButton(playButton).setPosition(new Point(gc.getWidth() - 10 - getButton(playButton).getWidth(),
				gc.getHeight() - 10 - getButton(playButton).getHeight()));

	}

	@Override
	public void parseClick(int button, Point click) {

		if (!super.clickedButton(click)) {
			if (players.click(click)) {

				confingureSelectors();

			} else {
				for (VisualList<AI> selector : selectors.values()) {

					if (selector.click(click)) {
						break;
					}

				}
			}
		}

	}

	@Override
	public void parseButton(int key, char c, Point mousePosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		super.drawImages(g);

		super.drawButtons(g);

		players.draw(g);

		selectors.forEach((playerNumber, selector) -> {

			if (selector.isVisible()) {

				selector.draw(g);

				Image playerIcon = getGame().players.getPlayerIcon(playerNumber);

				final int x = selector.getPosition().x + (selector.getWidth() / 2) - (playerIcon.getWidth() / 2);

				final int y = selector.getPosition().y - playerIcon.getHeight() - 10;

				g.drawImage(playerIcon, x, y);
			}

		});

	}

	@Override
	public Music getMusic() {
		return null;
	}

	public void loadGame() {

		for (int index = 1; index <= players.getSelected(); index++) {

			// The color assigned to that player.
			Color color = getGame().players.getColor(index);

			// Set the player with the AI that the user selected.
			Player player = new Player(index, color, selectors.get(index).getSelected());

			// The player Icon for that player.
			Image playerIcon = getGame().players.getPlayerIcon(index);

			player.setImage(new Point(100, 100), playerIcon);

			getGame().players.addPlayer(player);

		}

		getGame().enterState(getGame().states.loadingScreen);
	}

	private void addSelectors() {

		for (int index = 1; index <= PlayerHelper.MAX_PLAYERS; index++) {

			VisualList<AI> selector = new VisualList<>(new Point(100, 100), 100, 24, 5, 5);

			populateSelector(selector);

			players.add(Integer.toString(index + 1), index + 1);
			selectors.put(index, selector);
			super.addComponent(selector);

		}

	}

	private void confingureSelectors() {

		selectors.forEach((playerNumber, selector) -> {

			if (playerNumber <= players.getSelected()) {
				selector.show();
			} else {
				selector.hide();
			}

		});

	}

	private void positionSelectors(GameContainer gc) {
		selectors.forEach((playerNumber, selector) -> selector.setPosition(getSelectorPosition(playerNumber, gc)));
	}

	private Point getSelectorPosition(int playerNumber, GameContainer gc) {

		int width = gc.getWidth();
		int height = gc.getHeight();

		// All the selectors have the same Y
		int selectorY = height / 2;

		return new Point((width * (playerNumber)) / (PlayerHelper.MAX_PLAYERS + 2), selectorY);

	}

	private void populateSelector(VisualList<AI> selector) {
		allAI.forEach((AIName, AI) -> selector.add(AIName, AI));
	}

	private void addAllAI() {
		allAI.put("Monkey", new Monkey(getGame().api));
		allAI.put("None", AI.USER);
	}

}