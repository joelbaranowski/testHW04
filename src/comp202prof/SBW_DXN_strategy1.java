package comp202prof;

import model.*;
import java.awt.*;

public class SBW_DXN_strategy1 implements INextMoveStrategy {
	private int numMoves = 1;   // Can't start at zero because of divide by zero problems
	private int MAX_VALUE = Integer.MAX_VALUE-1;
	private double TOTAL_FACTOR = 2.4;
	private double WEIGHT_FACTOR = 10.0;//0.05;
	private int maxDepth = 7;

	private int totalMoves;
	private int[] levelSize;
	private int[] levelTotal;
	private int depth = 0;
	private boolean isHardBail = false;
	private long HARD_BAIL_TIME = 1000;

	private boolean isEndGame = false;
	//private int totalTokens = 0;

	private IBoardLambda<Integer>_payOff;

	private SBW_DXN_IAccFactory _accFac = new SBW_DXN_AlphaBetaFac();

	private IModel _model;
	private IBoardModel board;
	private Dimension dim;

	public SBW_DXN_strategy1(IModel model)
	{
		this._model = model;
		_payOff = new SBW_DXN_PayOff1(model.getBoardModel());
		dim = model.getBoardModel().getDimension();
		board = model.getBoardModel();
		levelSize = new int[dim.width*dim.height];
		levelTotal = new int[dim.width*dim.height];
	}
	/**
	 * **** Anonymous inner helper class ************************************
	 * @SBGen Variable (,helper,,0)
	 */
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
			// for diagnostics only
		}
	};

	private IBoardLambda<SBW_DXN_AAccumulator> minMaxEval = new IBoardLambda<SBW_DXN_AAccumulator>() {
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
				final int col, int value, final SBW_DXN_AAccumulator... accs) {
			// Make this move and see what happens
			IUndoMove undo = host.makeMove(row, col, accs[0].getPlayer(), validMoveVisitor,
					new IBoardStatusVisitor<Void, Void>() {
				public Void player0WonCase(IBoardModel host, Void... nu) {
					// Terminal situation -- update the accumulator.
					accs[0].updateBest(row, col, win0ValueForPlayer(accs[0].getModelPlayer()));
					return null;
				}

				public Void player1WonCase(IBoardModel host, Void... nu) {
					// Terminal situation -- update the accumulator.
					accs[0].updateBest(row, col, win1ValueForPlayer(accs[0].getModelPlayer()));
					return null;
				}

				public Void drawCase(IBoardModel host, Void... nu) {
					// Terminal situation -- update the accumulator.
					accs[0].updateBest(row, col, 0);
					return null;
				}

				public Void noWinnerCase(IBoardModel host, Void... nu) {
					// Non-terminal situation -- recur using the opposite accumulation.
					SBW_DXN_AAccumulator nextAcc = accs[0].makeOpposite();
					recur(nextAcc.getPlayer(), nextAcc);
					//host.map(nextAcc.getPlayer(), minMaxEval, nextAcc); // nextAcc contains the best move
					//and value of the child node.
					// Update the accumulator
					accs[0].updateBest(row, col, nextAcc.getVal());
					return null;
				}
			});
			undo.apply(validUndo);
			return accs[0].isNotDone();
		}

		public void noApply(int player, IBoardModel host, SBW_DXN_AAccumulator... accs) {
			// System.out.println("MinMax.noApply()::  skipping player "+player);

			SBW_DXN_AAccumulator nextAcc = accs[0].makeOpposite();
			recur(nextAcc.getPlayer(), nextAcc);
			//host.map(nextAcc.getPlayer(), minMaxEval, nextAcc); // nextAcc contains the best move
			//and value of the child node.
			// Update the accumulator
			accs[0].updateBest(-1,-1, nextAcc.getVal());
		}
	};

	// *** End of helper class *******************************************
	// ***************** Utility methods **********************************

	/**
	 *  Utility methods to convert from a player to value if player 0 won
	 */
	public int win0ValueForPlayer(int p) {
		return MAX_VALUE * (1 - 2*p);  //  for player = 0 -> 1, for player = 1 -> -1
	}

	/**
	 *  Utility methods to convert from a player to value if player 1 won
	 */
	public int win1ValueForPlayer(int p) {
		return MAX_VALUE * (2*p-1);  // for player = 0 -> -1, for player = 1 -> 1
	}

	// ***************** End of Utility methods **********************************
	// ***************** minMaxEval Wrapper method **********************************

	private final void recur(int player, SBW_DXN_AAccumulator accum)
	{
		depth++;   // recurring in
		//        System.out.println("Depth = "+depth);
		final int[] rows = new int[dim.height*dim.width];
		final int[] cols = new int[dim.height*dim.width];
		levelSize[depth] = 0;
		// get all valid moves
		board.map(player, new IBoardLambda<Void>() {
			public boolean apply(final int player, IBoardModel host, final int row,
					final int col, int value, Void... nu) {
				rows[levelSize[depth]] = row;
				cols[levelSize[depth]] = col;
				levelSize[depth]++;   // count # of moves @ this level
				totalMoves++;  // count total moves made
				return true;
			}
			public void noApply(int player, IBoardModel host, Void... nu) {
			}
		});
		levelTotal[depth] += levelSize[depth];   // count total width of tree at this depth
		if(depth<=maxDepth)  // Depth limit
		{
			if(levelSize[depth] == 0)
			{
				minMaxEval.noApply(player, board, accum);
			}
			else
			{
				for(int i = 0; i< levelSize[depth] && minMaxEval.apply(player, board, rows[i], cols[i], 0, accum) && !isHardBail; i++)
				{
					if(_model.getTimeLeft() < HARD_BAIL_TIME)
					{
						System.out.print("Hard bail at depth = " +depth+ " index = "+i+" ");
						accum.updateBest(accum.getMove().y, accum.getMove().x, payOff(depth, accum));
						isHardBail = true;
					}
				}
			}
		}
		else
		{
			accum.updateBest(accum.getMove().y, accum.getMove().x, payOff(depth, accum));
		}
		//      System.out.println("levelSize["+depth+"] = "+ levelSize[depth]);
		depth--;  // recurring out
	}

	private final int payOff(int dpth, SBW_DXN_AAccumulator a)
	{
		Integer[] payOffVal = {0, 0, 0, 0};
		board.mapAll(a.getModelPlayer(), _payOff, payOffVal);  // calc payoff for each player
		double factor = 1;
		int payVal = payOffVal[0]-payOffVal[1];   // net payoff
		if(depth>0)
		{
			//              double weight =1.0+(WEIGHT_FACTOR/numMoves);
			double weight = WEIGHT_FACTOR;
			if(numMoves > 10)
			{
				if(numMoves > 20) weight = 0.0;
				else weight = (WEIGHT_FACTOR/(numMoves-10));   // numMoves > 0 !, emphasize @ beginning of game
			}
			else
				factor = weight*((levelSize[dpth-1]+1)/(levelSize[dpth]+1));  // emphasize fewer moves for opponent.
			//              factor = weight*((levelSize[dpth-1]+1)/(weight*levelSize[dpth]+1));  // emphasize fewer moves for opponent.
			//System.out.println("factor = " + factor);
			//totalTokens = payOffVal[2]+payOffVal[3];
			if(isEndGame)
			{
				if(a.getPlayer() == 0)
				{
					payVal *= (((double)payOffVal[2])/payOffVal[3]);
				}
				else
				{
					payVal *= (((double)payOffVal[3])/payOffVal[2]);
				}   // go for more tokens
			}
			payVal = (int)(factor*payVal);

		}
		return payVal;
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
		isHardBail = false;
		isEndGame = false;

		if( ((dim.width*dim.height)-2*numMoves) > 1 ) {
			if( ((dim.width*dim.height)-2*numMoves) < 30 )
			{
				if(((dim.width*dim.height)-2*numMoves) < 20)
				{
					System.out.print("end-game mode B@"+(2*numMoves)+" moves done...");
					maxDepth = 12;
				}
				else maxDepth += 2;
				System.out.print("end-game mode A@"+(2*numMoves)+" moves done...");
				isEndGame = true;
			}
		}
		else numMoves = 1;
		System.out.print("using maxDepth = "+maxDepth+" ");
		long startTime = context.getTimeLeft();
		SBW_DXN_AAccumulator acc = _accFac.makeAcc(player);
		//        context.getBoardModel().map(player, minMaxEval, acc);  // Find the best move on the board
		totalMoves = 0;
		depth = -1;  // recur will increment before using.
		for(int i = 0; i<=maxDepth+1; i++) levelTotal[i] = 0;   // clear level totals
		recur(player, acc);
		int total = 0;
		for(int i = 0; i<=maxDepth+1; i++) total += levelTotal[i];
		totalMoves -= levelTotal[maxDepth+1];
		int nextTotal = total/levelTotal[0];
		//        System.out.println("totalMoves = " + totalMoves + " total = "+total + " nextTotal = "+ nextTotal);
		long endTime = context.getTimeLeft();

		double timeRatio = (startTime-endTime)/((double)startTime);
		if(totalMoves/(TOTAL_FACTOR*total) > timeRatio)
		{
			maxDepth++;
			if( Math.pow(totalMoves/(TOTAL_FACTOR*total), 2.0) > timeRatio) maxDepth++;
		}
		else if( (TOTAL_FACTOR*nextTotal)/totalMoves > timeRatio && maxDepth > 1)
		{
			maxDepth--;
			if( Math.pow((TOTAL_FACTOR*nextTotal)/totalMoves, 2.0) > timeRatio && maxDepth > 1) maxDepth--;
			if(maxDepth<4) maxDepth = 4;
		}

		numMoves++;
		return acc.getMove();  // retrieve and return the best move from the accumulator.
	}
}
