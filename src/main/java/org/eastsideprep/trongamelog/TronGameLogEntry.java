/*
 * This work is licensed under a Creative Commons Attribution-NonCommercial 3.0 United States License.
 * For more information go to http://creativecommons.org/licenses/by-nc/3.0/us/
 */
package org.eastsideprep.trongamelog;

import java.util.ArrayList;
import org.eastsideprep.gamelog.GameLogEntry;

/**
 *
 * @author gmein
 */
public class TronGameLogEntry extends GameLogEntry {

    // to record: moves, acceleration, state change (t,e, orbital, secrets), comm, trade, 
    public class Position {

        int x;
        int y;
    }

    int entryType;
    int id;
    Position p;

    public TronGameLogEntry(int id, Position p) {
        this.entryType = Type.POSUPDATE;
        this.id = id;
        this.p = p;
    }

    public TronGameLogEntry(int id, Position p, ArrayList<Position> trail) {
        this.entryType = Type.TRAIL;
        this.id = id;
        this.p = p;
    }
    
   public TronGameLogEntry(int id) {
        this.entryType = Type.DEATH;
        this.id = id;
    }  

    public TronGameLogEntry(TronGameLogEntry sge) {

    }

    public static class Type {

        public static final int POSUPDATE = 1;
        public static final int DEATH = 2;
        public static final int TRAIL = 3;

    }
}
