package peril.ui.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.io.FileReader;
import peril.ui.components.Viewable;

public final class LoadingScreen extends InteractiveState {

	private static String NAME = "loading screen";

	private List<FileReader> readers;

	private int index;

	private int progress;

	private int total;

	private Viewable background;

	private Music music;

	public LoadingScreen(Game game, int id) {
		super(game, NAME, id);
		index = 0;
		progress = 0;
		total = 0;
		readers = new ArrayList<>();
	}

	@Override
	public void addImage(Viewable image) {
		background = image;
		super.addImage(image);
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		music = getGame().musicHelper.read("loading");
		background.setImage(background.getPosition(),
				background.getImage().getScaledCopy(gc.getWidth(), gc.getHeight()));
		super.enter(gc, sbg);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		int windowWidth = gc.getWidth();
		int windowHeight = gc.getHeight();

		int windowPadding = (windowHeight / 10);

		Point barPosition = new Point(windowWidth / 20, windowHeight - windowPadding);

		double barWidth = windowWidth - (windowHeight / 10) - windowPadding;

		int currentProgress = (int) (progress * (barWidth / total));

		g.drawRect(barPosition.x, barPosition.y, (int) barWidth, 40);

		if (currentProgress > 0) {
			g.fillRect(barPosition.x, barPosition.y, currentProgress, 40);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);

		if (index == readers.size()) {
			getGame().autoDistributeCountries();
			getGame().enterState(getGame().setup.getID());
		} else {

			FileReader reader = readers.get(index);

			if (!reader.isFinished()) {
				reader.parseLine();
				progress++;
			} else {
				index++;
			}
		}

	}

	public void addReader(FileReader reader) {

		if (getGame().getCurrentState().getID() == this.getID()) {
			throw new IllegalStateException("You cant add a file reader when the game is already loading.");
		}

		total += reader.getLength();

		readers.add(reader);
	}

	@Override
	public void parseClick(int button, Point click) {
		super.clickedButton(click);
	}

	@Override
	public void parseButton(int key, char c, Point mousePosition) {
		// DO NOTHING
	}

	@Override
	public Music getMusic() {
		return music;
	}

}
