package comp202prof;

import model.*;

public abstract class SBW_DXN_ADepthAcc extends SBW_DXN_AAccumulator {
    protected SBW_DXN_AAccumulator _acc;
    protected int _maxDepth= 0;
    protected IBoardModel _boardModel;
    protected IBoardLambda<int[]>_payOff;

    public SBW_DXN_ADepthAcc(int mP, SBW_DXN_AAccumulator acc, int maxD, IBoardModel bm, IBoardLambda<int[]> pay) {
        super(mP);
        _acc = acc;
        _maxDepth = maxD;
        _boardModel = bm;
        _payOff = pay;
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

    public int getPlayer()
    {
    return _acc.getPlayer();
    }
    public void updateBest(int row, int col, int childVal)
    {
    _acc.updateBest(row, col, childVal);
    }

    public int getDepth()
    {
    return 0;
    }

    public int getMaxDepth()
    {
    return _maxDepth;
    }

    public boolean isNotDone() {
        return _acc.isNotDone();
    }
}