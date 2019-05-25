/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eastsideprep.org.troncommon;



/**
 *
 * @author gmein
 */
public interface TronLogInterface {
    void UpdatePosition (int id, Tuple t);
    void KillBike(int id);
    void Setup(int size);
}
