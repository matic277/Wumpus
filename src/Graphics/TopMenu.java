package Graphics;

import Game.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO: remove class
public class TopMenu {
    
//    Dimension buttonSize;
//    Color buttonOutlineClr = Color.BLACK;
//    Color buttonFillClr = Color.GREEN;
//    Painter painter;
//
//    JButton wumpusBtn;
//    JButton goldBtn;
//
//    public TopMenu(Painter painter) {
//        this.painter = painter;
//        buttonSize = new Dimension(80, 40);
//        initButtons();
//    }
//
//    private void initButtons() {
//        wumpusBtn = new JButton();
//        wumpusBtn.setSize(buttonSize);
//        wumpusBtn.setLocation(20, 20);
//        wumpusBtn.addActionListener(this::wumpusButtonPress);
//        wumpusBtn.setText("Wum");
//
//        goldBtn = new JButton();
//        goldBtn.setSize(buttonSize);
//        goldBtn.setLocation(20 + buttonSize.width + 20, 20);
//        goldBtn.addActionListener(this::goldButtonPress);
//        goldBtn.setText("Gld");
//    }
//
//    public void addButtons() {
//        painter.add(wumpusBtn);
//        painter.add(goldBtn);
//    }
//
//    public void wumpusButtonPress(ActionEvent e) {
//        System.out.println("Wumpus press");
//        painter.changeMousePointerShape((m, g) -> g.drawString("W", m.x, m.y));
//        painter.changeMousePointerHeldType(Tile.WUMPUS);
//    }
//
//    public void goldButtonPress(ActionEvent e) {
//        System.out.println("Gold press");
//        painter.changeMousePointerShape((m, g) -> g.drawString("G", m.x, m.y));
//        painter.changeMousePointerHeldType(Tile.GOLD);
//    }
}
