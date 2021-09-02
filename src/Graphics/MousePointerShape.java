package Graphics;

import Game.Tile;
import interfaces.IShape;
import java.awt.*;

// TODO: remove class, not used
/**
 * Also holds info about what shape it holds,
 * should refactor this eventaully.
 */
public class MousePointerShape {
    
    IShape currentShape = (m, g) -> { };
    Tile holdingTile;
    
    public void draw(Point mouseLocation, Graphics2D g) {
        g.setColor(Color.BLACK);
        currentShape.drawShape(mouseLocation, g);
    }
    
    public void setCurrentShape(IShape s) { currentShape = s; }
    
    public void setShapeToBlank() {
        currentShape = (m, g) -> { };
    }
    
    public void setHoldingTile(Tile tile) {
        this.holdingTile = tile;
    }
    
    public Tile getHeldTile() {
        return holdingTile;
    }
}
