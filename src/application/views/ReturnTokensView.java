package application.views;

import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Map;
import application.controllers.ReturnTokenController;
import application.domain.Gem;
import application.domain.ReturnTokens;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * @author Tom
 *
 */
public class ReturnTokensView {

	private BorderPane pane;

	private Stage stage;
	private HBox gemCounterDisplay;
	private Button confirmButton;

	private ReturnTokenController returnTokenController;

	public ReturnTokensView(ReturnTokens returnTokens, ReturnTokenController returnTokenController)
			throws RemoteException {
		this.returnTokenController = returnTokenController;
		this.pane = new BorderPane();

		Label labelText = new Label("You have more than 10 tokens. Please discard tokens until you have 10.");
		labelText.setFont(Font.font(26.0));

		gemCounterDisplay = new HBox(); // big hbox placed in the middle of the
										// pane with in it up to 6 for the gems
										// of the player.
		gemCounterDisplay.setAlignment(Pos.CENTER);

		confirmButton = new Button("Confirm");
		confirmButton.setOnAction(e -> {
			try {
				returnTokenController.confirmButton();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			stage.close();
		});

		HBox confirmBox = new HBox(confirmButton);
		confirmBox.setAlignment(Pos.CENTER);
		
		BorderPane.setAlignment(labelText, Pos.CENTER);

		pane.setTop(labelText);
		pane.setCenter(gemCounterDisplay);
		pane.setBottom(confirmBox);

		pane.setPadding(new Insets(10));

		Scene scene = new Scene(pane, 900, 550);
		scene.getStylesheets().add(getClass().getResource("../application.css").toExternalForm());

		stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Return tokens");
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setResizable(false);
		stage.show();

		returnTokens.registrate(this);
	}

	public void modelChanged(ReturnTokens returnTokens) {
		this.updateTokenGemCounts(returnTokens);
		this.updateConfirmButton(returnTokens);
	}

	private void updateConfirmButton(ReturnTokens returnTokens) {
		confirmButton.setDisable(!returnTokens.isAllowConfirm());
	}

	private void updateTokenGemCounts(ReturnTokens returnTokens) {
		gemCounterDisplay.getChildren().clear();

		LinkedHashMap<Gem, Integer> gemsCount = returnTokens.getTokenListNew().getTokenGemCount();

		// Create a vbox for every gem with curent amount of player.
		for (Map.Entry<Gem, Integer> entry : gemsCount.entrySet()) {
			VBox gemBox = createTokenGemCountBox(entry.getKey(), entry.getValue(), GameView.tokenSizeRadius);
			gemBox.setPadding(new Insets(10));
			gemCounterDisplay.getChildren().add(gemBox);

		}
	}

	// the display of a gem type and amount of tokens of that type owned, with
	// buttons to remove and add.
	private VBox createTokenGemCountBox(Gem gemType, int count, int tokenSizeRadius) {

		// buttons to edit amount of coins and confirmation of the turn.
		// TODO add Button action events.
		Button minusTokenButton = new Button("-");
		Button plusTokenButton = new Button("+");

		minusTokenButton.setMinWidth(85);
		plusTokenButton.setMinWidth(85);

		TokenView tokenView = new TokenView(gemType, tokenSizeRadius);
		tokenView.asPane().setPadding(new Insets(10));

		Label tokenCountLabel = new Label(String.valueOf(count));

		minusTokenButton.setOnAction(e -> {
			try {
				returnTokenController.minusToken(gemType);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		plusTokenButton.setOnAction(e -> {
			try {
				returnTokenController.plusToken(gemType);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		tokenCountLabel.getStyleClass().add("token-count");
		tokenCountLabel.setFont(Font.font(tokenSizeRadius * 2));

		VBox tokenColumn = new VBox(tokenView.asPane(), plusTokenButton, tokenCountLabel, minusTokenButton);
		tokenColumn.setAlignment(Pos.CENTER);

		return tokenColumn;
	}

}