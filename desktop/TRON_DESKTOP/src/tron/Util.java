/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

import java.lang.reflect.Field;
import javafx.scene.paint.Color;

/**
 *
 * @author gmein
 */
public class Util {
   static public String nameFromColor(Color c) {
            for (Field f : Color.class.getFields()) {
                try {
                    if (f.getType() == Color.class && f.get(null).equals(c)) {
                        return f.getName();
                    }
                } catch (java.lang.IllegalAccessException e) {
                    // it should never get to here
                }
            }
            return "<unknown color>";
    }   
}
