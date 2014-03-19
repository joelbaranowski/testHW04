
package model;

import java.awt.Point;

/**
 * An abstract strategy used to calculate the next move for a player.
 */
public interface INextMoveStrategy {
    /**
     * Calculates the next move as a Point (x = column, y = row).
     * @param context The IModel being used.
     * @param player The player whose move is being calculated.
     * @return Point where x = column and y = row.
     */
    public abstract Point getNextMove(IModel context, int player);
}
