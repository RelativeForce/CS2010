package peril.views.slick;

import java.util.ArrayList;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Holds all the visual elements of the frame.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Frame {

	private final ArrayList<LinkedList<Button>> planes;
	
	public final Graphics g;

	public Frame(Graphics g) {
		planes = new ArrayList<>();
		this.g= g;
	}

	public void draw(Button button) {
		add(button);
		g.drawImage(button.getImage(), button.getPosition().x, button.getPosition().y);
	}

	private void add(Button button) {

		boolean placed = false;

		for (LinkedList<Button> plane : planes) {

			boolean collided = false;

			for (Button planeButton : plane) {

				if (Region.overlap(button.getRegion(), planeButton.getRegion())) {
					collided = true;
				}

			}

			if (!collided) {
				placed = true;
				plane.add(button);
				break;
			}
		}
		
		if(!placed) {
			
			final LinkedList<Button> newPlane = new LinkedList<>();
			newPlane.add(button);
			planes.add(newPlane);
		}
	}

	public boolean click(Point click) {
		
		boolean clickProcessed = false;
		
		for(int planeIndex = planes.size() - 1; planeIndex >= 0; planeIndex--) {
			
			for(Button button : planes.get(planeIndex)) {
				
				if(button.isClicked(click)) {
					button.click();
					clickProcessed = true;
					break;
				}
			}
			
			if(clickProcessed) {
				break;
			}
		}
		
		return clickProcessed;
		
	}
	
	public void draw(Image image, int x, int y) {
		g.drawImage(image, x, y);
	}
	
	public void draw(Font font, String text, int x, int y) {
		font.draw(g, text, x, y);
	}
	
	public void setColor(Color color) {
		g.setColor(color);
	}

	public void drawLine(Point a, Point b) {
		g.drawLine(a.x, a.y, b.x, b.y);
		
	}

}
