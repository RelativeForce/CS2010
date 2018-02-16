package peril.views.slick;

import peril.views.slick.util.Point;

public interface EventListener {

	void mouseClick(Point mouse, int mouseButton);

	void mouseHover(Point mouse, int delta);

	void buttonPress(int key, Point mouse);
	
	void draw(Frame frame);

}
