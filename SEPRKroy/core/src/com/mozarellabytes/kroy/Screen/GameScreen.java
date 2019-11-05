package com.mozarellabytes.kroy.Screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.mozarellabytes.kroy.Kroy;

public class GameScreen implements Screen {

    private final Kroy game;

    private Sound errorSound;
    private Rectangle donutShape;
    private Texture donutImage;
    private OrthographicCamera camera;
    private int points;
    private float w;

    GameScreen(Kroy game) {
        this.game = game;

        errorSound = Gdx.audio.newSound(Gdx.files.internal("error.mp3"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        donutImage = new Texture(Gdx.files.internal("donut.png"));

        donutShape = new Rectangle();
        donutShape.width = (float) donutImage.getWidth()/2;
        donutShape.height = (float) donutImage.getHeight()/2;
        donutShape.x = camera.viewportWidth/2 - donutShape.getWidth();
        donutShape.y = camera.viewportHeight/2 - donutShape.getHeight();

        GlyphLayout layout = new GlyphLayout();
        String item = "Points: ";
        layout.setText(game.font, item);
        w = layout.width;

    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(donutImage, donutShape.x, donutShape.y, donutShape.width, donutShape.height);
        game.font.draw(game.batch, "Points: " + points, 10, camera.viewportHeight - 10);
        game.batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (donutShape.x > 0) {
                donutShape.x -= 200 * Gdx.graphics.getDeltaTime() * 2;
                points++;
            } else {
                errorSound.play();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (donutShape.x + donutShape.width < camera.viewportWidth) {
                donutShape.x += 200 * Gdx.graphics.getDeltaTime() * 2;
                points++;
            } else {
                errorSound.play();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (donutShape.y + donutShape.height < camera.viewportHeight) {
                donutShape.y += 200 * Gdx.graphics.getDeltaTime() * 2;
                points++;
            } else {
                errorSound.play();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (donutShape.y > 0) {
                donutShape.y -= 200 * Gdx.graphics.getDeltaTime() * 2;
                points++;
            } else {
                errorSound.play();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            dispose();
            System.exit(0);
        }

    }

    /**
     * @param width of window
     * @param height of window
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }
}
