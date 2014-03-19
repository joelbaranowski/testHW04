
package model.nextMove;

public class MinAcc extends AAccumulator {	/**
     * @param player
     * @SBGen Constructor
     */
    public MinAcc(int modelPlayer) {
        super(modelPlayer);
        _val = Integer.MAX_VALUE;
    }

    /**
     * @return
     */
    public AAccumulator makeOpposite() {
//        return new MaxAcc(1 - _modelPlayer);
        return new MaxAcc(_modelPlayer);
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

