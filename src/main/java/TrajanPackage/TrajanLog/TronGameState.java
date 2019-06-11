/*
 * This work is licensed under a Creative Commons Attribution-NonCommercial 3.0 United States License.
 * For more information go to http://creativecommons.org/licenses/by-nc/3.0/us/
 */
package TrajanPackage.TrajanLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author gmein
 */
public class TronGameState {

    private GameLog log;
    public int entries;
    public int totalTurns;
    public boolean forUpdates;
    public TronGameLogEntry lastTurn;
    private TronGameLogEntry lastGameState;

    private HashMap<Integer, TronGameLogEntry> bikes = new HashMap<>();
    private HashMap<Integer, TronGameLogEntry> trails = new HashMap<>();
    private ArrayList<TronGameLogEntry> deaths = new ArrayList<>();

    // what the console uses initially, 
    // and what "copy" uses internally
    public TronGameState(int total, int entries) {
        this.totalTurns = total;
        this.entries = entries;
    }

    // this is for clients who want to compact a set of log entries after getting them
    public TronGameState(boolean forUpdates) {
        this.totalTurns = 0;
        this.entries = 0;
        this.forUpdates = forUpdates; //purpose of the state, whether to record kills or just drop the alien
    }

    // this is used by the log to hand a new copy to a client
    public TronGameState copy() {
        TronGameState tronGameState = new TronGameState(totalTurns, entries);

        // deep-copy aliens, planets, species and stars
        tronGameState.bikes = new HashMap<>();
        for (Entry<Integer, TronGameLogEntry> e : bikes.entrySet()) {
            tronGameState.bikes.put(e.getKey(), new TronGameLogEntry()); //something goes there
        }
        return tronGameState;
    }

    // the log uses this to compact itself into the state
    public void addEntry(TronGameLogEntry ge) {
        TronGameLogEntry tge = null;
        try {
            tge = (TronGameLogEntry) ge;
        } catch (Exception e) {
            System.err.println("GameState: Invalid log entry added to state");
            return;
        }
        switch (tge.entryType) {
            case TronGameLogEntry.Type.NOBODYWINS: //shouldn't happen but here it is
                System.err.println("NOBODYWINS: how did we get here?");
                lastTurn = new TronGameLogEntry(tge);
                break;
            case TronGameLogEntry.Type.POSUPDATE:
                lastTurn = new TronGameLogEntry(tge);
                bikes.put(tge.bikeId, lastTurn);
                break;
            case TronGameLogEntry.Type.DEATH:
                if (forUpdates) {
                    // in updates, we really need to show the kill action
                    // unless it just kills an alien born in the same update
                    TronGameLogEntry tgeDead = bikes.get(tge.bikeId);
                    if (tgeDead == null) {
                        // this "bike" had not been mentioned in this update before
                        // so put a copy of the kill record in there
                        // the client will presumably know about this id
                        tgeDead = new TronGameLogEntry(tge);
                        deaths.add(tgeDead);
                        break;
                    } else if (tgeDead.entryType == TronGameLogEntry.Type.POSUPDATE) {
                        // if there was a prior MOVE record,
                        // we need to display the move before the kill, 
                        // so just add the kill.
                        tgeDead = new TronGameLogEntry(tge);
                        deaths.add(tgeDead);
                    }
                } else {
                    // in initial states, we just get rid of the guy altogether
                    bikes.remove(tge.bikeId);
                }
                break;
            case TronGameLogEntry.Type.TRAIL:
                lastTurn = new TronGameLogEntry(tge);
                trails.put(tge.bikeId, lastTurn);
                break;
            case TronGameLogEntry.Type.WIN:
                lastTurn = new TronGameLogEntry(tge);
                break;
        }
        entries++;
        totalTurns++;
    }

    // log needs this
    public int getEntryCount() {
        return entries;
    }

    // this is what the server will use to get entries to feed to the client. 
    public ArrayList<TronGameLogEntry> getCompactedEntries() {
        //GameLogEntry size = new GameLogEntry(bikes.size()); //NEED TO FIX
        ArrayList<TronGameLogEntry> result = new ArrayList<>();

        result.addAll(bikes.values());
        result.addAll(trails.values());
        result.add(null);
        result.addAll(deaths);
        if (lastTurn != null) {
            result.add(lastTurn);
        }
        if (lastGameState != null) {
            result.add(lastGameState);
        }
        return result;
    }

    public void onDeath() {
        //pointless method oof
    }

    public void setLog(GameLog log) {
        this.log = log;
    }
}
