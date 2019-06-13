package TrajanPackage;

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

    void init(/*TronLogInterface tl*/);

    Tuple[] run(int n);//runs n complete games, returns the winners by ID and times won

}
