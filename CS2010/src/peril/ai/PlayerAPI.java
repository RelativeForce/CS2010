package peril.ai;

import peril.board.Board;

public interface PlayerAPI{
	
	void reinforce(Board board);
	
	void attack(Board board);
	
	void fortify(Board board);
	
}

