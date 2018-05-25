package application.controllers;

import application.StageManager;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Roy
 *
 */
public class MenuControllerImpl implements MenuController {

	@Override
	public void joinLobby(Pane rootNode) {
		StageManager.getInstance().switchScene(rootNode);
	}

	@Override
	public void hostPreviousGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hostNewGame() {
		// TODO Auto-generated method stub

	}


}
