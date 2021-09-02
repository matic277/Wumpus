package Game;

import java.util.Comparator;

public enum Tile {
    UNKNOWN(0),
    SAFE(1),
    PATH(2),
    
    PIT(3),
    GOLD(4),
    WUMPUS(5),
    
    VISITED(6),
    PLAYER(7),
    
    WIND(8),
    SMELL(9),
    GOAL(10);
    
    int id;
    
    Tile(int id) {
        this.id = id;
    }
    
    public int getId() { return id; }
}
