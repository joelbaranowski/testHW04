package model.nextMove;


public class DepthFac implements IAccFactory {
    private int _maxDepth=0;
    private IAccFactory _accFac;

    public DepthFac(IAccFactory accFac, int maxDepth) {
        _accFac = accFac;
        _maxDepth = maxDepth;
    }

    public AAccumulator makeAcc(int player) {
        return new DepthAcc(player, _accFac.makeAcc(player), _maxDepth);
    }
}