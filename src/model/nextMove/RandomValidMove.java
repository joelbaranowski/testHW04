package model.nextMove;

import model.INextMoveStrategy;
import java.awt.Point;
import model.*;
import java.util.*;

/**
 * Selects a random move from the set of valid moves only.
 */
public class RandomValidMove implements INextMoveStrategy {
    private IBoardLambda<List<Point>>lambda = new IBoardLambda<List<Point>>() {
            public boolean apply(int player, IBoardModel host, 
                                 int row, int col, int value, @SuppressWarnings("unchecked") List<Point>... lstPoints) {
                lstPoints[0].add(new Point(col, row));
                return true;
            }
            public void noApply(int player, IBoardModel host, @SuppressWarnings("unchecked") List<Point>... nu) {
                throw new IllegalStateException("RandomMove2.lambda.noApply() should be unreachable!");
            }
        };


    @SuppressWarnings("unchecked")
	public Point getNextMove(IModel context, int player) {
        List<Point> acc = new ArrayList<Point>();
        context.getBoardModel().map(player, lambda, acc);
        return acc.get((int)(acc.size()*Math.random()));
    }
}