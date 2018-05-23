package application.domain;

import java.util.ArrayList;
import java.util.List;

public class Player {
	
	private String name;
	
	private List<Card> reservedCards;
	
	private List<Card> ownedCards;
	private List<Noble> ownedNobles;
	
	private TokenList tokenList;
	
	
	public Player(String name) {
		this.name = name;
		
		this.reservedCards = new ArrayList<>();
		this.ownedCards = new ArrayList<>();
		this.ownedNobles = new ArrayList<>();
		this.tokenList = new TokenList();
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Card> getReservedCards() {
		return reservedCards;
	}
	
	public void addReservedCard(Card card)
	{
		reservedCards.add(card);
	}
	
	public List<Card> getOwnedCards() {
		return ownedCards;
	}
	
	public List<Noble> getOwnedNobles() {
		return ownedNobles;
	}

	public TokenList getTokenList()
	{
		return tokenList;
	}
	
	public int getPrestige()
	{
		int prestige = 0;
		
		for(Card ownedCard : ownedCards)
		{
			prestige += ownedCard.getPrestigeValue();
		}
		
		for(Noble ownedNoble : ownedNobles)
		{
			prestige += ownedNoble.getPrestigeValue();
		}
		
		return prestige;
	}

}