package peril.ui;

import java.util.List;

import peril.Point;

public interface ButtonContainer {
	
	public void addButton(Button button);
	
	public List<Button> getButtons();
	
	public boolean clickedButton(Point click);

}
