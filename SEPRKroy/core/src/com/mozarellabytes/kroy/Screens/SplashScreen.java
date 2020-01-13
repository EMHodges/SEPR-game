package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import com.mozarellabytes.kroy.Kroy;
import com.badlogic.gdx.graphics.GL20;

/** This is the first screen shown when the user starts the game.
 * It shows the group's logo.
 */

public class SplashScreen implements Screen {


    private final Kroy game;

    /** The image displayed as the splash screen */
    private final Texture backgroundLogo;

    /** The time that the splash screen has been displayed to the screen */
    private long startTime;

    /** Constructor for the splash screen
     *
     * @param game
     */
    public SplashScreen(Kroy game) {
        this.game = game;
        backgroundLogo = new Texture(Gdx.files.internal("images/backgroundLogo.png"), true);
        backgroundLogo.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
    }


    @Override
    /** Logs the time that the screen was first rendered */
    public void show() {
        startTime = TimeUtils.millis();
    }

    /** Renders the splash screen image and changes the screen to the
     * menu screen after 3 seconds
     * */
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

    /** Called when this screen should release all resources. */
    @Override
    public void dispose() {
        backgroundLogo.dispose();
    }
}
