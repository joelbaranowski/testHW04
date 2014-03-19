
package model;

import java.awt.Dimension;


/**
 * Adminsitrative interface for the view.
 */
public interface IViewAdmin {
    /**
     * Tells the view that the game is a draw.
     */
    public abstract void draw();

    /**
     * Tells the view that the given player {0, 1} has won.
     * @param player
     */
    public abstract void win(int player);

    /**
     * Resets the view.
     */
    public abstract void reset();

    /**
     * Sets the board dimensions
     * @param dimension
     */
	public abstract void setDimension(Dimension dimension);
}
