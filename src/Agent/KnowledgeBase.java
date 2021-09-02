package Agent;

import Game.Tile;
import Game.TileRect;
import Main.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class KnowledgeBase {
    
    TileRect[][] grid;
    //TileRect[][] truthGrid;
    
    private Set<Pair<Integer, Integer>> visited;
    private Queue<Pair<Integer, Integer>> toExplore;
    private Set<Pair<Integer, Integer>> toExploreSet; // to prevent toExplore containing same values, LAZY
    
    public KnowledgeBase(TileRect[][] grid) {
        this.grid = grid;
        toExplore = new PriorityQueue<>(100, Utils.visitPlannerComparator);
        toExploreSet = new HashSet<>(100);
        visited = new HashSet<>(100);
    }
    
    public void addSimpleKnowledge(Pair<Integer, Integer> location, Tile tile) {
        Utils.gridAt(location).add(tile);
    }
    
    public void addKnowledge(Pair<Integer, Integer> position, Set<Tile> tiles) {
        tiles.forEach(t -> Utils.gridAt(position).add(t));
    }
    
    // Process known knowledge
    // Fixes logical inconsistencies
    // and possibly gives new destinations to explore
    public void processKnowledgeBase() {
        for (int i=0; i<grid.length; i++)
            for (int j=0; j<grid[i].length; j++)
            {
                var current = new Pair<>(i, j);
                if (Utils.gridAt(current).contains(Tile.WUMPUS)) {
                    boolean someVisitedDoesNotSmell = Utils.getVisitedNeighbors(current)
                            .stream().anyMatch(n -> !Utils.gridAt(n).contains(Tile.SMELL));
                    if (someVisitedDoesNotSmell) {
                        Utils.gridAt(current).remove(Tile.WUMPUS);
                        Utils.gridAt(current).add(Tile.SAFE);
                        if (!Utils.gridAt(current).contains(Tile.PIT)) addTileToExplore(current);
                    }
                }
                if (Utils.gridAt(current).contains(Tile.PIT)) {
                    boolean someVisitedIsNotWindy = Utils.getVisitedNeighbors(current)
                            .stream().anyMatch(n -> !Utils.gridAt(n).contains(Tile.WIND));
                    if (someVisitedIsNotWindy) {
                        Utils.gridAt(current).remove(Tile.PIT);
                        Utils.gridAt(current).add(Tile.SAFE);
                        if (!Utils.gridAt(current).contains(Tile.WUMPUS)) addTileToExplore(current);
                    }
                }
            }
    }
    
    public List<Pair<Integer, Integer>> getPositionsOfWumpuses() {
        return Arrays.stream(grid)
                .flatMap(Arrays::stream)
                .filter(g -> g.getTileTypes().contains(Tile.WUMPUS))
                .map(TileRect::getIndex)
                .collect(Collectors.toList());
    }
    
    public Queue<Pair<Integer, Integer>> getTilesToExplore() {
        return this.toExplore;
    }
    
    public void addTileToExplore(Pair<Integer, Integer> position) {
        if (visited.contains(position) || toExploreSet.contains(position)) return;
        toExploreSet.add(position);
        toExplore.add(position);
    }
    
    public void printCollection(String name, Collection c) {
        System.out.print(name +" "+ c.size()+": [ ");
        c.forEach(x -> System.out.print(x.toString() + ", "));
        System.out.println("]");
    }
    
    public void addVisitedTile(Pair<Integer, Integer> position) {
        visited.add(position);
    }
    
    public Set<Pair<Integer, Integer>> getVisitedTiles() {
        return visited;
    }
    
    public void removeToExplore(Pair<Integer, Integer> pos) {
        toExplore.remove(pos);
    }
}
