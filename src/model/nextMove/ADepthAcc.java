package model.nextMove;


public abstract class ADepthAcc extends AAccumulator {
    protected AAccumulator _acc;
    protected int _maxDepth= 0;

    public ADepthAcc(int mP, AAccumulator acc, int maxD) {
        super(mP);
        _acc = acc;
        _maxDepth = maxD;
        _row = -100;
        _col = -100;
    }

    public int getModelPlayer()  {
        return _acc.getModelPlayer();
    }

    public int getVal() {
        return _acc.getVal();
    }

    public java.awt.Point getMove() {
        return _acc.getMove();
    }

    public int getPlayer() {
        return _acc.getPlayer();
    }

    public void updateBest(int row, int col, int childVal) {
        _acc.updateBest(row, col, childVal);
    }

    public int getDepth() {
        return 0;
    }

    public int getMaxDepth() {
        return _maxDepth;
    }

    public boolean isNotDone() {
        return _acc.isNotDone();
    }
}