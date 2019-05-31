/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.tron;

import static spark.Spark.*;
import java.sql.*;
import java.util.ArrayList;
import org.eastsideprep.enginePackage.AbstractGameEngine;

/**
 *
 * @author fzhang
 */
public class main {

    //there might be multiple games going at once
    static AbstractGameEngine[] RunningAbstractGameEngines;

    public static void main(String[] args) {
        staticFiles.location("/public/");

        get("/hello", (req, res) -> "hello world");
        //get("/getGrid", "application/json", (req, res) -> getGrid(req), new JSONRT());//MAKE SURE TO PARSE THE int[][]!!!!
        get("/updateBikes", "application/json", (req, res) -> updateBikes(), new JSONRT());
    }

    //big bulky update stuff
//    public static int[][] getGrid(spark.Request req) {
//        //sends grid with all ocupied spaces marked by the following scheme
//        //  0 for empty square
//        //  1 for bike
//        //  2 for wall
//        try {
//            //GET grid
//            int gameId = Integer.parseInt(req.queryParams("id"));
//            for (AbstractGameEngine age : RunningAbstractGameEngines) {
//                if (age.AbstractGameEngineId == gameId){
//                    return age.getGrid();
//                }                
//            }
//            return null;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
    //smol update stuff
    //send bike position, added trail position of bike
    public static Object updateBikes() {
        try {
            //GET updated move

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
