
package model;

/**
 * Encapsulates the variant behavior for an undo process.
 */
public interface IUndoVisitor {
    /**
     * Called when there is no token to be undone, or if the undo is in someway
     * invalid or cannot be performed.
     */
    public void noTokenCase();

    /**
     * This method is called if the undo was successful.
     */
    public void tokenCase(int value);
}
