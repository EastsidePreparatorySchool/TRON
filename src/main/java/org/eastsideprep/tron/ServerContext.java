/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.tron;

import org.eastsideprep.enginePackage.AbstractGameEngine;

/**
 *
 * @author fzhang
 */
public class ServerContext {
    Observer observer;
    AbstractGameEngine engine;
    String engineName;
    String client;
    String clientSubID;
    
    public ServerContext() {
    }
}
