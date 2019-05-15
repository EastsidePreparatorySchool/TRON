/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sqldbfz;

import static spark.Spark.*;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author fzhang
 */
public class main {

    static Connection conn = null;
    public final static int LENGTH = 50;

    public static void main(String[] args) {
        staticFiles.location("/public/");
        connect();

        get("/hello", (req, res) -> "hello world");
        get("/dumpTable", "application/json", (req, res) -> dumpTable(req.queryParams("name")), new JSONRT());
    }

    public static void connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:chinook.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Object[][] dumpTable(String tableName) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + tableName); // select everything in the table

            System.out.println();
            System.out.println(tableName + ":");

            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            for (int i = 1; i <= numberOfColumns; i++) {
                System.out.println(rsmd.getColumnName(i) + ",  " + rsmd.getColumnTypeName(i)); // prints column name and type
            }

            System.out.println();
            System.out.println("Rows:");

            Object[][] res = new Object[LENGTH][numberOfColumns];
            int counter = 0;
            while (rs.next() && counter < LENGTH) { // prints the id and first two columns of all rows
                //String row = "";
                for (int j = 1; j <= numberOfColumns; j++) {
                    /*if(rsmd.getColumnTypeName(j).equals("INTEGER")) {
                        row += "" + rs.getInt(j);
                    } else {
                        row += "" + rs.getString(j);
                    }*/
                    res[counter][j-1] = rs.getString(j);
                    //row += "" + rs.getString(j) + " ";
                }
                //System.out.println(row);
                counter++;
            }

            //System.out.println();
            return res;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
