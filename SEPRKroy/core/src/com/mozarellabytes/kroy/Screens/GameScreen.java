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
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireStation;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.Constants;
import com.mozarellabytes.kroy.Utilities.GameInputHandler;

public class GameScreen implements Screen {

    private Kroy game;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public OrthographicCamera camera;
    private ShapeRenderer shape;
    private GameInputHandler ih;
    private MapLayers mapLayers;
    private int[] decorationLayersIndices, backgroundLayerIndex;

    public FireTruck activeTruck;
    public FireStation station;

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

        station = new FireStation(this,2,2);

        for (FireTruck truck : station.getTrucks()) {
            truck.setOrigin(Constants.TILE_WxH/2, Constants.TILE_WxH/2);
        }

        //Orders renderer to start rendering the background, then the player layer, then structures
        mapLayers = map.getLayers();
        backgroundLayerIndex = new int[] {  mapLayers.getIndex("background")};

        decorationLayersIndices = new int[] {   mapLayers.getIndex("structures"),
                                                mapLayers.getIndex("structures2"),
                                                mapLayers.getIndex("transparentStructures")};

        station.spawn();

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

        station.checkTrucks();

        renderer.setView(camera);

        renderer.render(backgroundLayerIndex);

        Batch sb = renderer.getBatch();
        sb.begin();
        for (FireTruck truck : station.getTrucks()) {
            truck.mouseMove();

            sb.draw(truck, truck.getX(), truck.getY(), 1, 1);

            if (truck.trailPath != null) {
                for (Vector2 tile : truck.trailPath) {
                    sb.draw(truck.getTrailImage(), tile.x, tile.y, 1, 1);
                }

            }

        }

        sb.end();

        renderer.render(decorationLayersIndices);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        int positionX = 40;
        int positionY = 40;
        int whiteBarH = 28;
        int whiteBarW = 18;
        int barH = 22;
        int scale = 1;

        for (FireTruck truck : station.getTrucks()) {
            if (truck.seeStats()) {
                // white background
                shape.rect(truck.getPosition().x + positionX/Constants.TILE_WxH, truck.getPosition().y + (positionY)/Constants.TILE_WxH, whiteBarW/Constants.TILE_WxH*scale,whiteBarH/Constants.TILE_WxH*scale);
                // Max HP
                shape.rect(truck.getPosition().x + (3+positionX)/Constants.TILE_WxH, truck.getPosition().y + (3+positionY)/Constants.TILE_WxH, 4/Constants.TILE_WxH*scale,barH/Constants.TILE_WxH*scale, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
                // HP
                shape.rect(truck.getPosition().x + (3+positionX)/Constants.TILE_WxH, truck.getPosition().y + (3+positionY)/Constants.TILE_WxH, 4/Constants.TILE_WxH*scale,truck.getHP()/Constants.TILE_WxH/truck.getMaxHP()*barH*scale, Color.RED, Color.RED, Color.RED, Color.RED);
                // Max Reserve
                shape.rect(truck.getPosition().x + (10+positionX)/Constants.TILE_WxH, truck.getPosition().y + (3+positionY)/Constants.TILE_WxH, 4/Constants.TILE_WxH*scale,barH/Constants.TILE_WxH*scale, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
                // Reserve
                shape.rect(truck.getPosition().x + (10+positionX)/Constants.TILE_WxH, truck.getPosition().y + (3+positionY)/Constants.TILE_WxH, 4/Constants.TILE_WxH*scale,truck.getReserve()/Constants.TILE_WxH/truck.getMaxReserve()*barH*scale, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
            }
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

    public boolean checkClick(Vector2 position) {
        for (int i=this.station.getTrucks().size()-1; i>=0; i--) {
            if (position.equals(this.station.getTrucks().get(i).getPosition())) {
                this.activeTruck = this.station.getTrucks().get(i);
                return true;
            }
        }
        return false;
    }

    public void checkHover(Vector2 position) {
        for (int i=this.station.getTrucks().size()-1; i>=0; i--) {
            if (position.equals(this.station.getTrucks().get(i).getPosition())) {
                this.station.getTrucks().get(i).setSeeStats(true);
            } else {
                this.station.getTrucks().get(i).setSeeStats(false);
            }
        }
    }
}
