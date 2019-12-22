package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.*;
import com.mozarellabytes.kroy.GameState;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.*;
import com.mozarellabytes.kroy.Entities.FireTruckType;

import java.util.ArrayList;

// when you click on another truck while a truck is following the path then try to move the path of the stationary truck
public class GameScreen implements Screen {

    private final Kroy game;
    public GameState gameState;

    public OrthographicCamera camera;
    public CameraShake camShake;

    private TiledMap map;
    public Object selectedEntity;
    private MapLayers mapLayers;
    private int[] structureLayersIndices, backgroundLayerIndex;
    private OrthogonalTiledMapRenderer renderer;

    private Batch batch;
    private ShapeRenderer shapeMapRenderer;
    public ArrayList<Fortress> fortresses;
    public FireTruck selectedTruck;
    public FireStation station;

    private State state;
    private Texture pauseImage;
    private GUI gui;

    public GameScreen(Kroy game) {
        this.game = game;

        gameState = new GameState();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camShake = new CameraShake();

        map = new TmxMapLoader().load("maps/YorkMap.tmx");
        //Orders renderer to start rendering the background, then the player layer, then structures
        mapLayers = map.getLayers();

        backgroundLayerIndex = new int[]{mapLayers.getIndex("background")};
        structureLayersIndices = new int[]{mapLayers.getIndex("structures"),
                mapLayers.getIndex("structures2"),
                mapLayers.getIndex("transparentStructures")};

        shapeMapRenderer = new ShapeRenderer();
        shapeMapRenderer.setProjectionMatrix(camera.combined);

        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.TILE_WxH);

        batch = renderer.getBatch();

        fortresses = new ArrayList<Fortress>();
        fortresses.add(new Fortress(12, 20, FortressType.Default));
        fortresses.add(new Fortress(30, 17, FortressType.Walmgate));
        fortresses.add(new Fortress(16, 3, FortressType.Clifford));

        station = new FireStation(this, 4, 2);
        station.spawn(FireTruckType.Ocean);
        station.spawn(FireTruckType.Speed);

        for (FireTruck truck : station.getTrucks()) {
            truck.setOrigin(Constants.TILE_WxH / 2, Constants.TILE_WxH / 2);
        }

        state = State.PLAY;
        pauseImage = new Texture(Gdx.files.internal("images/YorkMapEdit.png"), true);
        pauseImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        gui = new GUI(game, this,275, 275);
        Gdx.input.setInputProcessor(new GameInputHandler(this, gui));

        if (SoundFX.music_enabled) {
            SoundFX.sfx_soundtrack.setVolume(.5f);
            SoundFX.sfx_soundtrack.play();
        }
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

        switch (state) {
            case PLAY:

                gameState.hasGameEnded(game);

                camera.update();
                camShake.update(delta, camera, new Vector2(camera.viewportWidth / 2f, camera.viewportHeight / 2f));
                renderer.setView(camera);

                renderer.render(backgroundLayerIndex);
                renderEntities();
                renderer.render(structureLayersIndices);
                renderFortressRange();

                station.restoreTrucks();

                // uses standard for loop because it may delete truck when a truck is destroyed
                for (int i = 0; i < station.getTrucks().size(); i++) {
                    FireTruck truck = station.getTruck(i);
                    truck.move();
                    station.checkForCollisions(); // checks Trucks do not end on the same tile
                    truck.attack();

                    for (Fortress fortress : this.fortresses) {
                        fortress.attack(truck);
                    }
                    checkIfTruckDestroyed(truck);
                    truck.updateSpray(delta);
                    gui.renderTruckBars(truck, shapeMapRenderer);
                }

                checkIfFortressDestroyed();

                // for each fire truck put this in GUI?
                for (FireTruck truck : station.getTrucks()) {
                    for (int i = 0; i < truck.getSpray().size(); i++) {
                        Particle particle = truck.getSpray().get(i);
                        if (particle.isHit()) {
                            truck.damage(particle);
                            truck.removeParticle(particle);
                        } else {
                            shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
                            shapeMapRenderer.rect(particle.getPosition().x, particle.getPosition().y, particle.getSize(), particle.getSize(), particle.getColour(), particle.getColour(), particle.getColour(), particle.getColour());
                            shapeMapRenderer.end();
                        }
                    }
                }
                shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);

