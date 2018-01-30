package peril.views;

import java.util.function.Consumer;

import peril.controllers.GameController;
import peril.io.FileParser;
import peril.io.SaveFile;
import peril.model.ModelPlayer;
import peril.model.states.ModelState;

public interface View {

	void start() throws Exception;
	
	void init(GameController game) throws Exception;
	
	void setWinner(ModelPlayer winner);
	
	boolean isCurrentState(ModelState state);
	
	void setHelpMenuPage(int pageId);
	
	ModelView getModelView();
	
	void updateChallenges();

	void addLoser(ModelPlayer player);

	void showToolTip(String text);
	
	void toggleMusic(boolean state);
	
	boolean isMusicOn();

	void loadGame() throws Exception;

	void toggleChallengeMenu(boolean state);
	
	void togglePauseMenu(boolean state);
	
	void toggleWarMenu(boolean state);

	void save();
	
	boolean isPaused();

	void toggleHelpMenu(boolean state);
	
	void nextHelpPage();
	
	void previousHelpPage();

	void exit();
	
	void enterMainMenu();

	void enterReinforce();

	void enterCombat();

	void enterFortify();
	
	FileParser getMapLoader(String mapPath, SaveFile save);

	void attack();
	
	void centerBoard();

	void forEachLoser(Consumer<ModelPlayer> task);

}
