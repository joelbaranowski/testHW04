
package model.board;

import java.util.*;
import model.*;

public class OthelloBoard extends ABoardModel {
	/**
	 * The constructor for the class, specifying the number or rows and cols.
	 * @param nRows
	 * @param nCols
	 */
	public OthelloBoard(int nRows, int nCols) {
		super(nRows, nCols);
	}

	/**
	 * Returns the winner as {-1, 0, +1} where  0 = no winner or draw.
	 */
	synchronized private int checkWin(final int player0)  {
		final int[] totals = new int[]{0,0};
		final int[] winner = new int[]{0};  // no winner by default

		map(valueToPlayer(-playerToValue(player0)), new IBoardLambda<Void>() {
			public boolean apply(int player, IBoardModel host,
					int row, int col, int value,  Void... nu) {
				state = NonTerminalState.Singleton;
				return false;   // The game isn't over!
			}

			public void noApply(int player, IBoardModel host, Void... nu) {
				map(player0, new IBoardLambda<Void>() {
					public boolean apply(int player, IBoardModel host,
							int row, int col, int value, Void... nu) {
						state = NonTerminalState.Singleton;
						return false;   // The game isn't over!
					}

					public void noApply(int player, IBoardModel host, Void... nu) {
						// Game is over!
						winner[0] =countTokens(totals);
						switch(winner[0]) {
						case 0: state = DrawState.Singleton; break;
						case 1: state = Player1WonState.Singleton; break;
						case -1: state = Player0WonState.Singleton; break;
						default: break;
						}
					}
				});
			}
		});
		return winner[0];
	}

