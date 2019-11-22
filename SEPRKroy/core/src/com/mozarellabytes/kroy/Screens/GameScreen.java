package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.Constants;
import com.mozarellabytes.kroy.Utilities.GameInputHandler;

import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class GameScreen implements Screen {

    private Kroy game;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public OrthographicCamera camera;
    private ShapeRenderer shape;
    private GameInputHandler ih;
    private MapLayers mapLayers;
    private int[] decorationLayersIndices, backgroundLayerIndex;

    private TiledMapTileLayer pathLayer;
    private TiledMapTileLayer.Cell emptyCell;

    public FireTruck activeTruck;
    public ArrayList<FireTruck> trucks;

    public GameScreen(Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

        map = new TmxMapLoader().load("maps/YorkMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.TILE_WxH);
        shape = new ShapeRenderer();
        shape.setProjectionMatrix(camera.combined);

        ih = new GameInputHandler(this);
        Gdx.input.setInputProcessor(ih);

        this.trucks = new ArrayList<FireTruck>();

        this.trucks.add(new FireTruck(this, 10, 3, 0.5, "red"));
        this.trucks.add(new FireTruck(this, 10, 3, 0.2, "blue"));


        for (int i=0; i<2; i++) {
            this.trucks.get(i).setOrigin(Constants.TILE_WxH/2, Constants.TILE_WxH/2);
        }

        //Orders renderer to start rendering the background, then the player layer, then structures
        mapLayers = map.getLayers();
        backgroundLayerIndex = new int[] {  mapLayers.getIndex("background")};

        decorationLayersIndices = new int[] {   mapLayers.getIndex("structures"),
                                                mapLayers.getIndex("structures2"),
                                                mapLayers.getIndex("transparentStructures")};

    }

    public boolean isRoad(int x, int y) {
        if (((TiledMapTileLayer) mapLayers.get("collisions")).getCell(x,y).getTile().getProperties().get("road").equals(true)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        renderer.setView(camera);

        renderer.render(backgroundLayerIndex);

        Batch sb = renderer.getBatch();
        sb.begin();
        for (FireTruck truck : this.trucks) {
            truck.mouseMove();

            sb.draw(truck, truck.getCellX(), truck.getCellY(), 1, 1);

            if (truck.trailPath != null) {
                for (Vector3 tile : truck.trailPath) {
                    sb.draw(truck.getTrailImage(), tile.x, tile.y, 1, 1);
                }

            }

        }

        sb.end();

        renderer.render(decorationLayersIndices);


        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (FireTruck truck : this.trucks) {
//            shape.rect(truck.getPosition().x + 0.2f  , truck.getPosition().y + 1.3f, 0.5f,0.8f);
//            shape.rect(truck.getPosition().x , truck.getPosition().y + 1.7f, 0.2f,0.3f,Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
      //      shape.rect(truck.getPosition().x , truck.getPosition().y , 0.5f,truck.getHP(),Color.RED, Color.RED, Color.RED, Color.RED);
      //      shape.rect(truck.getPosition().x , truck.getPosition().y , 0.5f,0.1f,Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
      //      shape.rect(truck.getPosition().x , truck.getPosition().y , 0.5f,truck.getReserve(),Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
//            Gdx.app.log("truck 0 x", String.valueOf(truck.getPosition().x));
        }

        shape.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
    }

    public boolean checkClick(Vector3 position) {
        for (int i=this.trucks.size()-1; i>=0; i--) {
            if (position.equals(this.trucks.get(i).getPosition())) {
                this.activeTruck = this.trucks.get(i);
                return true;
            }
        }

        return false;
    }
}
