package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.GameOverInputHandler;

/** This screen is shown after the game has ended.
 * It tells the player if they have won or lost.
 */

public class GameOverScreen implements Screen {

    /** The game - to be able to use the fonts from game */
    private final Kroy game;

    /** The texture that makes up the background screen */
    private final Texture backgroundLogo;

    /** Camera to set the projection for the screen */
    private final OrthographicCamera camera;

    /** The format that the text will be displayed in */
    private final GlyphLayout layout;

    /** The text that will be displayed to the screen */
    private String text;

    /** Constructor for the game screen
     * @param game  LibGdx game
     * @param won <code> true </code> if the game was won
     *            <code> false </code> if th game was lost
     */
    public GameOverScreen(Kroy game, boolean won) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        backgroundLogo = new Texture(Gdx.files.internal("images/backgroundLogo.png"), true);
        backgroundLogo.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        Gdx.input.setInputProcessor(new GameOverInputHandler(game));

        layout = new GlyphLayout();
        if (won) {
            this.text = "We did it! Good job little guy.";
        } else {
            this.text = "Mission Failed. We'll get 'em next time.";
        }
        this.text = this.text + "\n" + "   Click to return to the main menu...";
        layout.setText(game.font26, this.text);
    }

    @Override
    public void show() {

    }

    /** Renders the game over screen
     *
     *  @param delta The time in seconds since the last render. */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font26.draw(game.batch, this.text, camera.viewportWidth/2 - layout.width/2, camera.viewportHeight/2 - layout.height/2);
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

    /** Called when this screen should release all resources. */
    @Override
    public void dispose() {
        backgroundLogo.dispose();
    }


}
