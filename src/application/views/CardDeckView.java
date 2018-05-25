package application.views;

import application.domain.Card;
import application.domain.CardDeck;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
/**
 * 
 * @author Sanchez
 *
 */
public class CardDeckView extends CardView {
	
	private CardDeck deck;
	
	public CardDeckView(CardDeck deck, int sizeX, int sizeY) {
		super(sizeX, sizeY);
		this.deck = deck;
		
		this.buildUI();
	}
	
	private void buildUI()
	{
    	rect = new Rectangle(sizeX, sizeY);
    	
    	// Add image
        ImagePattern imagePattern = new ImagePattern(new Image(getImagePath()));
        rect.setFill(imagePattern);
        
        // Make rounded corners
        rect.setArcHeight(10);
        rect.setArcWidth(10);
        rect.setStroke(Color.BLACK);
        rect.setStrokeType(StrokeType.INSIDE);
        rect.setStrokeWidth(5);
        
        // Add to root node
        root = new StackPane(rect);
	}
	
	protected String getImagePath()
	{
		return String.format("file:resources/cards/%s/%s.png", deck.top().getLevel().name().toLowerCase(), deck.top().getLevel().name().toLowerCase());
	}
}
