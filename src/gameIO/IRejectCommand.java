
package gameIO;

/**
 * A basic command that is executed when a move request has been rejected by the model.
 */
public interface IRejectCommand {
    /**
     * This method is called by the model when a move is rejected.
     */
    public abstract void execute();
}
