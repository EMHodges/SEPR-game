package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.MenuInputHandler;
import com.mozarellabytes.kroy.Utilities.ScreenHandler;
import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.awt.*;

public class MenuScreen implements Screen {

    private final Kroy game;
    public OrthographicCamera camera;
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
        playIdleTexture = new Texture(Gdx.files.internal("buttons/playIdle.png"), true);
        playIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        playHoverTexture = new Texture(Gdx.files.internal("buttons/playHover.png"), true);
        playHoverTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        ih = new MenuInputHandler(this);

        SoundFX.sfx_menu.setLooping(true);
        SoundFX.sfx_menu.setVolume(.5f);
        SoundFX.sfx_menu.play();

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

    @Override
    public void show() {

    }

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
        logoImage.dispose();
        backgroundImage.dispose();
        currentPlayTexture.dispose();
        playHoverTexture.dispose();
        playIdleTexture.dispose();
        SoundFX.sfx_menu.stop();
    }

    public void toControlScreen(){ ScreenHandler.ToControls(game, this, "menu"); }
}
