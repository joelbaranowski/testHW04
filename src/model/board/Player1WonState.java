
package model.board;
import model.*;

/**
 * Represents the situation where player #1 has won.
 */
class Player1WonState extends ATerminalState {
    public static Player1WonState Singleton = new Player1WonState();
    private Player1WonState() {}

    public <R, P> R execute(ABoardModel host, IBoardStatusVisitor<R, P> visitor, @SuppressWarnings("unchecked") P... params )  {
        return visitor.player1WonCase(host, params);
    }
}
