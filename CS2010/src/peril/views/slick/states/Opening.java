package peril.views.slick.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.controllers.GameController;
import peril.views.slick.Frame;
import peril.views.slick.SlickGame;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.io.ImageReader;
import peril.views.slick.util.Point;

public class Opening extends InteractiveState {

	private static final String NAME = "Opening";

	private final static int DURATION = 2;

	private Image goat;

	private Image initialGoat;

	private Point goatPosition;

	private float goatScale;

	private float maxGoatScale;

	private float initialGoatScale;

	public Opening(GameController game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		this.initialGoat = ImageReader.getImage(game.getDirectory().getUIPath() + "perilLogo.png");

		this.initialGoatScale = (float) (gc.getWidth() / 3) / initialGoat.getWidth();

		this.goatScale = initialGoatScale;

		this.maxGoatScale = (float) (gc.getWidth() * 9 / 10) / initialGoat.getWidth();

		this.goat = initialGoat.getScaledCopy(goatScale);

		this.goatPosition = getGoatPosition(gc);

	}

	@Override
	public void render(GameContainer gc, Frame frame) {
		frame.draw(goat, goatPosition.x, goatPosition.y);
	}

	@Override
	public void update(GameContainer gc, int delta, Frame frame) {

		if (goatScale >= maxGoatScale) {
			game.getView().enterMainMenu();
		} else {

			final float ratio = (((float) 1000f) / (float) SlickGame.FPS) / ((float) delta);

			goatScale += ((float) (maxGoatScale - initialGoatScale) / (DURATION * SlickGame.FPS)) / ratio;

			goat = initialGoat.getScaledCopy(goatScale);

			goatPosition = getGoatPosition(gc);

		}
	}

	@Override
	public Music getMusic() {
		return null;
	}

	private Point getGoatPosition(GameContainer gc) {
		final int x = (gc.getWidth() - goat.getWidth()) / 2;
		final int y = (gc.getHeight() - goat.getHeight()) / 2;
		return new Point(x, y);
	}

}
