
package model;

/**
 * This interface encapsulates the commands to follow when the model/board has
 * determined that a valid or invalid move was requested.
 */
public interface ICheckMoveVisitor {
    /**
    * The model calls this method after a valid move is been requested.
    */
    public abstract void invalidMoveCase();

    /**
    * This method is call by the model when an invalid move has been requested.
    */
    public abstract void validMoveCase();
}
