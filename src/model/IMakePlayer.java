package model;

/**
* An abstract factory to create APlayers.  Used privately by GameModel.
*/
public interface IMakePlayer {
    /**
     * Instantiates an APlayer object given the player's "player number".
     * Player number 0 plays first, player number 1 plays second.
     * @param playerNo The player number for the player to be instantiated.
     * @return APlayer object
     */
    public APlayer create(int playerNo);
}