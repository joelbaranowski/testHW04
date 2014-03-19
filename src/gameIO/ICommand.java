
package gameIO;

/**
 * The command supplied by the view that enables the model to set a token for a particular player at a given (row, col) location on the screen., clear the display at a (row, col), and to show a particular message on the screen.
 */
public interface ICommand {
    /**
     * Displays the token for a particular player {0, 1} on the screen at (row, col).
     * @param row
     * @param col
     * @param player
     */
    public abstract void setTokenAt(int row, int col, int player);

    /**
     * Clears the displayed token at  (row, col) on the screen.
     * @param row
     * @param col
     */
    public abstract void clearTokenAt(int row, int col);

    /**
     * Displays the supplied message on the screen.
     * @param s
     */
    public abstract void setMessage(String s);
}
