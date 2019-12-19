package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.*;
import com.mozarellabytes.kroy.GameState;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.*;
import com.mozarellabytes.kroy.Entities.FireTruckType;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;

// when you click on another truck while a truck is following the path then try to move the path of the stationary truck
public class GameScreen implements Screen {

    private final Kroy game;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public OrthographicCamera camera;
    private ShapeRenderer shapeMapRenderer;
    private ShapeRenderer shapeGUIRenderer;
    private MapLayers mapLayers;
    private int[] structureLayersIndices, backgroundLayerIndex;
    public CameraShake camShake;

    private Batch batch;

    public ArrayList<Fortress> fortresses;
    public FireTruck selectedTruck;
    public FireStation station;
    public Object selectedEntity;

    private Texture homeButtonIdle;
    private Texture homeButtonClicked;
    private Rectangle homeButton;
    private Texture currentHomeTexture;

    private Rectangle soundButton;
    private Texture soundOnIdleTexture;
    private Texture soundOffIdleTexture;
    private Texture soundOnClickedTexture;
    private Texture soundOffClickedTexture;
    private Texture currentSoundTexture;


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

        // test
        gui = new GUI(game, shapeGUIRenderer, 275, 275);

        Gdx.input.setInputProcessor(new GameInputHandler(this));

        gameState = new GameState();

        camShake = new CameraShake();

        if (SoundFX.music_enabled) {
            SoundFX.sfx_soundtrack.setVolume(.5f);
            SoundFX.sfx_soundtrack.play();
        }

        station = new FireStation(this,4,2);

        fortresses = new ArrayList<Fortress>();
        fortresses.add(new Fortress(this, 12, 20, FortressType.Default));
        fortresses.add(new Fortress(this, 30, 17, FortressType.Walmgate));
        fortresses.add(new Fortress(this, 16, 3, FortressType.Clifford));

        //Orders renderer to start rendering the background, then the player layer, then structures
        mapLayers = map.getLayers();
        backgroundLayerIndex = new int[] {  mapLayers.getIndex("background")};

        structureLayersIndices = new int[] { mapLayers.getIndex("structures"),
                mapLayers.getIndex("structures2"),
                mapLayers.getIndex("transparentStructures")};

        station.spawn(FireTruckType.Ocean);
        station.spawn(FireTruckType.Speed);

        for (FireTruck truck : station.getTrucks()) {
            truck.setOrigin(Constants.TILE_WxH/2, Constants.TILE_WxH/2);
        }

        batch = renderer.getBatch();

        homeButtonIdle = new Texture(Gdx.files.internal("ui/home_idle.png"), true);
        homeButtonClicked = new Texture(Gdx.files.internal("ui/home_clicked.png"), true);

