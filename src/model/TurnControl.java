package model;
import gameIO.*;
/**
 * This class encapsulates the turn control for a game.
 */
public class TurnControl {

    public static final TurnControl NullObject = // Null object pattern!
        new TurnControl() {
            public void setHalt() {
            }

            public void setProceed() {
            }
        };

    private TurnControl() {
    }

    /**
    * The circular list of players.
    */
    private APlayer players;
    /**
    * Flag to skip the next player if true.
    */
    private boolean skipPlayer = false;

    private boolean isFullHalt = false;

    /**
    * An abstract turn control strategy.
    */
    abstract class ANextTurn {
        /**
         * Runs the strategy.
         * @return True to continue, false to terminate the turn control and hence the game.
         */
        abstract boolean apply();
    }

    private IViewAdmin viewAdmin;
    private ICommand iCommand;

    public TurnControl(APlayer players) {
        this.players = players;
    }

    // Adds a new player to the turn control.  Last in/first play.
    public void addPlayer(APlayer player) {
        players.insertAsRest(player);

        // The following is just to print out the players list.
        APlayer temp = players;
        int i=1;
        do {
            System.out.print("player "+i+" = "+temp+" ");
        }
        while (players!=(temp=temp.getNextPlayer()));
        System.out.println("");
    }

    public void setAdapters( IViewAdmin viewAdmin, ICommand iCommand) {
        this.viewAdmin = viewAdmin;
        this.iCommand = iCommand;
    }

    // Advances to next player and tells it to take its turn.
    private ANextTurn proceedTurn = new ANextTurn() {
        boolean apply() {
            System.out.println("Proceeding to next player...");
            players = players.getNextPlayer();
            if(skipPlayer){
                skipPlayer = false;
                players = players.getNextPlayer();
            }
            setWait();
            restartCheckTime();
            players.takeTurn();
            return true;
        }
    };

    /**
    * Waits for the current player to finish.
    */
    private ANextTurn waitTurn = new ANextTurn(){
        boolean apply() {
            System.out.println("Waiting....");
            try  {
                Thread.sleep(500);
            }
            catch (Exception ex) {
                System.err.println(ex);
            }
            return true;
        }
    };

    /**
    * Halts the turn control.
    */
    private ANextTurn haltTurn = new ANextTurn() {
        boolean apply() {
            System.out.println("Halting....");
            return false;
        }
    };


    /**
    * The current strategy being used.
    */
    private volatile ANextTurn nextTurn = proceedTurn;

    /**
    * Starts the game by having the first player take its turn.
    * Assumes that players is pointing to the last player.
    */
    public void run(int maxTurnTime) {
        timeInterval = 1000 * maxTurnTime;
        isFullHalt = false;
        nextTurn = proceedTurn;
        runCheckTime();
        new Thread() {
            public void run() {
                try  {
                    while(nextTurn.apply()) {
                        if(isFullHalt)  {
                            System.out.println("TurnControll.run():  Full halt.");
                            break;
                        }
                    }
                }
                catch(Exception e) {
                    System.out.println("TurnControl.run() exception: "+e);
                }
            }
          }.start();
    }

    /**
    * Sets the control to proceed to the next player.
    * The skip control is reset to not skip.
    */
    synchronized public void setProceed() {
        pauseCheckTime();
        nextTurn = proceedTurn;
    }

    /**
    * Sets the control to wait for the current player.
    */
    synchronized public void setWait()  {
        nextTurn = waitTurn;
    }

    /**
    * Ends the game by stopping the turn control.
    */
    synchronized public void setHalt() {
        nextTurn = haltTurn;
        isTimeThreadContinue = false;
    }

    synchronized public void setFullHalt() {
        isFullHalt = true;
        setHalt();
    }

    /**
    * Input true sets the turn control to skip the next player.
    * @param skipPlayer true to skip, false to not skip.
    */
    synchronized public void setSkipPlayer(boolean skipPlayer){
        //System.out.println("Skip player = "+skipPlayer);
        this.skipPlayer = skipPlayer;
    }

    /**
    * The absolute time before which the player must move.
    */
    private long endTime;

    /**
    * The maximum time interval a player is allowed to move.
    */
    private long timeInterval = 10000;

    /**
    * The interval, in milliseconds, at which the system checks if the player
    * has exceeded their allotted time.
    */
    private int timeCheckInterval = 10;

    /**
    * Sets the maximum time interval for a player, in milliseconds.
    * @param timeInterval
    */
//    private void setTimeInterval(long timeInterval)  {
//        this.timeInterval = timeInterval;
//    }

    /**
    * The time remaining in milliseconds
    */
    public long getTimeLeft() {
        return (endTime - System.currentTimeMillis());
    }

    /**
    * Flag used to stop the time checking thread.
    */
    private boolean isTimeThreadContinue = true;


    /**
    * Restarts the player timer.
    */
    private void restartCheckTime() {
        endTime = System.currentTimeMillis() + timeInterval;
    }

    /**
    * Pauses the player timer.  Used when switching players.
    */
    private void pauseCheckTime() {
        System.out.println("Player #" + players.getPlayer() + " used " +
                           ((timeInterval - getTimeLeft())/1000.0) + " secs.");
        endTime = Long.MAX_VALUE;
    }


    /**
    * Starts up the player timer thread.
    */
    private void  runCheckTime()  {
        isTimeThreadContinue = true;
        pauseCheckTime();
        new Thread() {
            public void run() {
                while(isTimeThreadContinue)  {
                    iCommand.setMessage("Time left for player #" +
                                        players.getPlayer() + "= " +
                                        getTimeLeft()/1000.0);
                    if(getTimeLeft()<0)  {
                        setFullHalt();
                        iCommand.setMessage("Player #" + players.getPlayer() +
                                         " has exceeded their allotted time!");
                        viewAdmin.win(players.getNextPlayer().getPlayer());
                        break;
                    }
                    try  {
                        Thread.sleep(timeCheckInterval);
                    }
                    catch (Exception ex) {
                        System.out.println("TurnControl.runCheckTime:" + ex.getMessage());
                        break;
                    }
                }
            }
        }.start();
    }
}