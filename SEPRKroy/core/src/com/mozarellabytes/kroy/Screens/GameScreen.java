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

import java.util.ArrayList;

public class GameScreen implements Screen {

    private final Kroy game;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeMapRenderer;
    private final MapLayers mapLayers;
    private final int[] structureLayersIndices, backgroundLayerIndex;
    private final Batch mapBatch;
    private final CameraShake camShake;
    private State state;
    private final GUI gui;
    public final GameState gameState;

    private final ArrayList<Fortress> fortresses;
    private final FireStation station;

    public FireTruck selectedTruck;
    public Object selectedEntity;

    public GameScreen(Kroy game) {
        this.game = game;

        state = State.PLAY;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        TiledMap map = new TmxMapLoader().load("maps/YorkMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.TILE_WxH);
        mapRenderer.setView(camera);

        shapeMapRenderer = new ShapeRenderer();
        shapeMapRenderer.setProjectionMatrix(camera.combined);

        gui = new GUI(game, this);

        Gdx.input.setInputProcessor(new GameInputHandler(this, gui));

        gameState = new GameState();

        camShake = new CameraShake();

        //Orders renderer to start rendering the background, then the player layer, then structures
        mapLayers = map.getLayers();
        backgroundLayerIndex = new int[]{mapLayers.getIndex("background")};

        structureLayersIndices = new int[]{mapLayers.getIndex("structures"),
                mapLayers.getIndex("structures2"),
                mapLayers.getIndex("transparentStructures")};

        station = new FireStation(this, 4, 2);

        station.spawn(FireTruckType.Ocean);
        station.spawn(FireTruckType.Speed);

        fortresses = new ArrayList<Fortress>();
        fortresses.add(new Fortress(12, 20, FortressType.Default));
        fortresses.add(new Fortress(30, 17, FortressType.Walmgate));
        fortresses.add(new Fortress(16, 3, FortressType.Clifford));

        // sets the origin point to which all of the polygon's local vertices are relative to.
        for (FireTruck truck : station.getTrucks()) {
            truck.setOrigin(Constants.TILE_WxH / 2, Constants.TILE_WxH / 2);
        }

        mapBatch = mapRenderer.getBatch();

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

        // update camera
        camera.update();

        // renders the background layer of the map
        mapRenderer.render(backgroundLayerIndex);

        // =======================
        //  START BATCH RENDERER

        mapBatch.begin();

        // render trucks
        for (FireTruck truck : station.getTrucks()) {
            truck.drawPath(mapBatch);
            truck.drawSprite(mapBatch);
        }

        // render station
        station.draw(mapBatch);

        // render fortresses
        for (Fortress fortress : this.fortresses) {
            fortress.draw(mapBatch);
        }

        mapBatch.end();

        //   END BATCH RENDERER
        // =======================

        // render structures
        mapRenderer.render(structureLayersIndices);

        // =======================
        //  START SHAPE RENDERERS

        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Fortress fortress : fortresses) {
            fortress.drawRange(shapeMapRenderer);
        }
        shapeMapRenderer.end();

        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (FireTruck truck : station.getTrucks()) {
            truck.drawStats(shapeMapRenderer);
        }

        for (Fortress fortress : fortresses) {
            fortress.drawStats(shapeMapRenderer);
            for (Bomb bomb : fortress.getBombs()) {
                bomb.drawBomb(shapeMapRenderer);
            }
        }

        shapeMapRenderer.end();

        //   END SHAPE RENDERERS
        // =======================

        gui.renderSelectedEntity(selectedEntity);

        switch (state) {
            case PLAY:
                this.update(delta);
                break;
            case PAUSE:
                // render dark background
                Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeMapRenderer.setColor(0, 0, 0, 0.5f);
                shapeMapRenderer.rect(0, 0, this.camera.viewportWidth, this.camera.viewportHeight);
                shapeMapRenderer.end();
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
        mapRenderer.dispose();
        shapeMapRenderer.dispose();
        mapBatch.dispose();
        SoundFX.sfx_soundtrack.stop();
    }

    public void update(float delta) {

        gameState.hasGameEnded(game);
        camShake.update(delta, camera, new Vector2(camera.viewportWidth / 2f, camera.viewportHeight / 2f));

        station.restoreTrucks();
        entitiesAttack();
        checkIfTruckDestroyed();
        checkIfFortressDestroyed();

        for (int i = 0; i < station.getTrucks().size(); i++) {

            FireTruck truck = station.getTruck(i);

            station.checkForCollisions();
            truck.move();

            truck.updateSpray(delta);
            for (int j = 0; j < truck.getSpray().size(); j++) {
                WaterParticle particle = truck.getSpray().get(j);
                if (particle.isHit()) {
                    truck.damage(particle);
                    truck.removeParticle(particle);
                }
            }
        }

        for (int i = 0; i < fortresses.size(); i++) {

            for (int j = 0; j < fortresses.get(i).getBombs().size(); j++) {
                Bomb bomb = fortresses.get(i).getBombs().get(j);
                bomb.newUpdatePosition();
                if (bomb.checkHit()) {
                    bomb.damageTruck();
                    camShake.shakeIt(.2f);
                    fortresses.get(i).removeBomb(bomb);
                } else if ((int) bomb.getPosition().x == (int) bomb.getTargetPos().x && (int) bomb.getPosition().y == (int) bomb.getTargetPos().y) {
                    fortresses.get(i).removeBomb(bomb);
                }
            }
        }

        shapeMapRenderer.end();
        shapeMapRenderer.setColor(Color.WHITE);

        gui.renderSelectedEntity(selectedEntity);
    }

    public enum State {
        PLAY, PAUSE
    }

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

    public State getState() {
        return this.state;
    }

    private void entitiesAttack() {
        for (int i = 0; i < station.getTrucks().size(); i++) {

            FireTruck truck = station.getTruck(i);

            for (Fortress fortress : this.fortresses) {
                if (fortress.truckInRange(truck)) {
                    fortress.attack(truck);
                }
                if (truck.fortressInRange(fortress)) {
                    truck.attack(fortress);
                }
            }
        }
    }

    private void checkIfTruckDestroyed(){
        for (int i = 0; i < station.getTrucks().size(); i++) {
            FireTruck truck = station.getTruck(i);
            if (truck.getHP() <= 0) {
                station.destroyTruck(truck);
                if (truck.equals(this.selectedTruck)) {
                    this.selectedTruck = null;
                }
            }
        }
    }

    private void checkIfFortressDestroyed() {
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
}

