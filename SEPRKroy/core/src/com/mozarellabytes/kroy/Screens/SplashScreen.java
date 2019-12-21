package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import com.mozarellabytes.kroy.Kroy;
import com.badlogic.gdx.graphics.GL20;

public class SplashScreen implements Screen {

    private final Kroy game;
    private Texture backgroundLogo;
    private long startTime;

    public SplashScreen(Kroy game) {
        this.game = game;
        backgroundLogo = new Texture(Gdx.files.internal("images/backgroundLogo.png"), true);
        backgroundLogo.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
    }

    @Override
    public void show() {
        startTime = TimeUtils.millis();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backgroundLogo, 0, 0, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
        game.batch.end();

        if(TimeUtils.timeSinceMillis(startTime) > 3000){
            game.setScreen(new ControlsScreen(game, new MenuScreen(game), "menu"));
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
        backgroundLogo.dispose();
    }
}
