package Graphics;

import Game.Tile;
import Game.TileRect;
import interfaces.ITileDrawer;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TileDrawer implements ITileDrawer {
    
    public static TileDrawer drawer = new TileDrawer();
    static Map<Tile, ITileDrawer> tileMap;
    {
        tileMap = new HashMap<>(10);
        tileMap.put(Tile.SAFE, getSafeDrawer());
        tileMap.put(Tile.UNKNOWN, getUnknownDrawer());
        tileMap.put(Tile.WUMPUS, getWumpusDrawer());
        tileMap.put(Tile.GOLD, getGoldDrawer());
        tileMap.put(Tile.PLAYER, getPlayerDrawer());
        tileMap.put(Tile.PIT, getPitDrawer());
        tileMap.put(Tile.WIND, getWindDrawer());
        tileMap.put(Tile.SMELL, getSmellDrawer());
//        tileMap.put(Tile.DANGER, getDangerDrawer());
        tileMap.put(Tile.PATH, getPathDrawer());
        tileMap.put(Tile.VISITED, getVisitedDrawer());
        tileMap.put(Tile.GOAL, getGoalDrawer());
    }
    
    public static final Font NORMAL_FONT = new Font("Rage", Font.PLAIN, 11);
    public static final Font BIGGER_FONT = new Font("Rage", Font.BOLD, 20);
    public static final Font AGENT_FONT = new Font("Times New Roman", Font.BOLD, 50);
    
    private TileDrawer() { }
    
    @Override
    public void drawTile(Graphics2D g, TileRect tile) {
        List<Tile> tiles = tile.getTileTypes().stream().sorted(Comparator.comparingInt(Tile::getId)).collect(Collectors.toList());
        for (Tile t : tiles) {
            g.setFont(BIGGER_FONT);
            tileMap.get(t).drawTile(g, tile);
        }
        g.setFont(NORMAL_FONT);
    }
    
    public ITileDrawer getSafeDrawer() {
        return (g, t) -> {
            // do nothing
            g.setColor(new Color(162, 239, 48));
            g.fillRect(t.x + 3, t.y + 3, t.width - 6, t.height - 6);
        };
    }
    
    public ITileDrawer getSeenDrawer() {
        return (g, t) -> {
            // do nothing
            g.setColor(Color.BLACK);
            g.fillOval(t.x + 30, t.y + 30, t.width - 60, t.height - 60);
        };
    }
    
    public ITileDrawer getUnknownDrawer() {
        return (g, t) -> {
            g.setColor(Color.GRAY);
            g.fillRect(t.x + 3, t.y + 3, t.width - 6, t.height - 6);
//            g.setColor(Color.BLACK);
//            g.drawString("?", (int)t.getCenterX()-10, (int)t.getCenterY()+5);
        };
    }
    
    public ITileDrawer getWumpusDrawer() {
        return (g, t) -> {
            g.setColor(Color.BLUE);
            g.drawString("W", (int)t.getCenterX()-10, (int)t.getCenterY()+5);
            g.drawRect(t.x+3, t.y+3, t.width-6, t.height-6);
        };
    }
    
    public ITileDrawer getGoalDrawer() {
        return (g, t) -> {
            g.setColor(Color.BLACK);
            g.drawString("GOAL", (int)t.getCenterX()-28, (int)t.getCenterY()+5);
        };
    }
    
    public ITileDrawer getGoldDrawer() {
        return (g, t) -> {
            g.setColor(Color.ORANGE);
            g.drawString("G", (int)t.getCenterX()-10, (int)t.getCenterY()+5);
            g.drawRect(t.x+3, t.y+3, t.width-6, t.height-6);
            g.setColor(Color.BLACK);
        };
    }
    
    public ITileDrawer getPlayerDrawer() {
        return (g, t) -> {
            g.setColor(Color.BLACK);
            g.setFont(AGENT_FONT);
            g.drawString("A",(int)t.getCenterX()-16, (int)t.getCenterY()+18);
            g.setFont(NORMAL_FONT);
            g.drawRect(t.x+5, t.y+5, t.width-10, t.height-10);
            g.drawRect(t.x+7, t.y+7, t.width-14, t.height-14);
        };
    }
    
    public ITileDrawer getPitDrawer() {
        return (g, t) -> {
            g.setColor(new Color(0 ,125, 0));
            g.drawRect(t.x+3, t.y+3, t.width-6, t.height-6);
            g.fillOval(t.x+t.width/6, t.y+t.height/2,
                    (int)(t.width * 4d/6), (int)(1d/2 * t.height)-5);
        };
    }
    
    public ITileDrawer getVisitedDrawer() {
        return (g, t) -> {
            g.setColor(Color.PINK);
            g.fillOval(t.x+t.width/4, t.y+t.height/4, t.width/2, t.height/2);
        };
    }
    
    public ITileDrawer getWindDrawer() {
        return (g, t) -> {
            g.setColor(new Color(0 ,125, 0));
            g.setFont(NORMAL_FONT);
            g.drawString("wind", t.x+6, t.y+t.height-6);
            g.setFont(BIGGER_FONT);
        };
    }
    
    public ITileDrawer getSmellDrawer() {
        return (g, t) -> {
            g.setColor(Color.BLUE);
            g.setFont(NORMAL_FONT);
            g.drawString("smell", t.x+35, t.y+t.height-6);
            g.setFont(BIGGER_FONT);
        };
    }
    
    public ITileDrawer getDangerDrawer() {
        return (g, t) -> {
            g.setColor(Color.RED);
            g.drawString("!!!", (int)t.getCenterX()-10, (int)t.getCenterY()+5);
        };
    }
    
    public ITileDrawer getPathDrawer() {
        return (g, t) -> {
            g.setColor(Color.RED);
            g.drawOval(t.x+12, t.y+12, t.width-24, t.height-24);
        };
    }
}
