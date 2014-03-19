package comp202prof;

public class SBW_DXN_AlphaBetaFac implements SBW_DXN_IAccFactory
{
  public SBW_DXN_AAccumulator makeAcc(int player)
  {
      return new SBW_DXN_AlphaAcc(player);
  }
}