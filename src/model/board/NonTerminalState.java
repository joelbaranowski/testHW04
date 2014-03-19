
package model.board;

import model.*;

/**
 * Represents a non-terminal (no winner) state of the board.
 */
class NonTerminalState extends ABoardState {
    public static NonTerminalState Singleton = new NonTerminalState();
    protected NonTerminalState() {}

    public <T> void map(int player, final IBoardLambda<T> lambda,
                    final ABoardModel host, @SuppressWarnings("unchecked") T... params) {
        boolean noApply = true;
        int[][] cells = host.getCells();
        int[][] idx = host.randomizeIdx(cells.length, cells[0].length);
        for (int i = 0; i < idx.length; i++) {
            if(host.isValidMove(player, idx[i][0], idx[i][1])) {
                noApply = false;
                if(!lambda.apply(player, host,  idx[i][0], idx[i][1],
                    cells[idx[i][0]][idx[i][1]], params)) {
                        return;
                }
            }
        }
        if(noApply)  {
            lambda.noApply(player, host, params);
        }
    }

    public <R, P> R execute(ABoardModel host, IBoardStatusVisitor<R, P> visitor, @SuppressWarnings("unchecked") P... params)  {
        return visitor.noWinnerCase(host, params);
    }
}
