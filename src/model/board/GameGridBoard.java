package model.board;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import model.*;

public class GameGridBoard extends ABoardModel {

	ArrayList<ArrayList<Integer>> player1Scores;
	ArrayList<ArrayList<Integer>> player2Scores;
	
	
    public GameGridBoard(int nRows, int nCols)  {
        super(nRows, nCols);
    }

	synchronized public void reset()  {
		super.reset();
		player1Scores = new ArrayList<ArrayList<Integer>>();
		player2Scores = new ArrayList<ArrayList<Integer>>();
		Random rand = new Random();
		for(int i = 0; i < cells.length; i++){
			for(int j = 0; j < cells[i].length; j++){
				cells[i][j] = rand.nextInt(10) + 1;
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

    public void pprint(int[][] matrix){
		for (int i = 0; i < matrix.length; i++) {
		    for (int j = 0; j < matrix[i].length; j++) {
		        System.out.print(matrix[i][j] + " ");
		    }
		    System.out.print("\n");
		}
	}
    
    
	public synchronized IUndoMove makeMove(final int row, final int col, int player,
                                           ICheckMoveVisitor chkMoveVisitor,
                                           IBoardStatusVisitor<Void, Void> statusVisitor) {
		final Point[] old_pos = new Point[1];
		final int[] old_value_zero = new int[1];
        if (isValidMove(player,row,col)) {
        	for(int i = 0; i < cells.length; i++)
        		for(int j = 0; j < cells[i].length; j++)
        			if(cells[i][j] == playerToValue(player)){
        			 	old_pos[0] = new Point(i, j);
        			 	old_value_zero[0] = cells[i][j];
        				cells[i][j] = 0;
        			}
        	final int old_value = cells[row][col];
            
        	ArrayList<Integer> score_pair = new ArrayList<Integer>();
    		score_pair.add(row);
    		score_pair.add(col);
    		score_pair.add(cells[row][col]);
    		
            cells[row] [col] = playerToValue(player);
        	
    		
    		if (player == 0) {
        		player1Scores.add(score_pair);

        	} else if(player == 1){
        		player2Scores.add(score_pair);

        	}
    		
            //pprint(cells);
            chgState(winCheck(row, col));
            chkMoveVisitor.validMoveCase();
            execute(statusVisitor);
            return new IUndoMove() {
                public void apply(IUndoVisitor undoVisitor) {
                    undoMove(row, col, old_value, old_pos, old_value_zero, undoVisitor);
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
    private synchronized void undoMove(int row, int col, int old_value, Point[] old_pos, int[] old_value_zero, IUndoVisitor undoVisitor)  {

    	if(old_value_zero[0] == -2 || old_value_zero[0] == -1)
    		cells[old_pos[0].x][old_pos[0].y] = old_value_zero[0];
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
    			if(cells[i][j] > 0)
    				count++;
    	System.out.println("winCheck: Count "+count);
    	if(count == 0) {
    		
    		int player1TotalScore = 0;
    		int player2TotalScore = 0;
    		System.out.println("length 1 scores: " + this.player1Scores.size());
    		for (ArrayList<Integer> score : this.player1Scores) {
    			System.out.println("score get 2: " + score.get(2));
    			player1TotalScore += score.get(2);
    		}
    		
    		for (ArrayList<Integer> score : this.player2Scores) {
    			player2TotalScore += score.get(2);
    		}
    		
    		System.out.println("winCheck: Count2  " + player1TotalScore + " " + player2TotalScore);
    		if (player1TotalScore > player2TotalScore)
    			return -1;
    		else if (player2TotalScore > player1TotalScore)
    			return 1;
 
    	}
    	return 0;
    }

    protected boolean isValidMove(int player, int row, int col){
    	
    	int r = -1;
    	int c = -1;
    	System.out.println("player: " + player + " | player2value: " + playerToValue(player));
    	for(int i = 0; i < cells.length; i++)
    		for(int j = 0; j < cells[i].length; j++){
    			if(cells[i][j] == playerToValue(player)){
    				System.out.println("cell ij: " + cells[i][j]);
    				r = i;
    				c = j;
    			}
    		}
    	System.out.println("row, col:" + row + ", " + col + "| r, c: " + r + "," + c);
    	
    	if(cells[row][col] == 0)
    		return false;
    	
    	if(cells[row][col] == -2 || cells[row][col] == -1)
    		return false;
    	
    	if((r != -1 && c != -1) && (Math.abs(row - r) > 1 || Math.abs(col - c) > 1))
    		return false;
    	
    	return true;
    	   
    }
}
