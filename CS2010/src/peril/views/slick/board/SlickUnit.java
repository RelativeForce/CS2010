package peril.views.slick.board;

import org.newdawn.slick.Image;

import peril.model.board.ModelUnit;
import peril.views.slick.Clickable;
import peril.views.slick.Region;

public class SlickUnit extends Clickable{
	
	public final ModelUnit model;
	
	public final String fileName;
	
	public SlickUnit(ModelUnit model, Image image) {
		super(new Region(image), image);
		this.model = model;
		this.fileName = getImage().getResourceReference();
	}

}
