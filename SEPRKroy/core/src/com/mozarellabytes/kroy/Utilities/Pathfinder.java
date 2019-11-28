package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;
import java.util.HashMap;

public class Pathfinder {

    public Texture pathImage =  new Texture(Gdx.files.internal("images/pathfinderimage.png"));
    private ArrayList<Vector2> navGrid;

    public void initialize(TiledMap map, String layerName) {
        this.navGrid = createNavGrid(map, layerName);
    }

    /**
     * Explores in all directions until it reaches the end location, while keeping
     * track of where it went by stashing each location in a HashMap.
     * Each new location (value) visited came from its key (except for the startPosition)
     * So every location is both value and key in the HashMap.
     *
     * @param startPos  Starting coordinates
     * @param endPos    Ending coordinates
     * @return          Returns a Queue
     */
    public Queue<Vector2> pathfindMe(Vector2 startPos, Vector2 endPos) {
        Queue<Vector2> path = new Queue<Vector2>();
        Queue<Vector2> edge = new Queue<Vector2>();
        HashMap<Vector2, Vector2> cameFrom = new HashMap<Vector2, Vector2>();

        if (!navGrid.contains(startPos) || !navGrid.contains(endPos)) {
            System.out.println("One of the entered coordinates is not a navigable cell.");
            return null;
        }

        edge.addLast(startPos);
        Vector2 currentPos;
        cameFrom.put(startPos, null);

        while(!edge.isEmpty()) {
            currentPos = edge.get(0);
            edge.removeFirst();

            if(currentPos == endPos) {
                break;
            }

            for(Vector2 neighbor : getNeighbours(currentPos, navGrid)) {
                if(!cameFrom.containsValue(neighbor) && !cameFrom.containsKey(neighbor)) {
                    edge.addLast(neighbor);
                    cameFrom.put(neighbor, currentPos);
                    System.out.print(cameFrom);
                }
            }
        }

        /*
         * Uses the HashMap created to follow the path from the destination to
         * the start position. The value looks at its key, and this key locates
         * its key-value pair where the value is itself.
         */

        currentPos = endPos;

        while(currentPos != startPos) {
            path.addFirst(currentPos);
            currentPos = cameFrom.get(currentPos);
        }
        path.addFirst(startPos);

        return path;
    }

    /**
     * Finds and returns a List of valid neighbours of the current cell
     *
     * @param currentCell   Current coordinates being looked at
     * @param navGrid       Set of all navigable locations
     * @return              Returns a List of all neighbours that belong to the NavGrid
     */
    private ArrayList<Vector2> getNeighbours(Vector2 currentCell, ArrayList<Vector2> navGrid) {
        ArrayList<Vector2> neighbours = new ArrayList<Vector2>();

        Vector2 up = new Vector2(currentCell.x, currentCell.y + 1);
        Vector2 down = new Vector2(currentCell.x, currentCell.y - 1);
        Vector2 left = new Vector2(currentCell.x - 1, currentCell.y);
        Vector2 right = new Vector2(currentCell.x + 1, currentCell.y);

        System.out.println("Looking for neighbours of : " + currentCell);

        if(navGrid.contains(down)) {
            System.out.println("Current cell down is a neighbour :" + down);
            neighbours.add(down);
        }
        if(navGrid.contains(up)) {
            System.out.println("Current cell up is a neighbour :" + up);
            neighbours.add(up);
        }
        if(navGrid.contains(left)) {
            System.out.println("Current cell right is a neighbour :" + left);
            neighbours.add(left);
        }
        if(navGrid.contains(right)) {
            System.out.println("Current cell left is a neighbour :" + right);
            neighbours.add(right);
        }

        System.out.println("Neighbours returning : " + neighbours);
        return neighbours;
    }

    /**
     *
     * @param map       Used to retrieve height and width in cell number
     * @param layerName Used to retrieve layer and its properties
     * @return          Returns List of all navigable locations on layer
     */

    private ArrayList<Vector2> createNavGrid(TiledMap map, String layerName) {
        ArrayList<Vector2> mapGrid = new ArrayList<Vector2>();
        MapProperties prop = map.getProperties();
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);

        int width = prop.get("width", Integer.class);
        int height = prop.get("height", Integer.class);

        for (int i = 0; i <= width-1; i++) {
            for (int j = 0; j <= height-1; j++) {
                System.out.println(" i : " + i + " j : " + j);
                if (layer.getCell(i,j).getTile().getProperties().get("road").equals(true)) {
                    System.out.println("Tile x:" + i + "| y: " + j + " added to navigable grid.");
                    mapGrid.add(new Vector2(i, j));
                }
            }
        }

        System.out.println("NavGrid created.");
        return mapGrid;
    }
}
