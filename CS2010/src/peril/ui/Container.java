package peril.ui;

import peril.Point;
import peril.ui.components.Viewable;

public interface Container {

	public void addButton(Button button);

	public boolean clickedButton(Point click);

	public String getName();

	public void addImage(Viewable image);
}
