
package model;

import gameIO.*;

/**
 * The interface used to tell the view that the user needs to try a move.
 */
public interface ITurnAdmin {
    /**
     * Tells the view (user) to try a move.
     * @param requestor The requestor used by the view to communicate which move it wishes to try.
     */
    public void takeTurn(IViewRequestor requestor);
}

