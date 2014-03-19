package model.nextMove;


public class DepthAcc extends ADepthAcc {
    public DepthAcc(int mP, AAccumulator acc, int maxD){
        super(mP, acc, maxD);
    }

    public AAccumulator makeOpposite() {
        return new DepthAcc(_modelPlayer, _acc.makeOpposite(), _maxDepth) {
            public int getDepth() {
                return 1 + DepthAcc.this.getDepth();
            }

            public boolean isNotDone() {
                //System.out.println("Depth is " + getDepth());
                if(getDepth() >= getMaxDepth()) {
                  if(getDepth() == getMaxDepth())
                  {
                    //_acc._val = ((int)(3.0*Math.random()))-1;

//                    int[] payOffVal = {0, 0};
//                    _boardModel.mapAll(_modelPlayer, _payOff, payOffVal);
                    _acc.updateBest(_acc.getMove().y, _acc.getMove().x, payoff());
//                    _acc._val = payOffVal[0]-payOffVal[1];
                    //System.out.println("Payoff = " + _acc._val);

                    }
                    return false;
                }
                else
                    return _acc.isNotDone();
            }
        };
    }

    private int payoff(){
      return ((int)(3*Math.random()))-1;
    }
}