
package model;

/**
 * This interface defines an abstract function that an IBoardModel's map()
 * and mapAll() will apply to their respective elements.
 */
public interface IBoardLambda<T> {
    /**
     * This method is called for by IBoardModel.map and IBoardModel.mapAll()
     * for each element they respectively cover.
     * @param player The player passed to the mapping method.
     * @param host The IBoardModel that is doing the mapping.
     * @param param The arbitrary parameter passed to the mapping method.
     * @param row The the current row that is being processed.
     * @param col The current column that is being processed.
     * @param value The value {-1, 0, +1} found at the above (row, col)
     * where -1 = player #0, 0 = no player, +1 = player #1.
     * @return
     */
    public abstract boolean apply(int player, IBoardModel host,
                                  int row, int col, int value, @SuppressWarnings("unchecked") T... params);
    /**
     * This method is called by IBoardModel.map() when the system is in a
     * terminal state or there are no valid moves for the supplied player.
     * This method is never called by mapAll().
     * @param player The player passed to the mapping method.
     * @param host The IBoardModel that is doing the mapping.
     * @param param The arbitrary parameter passed to the mapping method.
     */
    public abstract void noApply(int player, IBoardModel host, @SuppressWarnings("unchecked") T... params);
}
