
package model;

import gameIO.*;
import java.util.*;
import model.nextMove.*;

/**
 * A concrete model of a game.  For descriptions of the methods, see the IModel
 * and IModelAdmin interface documentation. Except for the setPlayers(),
 * getPlayers() and addPlayer() methods, whose descriptions are below.
 */
public final class GameModel implements IModel {
	/**
	 * The maximum amount of time a player is alloted to take its turn.
	 */
	private int maxTurnTime = 15;   // default time in seconds

	/**
	 * For player management.  Initialized to a null TurnControl object.
	 */
	private TurnControl turnControl = TurnControl.NullObject;

	/**
	 * Adapter to talk to the view to display/clear a game piece ("token") or
	 * a String message.
	 */
	private ICommand iCommand;

	/**
	 * Adapter to talk to the view to announce winner, draw, or reset.
	 */
	private IViewAdmin viewAdmin;

	/**
	 * Adapter to talk to the view to tell that the human player needs to try a
	 * move.
	 */
	private ITurnAdmin turnAdmin;

	/**
	 * The invariant, encapsulated rules and behaviors of a game.
	 */
	private IBoardModel boardModel;

	/**
	 * Initializes this GameModel to a given IBoradModel and a max turn time.
	 * @param boardModel for example Othello or TicTacToe.
	 * @param maxTurnTime maximum allowable time to make the next move.
	 * @param iTurnAdmin 
	 * @param iViewAdmin 
	 */
	public GameModel(IBoardModel boardModel, int maxTurnTime, IViewAdmin viewAdmin, ITurnAdmin turnAdmin) {
		this.boardModel = boardModel;
		this.maxTurnTime = maxTurnTime;
		this.viewAdmin = viewAdmin;
		this.turnAdmin = turnAdmin;

	}

	private IRequestor requestor =
			new IRequestor() {
		public void setTokenAt(final int row, final int col, final int player,
				final IRejectCommand rejectCommand) {
			boardModel.makeMove(row, col, player,
					new ICheckMoveVisitor() {
				public void invalidMoveCase() {
					System.out.println("invalid");
					rejectCommand.execute();
				}
				public void validMoveCase() {
					System.out.println("cehck");
					boardModel.redrawAll(iCommand);
				}
			},
			new IBoardStatusVisitor<Void, Void>() {
				public Void player0WonCase(IBoardModel host, Void... nu) {
					turnControl.setHalt();
					host.displayScores();
					viewAdmin.win(0);
					return null;
				}
				public Void player1WonCase(IBoardModel host, Void... nu) {
					turnControl.setHalt();
					host.displayScores();
					viewAdmin.win(1);
					return null;
				}
				public Void drawCase(IBoardModel host, Void... nu) {
					turnControl.setHalt();
					viewAdmin.draw();
					return null;
				}
				public Void noWinnerCase(IBoardModel host, Void... nu) {
					turnControl.setSkipPlayer(boardModel.isSkipPlayer(1-player));
					turnControl.setProceed();
					return null;
				}
			});
		}
	};



	/**
	 * Initializes the adapter to talk to the view.
	 * @param command Adapter to talk to the view to display/clear a game piece
	 * ("token") or a String message.
	 */
	 public void setCommand(ICommand command) {
		iCommand = command;
	}

	public void reset() {
		System.out.println("Resetting");
		boardModel.reset();
		boardModel.redrawAll(iCommand);
		turnControl.setHalt();
	}

	/**
	 * Adds players created by the supplied factories to the TurnControl.
	 * Assumes that the players are IMakePlayer factory objects.
	 * Any existing players are lost.   Player 0 starts the game.
	 * @param player0 Factory for the starting player.
	 * @param player1 Factory for the second player.
	 */
	 public void setPlayers(IMakePlayer player0, IMakePlayer player1) {
		 // Last in/first play
		 turnControl = new TurnControl(player1.create(1));
		 turnControl.addPlayer( player0.create(0));
		 turnControl.setAdapters(viewAdmin, iCommand);
		 turnControl.run(maxTurnTime);
	 }

	 public IBoardModel getBoardModel() {
		 return boardModel;
	 }


