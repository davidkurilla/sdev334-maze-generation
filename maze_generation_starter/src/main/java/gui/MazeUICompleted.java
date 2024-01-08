package gui;

import generation.AlgorithmType;
import generation.DisjointSets;
import graphs.MyGraph;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * This Class is a completed version of the maze generator.
 *
 * @author David Kurilla
 * @version 1.0
 */
public class MazeUICompleted extends MazeUI
{

    // CONSTANTS
    public static final int RED = 66;
    public static final int GREEN = 33;
    public static final int BLUE = 99;

    // FIELDS
    private boolean[] north = {true, false, false, false};
    private boolean[] south = {false, false, true, false};
    private boolean[] east = {false, true, false, false};
    private boolean[] west = {false, false, false, true};
    private MyGraph<Integer> graph = new MyGraph<>();
    private Color pathColor = Color.rgb(RED, GREEN,BLUE);

    // OVERRIDE-METHOD: runAlgorithm
    @Override
    public void runAlgorithm(AlgorithmType type)
    {
        if (type == AlgorithmType.GENERATE_MAZE) {

            // Set Draw Settings
            drawBackgroundGrid();
            setStrokeWidth(2);
            setStrokeColor(Color.BLACK);

            // Set variable to determine total number of graph vertices needed.
            int totalVertices = this.getCols() * this.getRows();

            // Create a Disjoint Set and fill with vertices.
            DisjointSets mazeSet = new DisjointSets(totalVertices);
            for (int i = 0; i < totalVertices; i++) {
                graph.addVertex(i);
            }

            createMaze(mazeSet);

            drawMaze();

        } else if (type == AlgorithmType.BFS) {
            Queue<Integer> queue = new LinkedList<>();
            Set<Integer> seen = new HashSet<>();
            Map<Integer, Integer> path = new HashMap<>();
            queue.add(0);
            while(!queue.isEmpty()){

                int next = queue.remove();
                seen.add(next);

                for(int vertex : graph.getAdjacentVertices(next)){
                    if(!seen.contains(vertex)){
                        queue.add(vertex);
                    } else {
                        path.put(next, vertex);
                    }
                }
            }
            solveMaze(path, pathColor);
        } else {
            Map<Integer, Integer> path = dfs();
            solveMaze(path, pathColor);
        }
    }

    // PRIVATE-METHOD: dfs
    /**
     * This method performs a depth first search
     * @return
     */
    private Map<Integer, Integer> dfs() {
        Set<Integer> seen = new HashSet<>();
        Map<Integer, Integer> path = new HashMap<>();

        dfs(0, seen, path);
        return path;
    }

    // PRIVATE-METHOD: dfs
    private void dfs(int current, Set<Integer> seen, Map<Integer, Integer> path) {
        if(!seen.contains(current)){
            seen.add(current);
            for(int vertex : graph.getAdjacentVertices(current)){
                dfs(vertex, seen, path);
                path.put(vertex, current);
            }
        } else {
            path.remove(current);
        }
    }

    // PRIVATE-METHOD: solveMaze
    /**
     * This method draws a path to the end of the maze.
     * @param path
     * @param pathColor
     */
    private void solveMaze(Map<Integer, Integer> path, Color pathColor) {
        setFillColor(pathColor);
        int trail = path.get(this.getCols() * this.getRows()-1);
        fillCell(graph.edgeSize());
        do {
            fillCell(trail);
            trail = path.get(trail);
        } while (trail > 0);
        fillCell(trail);
    }

