package model.nextMove;

import model.IBoardModel;

public class AlphaBetaFac implements IAccFactory
{
  public AAccumulator makeAcc(int player)
  {
      return new AlphaAcc(player);
  }

@Override
public AAccumulator makeAcc(int player, IBoardModel ibm) {
	// TODO Auto-generated method stub
	return null;
}
}