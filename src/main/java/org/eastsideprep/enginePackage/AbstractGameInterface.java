package org.eastsideprep.enginePackage;

import org.eastsideprep.trongamelog.TronLog;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tespelien
 */
//probably useless?
public interface AbstractGameInterface {

    void init(TronLog tl);

    Tuple[] run(int n, Bike[] test);//runs n complete games, returns the winners by ID and times won

}
