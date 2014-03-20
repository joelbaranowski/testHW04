package model.nextMove;

import model.IBoardModel;

public interface IAccFactory
{
  public abstract AAccumulator makeAcc(int player);
  
  public abstract AAccumulator makeAcc(int player, IBoardModel ibm);
}