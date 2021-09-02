package Agent;

import Game.Tile;
import Game.TileRect;
import Main.Main;
import Main.Pair;

import java.util.*;

public class Algorithm implements Runnable {
    
    TileRect[][] grid;
    TileRect[][] trueGrid;
    KnowledgeBase db;
    
    Pair<Integer, Integer> goal;
    
    Pair<Integer, Integer> playerPos;
    
    public static boolean makeNextMove = false;
    public static final Object LOCK = new Object();
    
    int bulletsNum = 1;
    
    public Algorithm(TileRect[][] grid, TileRect[][] trueGrid, Pair<Integer, Integer> playerPos) {
        this.db = new KnowledgeBase(grid);
        this.playerPos = playerPos;
        Utils.getNeighbors(playerPos).forEach(n -> db.addTileToExplore(n));
        db.addVisitedTile(playerPos);
        
        this.grid = grid;
        this.trueGrid = trueGrid;
        
        
        // path tests
//        for (int i=0; i<8; i++) {
//            grid[7][i].removeTileType(Tile.UNKNOWN);
//            grid[7][i].addTileType(Tile.SAFE);
//        }
//
//        var start = new Pair<>(7, 0);
//        var goal = new Pair<>(6, 1);
//        System.out.println("Start, end: " + start.toString2() + ", " + goal.toString2());
//        bfs(start, goal);
//        List<Pair<Integer, Integer>> path = preparePath(goal);
//        for (Pair<Integer, Integer> node : path) {
//            System.out.println(node.toString2());
//        }
//         walkToGoal(path);
//
//        start = new Pair<>(7, 0);
//        goal = new Pair<>(7, 2);
//        System.out.println("Start, end: " + start.toString2() + ", " + goal.toString2());
//        bfs(start, goal);
//        path = preparePath(goal);
//        for (Pair<Integer, Integer> node : path) {
//            System.out.println(node.toString2());
//        }
//       // walkToGoal(path);
//
//        start = new Pair<>(7, 0);
//        goal = new Pair<>(7, 6);
//        System.out.println("Start, end: " + start.toString2() + ", " + goal.toString2());
//        bfs(start, goal);
//        path = preparePath(goal);
//        for (Pair<Integer, Integer> node : path) {
//            System.out.println(node.toString2());
//        }
//        // walkToGoal(path);
    }
    
    @Override
    public void run() {
        var toExplore = db.getTilesToExplore();
        db.addVisitedTile(new Pair<>(playerPos.getA(), playerPos.getB()));
        
        while (!toExplore.isEmpty()) {
            Pair<Integer, Integer> goal = toExplore.poll();
            if (goal.equals(playerPos)) continue;
            
            System.out.println("To visit: "+goal);
            bfs(playerPos, goal);
            List<Pair<Integer, Integer>> path = preparePath(goal);
            path.forEach(x -> { grid[x.getA()][x.getB()].addTileType(Tile.PATH); });
            
            // wait for mouse click
            synchronized (LOCK) {
                while(!makeNextMove) {
                    try { LOCK.wait(); }
                    catch (Exception e) { e.printStackTrace(); }
                }
                makeNextMove = false;
            }
            
            walkToGoal(path);
            
            // Crucial step! Removing parents and children from bfs
            playerPos = new Pair<>(goal.getA(), goal.getB());
            
            // we are now at goal
            db.addKnowledge(goal, getTilesInPosition(goal));
            
            // wait for mouse click
            synchronized (LOCK) {
                while(!makeNextMove) {
                    try { LOCK.wait(); }
                    catch (Exception e) { e.printStackTrace(); }
                }
                makeNextMove = false;
            }
            
            // Shoot wumpus?
            if (toExplore.isEmpty() && bulletsNum > 0) {
                bulletsNum--;
                shootWumpus();
            }
            
        }
        
        System.out.println("Noting to explore anymore!");
        if (this.goal == null) {
            System.out.println("Goal not found!");
            return;
        }
        if (playerPos.equals(this.goal)) {
            System.out.println("Goal found. GAMEOVER!");
            return;
        }
        bfs(playerPos, this.goal);
        List<Pair<Integer, Integer>> pathToGoal = preparePath(this.goal);
        walkToGoal(pathToGoal);
        System.out.println("Goal found. GAMEOVER!");
    }
    
    private void shootWumpus() {
        var wumpuses = db.getPositionsOfWumpuses();
        System.out.println("Wumpuses: " + Arrays.deepToString(wumpuses.toArray()));
        
        if (wumpuses.isEmpty()) return;
        
        // Shoot the first wumpus?
        var wumpusCoords = wumpuses.get(0);
    
        System.out.println("Shooting wupus at " + wumpusCoords);
        System.out.println("! NOT IMPLEMENTED !");
        System.out.println("->returning");
    }
    
    public Set<Tile> getTilesInPosition(Pair<Integer, Integer> position) {
        return trueGrid[position.getA()][position.getB()].getTileTypes();
    }
    
