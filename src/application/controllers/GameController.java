package application.controllers;

import java.rmi.RemoteException;
import java.util.Optional;

import application.domain.Game;
import application.domain.Gem;
import application.domain.MoveType;
import application.domain.ReturnTokens;
import application.domain.TempHand;
import application.util.ConfirmDialog;
import application.views.ReturnTokensView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

// TODO: Auto-generated Javadoc
/**
 * The Class GameController.
 *
 * @author Sanchez
 */
public class GameController {
	private Game game;

	/**
	 * Instantiates a new game controller.
	 *
	 * @param game the game
	 */
	public GameController(Game game) {
		this.game = game;
	}

	/**
	 * Reserve card.
	 *
	 * @param moveType
	 * @throws RemoteException
	 */
	public void reserveCard(MoveType moveType) throws RemoteException {
		game.findSelectableCards(moveType);
	}
	
	/**
	 * Purchase card.
	 *
	 * @param moveType
	 * @throws RemoteException
	 */
	public void purchaseCard(MoveType moveType) throws RemoteException {
		game.findSelectableCards(moveType);
	}
	
	/**
	 * Take tokens.
	 *
	 * @param moveType
	 * @throws RemoteException
	 */
	public void takeTokens(MoveType moveType) throws RemoteException {
		game.setTokensSelectable(moveType);
	}

	/**
	 * Debug next turn.
	 *
	 * @throws RemoteException
	 */
	public void debugNextTurn() throws RemoteException {
		game.nextTurn();
	}
	
	/**
	 * End turn.
	 *
	 * @throws RemoteException
	 */
	public void endTurn() throws RemoteException {
		game.getEndTurn().endTurn();
	}
	
	
	/**
	 * Leave game show an Confimartion Dialoag and bring te player to the mainscreen
	 */
	public void leaveGame() {
		ConfirmDialog dialog = new ConfirmDialog(AlertType.CONFIRMATION);
		dialog.setTitle("Confirmation Dialog");
		dialog.setHeaderText("You are leaving the game");
		dialog.setContentText("Are you sure you wish to quit?");
		
		Optional<ButtonType> results = dialog.showAndWait();
		if (results.get() == ButtonType.OK){
			try {
				this.terminateGame();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Reserve card from deck.
	 *
	 * @param cardRowIdx 
	 * @throws RemoteException
	 */
	public void reserveCardFromDeck(int cardRowIdx) throws RemoteException {
		game.reserveCardFromDeck(cardRowIdx);
	}

	/**
	 * On field card clicked.
	 *
	 * @param cardRowIdx
	 * @param cardIdx
	 * @throws RemoteException
	 */
	public void onFieldCardClicked(int cardRowIdx, int cardIdx) throws RemoteException {
		game.addCardToTempFromField(cardRowIdx, cardIdx);
	}
	
	/**
	 * On reserved card clicked.
	 *
	 * @param cardIdx
	 * @throws RemoteException
	 */
	public void onReservedCardClicked(int cardIdx) throws RemoteException {
		game.addCardToTempFromReserve(cardIdx);
	}

	/**
	 * On field token clicked.
	 *
	 * @param gemType
	 * @throws RemoteException
	 */
	public void onFieldTokenClicked(Gem gemType) throws RemoteException {	
		game.addTokenToTemp(gemType);
	}
	
	/**
	 * On field selected token clicked.
	 *
	 * @param gemType
	 * @throws RemoteException
	 */
	public void selectedTokenClicked(Gem gemType) throws RemoteException {
		game.removeTokenFromTemp(gemType);
	}
	
	/**
	 * Gets the temp hand.
	 *
	 * @return TempHand
	 * @throws RemoteException
	 */
	public TempHand getTempHand() throws RemoteException {
		return game.getPlayingField().getTempHand();
	}
/*	public void clearDeckSelection() throws RemoteException {
		game.getPlayingField().setDeckDeselected();
		game.updatePlayingFieldAndPlayerView();			
	}*/

	/**
	 * Reset turn.
	 *
	 * @throws RemoteException the remote exception
	 */
	public void resetTurn() throws RemoteException {
		game.cleanUpSelections();		
	}
	
	/**
	 * 
	 * @return 
	 * @throws RemoteException
	 */
	public boolean reserveCardInventoryFull() throws RemoteException {
		return game.reserveCardInventoryFull();
	}
	/**
	 * 
	 * @throws Open Return Tokens view
	 */
	public void showReturnTokensWindow() throws RemoteException {
		ReturnTokens data = new ReturnTokens(game);
		ReturnTokenController controller = new ReturnTokenController(data);
		ReturnTokensView view = new ReturnTokensView(data, controller);
	}
/**
 * 
 * @throws RemoteException
 */
	public void terminateGame() throws RemoteException {
		game.terminateGame();
		
	}
	
}