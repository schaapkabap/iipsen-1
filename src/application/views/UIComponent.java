package application.views;

import javafx.scene.layout.Pane;

/**
 * The Interface UIComponent.
 *
 * @author Sanchez
 */
public interface UIComponent {
	
	/**
	 * Returns the view as a pane so that it can be modified with JavaFX
	 * methods.
	 *
	 * @return Pane
	 */
	public Pane asPane();
}
