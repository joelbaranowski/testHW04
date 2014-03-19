
package model;

/**
 * This class represents an abstract player of the game.
 * The players are represented as a circular linked list.
 */
public abstract class APlayer {
    private IRequestor iRequestor;
    private int player;
    private APlayer nextPlayer = this;
    /**
     * The constructor for the class.
     * @param iRequestor The  requestor that the player uses to request moves of the board.
     * @param player The player number of this player.
     */
    public APlayer(IRequestor iRequestor, int player)   {
        this.iRequestor = iRequestor;
        this.player = player;
    }


    /**
     * Tells this player to take its turn.
     */
    public abstract void takeTurn();

    /**
     * Accessor method for the installed requestor.
     * @return
     */
    public IRequestor getRequestor()  {
        return(iRequestor);
    }

    /**
     * Accessor method for this player's number.
     * @return
     */
    public int getPlayer()  {
        return(player);
    }


    /**
     * Accessor method for the player whose turn is after this player's.
     * @return
     */
    public APlayer getNextPlayer() {
        return(nextPlayer);
    }

    /**
     * Accessor method for the player whose turn is after this player's.
     * @param nextPlayer
     */
    private void setNextPlayer(APlayer nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    /**
     * Inserts the given player as the player whose turn is next.
     * The current next player will become the supplied player's next player.
     * @param player
     */
    public void insertAsRest(APlayer player)  {
        player.setNextPlayer(getNextPlayer());
        setNextPlayer(player);
    }
}