        soundOnIdleTexture = new Texture(Gdx.files.internal("ui/sound_on_idle.png"), true);
        soundOnIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffIdleTexture = new Texture(Gdx.files.internal("ui/sound_off_idle.png"), true);
        soundOffIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOnClickedTexture = new Texture(Gdx.files.internal("ui/sound_on_clicked.png"), true);
        soundOnClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffClickedTexture = new Texture(Gdx.files.internal("ui/sound_off_clicked.png"), true);
        soundOffClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        currentHomeTexture = homeButtonIdle;

        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }

        homeButton = new Rectangle();
        homeButton.width = 30;
        homeButton.height = 30;
        homeButton.x = Gdx.graphics.getWidth() - homeButton.width - 3;
        homeButton.y = Gdx.graphics.getHeight() - homeButton.height - 3;

        soundButton = new Rectangle();
        soundButton.width = 30;
        soundButton.height = 30;
        soundButton.x = Gdx.graphics.getWidth() - homeButton.width - 40;
        soundButton.y = Gdx.graphics.getHeight() - homeButton.height - 3;

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
            for (Fortress fortress : this.fortresses) {
                fortress.checkRange(truck);
            }

            truck.attack();

            station.checkCollision();

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

            truck.updateSpray(delta);

        }

        for (int i=0; i<fortresses.size(); i++) {
            if(fortresses.get(i).getHP() <= 0) {
                gameState.addFortress();
                fortresses.remove(fortresses.get(i));
                if (SoundFX.music_enabled) {
                    SoundFX.sfx_fortress_destroyed.play();
                }
            }
        }

        // draw the station
        batch.draw(station.getTexture(), station.getPosition().x-1, station.getPosition().y, 5, 3);

        // draw the fortress
        for (Fortress fortress : this.fortresses) {
            batch.draw(fortress.getTexture(), fortress.getArea().x, fortress.getArea().y, fortress.getArea().width, fortress.getArea().height);
        }

        // finish rendering of entities
        batch.end();

        // render structures
        renderer.render(structureLayersIndices);

        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Fortress fortress: fortresses) {
            shapeMapRenderer.circle(fortress.getPosition().x, fortress.getPosition().y, fortress.getRange());
        }
        shapeMapRenderer.end();

        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // for each fire truck
        for (FireTruck truck : station.getTrucks()) {
            // 1: white background, 2: hp background, 3: hp, 4: reserve background, 5: reserve
            shapeMapRenderer.rect(truck.getPosition().x + 0.2f, truck.getPosition().y + 1.3f, 0.6f,0.8f);
            shapeMapRenderer.rect(truck.getPosition().x + 0.266f, truck.getPosition().y + 1.4f, 0.2f,0.6f, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
            shapeMapRenderer.rect(truck.getPosition().x + 0.266f , truck.getPosition().y + 1.4f, 0.2f,(float) truck.getReserve() / (float) truck.type.getMaxReserve() * 0.6f, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
            shapeMapRenderer.rect(truck.getPosition().x + 0.533f , truck.getPosition().y + 1.4f, 0.2f,0.6f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
            shapeMapRenderer.rect(truck.getPosition().x + 0.533f, truck.getPosition().y + 1.4f , 0.2f, (float) truck.getHP() / (float) truck.type.getMaxHP() * 0.6f, Color.RED, Color.RED, Color.RED, Color.RED);
            for (int i=0; i<truck.getSpray().size(); i++) {
                Particle particle = truck.getSpray().get(i);
                if (particle.isHit()) {
                    truck.damage(particle);
                    truck.removeParticle(particle);
                } else {
                    shapeMapRenderer.rect(particle.getPosition().x, particle.getPosition().y , particle.getSize(), particle.getSize(), particle.getColour(), particle.getColour(), particle.getColour(), particle.getColour());
                }
            }
        }

        for (Fortress fortress: fortresses) {

            shapeMapRenderer.rect(fortress.getPosition().x - 0.26f, fortress.getPosition().y + 1.4f, 0.6f, 1.2f);
            shapeMapRenderer.rect(fortress.getPosition().x - 0.13f, fortress.getPosition().y + 1.5f, 0.36f, 1f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
            shapeMapRenderer.rect(fortress.getPosition().x - 0.13f, fortress.getPosition().y + 1.5f, 0.36f, (float) fortress.getHP() / (float) fortress.getMaxHP() * 1f, Color.RED, Color.RED, Color.RED, Color.RED);

            for (int i = 0; i < fortress.getBombs().size(); i++) {
                Bomb bomb = fortress.getBombs().get(i);
                bomb.newUpdatePosition(delta);
                shapeMapRenderer.setColor(Color.RED);
                shapeMapRenderer.circle(bomb.getPosition().x, bomb.getPosition().y, 0.2f,40);

                if (bomb.checkHit()) {
                    bomb.boom();
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
        gui.renderHomeButton(currentHomeTexture, homeButton);
        gui.renderSoundButton(currentSoundTexture, soundButton);

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

    public void toControlScreen() {
        ScreenHandler.ToControls(game, this, "game");
    }

    public void clickedHomeButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        currentHomeTexture = homeButtonClicked;
    }

    public Rectangle getHomeButton(){
        return this.homeButton;
    }

    public void toHomeScreen() {
        ScreenHandler.ToMenu(game);
        SoundFX.sfx_soundtrack.dispose();
    }

    public void idleHomeButton() {
        currentHomeTexture = homeButtonIdle;
    }

    public Rectangle getSoundButton(){
        return this.soundButton;
    }

    public void clickedSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffClickedTexture;
        } else {
            currentSoundTexture = soundOnClickedTexture;
        }
    }

    public void changeSound() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOnIdleTexture;
            SoundFX.StopMusic();
        } else {
            currentSoundTexture = soundOffIdleTexture;
            SoundFX.PlayMusic();
        }
    }
}






