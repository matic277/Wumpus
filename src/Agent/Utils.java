package Agent;

import Game.Tile;
import Game.TileRect;
import Main.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {
    
    private static TileRect[][] grid;
    private static TileRect[][] trueGrid;
    private static int n, m;
    
    private Utils() { }
    
    public static void init(TileRect[][] grid, TileRect[][] truthGrid) {
        Utils.grid = grid;
        Utils.trueGrid = truthGrid;
        n = grid.length; m = grid[0].length;
    }
    
    public static Comparator<Pair<Integer, Integer>> visitPlannerComparator = Comparator.comparingInt(p -> p.getA() * 10 + p.getB());
    
    public static Set<Tile> gridAt(Pair<Integer, Integer> pos) { return grid[pos.getA()][pos.getB()].getTileTypes(); }
    
    public static Set<Tile> trueGridAt(Pair<Integer, Integer> pos) { return trueGrid[pos.getA()][pos.getB()].getTileTypes(); }
    
    public static List<Pair<Integer, Integer>> getVisitedNeighbors(Pair<Integer, Integer> pos) {
        return getNeighbors(pos).stream().filter(n -> gridAt(n).contains(Tile.VISITED)).collect(Collectors.toList());
    }
    
    public static List<Pair<Integer, Integer>> getNeighbors(Pair<Integer, Integer> pos) {
        var neighbors = new ArrayList<Pair<Integer, Integer>>(4);
        neighbors.add(new Pair<>(pos.getA()+1, pos.getB()));
        neighbors.add(new Pair<>(pos.getA()-1, pos.getB()));
        neighbors.add(new Pair<>(pos.getA(), pos.getB()+1));
        neighbors.add(new Pair<>(pos.getA(), pos.getB()-1));
        neighbors.removeIf(p -> p.getA() < 0 || p.getB() < 0 || p.getA() > n-1 || p.getB() > m-1);
        return neighbors;
    }
}