	 /**
	  * Returns a Vector filled with IMakePlayer factory objects.
	  * @return
	  */
	 public List<IMakePlayer> getPlayers() {
		 List<IMakePlayer> v = new ArrayList<IMakePlayer>();
		 v.add(new IMakePlayer() {
			 public APlayer create(int playerNo) {
				 return new HumanPlayer(requestor, playerNo, turnAdmin);
			 }
			 public String toString() {
				 return "Human player";
			 }
		 });

		 v.add(new IMakePlayer() {
			 public APlayer create(int playerNo) {
				 return new ComputerPlayer(requestor, playerNo, GameModel.this,
						 new RandomMoveStrategy());
			 }
			 public String toString() {
				 return "Computer RandomMove";
			 }
		 });

		 v.add(new IMakePlayer() {
			 public APlayer create(int playerNo) {
				 return new ComputerPlayer(requestor, playerNo, GameModel.this,
						 new RandomValidMove());
			 }
			 public String toString() {
				 return "Computer RandomValidMove";
			 }
		 });

		 v.add(new IMakePlayer() {
			 public APlayer create(int playerNo) {
				 return new ComputerPlayer(requestor, playerNo, GameModel.this,
						 new MinMax(new MinMaxFac()));
			 }
			 public String toString() {
				 return "Computer w. MinMax";
			 }
		 });

		 v.add(new IMakePlayer() {
			 public APlayer create(int playerNo) {
				 return new ComputerPlayer(requestor, playerNo, GameModel.this,
						 new MinMax(new AlphaBetaFac()));
			 }
			 public String toString() {
				 return "Computer w. AlphaBeta";
			 }
		 });

		 v.add(new IMakePlayer() {
			 public APlayer create(int playerNo) {
				 return new ComputerPlayer(requestor, playerNo, GameModel.this,
						 new MinMax(new DepthFac(new AlphaBetaFac(),2)));
			 }
			 public String toString() {
				 return "Computer w. Depth 2";
			 }
		 });

		 v.add(new IMakePlayer() {
			 public APlayer create(int playerNo) {
				 return new ComputerPlayer(requestor, playerNo, GameModel.this,
						 new MinMax(new DepthFac(new AlphaBetaFac(),3)));
			 }
			 public String toString() {
				 return "Computer w. Depth 3";
			 }
		 });

		 return v;
	 }

	 public void exit() {
		 //reset();
	 }


	 /**
	  * Add a ComputerPlayer based using the INextMoveStrategy specified by the
	  * fully qualified (i.e. included package name) String classname.
	  * The strategy is assumed to have either a no-parameter constructor or a
	  * constructor that takes an IModel as an input parameter.
	  * An IMakePlayer factory is returned.
	  * @param className Fully qualified class name for an INextMoveStrategy subclass.
	  * @return IMakePlayer object that can construct a new Computer player with the supplied strategy.
	  */
	 public IMakePlayer addPlayer(final String className) {
		 final INextMoveStrategy strategy;
		 try {
			 java.lang.reflect.Constructor<?> c =  Class.forName(className).getConstructors()[0];
			 Object [][] args = new Object[][]{new Object[]{}, new Object[]{this}};
			 strategy = (INextMoveStrategy) c.newInstance(args[c.getParameterTypes().length]);
		 }
		 catch(Exception ex) {
			 iCommand.setMessage(ex.toString());
			 return null;
		 }
		 iCommand.setMessage("");
		 return new IMakePlayer() {
			 public APlayer create(int playerNo) {
				 return new ComputerPlayer(requestor, playerNo, GameModel.this, strategy);
			 }
			 public String toString() {
				 return className;
			 }
		 };
	 }

	 /**
	  * Get the time left for the current player's turn in milliseconds.
	  * Return value is undefined if no player is currently taking their turn.
	  * @return
	  */
	 public long getTimeLeft() {
		 return turnControl.getTimeLeft();
	 }
	 /**
	  * Starts the game  model, which includes initializing its view with parameters from the model. */
	 public void start() {
		 // TODO Auto-generated method stub
		 viewAdmin.setDimension(getBoardModel().getDimension());
	 }
}