    // METHOD: createMaze
    /**
     * This method creates a maze using a Disjoint Set.
     * @param mazeSet DisjointSets | The DisjointSet instance that will seed the maze
     */
    public void createMaze(DisjointSets mazeSet) {
        int totalVertices = this.getCols() * this.getRows();
        for (int i = 0; i < totalVertices - 1; i++) {
            Random rng = new Random();
            int randPoint = rng.nextInt(totalVertices);
            int randDir = rng.nextInt(4);
            switch (randDir) {
                case 0:
                    // Go north
                    if (checkNorth(randPoint, mazeSet)) {
                        mazeSet.union(randPoint, randPoint-this.getCols());
                        graph.addEdge(randPoint, randPoint-this.getCols());
                    } else {
                        i--;
                    }
                    break;
                case 1:
                    // Go South
                    if (checkSouth(randPoint, mazeSet, totalVertices)) {
                        mazeSet.union(randPoint, randPoint+this.getCols());
                        graph.addEdge(randPoint, randPoint+this.getCols());
                    } else {
                        i--;
                    }
                    break;
                case 2:
                    // Go East
                    if (checkEast(randPoint, mazeSet, totalVertices)) {
                        mazeSet.union(randPoint, randPoint + 1);
                        graph.addEdge(randPoint, randPoint + 1);
                    } else {
                        i--;
                    }
                    break;
                case 3:
                    // Go West
                    if (checkWest(randPoint, mazeSet)) {
                        mazeSet.union(randPoint, randPoint-1);
                        graph.addEdge(randPoint, randPoint-1);
                    } else {
                        i--;
                    }
                    break;
            }
        }
    }

    // PRIVATE-METHOD checkNorth
    /**
     * This method determines if you are able to go north.
     * @param randPoint the point on the graph you are checking
     * @param mazeSet the disjoint sets of mazes
     * @return if you can go north
     */
    private boolean checkNorth(int randPoint, DisjointSets mazeSet) {
        return randPoint >= this.getCols() && !mazeSet.sameSet(randPoint, randPoint - this.getCols());
    }

    // PRIVATE-METHOD checkSouth
    /**
     * This method determines if you are able to go south.
     * @param randPoint the point on the graph you are checking
     * @param mazeSet the disjoint sets of mazes
     * @param totalVertices the total number of vertices
     * @return if you can go south
     */
    private boolean checkSouth(int randPoint, DisjointSets mazeSet, int totalVertices) {
        return randPoint < totalVertices-this.getCols() &&
                !mazeSet.sameSet(randPoint, randPoint+this.getCols());
    }

    // PRIVATE-METHOD checkEast
    /**
     * This method determines if you are able to go east.
     * @param randPoint the point on the graph you are checking
     * @param mazeSet the disjoint sets of mazes
     * @param totalVertices the total number of vertices
     * @return if you can go east
     */
    private boolean checkEast(int randPoint, DisjointSets mazeSet, int totalVertices) {
        return randPoint < totalVertices-1 &&
                (randPoint + 1) % this.getCols() != 0 &&
                !mazeSet.sameSet(randPoint, randPoint + 1);
    }

    // PRIVATE-METHOD checkWest
    /**
     * This method determines if you are able to go west.
     * @param randPoint the point on the graph you are checking
     * @param mazeSet the disjoint sets of mazes
     * @return if you can go west
     */
    private boolean checkWest(int randPoint, DisjointSets mazeSet) {
        return randPoint > 0 && randPoint % this.getCols() != 0 &&
                !mazeSet.sameSet(randPoint, randPoint-1);
    }

    // PRIVATE-METHOD: drawMaze
    /**
     * This method draws the maze.
     */
    private void drawMaze() {
        for (int i = 0; i < graph.vertexSize(); i++) {
            //north
            if(!graph.containsEdge(i, i-this.getCols())){
                this.drawCell(i, north);
            }
            //south
            if(!graph.containsEdge(i, i+this.getCols())){
                this.drawCell(i, south);
            }
            //east
            if(!graph.containsEdge(i, i+1)){
                this.drawCell(i, east);
            }
            //west
            if(!graph.containsEdge(i, i-1)){
                this.drawCell(i, west);
            }
        }
    }

    // OVERRIDE-METHOD: toString
    @Override
    public String toString() {
        return "MazeUICompleted{" +
                "north=" + Arrays.toString(north) +
                ", south=" + Arrays.toString(south) +
                ", east=" + Arrays.toString(east) +
                ", west=" + Arrays.toString(west) +
                ", graph=" + graph +
                ", pathColor=" + pathColor +
                '}';
    }
}
