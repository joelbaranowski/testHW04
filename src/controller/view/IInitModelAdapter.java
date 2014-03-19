package controller.view;

public interface IInitModelAdapter {

	void makeTicTacToe(int nRows, int nCols, int inARow, int maxTurnTime);
	void makeOthello(int nRows, int nCols, int maxTurnTime);
	void makeGameGrid(int nRows, int nCols, int maxTurnTime);

}
