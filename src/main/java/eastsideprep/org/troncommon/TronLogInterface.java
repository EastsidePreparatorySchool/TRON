/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eastsideprep.org.troncommon;

import org.eastsideprep.enginePackage.Bike;



/**
 *
 * @author gmein
 */
public interface TronLogInterface {
    void UpdatePosition (int id, Tuple t);
    void KillBike(int id);
    void Setup(int size);
    
    //TODO: implement
    void runResults(Bike[] testBikes);//these are the results of a certain number of complete games with these bikes
}
