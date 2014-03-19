
package model.board;
import model.*;

/**
 * Represents the situation where player #0 has won.
 */
class Player0WonState extends ATerminalState {
    public static Player0WonState Singleton = new Player0WonState();
    private Player0WonState() {}

    public <R, P> R execute(ABoardModel host, IBoardStatusVisitor<R, P> visitor, @SuppressWarnings("unchecked") P... params ) {
        return visitor.player0WonCase(host, params);
    }
}