                for (Fortress fortress : fortresses) {

                    shapeMapRenderer.rect(fortress.getPosition().x - 0.26f, fortress.getPosition().y + 1.4f, 0.6f, 1.2f);
                    shapeMapRenderer.rect(fortress.getPosition().x - 0.13f, fortress.getPosition().y + 1.5f, 0.36f, 1f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
                    shapeMapRenderer.rect(fortress.getPosition().x - 0.13f, fortress.getPosition().y + 1.5f, 0.36f, fortress.getHP() / fortress.getType().getMaxHP() * 1f, Color.RED, Color.RED, Color.RED, Color.RED);

                    for (int i = 0; i < fortress.getBombs().size(); i++) {
                        Bomb bomb = fortress.getBombs().get(i);
                        bomb.newUpdatePosition();
                        shapeMapRenderer.setColor(Color.RED);
                        shapeMapRenderer.circle(bomb.getPosition().x, bomb.getPosition().y, 0.2f, 40);
                        shapeMapRenderer.setColor(Color.WHITE);
                        if (bomb.checkHit()) {
                            bomb.damageTruck();
                            camShake.shakeIt(.2f);
                            fortress.removeBomb(bomb);
                        } else if ((int) bomb.getPosition().x == (int) bomb.getTargetPos().x && (int) bomb.getPosition().y == (int) bomb.getTargetPos().y) {
                            fortress.removeBomb(bomb);
                        }
                    }
                }

                shapeMapRenderer.end();
                shapeMapRenderer.setColor(Color.WHITE);

                gui.render(selectedEntity);
                break;

            case PAUSE:
                renderer.setView(camera);
                batch.begin();
                batch.draw(pauseImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
                batch.end();
                gui.renderPauseScreenText();
        }

        gui.renderButtons();

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
        shapeMapRenderer.dispose();
        batch.dispose();
        SoundFX.sfx_soundtrack.stop();
    }

    // this function checks whether the coordinates given are on a road
    public boolean isRoad(int x, int y) {
        return ((TiledMapTileLayer) mapLayers.get("collisions")).getCell(x, y).getTile().getProperties().get("road").equals(true);
    }

    public boolean checkClick(Vector2 position) {
        Vector2 squareClicked = new Vector2((float)Math.floor(position.x), (float)Math.floor(position.y));
        for (int i = this.station.getTrucks().size() - 1; i >= 0; i--) {
            FireTruck selectedTruck = this.station.getTruck(i);
            Vector2 truckTile = new Vector2((float) Math.round((selectedTruck.getX())), (float) Math.round(selectedTruck.getY()));
            if (squareClicked.equals(truckTile)) {
                this.selectedTruck = this.station.getTruck(i);
                this.selectedEntity = this.station.getTruck(i);
                return true;
            }
        }
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

    private void renderEntities() {
        batch.begin();
        for (FireTruck truck : station.getTrucks()) {
            batch.draw(truck, truck.getX(), truck.getY(), 1, 1); // draw r
            drawTrailPath(truck);
        }
        for (Fortress fortress : this.fortresses) {
            batch.draw(fortress.getType().getTexture(), fortress.getArea().x, fortress.getArea().y, fortress.getArea().width, fortress.getArea().height);
        }
        batch.draw(station.getTexture(), station.getPosition().x - 1, station.getPosition().y, 5, 3);
        batch.end();
    }

    public void toControlScreen() { ScreenHandler.ToControls(game, this, "game"); }

    public void toHomeScreen() {
        ScreenHandler.ToMenu(game);
        SoundFX.sfx_soundtrack.dispose();
    }

    public void changeState() {
        if (this.state.equals(State.PLAY)){
            this.state = State.PAUSE;
        } else {
            this.state = State.PLAY;
        }
    }

    public State getState(){
        return this.state;
    }

    private void drawTrailPath(FireTruck truck) {
        if (!truck.trailPath.isEmpty()) {
            for (Vector2 tile : truck.trailPath) {
                if (tile.equals(truck.trailPath.last())) {
                    batch.draw(truck.getType().getTrailImageEnd(), tile.x, tile.y, 1, 1); // overlay the border square
                }
                batch.draw(truck.getType().getTrailImage(), tile.x, tile.y, 1, 1); // draws transparent trail path
            }
        }
    }

    public void checkIfFortressDestroyed() {
        for (int i = 0; i < fortresses.size(); i++) {
            if (fortresses.get(i).getHP() <= 0) {
                gameState.addFortress();
                fortresses.remove(fortresses.get(i));
                if (SoundFX.music_enabled) {
                    SoundFX.sfx_fortress_destroyed.play();
                }
            }
        }
    }

    public void checkIfTruckDestroyed(FireTruck truck) {
        if (truck.getHP() <= 0) {
            station.destroyTruck(truck);
            if (truck.equals(this.selectedTruck)) {
                this.selectedTruck = null;
            }
        }
    }

    // Are we going to keep this?
    public void renderFortressRange() {
        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Fortress fortress : fortresses) {
            shapeMapRenderer.circle(fortress.getPosition().x, fortress.getPosition().y, fortress.getType().getRange());
        }
        shapeMapRenderer.end();

    }
}




