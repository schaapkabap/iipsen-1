package application.views;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import application.StageManager;
import application.controllers.GameController;
import application.domain.ColorBlindModes;
import application.domain.Game;
import application.domain.GameObserver;
import application.domain.GameState;
import application.domain.MoveType;
import application.domain.Player;
import application.util.AlertDialog;
import application.util.Logger;
import application.util.Logger.Verbosity;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Game view contains the entire visualisation of the game after the lobby. It contains multiple sub-views as well 
 * @author Sanchez
 *
 */
public class GameView extends UnicastRemoteObject implements UIComponent, Disableable, GameObserver  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 506196798346655004L;
	
	
	public final static int cardSizeX = 130, cardSizeY = 180; 
	public final static int tokenSizeRadius = 45;
	
	public final static int opponentsSpacing = 0;
	
	public static List<ColorChangeable> colorBlindViews;
	
	private Game game;
	private GameController gameController;	
	
	private BorderPane root;
	
	private Button btnReserveCard;
	private Button btnPurchaseCard;
	private Button btnTakeTwoTokens;
	private Button btnTakeThreeTokens;
	private Button btnResetTurn;
	private Button btnEndTurn;
	
	
	private List<Button> moveButtons;
	private List<OpponentView> opponentViews;

	private Pane buttons;
	private Pane opponents;
	
	private PlayingFieldView playingFieldView;
	private PlayerView playerView;

	private Player player;

