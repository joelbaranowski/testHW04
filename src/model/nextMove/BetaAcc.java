package model.nextMove;

public class BetaAcc extends MinAcc {

    public BetaAcc(int player)
    {
        super(player);
    }

/*    protected BetaAcc()
    {
        super();
    }
*/
    public AAccumulator makeOpposite() {
        return new AlphaAcc(_modelPlayer) {
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
                return this.getVal() <= BetaAcc.this.getVal();
            }
        };
    }
}