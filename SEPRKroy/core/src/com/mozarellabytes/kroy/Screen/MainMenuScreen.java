package com.mozarellabytes.kroy.Screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mozarellabytes.kroy.Kroy;

public class MainMenuScreen implements Screen {

    private final Kroy game;
    private OrthographicCamera camera;
    private Texture logoImage;
    private float w;

    public MainMenuScreen(final Kroy game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        logoImage = new Texture(Gdx.files.internal("logo.png"));
        GlyphLayout layout = new GlyphLayout();
        String item = "Tap anywhere to begin...";
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
        game.batch.draw(logoImage, camera.viewportWidth/2 - (float) logoImage.getWidth()/2, camera.viewportHeight/2 - (float) logoImage.getHeight()/2, logoImage.getWidth(), logoImage.getHeight());
        game.font.draw(game.batch, "Tap anywhere to begin...", camera.viewportWidth/2 - w/2, (float) (camera.viewportHeight*0.35));
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
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
