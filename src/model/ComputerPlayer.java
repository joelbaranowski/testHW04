
package model;

import gameIO.*;

import java.awt.*;

/**
 * This class represents a concrete computer player.
 */
public class ComputerPlayer extends APlayer {
    private INextMoveStrategy iNextMoveStrategy;
    private IModel model;


    /**
     * The constructor for the class.
     * @param iRequestor The requestor that this player will use to communicate with the model.
     * @param player This player's player number.
     * @param model A reference to the IModel.
     * Needed to call the getNextMove() method of the INextMoveStrategy.
     * @param iNextMoveStrategy The strategy used to calculate this player's next movfe.
     */
    public ComputerPlayer(IRequestor iRequestor, int player, IModel model,
                          INextMoveStrategy iNextMoveStrategy) {
        super(iRequestor, player);
        this.model = model;
        this.iNextMoveStrategy = iNextMoveStrategy;
        System.out.println("ComputerPlayer #" + player + " is using " + iNextMoveStrategy);
    }

    /**
     * Used by the TurnControl to tell this player to take its turn.
     * When the computer takes its turn, it will calculate its next move and
     * then request that move of  the model.
     * If the computer makes and invalid move, the computer will print an error
     * message to the screen and the  it will take its turn again.
     */
    public void takeTurn() {
        System.out.print("Computer player "+ getPlayer() +" ("+this+") takes turn...");
        final Point p = iNextMoveStrategy.getNextMove(model, getPlayer());
        System.out.println("and moves to "+p);
        getRequestor().setTokenAt(p.y, p.x, getPlayer(), new IRejectCommand() {
            public void execute()  {
                System.out.println("ComputerPlayer #" + getPlayer() +
                                ": The move at ("+p.x+", "+p.y+") is invalid.");
                takeTurn();
            }
        });
    }
}

