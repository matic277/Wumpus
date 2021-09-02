package Graphics;

import Agent.Algorithm;
import Game.Grid;
import Listeners.MousepadListener;
import interfaces.IObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Set;


public class Painter extends JPanel implements IObserver {
    
    private static final long serialVersionUID = 1L;
    
    Color bgColor = Color.white;
    
//    MousePointerShape pointerShape = new MousePointerShape();
    Grid grid;
    
    JFrame frame;
    Dimension bounds;
    Dimension tileSize;
    Point gridStartPosition;
    
    int fps = 144;
    
    public Painter(Dimension gridSize, MousepadListener ml, Grid grid) {
        this.tileSize = new Dimension(70, 70);
        this.bounds = new Dimension(gridSize.width * 2 * tileSize.width + 4 * 20, gridSize.height * tileSize.height + 2 * 50);
        if (bounds.width < 750) bounds.width = 750;
        this.grid = grid;
        this.gridStartPosition = new Point(20, 80);
        this.setFocusable(true);
        this.setLayout(null);
        this.setPreferredSize(bounds);
        this.addMouseListener(ml);
        this.addMouseMotionListener(ml);
        
        ml.addObserver(this);
        
        int truthGridOffset = gridStartPosition.x + tileSize.width*gridSize.width + gridStartPosition.x;
        grid.setTilePositionsAndSize(gridStartPosition, tileSize, truthGridOffset);
        
        frame = new JFrame("Window");
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    }
    
    @Override
    protected void paintComponent(Graphics gr){
        Graphics2D g = (Graphics2D) gr;
        g.setColor(bgColor);
        g.fillRect(0, 0, bounds.width, bounds.height);
        
        g.setColor(Color.BLACK);
        g.drawString("Left / Right click to see calculated path and to move.", 22, 25);
        g.drawString("Indicates visited square.", 360, 55);
        g.drawString("Indicates path.", 360, 25);
        g.drawString("Indicates safe / to_explore square.", 540, 25);
    
        g.setColor(Color.RED);
        g.drawOval(330, 10, 20, 20);
    
        g.setColor(Color.PINK);
        g.fillOval(330, 40, 20, 20);
    
        g.setColor(new Color(162, 239, 48));
        g.fillRect(510, 10, 20, 20);
        
        grid.draw(g);
        
//        pointerShape.draw(mouse, g);
        
        sleep(fps);
        super.repaint();
    }
    
    private void sleep(int t) {
        try { Thread.sleep(1000 / (long)t); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }
    
    @Override
    public void notifyMouseMoved(Point location) { }
    
    @Override
    public void notifyMousePressed(MouseEvent event) { }
    
    @Override
    public void notifyMouseReleased(MouseEvent event) { }
    
    @Override
    public void notifyMouseClicked(Point location) {
        synchronized (Algorithm.LOCK) {
            Algorithm.makeNextMove = true;
            Algorithm.LOCK.notify();
        }
    }
    
    @Override
    public void notifyKeysPressed(boolean[] keyCodes) {}
    
    @Override
    public void notifyCharacterKeyPressed(Set<Character> keys) { }
    
    @Override
    public void notifyRightPress(Point location) {}
    
    @Override
    public void notifyRightRelease(Point location) { }
    
    @Override
    public void notifyLeftPress(Point location) { }
    
    @Override
    public void notifyLeftRelease(Point location) { }
    
//    public void changeMousePointerHeldType(Tile tile) {
//        pointerShape.setHoldingTile(tile);
//    }

//    public void changeMousePointerShape(IShape shape) {
//        pointerShape.setCurrentShape(shape);
//    }
}