/**
 * Creates a new game view.
 *
 * @param game
 * @param gameController
 * @param player
 * @throws RemoteException
 */
	public GameView(Game game, GameController gameController, Player player) throws RemoteException {
	
		this.game = game;
		this.gameController = gameController;
		this.player = player;
		colorBlindViews = new ArrayList<>();
		
		this.opponentViews = new ArrayList<>();
		
		Logger.log("GameView::Building GameView UI for local player: " + player.getName(), Verbosity.DEBUG);
		this.buildUI();
		
		game.addObserver(this, player);
	}
	
	private void updateOpponentViews(String currentPlayerName) {
		for(OpponentView opponentView : this.opponentViews) {
			opponentView.updateCurrentPlayer(currentPlayerName.equals(opponentView.getPlayerName()));
		}
	}
	
	/* (non-Javadoc)
	 * @see application.domain.GameObserver#modelChanged(application.domain.Game)
	 */
	public void modelChanged(Game game) throws RemoteException
	{
		Platform.runLater(() ->
		{
			try {
				Logger.log("GameView::modelChanged()::Updating UI for local player: " + player.getName(), Verbosity.DEBUG);
				boolean disabled = game.isDisabled(this);
				Logger.log("GameView::modelChanged()::Is local player UI disabled? (aka not his turn): " + disabled, Verbosity.DEBUG);
				this.setDisabled(disabled);
				
				this.updateOpponentViews(game.getCurrentPlayer().getName());
				
				if(!disabled) {
					btnReserveCard.setDisable(gameController.reserveCardInventoryFull());
					btnPurchaseCard.setDisable(!game.anyCardsPurchasable());
					btnEndTurn.setDisable(game.getPlayingField().getTempHand().isEmpty());
					
					if(game.getEndTurn().returningTokens()) {
						gameController.showReturnTokensWindow();
					}	
				}	
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * Change color blind mode.
	 *
	 * @param mode
	 */
	public static void changeColorBlindMode(ColorBlindModes mode) {
		ColorBlindModes.CURRENT_MODE = mode;
		Iterator<ColorChangeable> i = colorBlindViews.iterator();
		while (i.hasNext()) {
			ColorChangeable view = i.next(); 
			if(view == null) {
				i.remove();
			}
			else {
				view.updateColors();
			}
		}
	}
	
	/**
	 * Build the actual game view.
	 *
	 * 
	 */
	private void buildUI()
	{
		root = new BorderPane();
		root.getStyleClass().add("game-view");
		
		try {
			System.out.println("[DEBUG] GameView::Building playing field");
			playingFieldView = buildPlayingField();
			
			System.out.println("[DEBUG] GameView::Building move buttons");
			buttons = buildButtons();
			
			System.out.println("[DEBUG] GameView::Building opponents");
			opponents = buildOpponents();
			
			
			System.out.println("[DEBUG] GameView::Building player");
			playerView = buildPlayer();
		} catch (RemoteException e) {
			new AlertDialog(AlertType.ERROR, "A network error has occured while setting up one of the views.").show();
			e.printStackTrace();
		}
		
		VBox center = new VBox(playingFieldView.asPane(), buttons);
		VBox.setVgrow(playingFieldView.asPane(), Priority.ALWAYS);
		
		root.setCenter(center);
		root.setLeft(opponents);
		root.setBottom(playerView.asPane());
	}
	
	/**
	 * Builds the playing field view (section with all the cards, nobles, tokens, etc.)
	 *
	 * @return PlayingFieldView
	 * @throws RemoteException
	 */
	private PlayingFieldView buildPlayingField() throws RemoteException {
		return new PlayingFieldView(game.getPlayingField(), gameController);
	}
	
	/**
	 * Builds the buttons the players uses to select their action for a turn.
	 *
	 * @return HBox
	 * @throws RemoteException 
	 */
	private HBox buildButtons() throws RemoteException {
		HBox buttons = new HBox(20);
		buttons.getStyleClass().add("buttons-view");
		buttons.setAlignment(Pos.CENTER);
		
		this.moveButtons = new ArrayList<>();
		
		btnReserveCard = new Button("Reserve Card");
		btnReserveCard.getStyleClass().add("move-button");
		btnReserveCard.setOnAction(e -> {
			try {
				btnReserveCard.setDisable(true);
				gameController.resetTurn();
				btnReserveCard.setDisable(true);
				gameController.reserveCard(MoveType.RESERVE_CARD);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		
		btnPurchaseCard = new Button("Purchase Card");
		btnPurchaseCard.getStyleClass().add("move-button");
		btnPurchaseCard.setOnAction(e -> {
			try {
				btnPurchaseCard.setDisable(true);
				gameController.resetTurn();
				btnPurchaseCard.setDisable(true);
				gameController.purchaseCard(MoveType.PURCHASE_CARD);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		btnTakeTwoTokens = new Button("Take Two Tokens");
		btnTakeTwoTokens.getStyleClass().add("move-button");
		btnTakeTwoTokens.setOnAction(e ->{
			try {
				btnTakeTwoTokens.setDisable(true);
				gameController.resetTurn();
				btnTakeTwoTokens.setDisable(true);
				gameController.takeTokens(MoveType.TAKE_TWO_TOKENS);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				new AlertDialog(AlertType.ERROR, "Dit hoor je niet te zien").show();
			}
		});
		
		btnTakeThreeTokens = new Button("Take Three Tokens");
		btnTakeThreeTokens.getStyleClass().add("move-button");		
		btnTakeThreeTokens.setOnAction(e ->{
			try {
				btnTakeThreeTokens.setDisable(true);
				gameController.resetTurn();
				btnTakeThreeTokens.setDisable(true);
				gameController.takeTokens(MoveType.TAKE_THREE_TOKENS);
			} catch (RemoteException e1) {
				e1.printStackTrace();
				new AlertDialog(AlertType.ERROR, "Dit hoor je niet te zien").show();
			}
		});
		
		btnResetTurn = new Button("Reset Turn");
		btnResetTurn.getStyleClass().add("move-button");
		btnResetTurn.setOnAction(e ->{
			try {
				btnResetTurn.setDisable(true);
				gameController.resetTurn();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		btnEndTurn = new Button("End Turn");
		btnEndTurn.getStyleClass().add("move-button");
		//btnEndTurn.setDisable(true);
		btnEndTurn.setOnAction(e -> {
				try {
					btnEndTurn.setDisable(true);
					gameController.endTurn();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		});

		this.moveButtons.addAll(Arrays.asList(btnReserveCard, btnPurchaseCard, btnTakeTwoTokens, btnTakeThreeTokens, btnResetTurn, btnEndTurn));
		buttons.getChildren().addAll(this.moveButtons);
		
		return buttons;
	}
	
	/**
	 * Build the box with the player information of the opponents.
	 *
	 * @return Pane
	 * @throws RemoteException
	 */
	private Pane buildOpponents() throws RemoteException
	{
		VBox opponentsRows = new VBox(opponentsSpacing);

		opponentsRows.setAlignment(Pos.CENTER_LEFT);
		opponentsRows.getStyleClass().add("opponents");
		opponentsRows.setPrefWidth(400);
		
		List<Player> players = game.getPlayers();
		for(Player player : players)
		{
			if(player.equals(this.player)) continue;
			System.out.println("[DEBUG] GameView::buildOpponents():: Building opponent player: " + player.getName());
			
			OpponentView opponentView = new OpponentView(player);
			this.opponentViews.add(opponentView);
			Pane opponentPane = opponentView.asPane();
			opponentsRows.getChildren().add(opponentPane);
		}
		
		return opponentsRows;
	}
	
	
	/**
	 * Builds the player view. This is the view with the player's own information
	 *
	 * @return PlayerView
	 * @throws RemoteException
	 */
	private PlayerView buildPlayer() throws RemoteException
	{
		return new PlayerView(this.player, gameController);
	}
	

	/* (non-Javadoc)
	 * @see application.views.UIComponent#asPane()
	 */
	public Pane asPane() {
		return root;
	}

	/* (non-Javadoc)
	 * @see application.views.Disableable#setDisabled(boolean)
	 */
	public void setDisabled(boolean disabled) {

		for(Button btn : this.moveButtons)
		{
			btn.setDisable(disabled);
		}
		
		this.playingFieldView.setDisabled(disabled);
		this.playerView.setDisabled(disabled);
	}

/**
 * close game for player. dialog is commented out because of double message
 *
 * @param gameState
 * @throws RemoteException
 */
	@Override
	public void disconnect(GameState gameState) throws RemoteException {
		Platform.runLater(() -> {
			StageManager.getInstance().showMainMenu();
			AlertDialog dialog;

			if(gameState == GameState.CLOSING)
			{
				new PopUpWindowView("A player has left the game", "The game has stopped.");
				dialog = new AlertDialog(AlertType.INFORMATION, "Server has been terminated. You have been disconnected.");
			} else if(gameState == GameState.FINISHED) {
				String winningPlayer = "A player";
				try {
					winningPlayer = game.getWinningPlayer().getName();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logger.log("GameView::disconnect()::Winning player = " + winningPlayer, Verbosity.DEBUG);
				new PopUpWindowView(winningPlayer + " has won the game!", "A player has won");
				dialog = new AlertDialog(AlertType.INFORMATION, "You have been disconnected from the server.");
			} else {
				dialog = new AlertDialog(AlertType.INFORMATION, "You have been disconnected from the server.");
			}
			
			dialog.setHeaderText("");
			dialog.show();
		});		
	}
	
	/**
	 * close game for player. dialog is commented out because of double message
	 *
	 * @param gameState
	 * @param winningPlayer
	 * @throws RemoteException
	 */
	@Override
	public void showWinScreen(GameState gameState, String winningPlayer) throws RemoteException {
		Platform.runLater(() -> {
			StageManager.getInstance().showMainMenu();
			AlertDialog dialog;

			if(gameState == GameState.CLOSING)
			{
				new PopUpWindowView("A player has left the game", "The game has stopped.");
				dialog = new AlertDialog(AlertType.INFORMATION, "Server has been terminated. You have been disconnected.");
			} else if(gameState == GameState.FINISHED) {
//				String winningPlayer = "A player";
//				try {
//					winningPlayer = game.getWinningPlayer().getName();
//				} catch (RemoteException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				Logger.log("GameView::disconnect()::Winning player = " + winningPlayer, Verbosity.DEBUG);
				new PopUpWindowView("has won the game!", winningPlayer);
				dialog = new AlertDialog(AlertType.INFORMATION, "You have been disconnected from the server.");
			} else {
				dialog = new AlertDialog(AlertType.INFORMATION, "You have been disconnected from the server.");
			}
			
			dialog.setHeaderText("");
			dialog.show();
		});		
	}
}
