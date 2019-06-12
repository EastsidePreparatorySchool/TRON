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

    public class Position {
        public int x;
        public int y;
    }
    
    public int entryType;
    public int id;
    public Position p;

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

    public TronGameLogEntry() {
        this.entryType = Type.GAMETURN;
    }

    public TronGameLogEntry(Position p) {
        this.entryType = Type.RESET;
        this.p = p; //actually the width and height of the board
    }

    public TronGameLogEntry(int id, String placeholderbctheresanothermethod) { //WIN
        this.entryType = Type.WIN;
        this.id = id;
    }

    public TronGameLogEntry(TronGameLogEntry tge) {
        this.entryType = tge.entryType;
        this.id = tge.id;
        this.p = tge.p;
    }

    public static class Type {

        public static final int NOBODYWINS = -1;
        public static final int POSUPDATE = 1;
        public static final int DEATH = 2;
        public static final int TRAIL = 3;
        public static final int GAMETURN = 4;
        public static final int RESET = 5;
        public static final int WIN = 6;

    }
}
