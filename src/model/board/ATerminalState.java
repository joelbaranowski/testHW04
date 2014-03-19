
package model.board;

import model.*;

/**
 * Represents a terminal state of the board.
 */
abstract class ATerminalState extends ABoardState {
    @SuppressWarnings("unchecked")
	public <T> void map(int player, IBoardLambda<T> lambda, ABoardModel host, T... params)  {
        // no valid moves available in a terminal state.
        lambda.noApply(player, host, params);
    }

    public abstract <R, P> R execute( ABoardModel host, IBoardStatusVisitor<R, P> visitor, @SuppressWarnings("unchecked") P... params);
}
