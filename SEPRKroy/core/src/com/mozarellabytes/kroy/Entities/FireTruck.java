package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Screens.GameScreen;

import java.util.ArrayList;
import java.util.Stack;

public class FireTruck extends Sprite {

    private int HP, AP, range, type, reserve;
    private double speed;
    private float x, y;
    private Texture lookLeft;
    private Texture lookRight;
    private Texture lookUp;
    private Texture lookDown;
    public Queue<Vector3> path;
    public Queue<Vector3> trailPath;
    private boolean moving;

    private Vector3 lastCoordinate;

    private Rectangle trail;
    private Texture trailImage;
    public TiledMapTileLayer.Cell pathCell;
    private Sprite redTile;

    private GameScreen gameScreen;

    public FireTruck(GameScreen gameScreen, float x, float y, double speed, String colour) {
        super(new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000_" + colour + ".png")));

        this.gameScreen = gameScreen;
        this.x = x;
        this.y = y;
        this.speed = speed;

        lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left/frame0000_" + colour + ".png"));
        lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000_" + colour + ".png"));
        lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up/frame0000_" + colour + ".png"));
        lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down/frame0000_" + colour + ".png"));
        path = new Queue<Vector3>();
        trailPath = new Queue<Vector3>();
        moving = false;

        if (colour == "red") {
            trailImage = new Texture(Gdx.files.internal("sprites/firetruck/red_trail.png"), true);
        } else {
            trailImage = new Texture(Gdx.files.internal("sprites/firetruck/blue_trail.png"), true);

        }

        trailImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        trail = new Rectangle();
        trail.width = 1;
        trail.height = 1;


        //Custom trail color for each truck
        redTile = new Sprite(new Texture(Gdx.files.internal("sprites/redtile.png")));
        pathCell = new TiledMapTileLayer.Cell();
        pathCell.setTile(new StaticTiledMapTile(redTile));

    }

    public Texture getTrailImage() {
        return this.trailImage;
    }

    public Rectangle getTrail() {
        return this.trail;
    }

    public void arrowMove() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            x -= 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            x += 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            y -= 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            y += 1;
        }
    }

    public void mouseMove() {
        if (this.moving) {
            followPath();
        }
    }

    public Vector3 getPosition() {
        return new Vector3(getCellX(), getCellY(), 0);
    }

    public float getCellX() {
        return this.x;
    }

    public float getCellY() {
        return this.y;
    }

    public Queue<Vector3> getPath() {
        return this.path;
    }

    public void addTileToPath(Vector3 coordinate) {
        if (this.path.size > 0) {
            Vector3 previous = this.path.last();
            int smallValues = (int) (5/speed);
            for (int i=0; i<smallValues; i++) {
                this.path.addLast(new Vector3(((coordinate.x - previous.x)/smallValues)*i + previous.x, ((coordinate.y - previous.y)/smallValues)*i + previous.y, 0));
            }
        }
        this.trailPath.addLast(new Vector3(((int) coordinate.x), ((int) coordinate.y), 0));
        this.path.addLast(new Vector3(((int) coordinate.x), ((int) coordinate.y), 0));
    }


    public void resetTilePath() {
        this.path.clear();
    }

    public boolean isValidMove(Vector3 coordinate) {
        if (gameScreen.isRoad(((int) coordinate.x), ((int) coordinate.y))) {
            if (!this.path.last().equals(coordinate)) {
                if (Math.abs(this.path.last().x - coordinate.x) <= 1 && Math.abs(this.path.last().y - coordinate.y) <= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setMoving(boolean t) {
        this.moving = t;
    }

    public void followPath() {
        if (this.path.size > 0 && !truckCollision() ) {

            Vector3 nextTile = path.first();

                this.x = nextTile.x;
                this.y = nextTile.y;

                gameScreen.clearPathCell((int)nextTile.x, (int)nextTile.y);
                changeSprite(nextTile);

                lastCoordinate = nextTile;
                gameScreen.clearPathCell((int)nextTile.x, (int)nextTile.y);
                path.removeFirst();


            }

        else if (this.path.size <= 0){
            moving = false;
            gameScreen.activeTruck = null;
            this.trailPath.clear();
        }
    }
    private void changeSprite(Vector3 nextTile) {
        if (lastCoordinate != null) {
            if (nextTile.x > lastCoordinate.x) { setTexture(lookRight); }
            else if (nextTile.x < lastCoordinate.x) { setTexture(lookLeft); }
            else if (nextTile.y > lastCoordinate.y) { setTexture(lookUp);  }
            else if (nextTile.y < lastCoordinate.y) { setTexture(lookDown);
            }
        }
    }

    private boolean truckCollision() {
        if (!gameScreen.trucks[0].path.isEmpty() && !gameScreen.trucks[1].path.isEmpty()){
            int truck1X = (int)gameScreen.trucks[0].path.first().x;
            int truck1Y = (int)gameScreen.trucks[0].path.first().y;
            int truck2X = (int)gameScreen.trucks[1].path.first().x;
            int truck2Y = (int)gameScreen.trucks[1].path.first().y;
            // need to add collisions of when one truck is static, also sometimes the sprites jump
            // over each other?
            if (truck1X + 1 == truck2X && truck1Y == truck2Y ||   // only checks if they are moving
            truck1X - 1 == truck2X && truck1Y == truck2Y ||
            truck1Y+1 == truck2Y && truck1X == truck2Y ||
            truck1Y-1 == truck2Y && truck1X == truck2X) {
                resetTrucks();
                return true;
            }
        }
        return false;

    }

    private void resetTrucks(){
        for(FireTruck truck: gameScreen.trucks){
            for(Vector3 vector3: truck.path) {
                gameScreen.clearPathCell((int) vector3.x, (int) vector3.y);
            }
            gameScreen.activeTruck = null;
            truck.path.clear();
            truck.addTileToPath(new Vector3((int)(truck.x), (int)(truck.y),0));
        }
    }

    protected void attack() {

        }

    }


