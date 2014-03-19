package model.nextMove;

public class AlphaBetaFac implements IAccFactory
{
  public AAccumulator makeAcc(int player)
  {
      return new AlphaAcc(player);
  }
}