package comp202prof;


import model.*;

public class SBW_DXN_DepthAcc extends SBW_DXN_ADepthAcc {
    public SBW_DXN_DepthAcc(int mP, SBW_DXN_AAccumulator acc, int maxD, IBoardModel bm, IBoardLambda<int[]> pay){
        super(mP, acc, maxD, bm, pay);
    }

    public SBW_DXN_AAccumulator makeOpposite() {
        return new SBW_DXN_DepthAcc(_modelPlayer, _acc.makeOpposite(), _maxDepth, _boardModel, _payOff) {
            public int getDepth() {
                return 1 + SBW_DXN_DepthAcc.this.getDepth();
            }

            public boolean isNotDone() {
                if(getDepth() >= getMaxDepth()) {
                  if(getDepth() == getMaxDepth())
                  {
                        int[] payOffVal = {0, 0};
                        _boardModel.mapAll(_modelPlayer, _payOff, payOffVal);
                        _acc.updateBest(_acc.getMove().y, _acc.getMove().x, payOffVal[0]-payOffVal[1]);

                    }
                    return false;
                }
                else
                    return _acc.isNotDone();
            }
        };
    }
}