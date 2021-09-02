package Main;

import Agent.Algorithm;
import Game.Grid;
import Graphics.Painter;
import Listeners.MousepadListener;
import java.awt.*;

public class Main {
    
    public static final String world = "./world2.txt";
    
    // how long the agent pauses in each cell before
    // moving to the next one
    public static final int sleep = 150; // ms
    
    public static void main(String[] args) {
        WorldReader reader = new WorldReader();
        reader.readWorld(world);
        Grid g = new Grid(reader);
        
        new Painter(new Dimension(reader.n,reader.m), new MousepadListener(), g);
        
        Algorithm a = new Algorithm(g.getTileGrid(), g.getTrueTileGrid(), reader.player);
        new Thread(a).start();
    }
}
