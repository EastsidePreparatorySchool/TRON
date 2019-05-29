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
public class TronGameState   implements GameLogState {

    private GameLog log;
    public int entries;
    public int totalTurns;
    public boolean forUpdates;
    public TronGameLogEntry lastTurn;
    private TronGameLogEntry lastGameState;

    private HashMap<Integer, TronGameLogEntry> bikes = new HashMap<>();
    private ArrayList<TronGameLogEntry> deaths = new ArrayList<>();
    private ArrayList<TronGameLogEntry> trails = new ArrayList<>();
//    private HashMap<Integer, SCGameLogEntry> planets = new HashMap<>();
//    private HashMap<Integer, SCGameLogEntry> orbits = new HashMap<>();
//    private ArrayList<SCGameLogEntry> stars = new ArrayList<>();
//    private ArrayList<SCGameLogEntry> species = new ArrayList<>();
//    private ArrayList<SCGameLogEntry> fights = new ArrayList<>();

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
    @Override
    public GameLogState copy() {
        TronGameState tron = new TronGameState(totalTurns, entries);

        // deep-copy aliens, planets, species and stars
        tron.bikes = new HashMap<>();
        for (Entry<Integer, TronGameLogEntry> e : bikes.entrySet()) {
            tron.bikes.put(e.getKey(), new TronGameLogEntry()); //something goes there
        }
//       tron.planets = new HashMap<>();
//       for (Entry<Integer, SCGameLogEntry> e : planets.entrySet()) {
//           sc.planets.put(e.getKey(), new SCGameLogEntry(e.getValue()));
//       }
//        sc.stars = new ArrayList<>();
//        for (SCGameLogEntry e : stars) {
//            sc.stars.add(new SCGameLogEntry(e));
//        }
//        sc.species = new ArrayList<>();
//        for (SCGameLogEntry e : species) {
//            sc.species.add(new SCGameLogEntry(e));
//        }
//
//        sc.orbits = new HashMap<>();
//        for (Entry<Integer, SCGameLogEntry> e : orbits.entrySet()) {
//            sc.orbits.put(e.getKey(), new SCGameLogEntry(e.getValue()));
//        }
//        sc.fights = new ArrayList<>(); // we don't record fights in compacted log for state or update
//        sc.burns = new ArrayList<>(); // ditto for burns
//
//        if (lastTurn != null) {
//            sc.lastTurn = new SCGameLogEntry(lastTurn);
//        }
//
//        if (lastGameState != null) {
//            sc.lastGameState = new SCGameLogEntry(lastGameState);
//        }
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
                break;
            case TronGameLogEntry.Type.DEATH:
                if (forUpdates) {
                    // in updates, we really need to show the kill action
                    // unless it just kills an alien born in the same update
                    TronGameLogEntry tgeDead = bikes.get(tge.id);
                    if (tgeDead == null) {
                        // this "bike" had not been mentioned in this update before
                        // so put a copy of the kill record in there
                        // the client will presumably know about this id
                        tgeDead = new TronGameLogEntry(tge);
                        deaths.add(tgeDead);
                        break;
                    }
                    else if (tgeDead.entryType == TronGameLogEntry.Type.POSUPDATE) {
                        // if there was a prior MOVE record,
                        // we need to display the move before the kill, 
                        // so just add the kill.
                        tgeDead = new TronGameLogEntry(tge);
                        deaths.add(tgeDead);
                    }
                } else {
                    // in initial states, we just get rid of the guy altogether
                    bikes.remove(tge.id);
                }
                break;
            case TronGameLogEntry.Type.TRAIL:
                lastTurn = new TronGameLogEntry(tge);
                trails.add(new TronGameLogEntry(tge));
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

    // log needs this
    @Override
    public int getEntryCount() {
        return entries;
    }

//    public static TronGameState safeGetNewState(GameLogObserver obs) {
//        try {
//            return (TronGameState) obs.getInitialState();
//        } catch (Exception e) {
//            System.err.println("invalid game state at safegetnewgamestate");
//        }
//        return null;
//    }

    // this is what the server will use to get entries to feed to the client. 
    @Override
    public ArrayList<GameLogEntry> getCompactedEntries() {
        ArrayList<GameLogEntry> result = new ArrayList<>();

        result.addAll(bikes.values());
        result.addAll(deaths);
        result.addAll(trails);

//        result.addAll(species);
//        result.addAll(aliens.values());
//        if (fights.size() > 100) {
//            fights.subList(0, fights.size()-100-1).clear();
//        }
//        result.addAll(fights);
//        result.addAll(burns);
//        result.addAll(kills);
//        result.addAll(orbits.values());

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
        // just tell the log that the game is paused, 
        // to put clients into idle mode
//        log.addLogEntry(new SCGameLogEntry(SCGameLogEntry.Type.STATECHANGE,
//                0, 0, 0, 0,
//                null, null, 0 /* paused */, -1,
//                0.0, 0.0));
    }

    @Override
    public void setLog(GameLog log) {
        this.log = log;
    }

}
