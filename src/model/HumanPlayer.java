
package model;

import gameIO.*;
/**
 * This class represents a concrete human player.
 */
public class HumanPlayer extends APlayer {

    private ITurnAdmin turnAdmin;
    /**
     * The constructor for this class.
     * @param iRequestor The requestor used to request a move of the model.
     * @param player The player number of this player.
     * @param turnAdmin The interface used to tell the view that the game is
     * requires the user to make a move.
     */
    public HumanPlayer(IRequestor iRequestor, int player, ITurnAdmin turnAdmin) {
        super(iRequestor, player);
        this.turnAdmin = turnAdmin;
    }


    /**
     * Used by the TurnControl to tell this player to take its turn.
     * The human player will package up the IRequestor inside an IViewRequestor
     * and inform the view to use the IViewRequestor to attempt a move.
     * The view's reject command is augmented with an automatic re-taking of the
     * turn if the move is rejected by the model.
     * The view does not have to handle the re-taking of the turn.
     */
    public void takeTurn() {
        System.out.println("Human player "+ getPlayer() +" takes turn.");
        turnAdmin.takeTurn(new IViewRequestor() {
            public void setTokenAt(int row, int col, final IRejectCommand rejectCommand) {
                getRequestor().setTokenAt(row, col, getPlayer(), new IRejectCommand() {
                    public void execute() {
                        rejectCommand.execute();
                        takeTurn();
                    }
                });
            }
        });
    }
}

