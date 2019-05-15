/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author gmein
 */
public class TRON extends Application {

    int rows = 75;
    int cols = 150;

    static Random random = new Random();
    HBox hb;
    Grid grid;
    VBox root;
    boolean running = false;
    CycleContainer[] ccs = new CycleContainer[4];
    ArrayList<Class> ac;
    ArrayList<Class> usedCycleClasses;

    public static void main(String[] args) {
        launch(args);
    }

    void makeAllCycleClasses() {
        this.ac = new ArrayList<>();
        this.usedCycleClasses = new ArrayList<>();

//        ac.add(MyCycle.class);
//        ac.add(YourCycle.class);
//        ac.add(CycleGM.class);
        ac.add(CycleSLMG.class);
    }

    LightCycle pickRandomCycle() {
        Constructor<LightCycle> c = null;
        int i = random.nextInt(ac.size());
        Class cs = ac.get(i);
        try {
            c = cs.getConstructor();
        } catch (NoSuchMethodException | SecurityException ex) {
            return null;
        }

        //ac.remove(cs);
        //usedCycleClasses.add(cs);
        LightCycle lc = null;

        try {
            lc = c.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            return null;
        }

        return lc;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        random.setSeed(1234);

        root = new VBox();
        root.setMinSize(cols * 5 + (cols - 1) * 2, rows * 5 + (rows - 1) * 2 + 25);

        Button bStart = new Button("Start");
        bStart.setOnAction((e) -> {
            running = true;
            new Thread(() -> run()).start();
        });
        Button bEnd = new Button("Stop");
        bEnd.setOnAction((e) -> pause());
        Button bReset = new Button("Reset");
        bReset.setOnAction((e) -> reset());
        hb = new HBox();

        hb.getChildren().addAll(bStart, bEnd, bReset);
        root.getChildren().add(hb);

        Scene scene = new Scene(root, cols * 7, rows * 7 + 25);

        primaryStage.setTitle("TRON");
        primaryStage.setScene(scene);

        reset();

        primaryStage.setOnCloseRequest((e) -> {
            System.exit(0);
        });

        primaryStage.show();

    }

    void reset() {
        if (running) {
            running = false;
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
            }
        }
        makeAllCycleClasses();

        ccs[0] = new CycleContainer(pickRandomCycle(), Color.BLUE, random.nextInt(cols), random.nextInt(rows), LightCycle.RIGHT);
        ccs[1] = new CycleContainer(pickRandomCycle(), Color.GREEN, random.nextInt(cols), random.nextInt(rows), LightCycle.LEFT);
        ccs[2] = new CycleContainer(pickRandomCycle(), Color.YELLOW, random.nextInt(cols), random.nextInt(rows), LightCycle.UP);
        ccs[3] = new CycleContainer(pickRandomCycle(), Color.PURPLE, random.nextInt(cols), random.nextInt(rows), LightCycle.DOWN);

        root.getChildren().remove(grid);
        grid = new Grid(cols, rows);
        root.getChildren().add(0, grid);
        root.layout();

        if (hb.getChildren().size() > 3) {
            hb.getChildren().remove(3);
        }

        for (int i = 0; i < ccs.length; i++) {
            ccs[i].mark(grid);
        }
    }

    void run() {
        while (running) {
            int count = 0;

            for (int i = 0; i < ccs.length; i++) {
                if (ccs[i].isAlive()) {
                    ccs[i].move(grid);
                }
                if (ccs[i].isAlive()) {
                    // notify the others
                    for (int j = 0; j < ccs.length && j != i; j++) {
                        ccs[j].cycle.noteOtherCycle(ccs[i].color, ccs[i].col,ccs[i].row,ccs[i].direction);
                    }
                    count++;
                }
            }

            // game over?
            if (count < 2) {
                displayWinner();
                break;
            }

            // now sleep a bit
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
    }

    void displayWinner() {
        int i;

        for (i = 0; i < ccs.length; i++) {
            if (ccs[i].isAlive()) {
                break;
            }
        }

        String winner = "nobody";
        if (i < ccs.length) {
            winner = ccs[i].cycle.getClass().getSimpleName()+" ("+Util.nameFromColor(ccs[i].color)+")";
        }

        Text w = new Text("    " + winner + " wins!");
        Platform.runLater(() -> hb.getChildren().add(w));
    }

    void pause() {
        running = false;
    }
}
