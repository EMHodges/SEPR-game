package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mozarellabytes.kroy.Entities.FireStation;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.FireTruckType;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.GameState;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.CameraShake;
import com.mozarellabytes.kroy.Utilities.Constants;
import com.mozarellabytes.kroy.Utilities.GameInputHandler;
import com.mozarellabytes.kroy.Entities.FireTruckType;
import com.mozarellabytes.kroy.Utilities.GUI;

import java.util.ArrayList;


// when you click on another truck while a truck is following the path then try to move the path of the stationary truck
public class GameScreen implements Screen {

    private final Kroy game;
    private Stage ui;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public OrthographicCamera camera;
    private OrthographicCamera guiCamera;
    private ShapeRenderer shapeMapRenderer;
    private ShapeRenderer shapeGUIRenderer;
    private MapLayers mapLayers;
    private int[] structureLayersIndices, backgroundLayerIndex;
    public CameraShake camShake;

    private Batch batch;

    public ArrayList<Fortress> fortresses;
    public FireTruck selectedTruck;
    public FireStation station;
    public Fortress fortress;
    public Object selectedEntity;

    private Label hpText;
    private GUI gui;

    public GameState gameState;

    public GameScreen(Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        // if this is commented out, it still works fine? so what does this actually do
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

        map = new TmxMapLoader().load("maps/YorkMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.TILE_WxH);

        shapeMapRenderer = new ShapeRenderer();
        shapeMapRenderer.setProjectionMatrix(camera.combined);

        shapeGUIRenderer = new ShapeRenderer();

        gui = new GUI(game, shapeGUIRenderer, 275, 275);

        Gdx.input.setInputProcessor(new GameInputHandler(this));

        gameState = new GameState();

        camShake = new CameraShake();

        station = new FireStation(this,4,2);
        fortress = new Fortress(this, 12, 19, 5, 100);

        fortresses = new ArrayList<Fortress>();
        fortresses.add(fortress);

        //Orders renderer to start rendering the background, then the player layer, then structures
        mapLayers = map.getLayers();
        backgroundLayerIndex = new int[] {  mapLayers.getIndex("background")};

        structureLayersIndices = new int[] {    mapLayers.getIndex("structures"),
                mapLayers.getIndex("structures2"),
                mapLayers.getIndex("transparentStructures")};

        station.spawn(FireTruckType.Ocean);
        station.spawn(FireTruckType.Speed);

        for (FireTruck truck : station.getTrucks()) {
            truck.setOrigin(Constants.TILE_WxH/2, Constants.TILE_WxH/2);
        }

        batch = renderer.getBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // Initial rendering "options"
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // check to see if the game has been won/lost
        if (gameState.checkWin()) {
            this.game.setScreen(new GameOverScreen(this.game, true));
        } else if (gameState.checkLose()) {
            this.game.setScreen(new GameOverScreen(this.game, false));
        }

        // update camera
        camera.update();

        // check to see if trucks can be repaired/refilled
        station.containsTrucks();

        // render what our camera sees
        renderer.setView(camera);

        camShake.update(delta, camera, new Vector2(camera.viewportWidth/2f, camera.viewportHeight/2f));

        // renders the background layer of the map
        renderer.render(backgroundLayerIndex);

        // setups batch for rendering entities
        batch.begin();

        // for each truck (uses standard for loop because it may delete truck when a truck is destroyed)
        for (int i=0; i<station.getTrucks().size();i++) {

            // creates local truck
            FireTruck truck = station.getTruck(i);

            // damages truck if within range of fortress
            fortress.checkRange(truck);

            truck.attack();

            // move the position of the truck
            truck.mouseMove();

            // draws the truck
            batch.draw(truck, truck.getX(), truck.getY(), 1, 1);

            // if truck has a path
            if (!truck.trailPath.isEmpty()) {

                // for each tile in the path
                for (Vector2 tile : truck.trailPath) {

                    // if last tile
                    if (tile.equals(truck.trailPath.last())) {

                        // overlay the border square
                        batch.draw(truck.getTrailImageEnd(), tile.x, tile.y, 1, 1);
                    }

                    // draws transparent trail path
                    batch.draw(truck.getTrailImage(), tile.x, tile.y, 1, 1);
                }
            }

            // if health of truck reaches 0
            if (truck.getHP() <= 0) {
                station.destroyTruck(truck);
                if (truck.equals(this.selectedTruck)) {
                    this.selectedTruck = null;
                }
            }
        }

        for (Fortress fortress: fortresses) {
            if(fortress.getHP() <= 0) {

                gameState.removeFortress();
            }
        }

        // draw the station
        batch.draw(station.getTexture(), station.getPosition().x-1, station.getPosition().y, 5, 3);

        // draw the fortress
        batch.draw(fortress.getTexture(), fortress.getArea().x, fortress.getArea().y, fortress.getArea().width, fortress.getArea().height);

        // finish rendering of entities
        batch.end();

        // render structures
        renderer.render(structureLayersIndices);

        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeMapRenderer.circle(fortress.getPosition().x, fortress.getPosition().y, fortress.getRange());
        shapeMapRenderer.end();

        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // for each fire truck
        for (FireTruck truck : station.getTrucks()) {
            // 1: white background, 2: hp background, 3: hp, 4: reserve background, 5: reserve
            shapeMapRenderer.rect(truck.getPosition().x + 0.2f, truck.getPosition().y + 1.3f, 0.6f,0.8f);
            shapeMapRenderer.rect(truck.getPosition().x + 0.266f, truck.getPosition().y + 1.4f, 0.2f,0.6f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
            shapeMapRenderer.rect(truck.getPosition().x + 0.266f , truck.getPosition().y + 1.4f, 0.2f,(float) truck.getHP() / (float) truck.type.getMaxHP() * 0.6f, Color.RED, Color.RED, Color.RED, Color.RED);
            shapeMapRenderer.rect(truck.getPosition().x + 0.533f , truck.getPosition().y + 1.4f, 0.2f,0.6f, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
            shapeMapRenderer.rect(truck.getPosition().x + 0.533f, truck.getPosition().y + 1.4f , 0.2f, (float) truck.getReserve() / (float) truck.type.getMaxReserve() * 0.6f, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
        }

        for (Fortress fortress: fortresses) {
            shapeMapRenderer.rect(fortress.getPosition().x - 0.26f, fortress.getPosition().y + 1.4f, 0.6f, 1.2f);
            shapeMapRenderer.rect(fortress.getPosition().x - 0.13f, fortress.getPosition().y + 1.5f, 0.36f, 1f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
            shapeMapRenderer.rect(fortress.getPosition().x - 0.13f, fortress.getPosition().y + 1.5f, 0.36f, (float) fortress.getHP() / (float) fortress.getMaxHP() * 1f, Color.RED, Color.RED, Color.RED, Color.RED);
        }

        shapeMapRenderer.end();

        gui.render(selectedEntity);
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
        shapeGUIRenderer.dispose();
        shapeMapRenderer.dispose();
    }

    // this function checks whether the coordinates given are on a road
    public boolean isRoad(int x, int y) {
        return ((TiledMapTileLayer) mapLayers.get("collisions")).getCell(x, y).getTile().getProperties().get("road").equals(true);
    }

    // this function is used to check whether a player clicks on
    // a truck, then makes that truck the active truck so that
    // operations such as adding to a trail can occur
    public boolean checkClick(Vector2 position) {
        // for each truck, but in reverse order
        // so that you can click on the top truck the player can see
        for (int i=this.station.getTrucks().size()-1; i>=0; i--) {

            // if there is a truck where the player clicked
            if (position.equals(this.station.getTruck(i).getPosition())) {

                // sets the truck to the selected truck
                this.selectedTruck = this.station.getTruck(i);
                this.selectedEntity = this.station.getTruck(i);

                // returns true to show that a truck is selected
                return true;
            }
        }

        // truck was not selected
        return false;
    }

    public ArrayList<Fortress> getFortresses() {
        return this.fortresses;
    }

    // this function is used to see whether the player clicks on
    // the last tile of a path, so that they can extend it
    public boolean checkTrailClick(Vector2 position) {
        // for each truck, but in reverse order
        // so that you can click on the top trail the player can see
        for (int i=this.station.getTrucks().size()-1; i>=0; i--) {

            // if the truck has a path
            if (!this.station.getTruck(i).path.isEmpty()) {

                // if player clicks on the last tile of a path
                if (position.equals(this.station.getTruck(i).path.last())) {

                    // makes that truck the selected truck again
                    this.selectedTruck = this.station.getTruck(i);
                    this.selectedEntity = this.station.getTruck(i);
                    return true;
                }
            }
        }
        return false;
    }

}






