
package model.nextMove;

public class MaxAcc extends AAccumulator {

    /**
     * @param player
     * @SBGen Constructor
     */
    public MaxAcc(int modelPlayer) {
        super(modelPlayer);
        _val = -Integer.MAX_VALUE;
    }

    /**
     * @return
     */
    public AAccumulator makeOpposite() {
//        return new MinAcc(1 - _modelPlayer);
        return new MinAcc( _modelPlayer);
    }

    /**
     * Search for the maximum value
     * @param row
     * @param col
     * @param childVal
     */
    public void updateBest(int row, int col, int childVal) {
        if (_val <= childVal) {
            _row = row;
            _col = col;
            _val = childVal;
        }
    }

    /**
     * The player who is trying to find the best move is the player that
     * is using this accumulator
     */
    public int getPlayer() { return _modelPlayer; }
}

