package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.MenuInputHandler;

import java.awt.*;

public class MenuScreen implements Screen {

    private final Kroy game;
    private OrthographicCamera camera;
    private Texture logoImage;
    private Texture backgroundImage;
    private Rectangle playButton;
    private Texture playIdleTexture;
    private Texture playHoverTexture;
    private Texture currentPlayTexture;

    private MenuInputHandler ih;


    public MenuScreen(final Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        logoImage = new Texture(Gdx.files.internal("images/logo.png"), true);
        backgroundImage = new Texture(Gdx.files.internal("images/YorkMapEdit.png"), true);
        backgroundImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        playIdleTexture = new Texture(Gdx.files.internal("images/play.png"), true);
        playIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        playHoverTexture = new Texture(Gdx.files.internal("images/playHover.png"), true);
        playHoverTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        ih = new MenuInputHandler(this);

        currentPlayTexture = playIdleTexture;

        playButton = new Rectangle();
        playButton.width = currentPlayTexture.getWidth()/3;
        playButton.height = currentPlayTexture.getHeight()/3;
        playButton.x = (int) (camera.viewportWidth/2 - playButton.width/2);
        playButton.y = (int) ((camera.viewportHeight/2 - playButton.height/2)*0.4);

        Gdx.input.setInputProcessor(ih);
    }

    public void toGameScreen() {
        game.setScreen(new GameScreen(game));
        this.dispose();
    }

    public Rectangle getPlayButton() {
        return playButton;
    }

    public void hoverPlayButton() {
        currentPlayTexture = playHoverTexture;
    }

    public void idlePlayButton() {
        currentPlayTexture = playIdleTexture;
    }

    public int getScreenY() {
        return Gdx.graphics.getHeight();
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
        game.batch.draw(backgroundImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.batch.draw(logoImage, camera.viewportWidth/2 - logoImage.getWidth()/2, camera.viewportHeight/2 - logoImage.getHeight()/2, logoImage.getWidth(), logoImage.getHeight());
        game.batch.draw(currentPlayTexture, playButton.x, playButton.y, playButton.width, playButton.height);
        game.batch.end();
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
