package Game;

import Main.Pair;
import interfaces.IDrawable;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import Graphics.TileDrawer;

public class TileRect extends Rectangle implements IDrawable {
    
    Set<Tile> types;
    Pair<Integer, Integer> index;
    
    public TileRect (Set<Tile> types_, Pair<Integer, Integer> index) {
        this.types = ConcurrentHashMap.newKeySet(4);
        types.addAll(types_);
        this.index = index;
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.draw(this);
        TileDrawer.drawer.drawTile(g, this);
        g.setColor(Color.BLACK);
        g.drawString("[" +index.getA()+", "+index.getB()+"]", this.x+10, this.y+18);
    }
    
    public Set<Tile> getTileTypes() { return types; }
    
    // changing tile type should also change tiledrawer, maybe change this, separate?
    public void addTileType(Tile type) {
        this.types.add(type);
    }
    
    public void removeTileType(Tile type) {
        this.types.remove(type);
    }
    
    public Pair<Integer, Integer> getIndex() { return this.index; }
}
