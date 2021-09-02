package Main;

import Game.Tile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class WorldReader {
    
    public Tile[][] world;
    public int n, m;
    
    public List<Pair<Integer, Integer>> pits;
    public List<Pair<Integer, Integer>> wumpuses;
    public List<Pair<Integer, Integer>> gold;
    public Pair<Integer, Integer> player;
    public Pair<Integer, Integer> goal;
    
    public Tile[][] readWorld(String fileName) {
        wumpuses = new LinkedList<>();
        pits = new LinkedList<>();
        gold = new LinkedList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(this::processLine);
        }
        catch (Exception e) {
            System.out.println("Error reading file '" + fileName + "'.");
            e.printStackTrace();
        }
        buildWorld();
        return world;
    }
    
    private void buildWorld() {
//        System.out.println("Pits:");
//        pits    .forEach(t -> System.out.print(t + ", "));
//        System.out.println("\nGold:");
//        gold    .forEach(t -> System.out.print(t + ", "));
//        System.out.println("\nWumpuses:");
//        wumpuses.forEach(t -> System.out.print(t + ", "));
//        System.out.println();
        
        world = new Tile[n][m];
        
        pits    .forEach(t -> world[t.getA()][t.getB()] = Tile.PIT);
        gold    .forEach(t -> world[t.getA()][t.getB()] = Tile.GOLD);
        wumpuses.forEach(t -> world[t.getA()][t.getB()] = Tile.WUMPUS);
        
        world[player.getA()][player.getB()] = Tile.PLAYER;
        world[goal.getA()  ][goal.getB()  ] = Tile.GOAL;
    }
    
    public List<Pair<Integer, Integer>> getNeighbors(Pair<Integer, Integer> pos) {
        var neighbors = new ArrayList<Pair<Integer, Integer>>(4);
        neighbors.add(new Pair<>(pos.getA()+1, pos.getB()));
        neighbors.add(new Pair<>(pos.getA()-1, pos.getB()));
        neighbors.add(new Pair<>(pos.getA(), pos.getB()+1));
        neighbors.add(new Pair<>(pos.getA(), pos.getB()-1));
        neighbors.removeIf(p -> p.getA() < 0 || p.getB() < 0 || p.getA() > n-1 || p.getB() > m-1);
        return neighbors;
    }
    
    /**
    * Axy  = Agent se nahaja na polju (x,y)
    * Bxy  = Na polju (x,y) je vetrovno (breeze)
    * Gxy  = Na polju (x,y) je zlato (gold)
    * GOxy = (x,y) je ciljno polje - izhod iz jame (goal)
    * Mxy  = Jama je široka x in visoka y polj (map size)
    * Pxy  = Na polju (x,y) je brezno (pit)
    * Sxy  = Na polju (x,y) je smrad (stench)
    * Wxy  = Na polju (x,y) je Wumpus
    */
    private void processLine(String line) {
        line = line.trim();
        if (line.isBlank() || line.isEmpty()) return;
        if (line.contains("M")) {
            n = Integer.parseInt(""+ line.charAt(1));
            m = Integer.parseInt(""+ line.charAt(2));
            return;
        }
        String info = (Character.isDigit(line.charAt(1))) ?
                line.substring(0, 1) : line.substring(0, 2);
        String pos = line.substring(line.length() - 2);
        
        switch (info) {
            case "A":  player = getLocation(pos);      break;
            case "G":  gold.add(getLocation(pos));     break;
            case "GO": goal = getLocation(pos);        break;
            case "P":  pits.add(getLocation(pos));     break;
            case "W":  wumpuses.add(getLocation(pos)); break;
            
            // ignore smell and wind
            case "B": break;
            case "S": break;
            
            default: System.out.println("Can't parse '"+info+"'."); break;
        }
    }
    
    private Pair<Integer, Integer> getLocation(String s) {
        return new Pair<>(
                Integer.parseInt(""+ s.charAt(1))-1,
                Integer.parseInt(""+ s.charAt(0))-1);
    }
}
