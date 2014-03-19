
package model.nextMove;

import java.awt.*;
import model.*;

public class MinMax implements INextMoveStrategy {
    private int MAX_VALUE = Integer.MAX_VALUE-1;
    private IAccFactory _accFac;
    public MinMax(IAccFactory accFac) {
        this._accFac = accFac;
    }
    /**
    * **** Anonymous inner helper class ************************************
    */
    private IBoardLambda<AAccumulator> minMaxEval = new IBoardLambda<AAccumulator>() {
        //int undoPlayer; // for diagnostics only
        ICheckMoveVisitor validMoveVisitor = new ICheckMoveVisitor() {
            public void invalidMoveCase() {
                throw new IllegalStateException(
                "MinMax.minMaxEval.validMoveVisitor.invalidMoveCase() should be unreachable!");
              }
            public void validMoveCase() {
                // do nothing -- just want to see if a terminal state was reached or not.
            }
        };
        IUndoVisitor validUndo = new IUndoVisitor() {
            public void noTokenCase() {
                throw new IllegalStateException(
                "MinMax.minMaxEval.validUndo.noTokenCase() should be unreachable!");
            }
            public void tokenCase(int value) {
                //undoPlayer = value;  // for diagnostics only
            }
        };
        /**
        * @param host board
        * @param param The accumulator for the best move and value so far.
        * @param row The row of the move being currently tested.
        * @param col The col of the move being currently tested.
        * @param value The value of the token on this (row, col),
        * e.g. always 0 = empty for TicTacToe
        * @return true always = process all valid moves.
        */
        public boolean apply(final int player, IBoardModel host,  final int row,
                             final int col, int value, final AAccumulator... accs) {
            IUndoMove undo = host.makeMove(row, col, accs[0].getPlayer(), validMoveVisitor,
                new IBoardStatusVisitor<Void, Void>() {
                    public Void player0WonCase(IBoardModel host, Void... nu) {
                        accs[0].updateBest(row, col, win0ValueForPlayer(accs[0].getModelPlayer()));
                        return null;
                    }

                    public Void player1WonCase(IBoardModel host, Void... nu) {
                        accs[0].updateBest(row, col, win1ValueForPlayer(accs[0].getModelPlayer()));
                        return null;
                    }

                    public Void drawCase(IBoardModel host, Void... nu) {
                        accs[0].updateBest(row, col, 0);
                        return null;
                    }

                    public Void noWinnerCase(IBoardModel host, Void... nu) {
                        AAccumulator nextAcc = accs[0].makeOpposite();
                        host.map(nextAcc.getPlayer(), minMaxEval, nextAcc); // nextAcc contains the best move
                                                       //and value of the child node.
                        accs[0].updateBest(row, col, nextAcc.getVal());
                        return null;
                    }
                });
              undo.apply(validUndo);
              return accs[0].isNotDone();
        }

        public void noApply(int player, IBoardModel host, AAccumulator... accs) {
            Point p = accs[0].getMove();
            AAccumulator nextAcc = accs[0].makeOpposite();
            host.map(nextAcc.getPlayer(), minMaxEval, nextAcc); // nextAcc contains the best move
                                                       //and value of the child node.
            // Update the accumulator
//acc.updateBest(nextAcc.getMove().y,nextAcc.getMove().x, nextAcc.getVal());
            accs[0].updateBest(p.y, p.x, nextAcc.getVal());
        }
    };

// *** End of helper class *******************************************

    /**
     *  Utility methods to convert from a player to value if player 0 won
     */
    private int win0ValueForPlayer(int p) {
      return MAX_VALUE * (1 - 2*p);  //  for player = 0 -> 1, for player = 1 -> -1
    }

    /**
     *  Utility methods to convert from a player to value if player 1 won
     */
    private int win1ValueForPlayer(int p) {
      return MAX_VALUE * (2*p-1);  // for player = 0 -> -1, for player = 1 -> 1
    }

// ***************** End of Utility methods **********************************

    /**
     * Calculate the best move for the given player.
     * Pre-condition: The context's board must be in a non-terminal state.
     * Should be called only when the board is non-terminal.
     * @param context The model that is running this game.
     * @return The best move that can be made
     */
    public Point getNextMove(IModel context, int player) {
        AAccumulator acc = _accFac.makeAcc(player);
        context.getBoardModel().map(player, minMaxEval, acc);  // Find the best move on the board
        return acc.getMove();  // retrieve and return the best move from the accumulator.
    }
}


