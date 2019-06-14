/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.trongamelog;

import eastsideprep.org.troncommon.TronLogInterface;
import eastsideprep.org.troncommon.Tuple;
import org.eastsideprep.enginePackage.Bike;
import org.eastsideprep.gamelog.GameLog;
import org.eastsideprep.gamelog.GameLogState;

/**
 *
 * @author fzhang
 */
public class TronGameLog extends GameLog implements TronLogInterface  {

    //to be 100% honest, i have no idea what to put here
    //idk why we even need this
    
    TronGameState state;
    
    public TronGameLog(GameLogState state) {
        super(state);
        this.state = (TronGameState) state;
    }

    @Override
    public void UpdatePosition(int id, Tuple t) {
        state.addEntry(new TronGameLogEntry(id, t));
    }

    @Override
    public void KillBike(int id) {
        state.addEntry(new TronGameLogEntry(id));
    }

    @Override
    public void GameTurn() {
        state.addEntry(new TronGameLogEntry());
    }
    
    @Override
    public void runResults(Bike[] testBikes) {
        //???
        //what the heck is this and why is it in my log.
        
        //traj please explain
    }
    
}
