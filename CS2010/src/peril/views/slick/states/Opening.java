package peril.views.slick.states;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

import peril.controllers.GameController;
import peril.views.slick.Frame;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.io.ImageReader;
import peril.views.slick.util.Point;

public class Opening extends InteractiveState {

	private static final String NAME = "Opening";

	private int index;

	private final List<SplashState> states;

	public Opening(GameController game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);

		index = 0;

		states = new LinkedList<>();

		addStates();
	}

	private void addStates() {
		states.add(new SpinningGoat());
		states.add(new Bah());
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		states.forEach(state -> {
			try {
				state.init(gc);
			} catch (SlickException e) {
				e.printStackTrace();

			}

		});

	}

	@Override
	public void parseButton(Frame frame, int key, Point mousePosition) {
		frame.pressButton(key, mousePosition);
	}

	@Override
	public void render(GameContainer gc, Frame frame) {
		states.get(index).draw(frame);
	}

	@Override
	public void update(GameContainer gc, int delta, Frame frame) {

		SplashState current = states.get(index);

		if (current.isFinished()) {

			if (index == states.size() - 1) {
				slick.enterMainMenu();
			} else {
				index++;
			}

		} else {
			current.update(gc);

		}

	}

	@Override
	public Music getMusic() {
		return null;
	}

	interface SplashState {

		void draw(Frame g);

		void init(GameContainer gc) throws SlickException;

		void update(GameContainer gc);

		boolean isFinished();

	}

	private final class SpinningGoat implements SplashState {

		private final static int NUMBER_OF_ROTATIONS = 3;

		private final static int NUMBER_OF_FRAMES = 120;

		private Image goat;

		private float goatX;

		private float goatY;

		private float rotationSpeed;

		private float maxY;

		private float fallingSpeed;

		private boolean isFinished;

		public SpinningGoat() {
			isFinished = false;
		}

		@Override
		public void draw(Frame frame) {
			frame.draw(goat, (int) goatX, (int) goatY);
		}

		@Override
		public void init(GameContainer gc) {

			goat = ImageReader.getImage(game.getDirectory().getSplashPath()+ "goat.png");

			final float goatScale = (float) (gc.getWidth() / 3) / goat.getWidth();

			goat = goat.getScaledCopy(goatScale);

			goatX = (gc.getWidth() - goat.getWidth()) / 2;
			goatY = -goat.getHeight();

			maxY = gc.getHeight() + goat.getHeight();

			fallingSpeed = (maxY - goatY) / NUMBER_OF_FRAMES;

			rotationSpeed = (360f / NUMBER_OF_FRAMES) * NUMBER_OF_ROTATIONS;

		}

		@Override
		public void update(GameContainer gc) {
			goat.rotate(rotationSpeed);

			goatY += fallingSpeed;

			if (goatY >= maxY) {
				isFinished = true;
			}
		}

		@Override
		public boolean isFinished() {
			return isFinished;
		}
	}

	private final class Bah implements SplashState {

		private Sound bah;

		private boolean isFinished;

		public Bah() {
			isFinished = false;
		}

		@Override
		public void draw(Frame g) {
			// Draw nothing
		}

		@Override
		public void init(GameContainer gc) throws SlickException {
			bah = new Sound(game.getDirectory().getMusicPath() + "bah.ogg");
		}

		@Override
		public void update(GameContainer gc) {
			bah.play();

			isFinished = true;
		}

		@Override
		public boolean isFinished() {
			return isFinished;
		}
	}

	private final class TitleCard implements SplashState {

		private boolean isFinished;

		public TitleCard() {
			isFinished = true;
		}

		@Override
		public void draw(Frame g) {

		}

		@Override
		public void init(GameContainer gc) throws SlickException {

		}

		@Override
		public void update(GameContainer gc) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isFinished() {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
