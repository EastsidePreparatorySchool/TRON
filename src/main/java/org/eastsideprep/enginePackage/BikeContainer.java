package org.eastsideprep.enginePackage;

import org.eastsideprep.enginePackage.Position;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tespelien
 */
public class BikeContainer {

    Bike bike;
    Position currentPosition;
    int id;
    int velocity;
    ArrayList<Position> trail = new ArrayList<>();

    BikeContainer(Bike b, Position p, int v) {
        this.id = bike.id;
        this.bike = b;
        this.currentPosition = p;
        this.velocity = v;
    }
    
    

    //not used yet
    int fuel;
    int acceleration;

}
