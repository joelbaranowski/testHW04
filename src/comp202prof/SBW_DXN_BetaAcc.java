package comp202prof;

public class SBW_DXN_BetaAcc extends SBW_DXN_MinAcc {

    public SBW_DXN_BetaAcc(int player)
    {
        super(player);
    }

    public SBW_DXN_AAccumulator makeOpposite() {
        return new SBW_DXN_AlphaAcc(_modelPlayer) {
            /**
             * BetaAcc.this.getVal() is "beta", an upper bound for the best
             * value of the parent node (which is a min node).
             * this.getVal() is the current best value for the child node.
             * As long as this value is less than beta, we should continue
             * evaluating the value of the child node.
             * As soon as this value is greater or equal to beta, there is no
             * need to continue computing the value of the child node.
             */
            public boolean isNotDone() {
                return this.getVal() <= SBW_DXN_BetaAcc.this.getVal();
            }
        };
    }
}