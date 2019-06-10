/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.tron;

import java.util.ArrayList;
import org.eastsideprep.gamelog.GameLog;
import org.eastsideprep.gamelog.GameLogEntry;
import static org.eastsideprep.tron.main.STATE;
import org.eastsideprep.trongamelog.TronGameLogEntry;

/**
 *
 * @author fzhang
 */
public class Observer {

    GameLog log;

    public Observer(GameLog log) {
        this.log = log;
    }

    ArrayList<GameLogEntry> getNewItems() {
        ArrayList<GameLogEntry> log = STATE.getCompactedEntries(); //idk how to get new items so here they are
        return log;
    }
}
