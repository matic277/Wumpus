package Game;

import Agent.Utils;
import Main.Pair;
import Main.WorldReader;
import interfaces.IDrawable;

import java.awt.*;
import java.util.HashSet;

public class Grid implements IDrawable {
    
    private int n;
    private int m;
    
    private TileRect[][] grid;
    private TileRect[][] truthGrid;
    
    public Grid() { }
    
    public Grid(WorldReader world) {
        n = world.n;
        m = world.m;
        grid = new TileRect[n][m];
        truthGrid = new TileRect[n][m];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                var tiles = new HashSet<Tile>();
                tiles.add(Tile.UNKNOWN);
                grid[i][j] = new TileRect(tiles, new Pair<>(i, j));
    
                var truthTiles = new HashSet<Tile>();
                if (world.world[i][j] != null) truthTiles.add(world.world[i][j]);
                truthGrid[i][j] = new TileRect(truthTiles, new Pair<>(i, j));
            }
        }
        
        Utils.init(grid, truthGrid);
        
        // set smell and wind
        // LAZY
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (truthGrid[i][j].getTileTypes().contains(Tile.PIT)) {
                   Utils.getNeighbors(new Pair<>(i, j)).forEach(n -> {
                       if (!(truthGrid[n.getA()][n.getB()].getTileTypes().contains(Tile.WUMPUS) ||
                             truthGrid[n.getA()][n.getB()].getTileTypes().contains(Tile.PIT))) {
                           truthGrid[n.getA()][n.getB()].addTileType(Tile.WIND);
                       }
                   });
               } else if (truthGrid[i][j].getTileTypes().contains(Tile.WUMPUS)) {
                    Utils.getNeighbors(new Pair<>(i, j)).forEach(n -> {
                        if (!(truthGrid[n.getA()][n.getB()].getTileTypes().contains(Tile.WUMPUS) ||
                                truthGrid[n.getA()][n.getB()].getTileTypes().contains(Tile.PIT))) {
                            truthGrid[n.getA()][n.getB()].addTileType(Tile.SMELL);
                        }
                    });
               }
            }
        }
        
        Utils.gridAt(world.player).remove(Tile.UNKNOWN);
        Utils.gridAt(world.player).add(Tile.VISITED);
        Utils.gridAt(world.player).add(Tile.SAFE);
        Utils.getNeighbors(world.player).forEach(n -> { Utils.gridAt(n).add(Tile.SAFE); });
    }
    
    public void init() {
        n = 8;
        m = 8;
        grid = new TileRect[n][m];
        truthGrid = new TileRect[n][m];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                var tiles = new HashSet<Tile>();
                tiles.add(Tile.UNKNOWN);
                grid[i][j] = new TileRect(tiles, new Pair<>(i, j));
                truthGrid[i][j] = new TileRect(new HashSet<Tile>(), new Pair<>(i, j));
            }
        }
        // player
//        grid[n-1][0].addTileType(Tile.PLAYER);
        grid[n-1][0].addTileType(Tile.SAFE); grid[n-1][0].removeTileType(Tile.UNKNOWN);
        grid[n-2][0].addTileType(Tile.SAFE); grid[n-2][0].removeTileType(Tile.UNKNOWN);
        grid[n-2][1].addTileType(Tile.SAFE); grid[n-2][1].removeTileType(Tile.UNKNOWN);
        grid[n-1][1].addTileType(Tile.SAFE); grid[n-1][1].removeTileType(Tile.UNKNOWN);
        addWumpus(5, 5);
        addWumpus(3, 3);
        addWumpus(5, 4);
        addGold(6, 6);
        addPit(1, 1);
        
        // goal
        grid[0][m-1].addTileType(Tile.GOAL);
        
        Utils.init(grid, truthGrid);
    }
    
    private void addWumpus(int i, int j) {
//        grid[i][j].addTileType(Tile.WUMPUS);
//        grid[i-1][j].addTileType(Tile.SMELL);
//        grid[i+1][j].addTileType(Tile.SMELL);
//        grid[i][j-1].addTileType(Tile.SMELL);
//        grid[i][j+1].addTileType(Tile.SMELL);
        truthGrid[i][j].addTileType(Tile.WUMPUS);
        truthGrid[i-1][j].addTileType(Tile.SMELL);
        truthGrid[i+1][j].addTileType(Tile.SMELL);
        truthGrid[i][j-1].addTileType(Tile.SMELL);
        truthGrid[i][j+1].addTileType(Tile.SMELL);
    }
    
    private void addPit(int i, int j) {
//        grid[i][j].addTileType(Tile.PIT);
//        grid[i-1][j].addTileType(Tile.WIND);
//        grid[i+1][j].addTileType(Tile.WIND);
//        grid[i][j-1].addTileType(Tile.WIND);
//        grid[i][j+1].addTileType(Tile.WIND);
        truthGrid[i][j].addTileType(Tile.PIT);
        truthGrid[i-1][j].addTileType(Tile.WIND);
        truthGrid[i+1][j].addTileType(Tile.WIND);
        truthGrid[i][j-1].addTileType(Tile.WIND);
        truthGrid[i][j+1].addTileType(Tile.WIND);
    }
    
    private void addGold(int i, int j) {
//        grid[i][j].addTileType(Tile.GOLD);
        truthGrid[i][j].addTileType(Tile.GOLD);
    }
    
    @Override
    public void draw(Graphics2D g) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j].draw(g);
                truthGrid[i][j].draw(g);
            }
        }
    }
    
    public void setTilePositionsAndSize(Point gridStartPosition, Dimension tileSize, int truthGridOffset) {
        for (int i=0, y=gridStartPosition.y; i<grid.length;    i++, y+=tileSize.height)
        for (int j=0, x=gridStartPosition.x; j<grid[i].length; j++, x+=tileSize.width) {
            grid[i][j].setBounds(x, y, tileSize.width, tileSize.height);
            truthGrid[i][j].setBounds(x+truthGridOffset, y, tileSize.width, tileSize.height);
        }
    }
    
    public TileRect[][] getTileGrid() { return grid; }
    public TileRect[][] getTrueTileGrid() { return truthGrid; }
    
    public TileRect getClickedTile(Point location) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].contains(location)) return grid[i][j];
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<grid.length; i++) {
            for (int j=0; j<grid[i].length; j++) {
                sb.append(grid[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
