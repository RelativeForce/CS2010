package peril.views.slick.states;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.controllers.GameController;
import peril.views.slick.Frame;
import peril.views.slick.Point;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.io.ImageReader;

public class Opening extends InteractiveState {

	private static final String NAME = "Opening";

	private Splash splash;

	public Opening(GameController game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		final int frameRate = 1000 / 30;

		final String splashPath = game.getDirectory().getSplashPath();

		this.splash = new Splash();
		Image frame = null;
		int index = 0;

		// Iterate over the frames of the splash screen until no frame is found.
		do {

			frame = ImageReader.getImage(splashPath + index + ".png");

			if (frame != null) {

				// Scale the frame
				frame = frame.getScaledCopy(gc.getScreenWidth(), gc.getScreenHeight());

				// Add the frame to the splash screen.
				splash.addFrame(frame, frameRate);

				index++;
			}

			// If frame is null then there are no more frames.
		} while (frame != null);

		// Set the splash to finish at the final frame.
		splash.stopAt(index);

	}

	@Override
	public void parseButton(Frame frame, int key, Point mousePosition) {
		frame.pressButton(key, mousePosition);
	}

	@Override
	public void render(GameContainer gc, Frame frame) {
		// splash.draw();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);

		// If the splash page has finished enter the main menu
		if (splash.isStopped()) {
			game.getView().enterMainMenu();
		}

	}

	@Override
	public Music getMusic() {
		return null;
	}

	private final class Splash extends Animation {

		@Override
		protected final void finalize() throws Throwable {

			// Destroy all the images in the splash screen.
			for (int index = 0; index < getFrameCount(); index++) {
				getImage(index).destroy();
			}

			super.finalize();
		}

	}

}
