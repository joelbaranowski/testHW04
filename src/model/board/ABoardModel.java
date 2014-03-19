package model.board;

import gameIO.*;

import java.awt.*;
import model.*;

/**
 * An abstract implementation of IBoardModel that uses the state design pattern
 * to represent the game states.
 * A 2-dimensional array of int represents the game board.
 * Mapping is done randomly across the applicable rows and cols.
 */
public abstract class ABoardModel implements IBoardModel {
    protected static final int EMPTY = 0;

    /**
     * The game board.
     */
    protected int[] [] cells;

    /**
     * State pattern!
     */
    protected ABoardState state = DrawState.Singleton;

    /**
     * "Vectors" pointing to different directions in the game board.  Used to
     * facilitate board traversal and other operations.
     */
    protected int[][] directions = {{1,0},{0,1},{1,1},{1,-1}};

    /**
     * The constructor for the class.
     * @param nRows The number of rows in the board.
     * @param nCols The number of columns in the board.
     */
    public ABoardModel(int nRows, int nCols) {
        cells = new int[nRows][nCols];
    }

    /**
     * Uitility method to convert a board value {-1, 0, +1} to a player value
     * {0, 1}, assuming that the value is non-zero.
     * @param value
     * @return
     */
    public int valueToPlayer(int value)  {
        if(value == -2){
        	return 0;
        }
        else if(value == -1){
        	return 1;
        }
		return value;
    }

    /**
     * Utility method to convert a player number {0,1}, to a boardvalue (-1, +1).
     * @param player
     * @return
     */
    public int playerToValue(int player) {
    	if(player == 0){
    		return -2;
    	}
    	else if(player == 1){
    		return -1;
    	}
		return player;
    }


    public Dimension getDimension() {
        return new Dimension(cells[0].length, cells.length);
    }


    public abstract IUndoMove makeMove(int r, int c, int plyr,
                                 ICheckMoveVisitor cm, IBoardStatusVisitor<Void, Void> bs);

    synchronized public void reset() {
        mapAll(0, new IBoardLambda<Void>() {
                public boolean apply(int player, IBoardModel host, 
                                     int row, int col, int value, Void... nu) {
                    cells[row][col] = EMPTY;
                    return true;
                }
                public void noApply(int player, IBoardModel host, Void... nu) {
                }
            });
        state = NonTerminalState.Singleton;
    }

    /**
    * Returns an array of arrays, representing board.   The token at (row, col) is given by
    * (board.getCells())[row][col] =  -1 (player #0), 0 (no player), or +1 (player #1).
    * @return
    */
    int[][] getCells()  {
        return cells;
    }


    public <T> void map(int player,IBoardLambda<T> lambda, @SuppressWarnings("unchecked") T... params) {
        state.map(player, lambda, this, params);
    }

    public <T> void mapAll(int player, IBoardLambda<T> lambda, @SuppressWarnings("unchecked") T... params)  {
        int[][] idx = randomizeIdx(cells.length, cells[0].length);
        for (int i = 0; i < idx.length; i++) {
            if(!lambda.apply(player,this,  idx[i][0], idx[i][1], cells[idx[i][0]][idx[i][1]], params))
                return;
        }
    }

    public int playerAt(int row, int col) {
        return cells[row][col];
    }

    public <R, P> R execute(IBoardStatusVisitor<R, P> visitor, @SuppressWarnings("unchecked") P... params) {
        return state.execute(this, visitor, params);
    }

    protected abstract boolean isValidMove(int player, int row, int col);

    public void redrawAll(final ICommand command) {
        mapAll(0, new IBoardLambda<Void>() {
                public boolean apply(int player, IBoardModel host, 
                                       int row, int col, int value, Void... nu) {
                    value = cells[row][col];
                    command.setTokenAt(row, col, value);
                    return true;
                }
                public void noApply(int player, IBoardModel host, Void... nu) {
                    throw new IllegalStateException(
                        "ABoardModel.redrawAll(): noApply() should be unreachable!");
                }
            });
    }

    public boolean isSkipPlayer(int player) {
        final boolean[] result = new boolean[]{false};
        map(player, new IBoardLambda<Void>(){
                public boolean apply(int player, IBoardModel host,
                                     int row, int col, int value, Void...nu) {
                    return false;
                }
                public void noApply(int player, IBoardModel host, Void... nu) {
                    result[0] = true;
                }
            });
        return result[0];
    }


    /**
     * Utility method that creates a randomized traversal across nRows and nCols.
     * @param nRows
     * @param nCols
     * @return An array of the random (row, col) indices that will completely
     * traverse the nRows and nCols.
     */
    final int[][] randomizeIdx(int nRows, int nCols) {
        int[][] idx = new int[nRows * nCols][2];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                idx[i * nCols + j][0] = i;
                idx[i * nCols + j][1] = j;
            }
        }

        for (int i = 0; i < idx.length; i++) {
            int[] temp = idx[i];
            int j = (int)(idx.length * Math.random());
            idx[i] = idx[j];
            idx[j] = temp;
        }
        return idx;
   }
}