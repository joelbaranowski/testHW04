package model.nextMove;

import model.IBoardModel;

public class MinMaxFac implements IAccFactory
{
  public AAccumulator makeAcc(int player)
  {
      return new MaxAcc(player);
  }

@Override
public AAccumulator makeAcc(int player, IBoardModel ibm) {
	// TODO Auto-generated method stub
	return null;
}
}