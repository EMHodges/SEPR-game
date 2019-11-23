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


// when you click on another truck while a truck is following the path then try to move the path of the stationary truck
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

        station.spawn("red");

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
                    if (tile.equals(truck.trailPath.last())) {
                        sb.draw(truck.getTrailImageEnd(), tile.x, tile.y, 1, 1);
                    }
                    sb.draw(truck.getTrailImage(), tile.x, tile.y, 1, 1);

                }

            }

        }

        sb.end();

        renderer.render(decorationLayersIndices);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (FireTruck truck : station.getTrucks()) {
            shape.rect(truck.getPosition().x + 0.2f, truck.getPosition().y + 1.3f, 0.6f,0.8f);
            shape.rect(truck.getPosition().x + 0.266f, truck.getPosition().y + 1.4f, 0.2f,0.6f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
            shape.rect(truck.getPosition().x + 0.266f , truck.getPosition().y + 1.4f, 0.2f,(float) truck.getHP() / (float) truck.getMaxHP() * 0.6f, Color.RED, Color.RED, Color.RED, Color.RED);
            shape.rect(truck.getPosition().x + 0.533f , truck.getPosition().y + 1.4f, 0.2f,0.6f, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
            shape.rect(truck.getPosition().x + 0.533f, truck.getPosition().y + 1.4f , 0.2f, (float) truck.getReserve() / (float) truck.getMaxReserve() * 0.6f, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
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
            if (position.equals(this.station.getTruck(i).getPosition())) {
                this.activeTruck = this.station.getTruck(i);
                return true;
            }
        }

        return false;
    }

    public void checkTrailClick(Vector2 position) {
        for (int i=this.station.getTrucks().size()-1; i>=0; i--) {
            if (!this.station.getTruck(i).path.isEmpty()) {
                if (position.equals(this.station.getTruck(i).path.last())) {
                    this.activeTruck = this.station.getTruck(i);
                }
            }
        }
    }
}
