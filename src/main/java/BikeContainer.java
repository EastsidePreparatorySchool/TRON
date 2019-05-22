
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
    int[] currentPosition = new int[2];
    ArrayList<int[]> trail = new ArrayList<>();
    int velocity;

    BikeContainer(Bike b, int[] pos, int v) {
        bike = b;
        currentPosition = pos;
        velocity = v;
    }
    
    //not used yet
    int fuel;
    int acceleration;

}