    public void walkToGoal(List<Pair<Integer, Integer>> pathList) {
        var path = (LinkedList<Pair<Integer, Integer>>) pathList;
        var current = path.removeFirst(); // we are already here
        while (true) {
            sleep(Main.sleep);
            Utils.gridAt(current).remove(Tile.PLAYER);
            Utils.gridAt(current).remove(Tile.PATH);
            current = path.removeFirst();
            playerPos = current;
            processStep(current);
            if (path.isEmpty()) break;
        }
    }
    
    public void processStep(Pair<Integer, Integer> current) {
        playerPos = current;
        db.addSimpleKnowledge(current, Tile.SAFE);
        db.addKnowledge(current, Utils.trueGridAt(current));
        db.addVisitedTile(current);
        db.removeToExplore(current);
        
        grid[current.getA()][current.getB()].removeTileType(Tile.UNKNOWN);
        grid[current.getA()][current.getB()].removeTileType(Tile.PATH);
        grid[current.getA()][current.getB()].addTileType(Tile.VISITED);
        grid[current.getA()][current.getB()].addTileType(Tile.PLAYER);
        
        if (Utils.gridAt(current).contains(Tile.GOAL)) this.goal = current;
        
        var currentTiles = Utils.trueGridAt(current);
        System.out.println("Current "+current+": " + Arrays.deepToString(currentTiles.toArray()));
        
        // This tile does not smell and is not windy
        if (!currentTiles.contains(Tile.SMELL) && !currentTiles.contains(Tile.WIND) ) {
            Utils.getNeighbors(current).forEach(n -> {
                db.addTileToExplore(n);
                db.addSimpleKnowledge(n, Tile.SAFE);
                Utils.gridAt(n).remove(Tile.PIT);
                Utils.gridAt(n).remove(Tile.WUMPUS);
            });
        }
        
        // if tile smells/windy, mark wumpuses/pits
        // if not, unmark neighbors as wumpuses/pits
        if (currentTiles.contains(Tile.SMELL)) {
            Utils.getNeighbors(current).forEach(n -> {
                db.addSimpleKnowledge(n, Tile.WUMPUS);
            });
        }
        if (currentTiles.contains(Tile.WIND)) {
            Utils.getNeighbors(current).forEach(n -> {
                db.addSimpleKnowledge(n, Tile.PIT);
            });
        }
        
        db.processKnowledgeBase();
        
        if (Utils.trueGridAt(playerPos).contains(Tile.WUMPUS) ||
            Utils.trueGridAt(playerPos).contains(Tile.PIT)) {
            System.out.println("GAME OVER");
            System.exit(1);
        }
    }
    
    // Simple breadth-first-search, used for shortest path
    public void bfs(Pair<Integer, Integer> current, Pair<Integer, Integer> goal) {
        var visited = new HashSet<Pair<Integer, Integer>>(300);
        var queue = new LinkedList<Pair<Integer, Integer>>();
        
        visited.add(current);
        queue.add(current);
        
        while (!queue.isEmpty()) {
            var toVisit = queue.poll();
            if (goal.equals(toVisit)) {
                goal.setParent(toVisit);
                toVisit.setChild(goal);
                break;
            }
            var neighbors = Utils.getNeighbors(toVisit);
            neighbors.forEach(p -> {
                // only move through SAFE tiles!
                // unless the neighbor is goal node!
                if (p.equals(goal) ||
                    (!visited.contains(p) &&
                     grid[p.getA()][p.getB()].getTileTypes().contains(Tile.SAFE)))
                {
                    visited.add(p);
                    p.setParent(toVisit);
                    toVisit.setChild(p);
                    queue.add(p);
                }
            });
        }
    }
    
    // Bfs gives path:
    // goal -> node -> node -> ... -> start
    // We must reverse it!
    public List<Pair<Integer, Integer>> preparePath(Pair<Integer, Integer> goal) {
        var path = new LinkedList<Pair<Integer, Integer>>();
        path.add(goal);
        var parent = goal.getParent();
        while(parent != null) {
            path.addFirst(parent);
            parent = parent.getParent();
        }
        path.removeLast();
        return path;
    }
    
    public static void sleep(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }
    
    private void printPath(List<Pair<Integer, Integer>> path) {
        System.out.print("Path "+path.size()+": [ ");
        path.forEach(x -> System.out.print(x.toString() + " "));
        System.out.println("]");
    }
    
    public static List<Pair<Integer, Integer>> pathToString(Pair<Integer, Integer> goal) {
        var path = new ArrayList<Pair<Integer, Integer>>(10);
        path.add(goal);
        var sb = new StringBuilder().append("\n");
        var parent = goal.getParent();
        int i = 1;
        while(parent != null) {
            path.add(parent);
            sb.append(i++).append(": ").append(parent).append("\n");
            parent = parent.getParent();
        }
        System.out.println(sb);
        return path;
    }
    
    public KnowledgeBase getKB() { return db; }
}
