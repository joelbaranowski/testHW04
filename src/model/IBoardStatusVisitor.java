
package model;

/**
 * The visitor to an IBoardModel that provides variant behaviors for the
 * different states of the board.
 */
public interface IBoardStatusVisitor<R, P> {
    /**
     * Executed when player #0 has won the game.
     * @param host
     * @param param
     * @return
     */
    public abstract R player0WonCase(IBoardModel host, @SuppressWarnings("unchecked") P... params);

    /**
     * Executed when player #1 has won the game.
     * @param host
     * @param param
     * @return
     */
    public abstract R player1WonCase(IBoardModel host, @SuppressWarnings("unchecked") P... params);

    /**
     * Executed when the game is a draw, that is, neither player can make a
     * valid move but there is no determinable winner.
     * @param host
     * @param param
     * @return
     */
    public abstract R drawCase(IBoardModel host, @SuppressWarnings("unchecked") P... params);

    /**
     * Executed when no-one has won the game yet.    One of the players may not
     * be able to make a move however.
     * @param host
     * @param param
     * @return
     */
    public abstract R noWinnerCase(IBoardModel host, @SuppressWarnings("unchecked") P... params);
}
