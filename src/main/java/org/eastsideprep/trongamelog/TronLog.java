/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.trongamelog;

import enginePackage.Position;

/**
 *
 * @author gmein
 */
public interface TronLog {
    void UpdatePosition (int id, Position p);
    void KillBike(int id);
}
