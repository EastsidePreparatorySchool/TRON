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
 * @author tespelien
 */
//probably useless?
public interface AbstractGameInterface {

    void init(TronLogInterface tl);

    Tuple[] run(int n, Bike[] test, int size);//runs n complete games, returns the winners by ID and times won

}
