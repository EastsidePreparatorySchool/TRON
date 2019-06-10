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

    public TronGameLog(GameLogState state) {
        super(state);
    }

    @Override
    public void UpdatePosition(int id, Tuple t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void KillBike(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Setup(int size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void runResults(Bike[] testBikes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