	public synchronized IUndoMove makeMove(final int row, final int col,
			final int player,
			ICheckMoveVisitor chkMoveVisitor,
			IBoardStatusVisitor<Void, Void> statusVisitor) {
		if (isValidMove(player,row,col)) {
			final List<int[]> flipped = flipAll(row, col, player);
			checkWin(player);
			chkMoveVisitor.validMoveCase();
			execute(statusVisitor);
			return new IUndoMove() {
				public void apply(IUndoVisitor undoVisitor) {
					undoMove(player, row, col, undoVisitor, flipped);
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
	 * Undoes the move at (row, col) by player.   Flipped holds a vector of all
	 * the rows and cols whose tokens were converted to players, on the move.
	 * The appropriate method on the  undo visitor is called after the undo is performed.
	 * @param player
	 * @param row
	 * @param col
	 * @param undoVisitor
	 * @param flipped
	 */
	private final synchronized void undoMove(int player, int row, int col,
			IUndoVisitor undoVisitor, List<int[]> flipped) {
		int value = cells[row][col];
		if (value == EMPTY) {
			undoVisitor.noTokenCase();
		}
		else {
			cells[row][col] = EMPTY;
			undoVisitor.tokenCase((value+1)/2);
		}
		for (int i = 0; i < flipped.size(); i++) {
			int[] rc = flipped.get(i);
			cells[rc[0]][rc[1]] = - playerToValue(player);
		}
		state = NonTerminalState.Singleton;
	}


	synchronized public void reset()  {
		super.reset();

		int r = cells.length/2;
		int c = cells[0].length/2;

		cells[r][c] = playerToValue(1);
		cells[r-1][c] = playerToValue(0);
		cells[r][c-1] = playerToValue(0);
		cells[r-1][c-1] = playerToValue(1);

	}

	protected boolean isValidMove(int player, int row, int col) {
		if (cells[row][col] == EMPTY) {
			for (int i = 0; i < directions.length; i++) {
				int vr = directions[i][0];
				int vc = directions[i][1];
				if (isValidMove1(player, row+vr, col+vc,vr, vc, row-vr, col-vc))
					return true;
			}
			return false;
		}
		else return false;
	}


	/**
	 * Helper method for isValidMove.
	 * @param player
	 * @param row0
	 * @param col0
	 * @param vRow
	 * @param vCol
	 * @param row1
	 * @param col1
	 * @return
	 */
	private boolean isValidMove1(int player, int row0, int col0, int vRow,
			int vCol, int row1, int col1) {
		if ((row0<0) || (col0<0) || (row0>=cells.length) || (col0>=cells[row0].length))
			return isValidMove2(player, row1, col1, -vRow, -vCol);
		else if ((cells[row0][col0] ==playerToValue( player)) || (cells[row0][col0] == EMPTY))
			return isValidMove2(player, row1, col1, -vRow, -vCol);
		else
			return isValidMove1a(player, row0+vRow, col0+vCol, vRow, vCol, row1, col1);
	}

	/**
	 * Helper method for isValidMove.
	 * @param player
	 * @param row0
	 * @param col0
	 * @param vRow
	 * @param vCol
	 * @param row1
	 * @param col1
	 * @return
	 */
	private boolean isValidMove1a(int player, int row0, int col0, int vRow,
			int vCol, int row1, int col1) {
		if ((row0<0) || (col0<0) || (row0>=cells.length) || (col0>=cells[row0].length))
			return isValidMove2(player, row1, col1, -vRow, -vCol);
		else if(cells[row0][col0] == EMPTY)
			return isValidMove2(player, row1, col1, -vRow, -vCol);
		else if(cells[row0][col0] == playerToValue(player))
			return true;
		else
			return isValidMove1a(player, row0+vRow, col0+vCol, vRow, vCol, row1, col1);
	}

	/**
	 * Helper method for isValidMove.
	 * @param player
	 * @param row0
	 * @param col0
	 * @param vRow
	 * @param vCol
	 * @return
	 */
	private boolean isValidMove2(int player, int row0, int col0, int vRow, int vCol) {
		if((row0<0) || (col0<0) ||(row0>=cells.length) || (col0>=cells[row0].length))
			return false;
		else if ((cells[row0][col0] == playerToValue(player))|| (cells[row0][col0] == EMPTY))
			return false;
		else
			return isValidMove2a(player, row0+vRow, col0+vCol, vRow, vCol);
	}

	/**
	 * Helper method for isValidMove.
	 * @param player
	 * @param row0
	 * @param col0
	 * @param vRow
	 * @param vCol
	 * @return
	 */
	private boolean isValidMove2a(int player, int row0, int col0, int vRow, int vCol) {
		if ((row0<0) || (col0<0) ||(row0>=cells.length) || (col0>=cells[row0].length))
			return false;
		else if (cells[row0][col0] == EMPTY)
			return false;
		else if (cells[row0][col0] == playerToValue(player))
			return true;
		else
			return isValidMove2a(player, row0+vRow, col0+vCol, vRow, vCol);
	}


	/**
	 * Flips all the tokens to the supplied player's token when the player places a token at (row, col).
	 * @param row
	 * @param col
	 * @param player
	 * @return A vector of all the (row, col)'s that were flipped.
	 */
	private List<int[]> flipAll(int row, int col, int player)  {
		ArrayList<int[]> flipped = new ArrayList<int[]>();

		cells[row][col] = playerToValue(player);
		for (int i = 0; i < directions.length; i++) {
			int vr = directions[i][0];
			int vc = directions[i][1];
			flip(player, row+vr, col+vc,  vr,  vc, flipped);
			flip(player, row-vr, col-vc, -vr, -vc, flipped);
		}
		return flipped;
	}


	/**
	 * helper method for flipAll.
	 * @param player
	 * @param row0
	 * @param col0
	 * @param vRow
	 * @param vCol
	 * @param flipped
	 */
	private void flip(int player, int row0, int col0, int vRow, int vCol, List<int[]> flipped) {
		if ((row0<0) || (col0<0) || (row0>=cells.length) || (col0>=cells[row0].length))
			return;
		else if ((cells[row0][col0] == playerToValue(player)) || (cells[row0][col0] == EMPTY))
			return;
		else if (flip_h(player, row0+vRow, col0+vCol, vRow, vCol, flipped))  {
			cells[row0][col0] = playerToValue(player);
			flipped.add(new int[]{row0, col0});
		}
	}

	/**
	 * Helper method for flip.
	 * @param player
	 * @param row0
	 * @param col0
	 * @param vRow
	 * @param vCol
	 * @param flipped
	 * @return
	 */
	private boolean flip_h(int player, int row0, int col0, int vRow, int vCol, List<int[]> flipped) {
		if((row0<0) || (col0<0) || (row0>=cells.length) || (col0>=cells[row0].length))
			return false;
		else if(cells[row0][col0] == EMPTY)
			return false;
		else if(cells[row0][col0] == playerToValue(player))
			return true;
		else if(flip_h(player, row0+vRow, col0+vCol, vRow, vCol, flipped)) {
			cells[row0][col0] = playerToValue(player);
			flipped.add(new int[]{row0, col0});
			return true;
		}
		else
			return false;
	}

	/**
	 * Counts the tokes for each player.
	 * Player #0's totals are returned in index 0 or the result and the player #1's totals are at index 1.
	 * @param totals
	 * @return
	 */
	private int countTokens(final int[] totals) {
		mapAll(0,new IBoardLambda<Void>() {
			public boolean apply(int player, IBoardModel host, 
					int row, int col, int value, Void... nu) {
				if(cells[row][col] != EMPTY) {
					totals[valueToPlayer(cells[row][col])]++;
				}
				return true;
			}

			public void noApply(int player, IBoardModel host, Void... nu) {
			}
		});
		if (totals[0] > totals[1]) return -1;
		else if (totals[1] > totals[0]) return 1;
		return 0;
	}

	@Override
	public void displayScores() {
		// TODO Auto-generated method stub
		
	}
}
