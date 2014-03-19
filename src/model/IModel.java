
package model;

import gameIO.*;



/**
 * This interface represents the model for a game in an MVC architecture.
 */
public interface IModel{
    /**
     * Used by the MVC controller to install the command that enables the model
     * to display information on the screen.
     * @param iCommand
     */
    public abstract void setCommand(ICommand iCommand);

    /**
     * Getter method for the IBoardModel being used by this game model.
     * @return
     */
    public abstract IBoardModel getBoardModel();
    /**
     * Get the time left for the current player's turn in milliseconds.
     * Return value is undefined if no player is currently taking their turn.
     */
    public abstract long getTimeLeft();
}
