package model.nextMove;

public class AlphaAcc extends MaxAcc {
    public AlphaAcc(int player)
    {
        super(player);
    }

/*    protected AlphaAcc()
    {
        super();
    }
*/
    public AAccumulator makeOpposite() {
        return new BetaAcc(_modelPlayer) {
            /**
             * AlphaAcc.this.getVal() is "alpha", a lower bound for the best
             * value of the parent node (which is a max node).
             * this.getVal() is the current best value for the child node.
             * As long as this value is greater than alpha, we should continue
             * evaluating the value of the child node.
             * As soon as this value is less or equal to alpha, there is no
             * need to continue computing the value of the child node.
             */
            public boolean isNotDone() {
                return AlphaAcc.this.getVal() <= this.getVal();
            }
        };
    }
}