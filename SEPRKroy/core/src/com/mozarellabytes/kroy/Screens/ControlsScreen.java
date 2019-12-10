package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.ControlScreenInputHandler;

import java.awt.*;

public class ControlsScreen implements Screen {

    private final Kroy game;

    private Texture backgroundImage;
    private Texture blueTileImage;
    private Texture blueEndTileImage;
    private Texture redTileImage;
    private Texture redEndTileImage;
    private Texture blueTruck;
    private Texture redTruck;
    private Texture fortress;

    public OrthographicCamera camera;

    private Rectangle exitButton;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont font2;
    private BitmapFont font3;
    private BitmapFont font4;
    private ShapeRenderer renderer;
    private int HP;
    private int count;

    public ControlsScreen(Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        Gdx.input.setInputProcessor(new ControlScreenInputHandler(this));

        backgroundImage = new Texture(Gdx.files.internal("images/YorkMapEdit.png"), true);
        backgroundImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        blueTileImage = new Texture(Gdx.files.internal("sprites/firetruck/Blue_trail.png"), true);
        blueEndTileImage = new Texture(Gdx.files.internal("sprites/firetruck/Blue_trail_end.png"), true);

        redTileImage = new Texture(Gdx.files.internal("sprites/firetruck/Red_trail.png"), true);
        redEndTileImage = new Texture(Gdx.files.internal("sprites/firetruck/Red_trail_end.png"), true);

        blueTruck = new Texture(Gdx.files.internal("sprites/firetruck/right/frame0000_Blue.png"), true);

        redTruck = new Texture(Gdx.files.internal("sprites/firetruck/left/frame0000_Red.png"), true);

        fortress = new Texture(Gdx.files.internal("sprites/fortress.png"), true);

        batch = new SpriteBatch();
        renderer = new ShapeRenderer();

        HP = 200;
        count = 0;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Magero.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 50;
        font = generator.generateFont(parameter);
        parameter.size = 25;
        font2 = generator.generateFont(parameter);

        parameter.size = 33;
        font3 = generator.generateFont(parameter);

        parameter.size = 33;
        parameter.color = Color.FIREBRICK;
        font4 = generator.generateFont(parameter);

        exitButton = new Rectangle();
        exitButton.x = 1185;
        exitButton.y = (int) (camera.viewportHeight - 90);
        exitButton.width = 30;
        exitButton.height = 30;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.3f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    //    Color purple = new Color(65f/255f,30f/255f,80f/255f,1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(backgroundImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(50,50, camera.viewportWidth - 100 , camera.viewportHeight - 100, Color.BLACK, Color.BLACK,Color.BLACK, Color.BLACK);
        renderer.end();

        batch.begin();
        font.draw(batch, "Control screen", 430, camera.viewportHeight - 115);
        font2.draw(batch, "Flood the fortresses before the fortresses destroy your fire trucks to win", 110,camera.viewportHeight - 180);
        font3.draw(batch, "Moving the Fire Trucks", 135, camera.viewportHeight - 250 );
        font2.draw(batch, "Click on a truck and drag it", 165,camera.viewportHeight - 310);
        font2.draw(batch, "This gives the truck a path:", 165,camera.viewportHeight - 345);
        font2.draw(batch, "Unclick and the truck will", 165,camera.viewportHeight - 495);
        font2.draw(batch, "follow the path", 165,camera.viewportHeight - 525);
        font2.draw(batch, "Or click and drag from the", 165,camera.viewportHeight - 590);
        font2.draw(batch, "end of the trucks path", 165,camera.viewportHeight - 625);


        batch.end();

        batch.begin();
        batch.draw(blueTileImage, 180, camera.viewportHeight - 450);
        batch.draw(blueTileImage, 230, camera.viewportHeight - 450);
        batch.draw(blueTileImage, 280, camera.viewportHeight - 450);
        batch.draw(blueTileImage, 330, camera.viewportHeight - 450);
        batch.draw(blueTileImage, 380, camera.viewportHeight - 450);
        batch.draw(blueTileImage, 430, camera.viewportHeight - 450);
        batch.draw(blueTileImage, 480, camera.viewportHeight - 450);
        batch.draw(blueEndTileImage, 480, camera.viewportHeight - 450);
        batch.draw(blueTruck, 172, camera.viewportHeight - 457);
        batch.end();

        batch.begin();
        batch.draw(blueEndTileImage, 380, camera.viewportHeight - 710);
        batch.draw(redEndTileImage, 270, camera.viewportHeight - 710);
        batch.draw(blueTileImage, 380, camera.viewportHeight - 710);
        batch.draw(redTileImage, 270, camera.viewportHeight - 710);
        batch.end();

        batch.begin();
        font3.draw(batch, "Attacking the fortresses", 680, camera.viewportHeight - 250);
        font2.draw(batch, "When a firetruck is within range ", 685,camera.viewportHeight - 310);
        font2.draw(batch, "of a fortress press A to attack", 685,camera.viewportHeight - 345);
        batch.end();

        batch.begin();
        batch.draw(fortress,790, camera.viewportHeight - 670);
        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(870,  camera.viewportHeight - 490, 35, 60);
        renderer.rect(875, camera.viewportHeight - 485, 24, 50, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        renderer.rect(875, camera.viewportHeight - 485, 24,  HP / 4, Color.RED, Color.RED, Color.RED, Color.RED);
        renderer.end();

        batch.begin();
        batch.draw(redTruck,1040, camera.viewportHeight - 610);
        batch.end();

        if (count <= 30) {

            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.rect(1055, camera.viewportHeight - 540, 30, 30);
            renderer.end();

            batch.begin();
            font3.draw(batch, "A", 1062, camera.viewportHeight - 515);
            batch.end();
            damageHP();

        }

        count += 1;

        if (count == 60){
            count = 0;
        }

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(1185,  camera.viewportHeight - 90, 30, 30, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        renderer.rect(1187,  camera.viewportHeight - 88, 26, 26, Color.RED, Color.RED, Color.RED, Color.RED);
        renderer.end();

        batch.begin();

        font4.draw(batch, "X", 1192, camera.viewportHeight - 65);
        batch.end();

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
        batch.dispose();
        renderer.dispose();

    }

    public int damageHP() {
        if (HP == 0) {
            HP = 200;
            return HP;
        } else {
            return HP -= 1;
        }
    }

    public void toMenuScreen() { this.game.setScreen(new MenuScreen(this.game)); }

    public Rectangle getExitButton(){
        return this.exitButton;
    }
}
