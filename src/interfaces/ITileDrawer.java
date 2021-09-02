package interfaces;

import Game.TileRect;

import java.awt.*;

public interface ITileDrawer {
    void drawTile(Graphics2D g, TileRect tile);
}
