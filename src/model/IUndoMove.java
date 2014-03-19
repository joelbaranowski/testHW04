package model;

/**
 * This interface encapsulates a command that can undo a move performed by IBoardModel.makeMove().
 */
public interface IUndoMove {
    /**
     * Performs the undo-ing of the move.
     * @param undoVisitor Variant behavior for  valid and invalid undo's.
     */
    public abstract void apply(IUndoVisitor undoVisitor);
}