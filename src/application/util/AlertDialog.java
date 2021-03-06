package application.util;

import application.Main;
import application.StageManager;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

/**
 * Shows a new alert dialog window.
 * @author Sanchez
 *
 */
public class AlertDialog extends javafx.scene.control.Alert {

	/**
	 * Shows a new alert dialog window.
	 *
	 * @param alertType
	 */
	public AlertDialog(AlertType alertType) {
		super(alertType);
		this.stylizeAlert();
	}

	/**
	 * Shows a new alert dialog window with custom text and buttons.
	 *
	 * @param alertType
	 * @param contentText
	 * @param buttons
	 */
	public AlertDialog(AlertType alertType, String contentText, ButtonType... buttons) {
		super(alertType, contentText, buttons);
		this.stylizeAlert();
	}
	
	/**
	 * Styles the alert dialog window.
	 *
	 */
	private void stylizeAlert() {
		this.initStyle(StageStyle.TRANSPARENT);
		this.setHeaderText("Error");
		
		DialogPane dialogPane = this.getDialogPane();
		dialogPane.getScene().getStylesheets().add(Main.class.getResource(Util.getCSSname()).toExternalForm());
		dialogPane.getScene().setFill(null);
		dialogPane.getStyleClass().add("alert-dialog");

	}
}
