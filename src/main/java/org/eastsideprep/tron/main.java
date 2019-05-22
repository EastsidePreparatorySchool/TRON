/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.tron;

import static spark.Spark.*;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author fzhang
 */
public class main {


    public static void main(String[] args) {
        staticFiles.location("/public/");

        get("/hello", (req, res) -> "hello world");
        get("/getGrid", "application/json", (req, res) -> getGrid(), new JSONRT());
        get("/getBikeSpawn", "application/json", (req, res) -> getBikeSpawn(), new JSONRT());
        get("/updateBikes", "application/json", (req, res) -> updateBikes(), new JSONRT());
    }

    //big bulky update stuff
    //sends grid with all ocupied spaces marked
    public static Object[][] getGrid() {
        try {
            //GET grid
            return null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    //sends coordinates of all bike spawns
    public static Object[] getBikeSpawn() {
        try {
            //GET bikes
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    //smol update stuff
    //send bike position, added trail position, and isAlive status of bike
    public static Object updateBikes() {
        try {
            //GET updated move
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
