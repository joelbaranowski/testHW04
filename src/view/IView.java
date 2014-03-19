
package view;

import java.util.List;
import java.awt.*;
import gameIO.*;

/**
 * The running interface of the view.
 */
public interface IView<TPlayer> {
    /**
     * Gets the ICommand object used by the model to display tokens, etc. on the view.
     */
    public abstract ICommand getCommand();

    /**
     * Stores a requestor for use by the view.
     * @param requestor
     */
    public abstract void setRequestor(IViewRequestor requestor);

    /**
     * Installs a Vector of Objects to be used by the view to give a choice of players.
     * The view will select two of the elements of the Vector (possibly the same
     * element twice), to be used to determine the players of the game.
     * @param players
     */
    public abstract void setPlayers(List<TPlayer> players);

    /**
     * Sets the width (columns) and height (rows) of the displayed board.
     * This information should be obtained from the model.
     * @param size
     */
    public abstract void setDimension(Dimension size);
}
