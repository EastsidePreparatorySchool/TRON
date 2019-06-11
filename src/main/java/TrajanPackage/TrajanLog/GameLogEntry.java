/*
 * This work is licensed under a Creative Commons Attribution-NonCommercial 3.0 United States License.
 * For more information go to http://creativecommons.org/licenses/by-nc/3.0/us/
 */
package TrajanPackage.TrajanLog;

/**
 *
 * @author gmein
 */
public class GameLogEntry {

    int type;
    int id;

    GameLogEntry() {
    }

    GameLogEntry(int t, int id) {
        this.type = t;
        this.id = id;
    }
}
