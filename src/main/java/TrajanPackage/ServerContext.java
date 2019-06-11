/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrajanPackage;

import TrajanPackage.TrajanLog.GameLogObserver;

/**
 *
 * @author fzhang
 */
public class ServerContext {

    GameLogObserver observer;
    AbstractGameEngine engine;
    String engineName;
    String client;
    String clientSubID;

    public ServerContext() {
    }
}
