
package model.nextMove;

import java.awt.Point;


public abstract class AAccumulator {
    public int _modelPlayer;  // The player that is using this accumulator to try to find the best next move.
    protected int _row = -10;
    protected int _col = -10;
    protected int _val;

    protected AAccumulator(int modelPlayer) {
      _modelPlayer = modelPlayer;
    }

    public abstract AAccumulator makeOpposite();

    /**
     * @return The player who is trying to find the best move.
     */
    public int getModelPlayer()  {
        return _modelPlayer;
    }

    /**
     * player = modelPlayer for MaxAcc
     * player = opposite of modelPlayer for MinAcc
     * @return The player is making the move that uses this accumulator.
     */
    public abstract int getPlayer();

    public int getVal() {
        return _val;
    }

    public Point getMove() {
        return new Point(_col, _row);
    }

    /**
     * @param row
     * @param col
     * @param childVal
     */
    public abstract void updateBest(int row, int col, int childVal);

    public boolean isNotDone() {
        return true;
    }

}

