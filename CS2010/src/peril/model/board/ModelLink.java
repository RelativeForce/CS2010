package peril.model.board;

public class ModelLink {
	
	private boolean isBlocked;
	
	private int duration;
	
	public ModelLink(boolean isBlocked){
		this.isBlocked = isBlocked;
		this.duration = 0;
	}
	
	public void block(int numberOfRounds) {
		duration = numberOfRounds;
		isBlocked = true;
	}
	
	public boolean isBlocked() {
		return isBlocked;
	}
	
	public void elapseBlock() {
		if(isBlocked()) {
			duration--;
			
			if(duration <= 0) {
				removeBlock();
			}
		}
	}

	public void removeBlock() {
		isBlocked = false;
		duration = 0;
	}
}
