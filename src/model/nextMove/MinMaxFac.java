package model.nextMove;

public class MinMaxFac implements IAccFactory
{
  public AAccumulator makeAcc(int player)
  {
      return new MaxAcc(player);
  }
}