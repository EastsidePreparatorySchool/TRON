package eastsideprep.org.troncommon;

import eastsideprep.org.troncommon.Tuple;
import org.eastsideprep.enginePackage.Bike;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
// * @author tespelien
 */
//probably useless?
//yes, i think so,, but im too lazy to get rid of this -f
//aLSO THIS IS A PAIN -f
public interface AbstractGameInterface {

    void init(TronLogInterface tl);

    void run() throws InterruptedException; //runs a game
    Tuple[] run(int n);//runs n complete games, returns the winners by ID and times won

}
