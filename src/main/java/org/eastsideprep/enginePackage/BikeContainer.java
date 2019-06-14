package org.eastsideprep.enginePackage;

import eastsideprep.org.troncommon.Tuple;
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
    Tuple pos;
    int direction;
    int id;
    boolean isAlive;
    int velocity;
    ArrayList<Tuple> trail = new ArrayList<>();

    BikeContainer(Bike b, Tuple p, int v) {

        this.bike = b;
        this.id = b.bikeId;
        this.pos = p;
        isAlive = true;
        this.velocity = v;
        this.direction = bike.direction;
    }
    
    public String toString() {return "id=" + id + " isAlive=" + isAlive + " pos=" + "(" + pos.x + ", " + pos.y + ")";};

    Tuple kill() {
        this.isAlive = false;
        return pos;
    }

    //not used yet
    int fuel;
    int acceleration;
}
