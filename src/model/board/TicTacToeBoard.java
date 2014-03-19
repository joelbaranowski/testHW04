package model.board;

import model.*;

public class TicTacToeBoard extends ABoardModel {
    private int IN_ROW= 3;
    public TicTacToeBoard(int nRows, int nCols, int in_row)  {
        super(nRows, nCols);
        IN_ROW = in_row;
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

	public synchronized IUndoMove makeMove(final int row, final int col, int player,
                                           ICheckMoveVisitor chkMoveVisitor,
                                           IBoardStatusVisitor<Void, Void> statusVisitor) {
        if (isValidMove(player,row,col)) {
            cells[row] [col] = playerToValue(player);
            chgState(winCheck(row, col));
            chkMoveVisitor.validMoveCase();
            execute(statusVisitor);
            return new IUndoMove() {
                public void apply(IUndoVisitor undoVisitor) {
                    undoMove(row, col, undoVisitor);
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
    private synchronized void undoMove(int row, int col, IUndoVisitor undoVisitor)  {
        int value = cells[row][col];
        if (value ==EMPTY)
            undoVisitor.noTokenCase();
        else  {
            cells[row][col] = EMPTY;
            undoVisitor.tokenCase((value+1)/2);
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
        int player = cells[row][col];
        if(player != EMPTY) {
            for (int i = 0; i < directions.length; i++) {
                int vr = directions[i][0];
                int vc = directions[i][1];
                if (winCheckHelp1(player, IN_ROW-2, row+vr, col+vc, vr, vc, row-vr, col-vc))
                    return player;
            }
        }
        return 0;
    }

    /**
     * helper method for winCheck.
     * @param player
     * @param count
     * @param row0
     * @param col0
     * @param vRow
     * @param vCol
     * @param row1
     * @param col1
     * @return
     */
    private final boolean winCheckHelp1(int player, int count, int row0, int col0,
                                        int vRow, int vCol, int row1, int col1) {
        if ((row0<0) || (col0<0) || (row0>=cells.length) || (col0>=cells[row0].length))
            return winCheckHelp2(player, count, row1, col1, -vRow, -vCol);
        else if (cells[row0][col0] != player)
            return winCheckHelp2(player, count, row1, col1, -vRow, -vCol);
        else if (0 == count)
            return true;
        else
            return winCheckHelp1(player, --count, row0+vRow, col0+vCol, vRow, vCol, row1, col1);
    }

    /**
     * helper method for winCheck.
     * @param player
     * @param count
     * @param row0
     * @param col0
     * @param vRow
     * @param vCol
     * @return
     */
    private final boolean winCheckHelp2(int player, int count, int row0,
                                        int col0, int vRow, int vCol){
        if ((row0<0) || (col0<0) ||(row0>=cells.length)
            || (col0>=cells[row0].length) || (cells[row0][col0] != player))
            return false;
        else if (0 == count)
            return true;
        else
            return winCheckHelp2(player, --count, row0+vRow, col0+vCol, vRow, vCol);
    }

    protected boolean isValidMove(int player, int row, int col){
        return EMPTY == cells[row][col];
    }
}
