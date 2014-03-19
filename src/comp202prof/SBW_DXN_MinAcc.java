
package comp202prof;

public class SBW_DXN_MinAcc extends SBW_DXN_AAccumulator { /**
     * @param player
     * @SBGen Constructor
     */
    public SBW_DXN_MinAcc(int modelPlayer) {
        super(modelPlayer);
        _val = Integer.MAX_VALUE;
    }

    /**
     * @return
     */
    public SBW_DXN_AAccumulator makeOpposite() {
        return new SBW_DXN_MaxAcc(_modelPlayer);
    }

    /**
     * Search for the minimum value
     * @param row
     * @param col
     * @param childVal
     */
    public void updateBest(int row, int col, int childVal) {
        if (childVal <= _val) {
            _row = row;
            _col = col;
            _val = childVal;
        }
    }

    /**
     * The player who is trying to find the best move is the opposite player
     * to  the one that is using this accumulator.
     */
    public int getPlayer() { return 1 - _modelPlayer; }
}

