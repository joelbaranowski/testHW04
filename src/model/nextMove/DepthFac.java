package model.nextMove;

import model.IBoardModel;


public class DepthFac implements IAccFactory {
    private int _maxDepth=0;
    private IAccFactory _accFac;

    public DepthFac(IAccFactory accFac, int maxDepth) {
        _accFac = accFac;
        _maxDepth = maxDepth;
    }

    public AAccumulator makeAcc(int player, IBoardModel ibm) {
        return new DepthAcc(player, _accFac.makeAcc(player), _maxDepth, ibm);
    }

	@Override
	public AAccumulator makeAcc(int player) {
		// TODO Auto-generated method stub
		return null;
	}
}