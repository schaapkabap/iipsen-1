package application.controllers;

import application.domain.Card;
import application.domain.CardRowImpl;
import application.domain.Game;

/**
 * @author Sanchez
 *
 */
public class GameControllerImpl implements GameController {
	private Game game;

	public GameControllerImpl(Game game) {
		this.game = game;
	}

	@Override
	public void reserveCard() {
		// Creating POC variables - basically specifying: Hey controller, I clicked on this card
		CardRowImpl row = game.getPlayingField().getCardRows().get(1); // Second row
		Card card = row.getCardSlots()[1]; // Second card
		
		//if(!card.equals(card2wantdezeisspeciaal)) return;
		
		game.reserveCardFromField(row, card);
	}
	
	@Override
	public void endTurn() {
		if (game.getPlayers().get(game.getCurrentPlayerIdx()).getTokenList().getAll().size() + game.getTurn().getTokenList().getAll().size() > 10) {
			//TODO: ReturnTokens
		}
		//game.getPlayers().get(game.getCurrentPlayerIdx()).getOwnedCards().add(game.getTurn().getBoughtCard());
		//TODO: subtract tokens from player.
		//game.getPlayers().get(game.getCurrentPlayerIdx()).getReservedCards().add(game.getTurn().getReservedCard());
		//TODO: check for nobles
		game.getTurn().emptyhand();
		//TODO: Save Game
		//TODO: Check win condition
		//TODO: Determine next player
		
		game.nextTurn();
	}

	
	
}
