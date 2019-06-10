/*
 * This work is licensed under a Creative Commons Attribution-NonCommercial 3.0 United States License.
 * For more information go to http://creativecommons.org/licenses/by-nc/3.0/us/
 */
package org.eastsideprep.trongamelog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.eastsideprep.gamelog.GameLog;
import org.eastsideprep.gamelog.GameLogEntry;
import org.eastsideprep.gamelog.GameLogState;

/**
 *
 * @author gmein
 */
public class TronGameState implements GameLogState {

    private GameLog log;
    public int entries;
    public int totalTurns;
    public boolean forUpdates; //ew dont?
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
        this.forUpdates = forUpdates; //purpose of the state, whether to record kills or just drop the bike
    }

    // this is used by the log to hand a new copy to a client
    @Override
    public GameLogState copy() {
        TronGameState tron = new TronGameState(totalTurns, entries);

        // deep-copy aliens, planets, species and stars
        tron.bikes = new HashMap<>();
        for (Entry<Integer, TronGameLogEntry> e : bikes.entrySet()) {
            tron.bikes.put(e.getKey(), new TronGameLogEntry()); //something goes there
        }

        tron.trails = new HashMap<>();
        for (Entry<Integer, TronGameLogEntry> e : trails.entrySet()) {
            tron.trails.put(e.getKey(), new TronGameLogEntry(e.getValue()));
        }

        tron.deaths = new ArrayList<>();
        for (TronGameLogEntry e : deaths) {
            tron.deaths.add(new TronGameLogEntry(e));
        }

        return tron;
    }

    // the log uses this to compact itself into the state
    @Override
    public void addEntry(GameLogEntry ge) {
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
                bikes.put(tge.id, lastTurn);
                break;
            case TronGameLogEntry.Type.DEATH:
                bikes.remove(tge.id);
                break;
            case TronGameLogEntry.Type.TRAIL:
                lastTurn = new TronGameLogEntry(tge);
                trails.put(tge.id, lastTurn);
                break;
            case TronGameLogEntry.Type.GAMETURN:
                lastTurn = new TronGameLogEntry(tge);
                totalTurns++;
                break;
            case TronGameLogEntry.Type.RESET:
                lastTurn = new TronGameLogEntry(tge);
                //i dont know what to do here so
                break;
            case TronGameLogEntry.Type.WIN:
                lastTurn = new TronGameLogEntry(tge);
                break;
            default:
                break;
        }
        entries++;
    }

    @Override
    public int getEntryCount() {
        return entries;
    }

    @Override
    public ArrayList<GameLogEntry> getCompactedEntries() {
        GameLogEntry size = new TronGameLogEntry(bikes.size()); //NEED TO FIX
        ArrayList<GameLogEntry> result = new ArrayList<>();

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

    @Override
    public void onDeath() {
        System.err.println("*me thinking about death*");
    }

    @Override
    public void setLog(GameLog log) {
        this.log = log;
    }
    
}