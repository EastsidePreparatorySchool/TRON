/*
 * This work is licensed under a Creative Commons Attribution-NonCommercial 3.0 United States License.
 * For more information go to http://creativecommons.org/licenses/by-nc/3.0/us/
 */
package org.eastsideprep.gamelog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author gmein
 */
public class GameLog {

    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock rlock;
    private final Lock wlock;

    private final GameLogState state;
    private ArrayList<GameLogEntry> log = new ArrayList<>();
    private final LinkedList<GameLogObserver> observers = new LinkedList<>();

    private int start = 0;
    private int end = 0;
    private int minRead = 0;
    private final int COLLAPSE_THRESHOLD = 1000;
    public int turnsCompleted = 0;

    public GameLog(GameLogState state) {
        rlock = rwl.readLock();
        wlock = rwl.writeLock();
        this.state = state;
        state.setLog(this);
    }

    public void addLogEntry(GameLogEntry item) {
        wlock.lock();
        try {
            log.add(item);
            end++;
        } finally {
            wlock.unlock();
        }

        removeStaleObservers();
//        printLogInfo("AE");
    }

    public void addLogEntries(List<GameLogEntry> list) {
        wlock.lock();
        try {
            log.addAll(list);
            end += list.size();
        } finally {
            wlock.unlock();
        }
        removeStaleObservers();
//        printLogInfo("AES");
    }

    public GameLogObserver addObserver(String client) {
        GameLogObserver obs = null;
        wlock.lock();
        try {
            removeStaleObservers();
            collapseRead();
            synchronized (observers) {
                obs = new GameLogObserver(this, client);
                obs.myState = getNewGameLogState();
                obs.maxRead = obs.myState.getEntryCount();
                observers.add(obs);
            }
        } finally {
            wlock.unlock();
        }
        printLogInfo("AO");
        return obs;
    }

    public int getLogSize() {
        return collapseRead();
    }

    public LinkedList<GameLogObserver> getObservers() {
        synchronized (observers) {
            return new LinkedList<>(observers);
        }
    }

    public void removeObserver(GameLogObserver obs) {
        synchronized (observers) {
            observers.remove(obs);
            updateMinRead();
        }
        printLogInfo("RO");
        collapseRead();
    }

    public int collapseRead() {
        int result = -1;
        updateMinRead();
        wlock.lock();
        try {
            if (minRead - start >= COLLAPSE_THRESHOLD) {
//            printLogInfo("CR1");
//            System.out.println("Log: compacting");
                // process all read items into state log, 
                for (int i = 0; i < minRead - start; i++) {
                    state.addEntry(log.get(i));
                }

                // take the sublist of unread items, make it the new list, 
                ArrayList<GameLogEntry> newLog = new ArrayList<>();
                newLog.addAll(log.subList(minRead - start, end - start));
                log = newLog;

                // and adjust the "start" offset
                start = minRead;
//            printLogInfo("CR2");
                //System.out.println("Log: compacted");
            }
            result = log.size();

        } finally {
            wlock.unlock();
        }
        return result;
    }

    public ArrayList<GameLogEntry> getNewItems(GameLogObserver obs) {
        ArrayList<GameLogEntry> result = new ArrayList<>();

        rlock.lock();
        int oldMinRead = minRead;
        try {
//            printLogInfo("GNI1", obs);
            int items = end - obs.maxRead;
            if (items > 0) {
                // copy the new items to the result
                result.addAll(log.subList(obs.maxRead - start, end - start));

                // update maxRead, and possibly minRead
                // need to lock this, multiple threads might want to do it
                synchronized (observers) {
                    int oldMax = obs.maxRead;
                    obs.maxRead = end;

                    // if we were at minRead we might need to move it
                    if (oldMax == minRead) {
                        updateMinRead();
                    }
                }
//                printLogInfo("GNI2", obs);

            }

        } finally {
            rlock.unlock();
        }

        collapseRead();

        return result;
    }

    public GameLogState getNewGameLogState() {
        GameLogState result = null;
        wlock.lock();
        try {
            result = state.copy();
        } finally {
            wlock.unlock();
        }
        return result;
    }

    private void updateMinRead() {
        int currentMin = end;
        boolean haveStales = false;

        synchronized (observers) {
            for (GameLogObserver o : observers) {
                if (o.isStale()) {
                    haveStales = true;
                } else if (o.maxRead < currentMin) {
                    currentMin = o.maxRead;
                }
            }
        }
        // record new minimum
        minRead = currentMin;

        // deal with delinquents
        // we do this last because we can't update the collection while iterating
        if (haveStales) {
            synchronized (observers) {
                observers.removeIf((o) -> o.isStale());
            }
        }
    }

    public void onDeath() {
        state.onDeath();
    }

    public void removeStaleObservers() {
        synchronized (observers) {
            observers.removeIf((o) -> o.isStale());
        }
    }

    private void printLogInfo(String op) {
        if (end < start || minRead < start || minRead > end) {
            System.out.println("---- log corrupt");
        }
        System.out.println("Log" + op + ": array size:" + log.size() + ", start:" + start + ", end:" + end + ", minRead:" + minRead);
    }

    private void printLogInfo(String op, GameLogObserver obs) {
        printLogInfo(op);
        if (obs.maxRead < start || obs.maxRead > end) {
            System.out.println("---- obs data corrupt");
        }
        System.out.println("  obs:" + Integer.toHexString(obs.hashCode()) + ", maxRead:" + obs.maxRead);
    }
}
