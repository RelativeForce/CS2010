package peril.ui;

import org.newdawn.slick.Image;

import peril.Point;
import peril.ui.visual.Clickable;

public class Button extends Clickable {
	
	
	Button(Point position, Image image){
		super();
		this.setImage(position, image);
	}
}
