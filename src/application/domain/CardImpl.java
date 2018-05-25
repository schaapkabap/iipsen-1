package application.domain;

import java.util.Map;

/**
 * @author Sanchez
 *
 */
public class CardImpl implements Card {

	private boolean reservedFromDeck = false;
	private CardLevel level;
	private int prestigeValue;
	private String illustration;
	private Gem bonusGem;
	private Map<Gem, Integer> costs;
	
	
	public CardImpl(CardLevel level, int prestigeValue, String illustration, Gem bonus, Map<Gem, Integer> costs) {
		this.level = level;
		this.prestigeValue = prestigeValue;
		this.illustration = illustration;
		this.bonusGem = bonus;
		this.costs = costs;
	}
	
	
	public boolean isReservedFromDeck() {
		return reservedFromDeck;
	}

	// TODO: find a better way to make a card be a reserved card (perhaps separate class?)
	public void setReservedFromDeck(boolean reservedFromDeck) {
		this.reservedFromDeck = reservedFromDeck;
	}

	public int getPrestigeValue() {
		return prestigeValue;
	}

	public String getIllustration() {
		return illustration;
	}


	public Gem getBonusGem() {
		return bonusGem;
	}


	public Map<Gem, Integer> getCosts() {
		return costs;
	}


	public CardLevel getLevel() {
		return level;
	}

	
}
