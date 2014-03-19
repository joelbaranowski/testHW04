package model.nextMove;

public interface IAccFactory
{
  public abstract AAccumulator makeAcc(int player);
}