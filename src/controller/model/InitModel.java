package controller.model;

import model.board.GameGridBoard;
import model.board.OthelloBoard;
import model.board.TicTacToeBoard;

public class InitModel {

	public void makeTicTacToe(int nRows, int nCols, int inARow, int maxTurnTime){
		 new GameController(new TicTacToeBoard(nRows, nCols, inARow), maxTurnTime).start();
	}
	
	public void makeOthello(int nRows, int nCols, int maxTurnTime){
		 new GameController(new OthelloBoard(nRows, nCols), maxTurnTime).start();
	}
	
	public void makeGameGrid(int nRows, int nCols, int maxTurnTime){
		 new GameController(new GameGridBoard(nRows, nCols), maxTurnTime).start();
	}
}
