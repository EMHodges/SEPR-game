package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireStation;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.GameState;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.CameraShake;
import com.mozarellabytes.kroy.Utilities.Constants;
import com.mozarellabytes.kroy.Utilities.GameInputHandler;


// when you click on another truck while a truck is following the path then try to move the path of the stationary truck
public class GameScreen implements Screen {

    private final Kroy game;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public OrthographicCamera camera;
    private ShapeRenderer truckStatsRenderer;
    private ShapeRenderer bigTruckStatsRenderer;
    private MapLayers mapLayers;
    private int[] structureLayersIndices, backgroundLayerIndex;
    public CameraShake camShake;

    public FireTruck selectedTruck;
    public FireStation station;
    public Fortress fortress;

    public GameState gameState;

    public GameScreen(Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

        map = new TmxMapLoader().load("maps/YorkMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.TILE_WxH);

        truckStatsRenderer = new ShapeRenderer();
        truckStatsRenderer.setProjectionMatrix(camera.combined);

        bigTruckStatsRenderer = new ShapeRenderer();
        bigTruckStatsRenderer.setProjectionMatrix(camera.combined);

        Gdx.input.setInputProcessor(new GameInputHandler(this));

        gameState = new GameState();

        camShake = new CameraShake();

        station = new FireStation(this,4,2);
        fortress = new Fortress(this, 12, 19, 5);

        for (FireTruck truck : station.getTrucks()) {
            truck.setOrigin(Constants.TILE_WxH/2, Constants.TILE_WxH/2);
        }

        //Orders renderer to start rendering the background, then the player layer, then structures
        mapLayers = map.getLayers();
        backgroundLayerIndex = new int[] {  mapLayers.getIndex("background")};

        structureLayersIndices = new int[] {    mapLayers.getIndex("structures"),
                                                mapLayers.getIndex("structures2"),
                                                mapLayers.getIndex("transparentStructures")};

        station.spawn("red");
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

        // Make sure the batch abides by our tiled map
        game.batch.setProjectionMatrix(camera.combined);

        // render what our camera sees
        renderer.setView(camera);

        camShake.update(delta, camera, new Vector2(camera.viewportWidth/2f, camera.viewportHeight/2f));

        // renders the background layer of the map
        renderer.render(backgroundLayerIndex);

        // initialise batch
        Batch sb = renderer.getBatch();

        // setups batch for rendering entities
        sb.begin();

        // for each truck (uses standard for loop because it may delete truck when a truck is destroyed)
        for (int i=0; i<station.getTrucks().size();i++) {

            // creates local truck
            FireTruck truck = station.getTruck(i);

            // damages truck if within range of fortress
            fortress.checkRange(truck);

            // move the position of the truck
            truck.mouseMove();

            // draws the truck
            sb.draw(truck, truck.getX(), truck.getY(), 1, 1);

            // if truck has a path
            if (!truck.trailPath.isEmpty()) {

                // for each tile in the path
                for (Vector2 tile : truck.trailPath) {

                    // if last tile
                    if (tile.equals(truck.trailPath.last())) {

                        // overlay the border square
                        sb.draw(truck.getTrailImageEnd(), tile.x, tile.y, 1, 1);
                    }

                    // draws transparent trail path
                    sb.draw(truck.getTrailImage(), tile.x, tile.y, 1, 1);
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

        // draw the station
        sb.draw(station.getTexture(), station.getPosition().x-1, station.getPosition().y, 5, 3);

        // draw the fortress
        sb.draw(fortress.getTexture(), fortress.getPosition().x-2, fortress.getPosition().y-2, 4, 6);

        // finish rendering of entities
        sb.end();

        // render structures
        renderer.render(structureLayersIndices);

        truckStatsRenderer.begin(ShapeRenderer.ShapeType.Line);
        truckStatsRenderer.circle(fortress.getPosition().x, fortress.getPosition().y, fortress.getRange());
        truckStatsRenderer.end();

        // begin rendering of the small stats over each truck
        truckStatsRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // for each fire truck
        for (FireTruck truck : station.getTrucks()) {
            // 1: white background, 2: hp background, 3: hp, 4: reserve background, 5: reserve
            truckStatsRenderer.rect(truck.getPosition().x + 0.2f, truck.getPosition().y + 1.3f, 0.6f,0.8f);
            truckStatsRenderer.rect(truck.getPosition().x + 0.266f, truck.getPosition().y + 1.4f, 0.2f,0.6f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
            truckStatsRenderer.rect(truck.getPosition().x + 0.266f , truck.getPosition().y + 1.4f, 0.2f,(float) truck.getHP() / (float) truck.getMaxHP() * 0.6f, Color.RED, Color.RED, Color.RED, Color.RED);
            truckStatsRenderer.rect(truck.getPosition().x + 0.533f , truck.getPosition().y + 1.4f, 0.2f,0.6f, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
            truckStatsRenderer.rect(truck.getPosition().x + 0.533f, truck.getPosition().y + 1.4f , 0.2f, (float) truck.getReserve() / (float) truck.getMaxReserve() * 0.6f, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
        }

        // stops rendering of small stats over each truck
        truckStatsRenderer.end();

        if (selectedTruck != null) {
            // allows for transparency
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            // starts render of the big stats for selected truck
            bigTruckStatsRenderer.begin(ShapeRenderer.ShapeType.Filled);

            // sets colour or render to transparent?
            bigTruckStatsRenderer.setColor(0, 0, 0, 0.5f);

            // if there is a truck selected
            // 1: white background, 2: hp background, 3: hp, 4: reserve background, 5: reserve
            // these are positioned in the top left corner
            bigTruckStatsRenderer.rect(1, Constants.VIEWPORT_HEIGHT-1-8, 0.6f*10,0.8f*10);
            bigTruckStatsRenderer.rect(1.75f, Constants.VIEWPORT_HEIGHT-1-7, 0.2f*10,0.6f*10, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
            bigTruckStatsRenderer.rect(1.75f, Constants.VIEWPORT_HEIGHT-1-7, 0.2f*10,(float) selectedTruck.getHP() / (float) selectedTruck.getMaxHP() * 0.6f*10, Color.RED, Color.RED, Color.RED, Color.RED);
            bigTruckStatsRenderer.rect(4.25f, Constants.VIEWPORT_HEIGHT-1-7, 0.2f*10,0.6f*10, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
            bigTruckStatsRenderer.rect(4.25f, Constants.VIEWPORT_HEIGHT-1-7, 0.2f*10, (float) selectedTruck.getReserve() / (float) selectedTruck.getMaxReserve() * 0.6f*10, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);

            // ends render of the big stats for selected truck
            bigTruckStatsRenderer.end();

            // disabled transparent mode
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

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
        truckStatsRenderer.dispose();
        bigTruckStatsRenderer.dispose();
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

                // returns true to show that a truck is selected
                return true;
            }
        }

        // truck was not selected
        return false;
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
                    return true;
                }
            }
        }
        return false;
    }
}
