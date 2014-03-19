package comp202prof;
import model.*;

public class SBW_DXN_DepthFac implements SBW_DXN_IAccFactory {
    private int _maxDepth=0;
    private SBW_DXN_IAccFactory _accFac;
    private IBoardModel _boardModel;
    private IBoardLambda<int[]> _payOff;

    public SBW_DXN_DepthFac(SBW_DXN_IAccFactory accFac, int maxDepth, IBoardModel bm, IBoardLambda<int[]> pay) {
        _accFac = accFac;
        _maxDepth = maxDepth;
        _boardModel = bm;
        _payOff = pay;
    }

    public SBW_DXN_AAccumulator makeAcc(int player) {
        return new SBW_DXN_DepthAcc(player, _accFac.makeAcc(player), _maxDepth, _boardModel, _payOff);
    }
}