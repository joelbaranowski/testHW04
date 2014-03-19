package model.board;

import model.*;

/**
 * Represents an abstract state of the board.
 */
abstract class ABoardState {
    abstract <T> void map(int player, IBoardLambda<T> lambda,  ABoardModel host, @SuppressWarnings("unchecked") T... params);
    abstract <R, P> R execute(ABoardModel host, IBoardStatusVisitor<R, P> visitor, @SuppressWarnings("unchecked") P... params);
}