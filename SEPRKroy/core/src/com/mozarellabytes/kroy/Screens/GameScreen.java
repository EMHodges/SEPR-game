package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.Constants;
import com.mozarellabytes.kroy.Utilities.GameInputHandler;
import com.mozarellabytes.kroy.Utilities.MenuInputHandler;

public class GameScreen implements Screen {

    private Kroy game;
    private TiledMap map;
    private TiledMapRenderer renderer;
    private TiledMapTileLayer playerLayer;
    public OrthographicCamera camera;
    private GameInputHandler ih;
    private MapLayers mapLayers;
    private int[] decorationLayersIndices;

    public FireTruck truck;
    private TiledMapTileLayer.Cell cell;
    private SpriteBatch sb;

    public GameScreen(Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

        map = new TmxMapLoader().load("maps/YorkMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.TILE_WxH);

        ih = new GameInputHandler(this);
        Gdx.input.setInputProcessor(ih);

        //sb = new SpriteBatch();

        truck = new FireTruck();
        truck.setOrigin(Constants.TILE_WxH/2, Constants.TILE_WxH/2);
        //truck.setPosition(2*Constants.TILE_WxH, 1*Constants.TILE_WxH);

        //Orders renderer to start rendering the background, then the player layer, then structures
        mapLayers = map.getLayers();
        decorationLayersIndices = new int[]    {mapLayers.getIndex("background"),
                                                mapLayers.getIndex("entities"),
                                                mapLayers.getIndex("structures2"),
                                                mapLayers.getIndex("structures"),
                                                mapLayers.getIndex("transparentStructures")};

        playerLayer = (TiledMapTileLayer) mapLayers.get("entities");
        //Assigns the instantiated truck sprite to a cell on the entities layer.
        //We'll make a loop to cycle through all entities dynamically instantiated later
        //(and call that method from render())
        cell = new TiledMapTileLayer.Cell();
        cell.setTile(new StaticTiledMapTile(truck));
        playerLayer.setCell(9, 3, cell);

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
        renderer.render(decorationLayersIndices);

        playerLayer.setCell(truck.getCellX(), truck.getCellY(), new TiledMapTileLayer.Cell());
        truck.arrowMove();
        truck.mouseMove();
        playerLayer.setCell(truck.getCellX(), truck.getCellY(), cell);

        //truck.move();

//        sb.begin();
//        truck.draw(sb);
//        sb.end();

    }

    private void addEntitiesToLayer() {

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
        //sb.dispose();
    }
}
