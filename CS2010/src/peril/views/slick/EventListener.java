package peril.views.slick;

public interface EventListener {

	void mouseClick(Point mouse);

	void mouseHover(Point mouse, int delta);

	void buttonPress(int Key, Point mouse);

}
