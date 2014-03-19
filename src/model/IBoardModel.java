
package model;
import gameIO.*;

/**
 * This interface represents the invariant, encapsulated rules and behaviors of a game.
 */
public interface IBoardModel {
    /**
     * Gets the number of rows (height) and colums (width) in the game as a Dimension object.
     * @return
     */
    public abstract java.awt.Dimension getDimension();

    /**
     * Used to ask the board to place a given player's token on the given (row, col).
     * If the requested move is invalid, the move will not take place and the
     * invalidMoveCase() of the ICheckMoveVisitor will be called.
     * If the move is valid, the move will be taken and then the validMoveCase()
     * of the ICheckMoveVisitor will be called.
     * After a valid move has been made, the board will execute the IBoardStatusVisitor.
     * @param r The row to place the token on.
     * @param c The column to place the token on.
     * @param plyr The player number {0, 1}, whose token is being placed.
     * @param cm The ICheckMoveCommand encapsulating the variant behaviors for
     * valid and invalid moves.
     * @param bs the IBoradStatusVisitor that encapsulates the variant behaviors
     * for each possible resulting state of the board.
     * @return An IUndoMove object that can undo this move.
     */
    public abstract IUndoMove makeMove(int r, int c, int plyr, ICheckMoveVisitor cm, IBoardStatusVisitor<Void, Void> bs);

    /**
     * Resets the board back to its defaults.
     */
    public abstract void reset();

    /**
     * Applies the supplied IBoardLambda to all valid moves for the given player,
     * using the supplied parameter.
     * The IBoardLambda.noApply() method is called if there are no valid moves
     * for that player.
     * @param player
     * @param lambda
     * @param param
     */
	public abstract <T> void map(int player,IBoardLambda<T> lambda, @SuppressWarnings("unchecked") T... params);

    /**
     * Maps the supplied lambda for all board locations, independent of whether
     * they  are valid or invalid moves.
     * The player number and the parameter are simply passed to the IBoardLamda.
     * The IBoardLambda.noApply() method is called if the board is in a terminal state.
     * @param player
     * @param lambda
     * @param param
     */
    public abstract <T> void mapAll(int player, IBoardLambda<T> lambda, @SuppressWarnings("unchecked") T... params);

    /**
     * Returns the player at the supplied (row, col).
     * @param row
     * @param col
     * @return -1 = player #0, 0 = no player, +1 = player #1
     */
    public abstract int playerAt(int row, int col);

    /**
     * Hook method for the IBoardStatus visitor.
     * @param visitor
     * @param param
     * @return
     */
    public abstract <R, P> R execute(IBoardStatusVisitor<R, P> visitor, @SuppressWarnings("unchecked") P... params);

    /**
     * Redraws the entire board on the view using the suppled ICommand.
     * This is more generally useful than upadating the screen one location at
     * a time, as it automatically handles situations where multiple tokens may
     * be placed/changed on the board.
     * @param command
     */
    public abstract void redrawAll(ICommand command);

    /**
     * Returns true if the supplied player {0, 1}, has no valid moves.
     * Note: given a player, the other player is 1 - player.
     * @param player
     * @return
     */
    public abstract boolean isSkipPlayer(int player);
}
