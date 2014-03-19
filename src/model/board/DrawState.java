
package model.board;

import model.*;

/**
 * Represents the situation where the game is a draw.
 */
class DrawState extends ATerminalState {
    public static DrawState Singleton = new DrawState();
    private DrawState() {};

    public<R, P> R execute( ABoardModel host, IBoardStatusVisitor<R, P> visitor, @SuppressWarnings("unchecked") P... params)  {
        return visitor.drawCase(host, params);
    }
}
