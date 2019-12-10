package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.GameOverInputHandler;

import java.awt.*;

public class ControlsScreen implements Screen {

    private final Kroy game;

    private Texture backgroundImage;
    private OrthographicCamera camera;

    private GlyphLayout layout;
    private Stage stage;
    private String text;
    private String clicktext;
    public SpriteBatch batch;
    public BitmapFont font;
    public BitmapFont font2;
    public BitmapFont font3;
    public ShapeRenderer renderer;
    public Texture backgroundLogo;
    public Label.LabelStyle labelStyle;
//    public BitmapFont font;


    public ControlsScreen(Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        backgroundImage = new Texture(Gdx.files.internal("images/YorkMapColour.png"), true);
        backgroundImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);


        batch = new SpriteBatch();
        renderer = new ShapeRenderer();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Magero.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();


        parameter.size = 50;
        font = generator.generateFont(parameter);
        parameter.size = 25;
        font2 = generator.generateFont(parameter);

        parameter.size = 33;
        font3 = generator.generateFont(parameter);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.3f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Color purple = new Color(65f/255f,30f/255f,80f/255f,1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(backgroundImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(50,50, camera.viewportWidth - 100 , camera.viewportHeight - 100, purple, purple, purple, purple);
        renderer.end();

        batch.begin();
        font.draw(batch, "Control screen", 430, 720);
        font2.draw(batch, "Flood the fortresses before the fortresses destroy your fire trucks to win", 110,camera.viewportHeight - 140);
        font3.draw(batch, "Move the Fire Trucks", 105, camera.viewportHeight - 200 );
        font2.draw(batch, "Click on a truck and drag it. It's path will be shown using the tiles below ", 110,camera.viewportHeight - 245);
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

    }
}
