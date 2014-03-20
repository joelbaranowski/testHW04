package model.board;

import java.awt.Point;
import java.util.Random;
import java.util.ArrayList;
import model.*;

public class GameGridBoard extends ABoardModel {

	ArrayList<ArrayList<Integer>> player1Scores = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> player2Scores = new ArrayList<ArrayList<Integer>>();
	
    public GameGridBoard(int nRows, int nCols)  {
        super(nRows, nCols);
    }

	synchronized public void reset()  {
		super.reset();
		Random rand = new Random();
		for(int i = 0; i < cells.length; i++){
			for(int j = 0; j < cells[i].length; j++){
				cells[i][j] = rand.nextInt(10);
			}
		}		
	}
	
    /**
     * Changes the state of the board according to the input.
     * @param winner Which player is the winner {-1, 0, +1} where 0 = no winner or draw.
     */
    synchronized private void chgState(int winner)  {
        if (winner == -1) {
            state = Player0WonState.Singleton;
        }
        else if (winner == 1) {
            state = Player1WonState.Singleton;
        }
        else  {// winner == 0 -> no winner, but perhaps a draw
            map(winner, new IBoardLambda<Void>() {
                public boolean apply(int player, IBoardModel host, 
                                     int row, int col, int value, Void... nu) {
                    state = NonTerminalState.Singleton;
                    return false;
                }

                public void noApply(int player, IBoardModel host, Void... nu) {
                    state = DrawState.Singleton;
                }
            });
        }
    }

    Point old_pos;
	int old_value_zero;
	public synchronized IUndoMove makeMove(final int row, final int col, int player,
                                           ICheckMoveVisitor chkMoveVisitor,
                                           IBoardStatusVisitor<Void, Void> statusVisitor) {
        if (isValidMove(player,row,col)) {
        	
        	
      
        	final int old_value = cells[row][col];
        	
        	// adding <coordinate, score> pair player's score record
        	ArrayList<Integer> score_pair = new ArrayList<Integer>();
    		score_pair.add(row);
    		score_pair.add(col);
    		score_pair.add(cells[row][col]);
        	
    		System.out.println("makeMove "+player);
    		
    		if (player == 0) {
        		player1Scores.add(score_pair);
        	} else {
        		player2Scores.add(score_pair);
        	}
        	
            cells[row] [col] = 0;
            chgState(winCheck(row, col));
            chkMoveVisitor.validMoveCase();
            execute(statusVisitor);
            return new IUndoMove() {
                public void apply(IUndoVisitor undoVisitor) {
                    undoMove(row, col, old_value, undoVisitor);
                }
            };
        }
        chkMoveVisitor.invalidMoveCase();
        return new IUndoMove() {
            public void apply(IUndoVisitor undoVisitor) {
                // no-op
            }
        };
    }

    /**
     * Undoes the move at (row,col).
     * @param row
     * @param col
     * @param undoVisitor The appropriate method of the visitor is called after the undo is performed.
     */
    private synchronized void undoMove(int row, int col, int old_value, IUndoVisitor undoVisitor)  {

        cells[row][col] = old_value;
    
   		for (ArrayList<Integer> score : this.player1Scores) {
   			if (score.get(0) == row && score.get(1) == col) {
   				this.player1Scores.remove(score);
   				break;
   			}
		}
		
		for (ArrayList<Integer> score : this.player2Scores) {
   			if (score.get(0) == row && score.get(1) == col) {
   				this.player2Scores.remove(score);
   				break;
   			}
		}
		
        state = NonTerminalState.Singleton;
    }



    /**
     * Checks for a winner when a token is placed at (row, col).
     * @param row
     * @param col
     * @return The winner {-1, 0, +1} where 0 = no winner or draw.
     */
    private int winCheck(int row, int col){
    	int count = 0;
    	for(int i = 0; i < cells.length; i++)
    		for(int j = 0; j < cells[i].length; j++)
    			if(cells[i][j] != 0)
    				count++;
    	
    	System.out.println("winCheck: Count "+count);
    	if(count == 0) {
    		
    		int player1TotalScore = 0;
    		int player2TotalScore = 0;
    		
    		for (ArrayList<Integer> score : this.player1Scores) {
    			player1TotalScore += score.get(2);
    		}
    		
    		for (ArrayList<Integer> score : this.player2Scores) {
    			player2TotalScore += score.get(2);
    		}
    		
    		System.out.println("winCheck: Count  " + player1TotalScore + " " + player2TotalScore);
    		if (player1TotalScore > player2TotalScore)
    			return -1;
    		else if (player2TotalScore > player1TotalScore)
    			return 1;
 
    	}
    	return 0;
    }

    protected boolean isValidMove(int player, int row, int col){
        if(cells[row][col] != 0)
        	return true;
        return false;
    }
}
