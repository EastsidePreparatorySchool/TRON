/*
 * This work is licensed under a Creative Commons Attribution-NonCommercial 3.0 United States License.
 * For more information go to http://creativecommons.org/licenses/by-nc/3.0/us/
 */
package TrajanPackage.TrajanLog;

import TrajanPackage.Tuple;
import java.util.ArrayList;

/**
 *
 * @author gmein
 */
public class TronGameLogEntry extends GameLogEntry {

    int entryType;
    int bikeId;
    Tuple p;

    public TronGameLogEntry() {
        this.entryType = Type.DEFAULT;
        this.bikeId = -1;
        this.p = null;
    }

    public TronGameLogEntry(GameLogEntry ge) {
        this.entryType = ge.type;
        this.bikeId = ge.id;
        this.p = null;
    }

    public TronGameLogEntry(int id, Tuple p) {
        this.entryType = Type.POSUPDATE;
        this.bikeId = id;
        this.p = p;
    }

    public TronGameLogEntry(int id, Tuple p, String s) {
        this.bikeId = id;
        this.p = p;
        if (s.equals("t")) {
            this.entryType = Type.TRAIL;
        } else if (s.equals("w")) {
            this.entryType = Type.WIN;
        }
    }

    public TronGameLogEntry(int id) {
        this.entryType = Type.DEATH;
        this.bikeId = id;
        this.p = null;
    }

    public TronGameLogEntry(TronGameLogEntry tge) {
        this.entryType = tge.entryType;
        this.bikeId = tge.bikeId;
        this.p = tge.p;
    }

    public static class Type {

        public static final int NOBODYWINS = -1;
        public static final int DEFAULT = 0;
        public static final int POSUPDATE = 1;
        public static final int DEATH = 2;
        public static final int TRAIL = 3;
        public static final int WIN = 4;
    }

    public static ArrayList<TronGameLogEntry> translateEntryTypes(ArrayList<GameLogEntry> gleal) {
        ArrayList<TronGameLogEntry> tgleal = new ArrayList<>();//the best naming conventions
        for (GameLogEntry gle : gleal) {
            tgleal.add(new TronGameLogEntry(gle));
        }
        return tgleal;
    }

}
