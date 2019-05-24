package org.eastsideprep.enginePackage;

import org.eastsideprep.enginePackage.Tuple;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tespelien
 */
public class Bike {

    //a bike only knows its direction and starting position, otherwise it could cheat
    int id;
    int direction;
    Tuple startingPosition;

    Bike(int s, Tuple p) {
        id = s;
        startingPosition = p;
    }
}
