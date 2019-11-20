package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.Constants;
import com.mozarellabytes.kroy.Utilities.GameInputHandler;

public class GameScreen implements Screen {

    private Kroy game;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public OrthographicCamera camera;
    private GameInputHandler ih;
    private MapLayers mapLayers;
    private int[] decorationLayersIndices, backgroundLayerIndex;

    private TiledMapTileLayer pathLayer;
    private TiledMapTileLayer.Cell emptyCell;

    public FireTruck activeTruck;
    public FireTruck[] trucks;

    public GameScreen(Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

        map = new TmxMapLoader().load("maps/YorkMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.TILE_WxH);

        ih = new GameInputHandler(this);
        Gdx.input.setInputProcessor(ih);

        this.trucks = new FireTruck[2];

        this.trucks[0] = new FireTruck(this, 9, 3, 0.5, "red");
        this.trucks[1] = new FireTruck(this, 10, 3, 0.2, "blue");

        for (int i=0; i<2; i++) {
            this.trucks[i].setOrigin(Constants.TILE_WxH/2, Constants.TILE_WxH/2);
        }

        //Orders renderer to start rendering the background, then the player layer, then structures
        mapLayers = map.getLayers();
        backgroundLayerIndex = new int[] {  mapLayers.getIndex("background"),
                                            mapLayers.getIndex(("path"))
                                         };

        decorationLayersIndices = new int[] {   mapLayers.getIndex("structures"),
                                                mapLayers.getIndex("structures2"),
                                                mapLayers.getIndex("transparentStructures")};

        pathLayer = (TiledMapTileLayer) mapLayers.get("path");

        emptyCell = new TiledMapTileLayer.Cell();
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

        drawPath();

        renderer.render(backgroundLayerIndex);

        Batch sb = renderer.getBatch();
        sb.begin();
        for (FireTruck truck : this.trucks) {
            truck.arrowMove();
            truck.mouseMove();
            sb.draw(truck, truck.getCellX(), truck.getCellY(), 1, 1);
        }
        sb.end();

        renderer.render(decorationLayersIndices);
    }

    private void drawPath() {
        if (trucks.length != 0) {
            for (FireTruck truck : this.trucks) {
                if (truck.getPath().size != 0) {
                    for (Vector3 vector : truck.getPath()) {
                        pathLayer.setCell((int) vector.x, (int) vector.y, truck.pathCell);
                    }
                }
            }
        }
    }

    public void clearPathCell(int x, int y) {
        pathLayer.setCell(x, y, emptyCell);
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
        for (FireTruck truck : this.trucks) {
            if (position.equals(truck.getPosition())) {
                this.activeTruck = truck;
                Gdx.app.log("Active truck", this.activeTruck.getPosition().toString());
                return true;
            }
        }
        return false;
    }
}
