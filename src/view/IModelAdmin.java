
package view;

import gameIO.ICommand;

import java.util.*;

/**
 * Administrative interface for the model.
 */
public interface IModelAdmin<TPlayer> {
    /**
     * Resets the model to its startup defaults.
     */
    public abstract void reset();

    /**
     * Halts the game after the current turn is completed.
     */
    public abstract void exit();

    /**
     * Returns a list of Objects that the view can use for selecting which type
     * of player to be used.  The toString() methods of the Objects in the Vector
     * will return a String description of the type of player.
     * @return
     */
    public abstract List<TPlayer> getPlayers();

    /**
     * Used by the view's adapter to inform the model which players were
     * selected from the set of possible players supplied by the getPlayers() method.
     * @param player1 The player that plays first.
     * @param player2 The player that plays second.
     */
    public abstract void setPlayers(TPlayer player1, TPlayer player2);

    /**
     * Creates a factory that can the model can use to instantiate a new
     * ComputerPlayer, given the class name of the required INextMoveStrategy.
     * @param className The fully qualified class name of the INextMoveStrategy to use.
     * @return
     */
    public abstract TPlayer addPlayer(String className);

    /**
    * Initializes the adapter to talk to the view.
    * @param command Adapter to talk to the view to display/clear a game piece
    * ("token") or a String message.
    */
	public abstract void setCommand(ICommand command);
}
