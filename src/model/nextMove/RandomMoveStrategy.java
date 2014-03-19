
package model.nextMove;

import java.awt.Point;
import model.*;

/**
 * Selects a random move within the bounds of the board.  The result may be an
 * invalid (i.e. illegal) move.
 */
public class RandomMoveStrategy implements INextMoveStrategy {
    public Point getNextMove(IModel context, int player) {
        int width = context.getBoardModel().getDimension().width;
        int height = context.getBoardModel().getDimension().height;
        return new Point ((int)(Math.random() * width), (int)(Math.random() * height));
    }
}
