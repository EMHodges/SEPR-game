package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.TimeUtils;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.GameInputHandler;
import com.mozarellabytes.kroy.Utilities.GameOverInputHandler;

public class GameOverScreen implements Screen {

    private final Kroy game;

    private Texture backgroundLogo;
    private OrthographicCamera camera;

    private boolean won;

    private GlyphLayout layout;
    private String text;
    private String clicktext;

    public GameOverScreen(Kroy game, boolean won) {
        this.game = game;
        this.won = won;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        backgroundLogo = new Texture(Gdx.files.internal("images/backgroundLogo.png"), true);
        backgroundLogo.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        Gdx.input.setInputProcessor(new GameOverInputHandler(this));

        layout = new GlyphLayout();
        if (this.won) {
            this.text = "We did it! Good job little guy.";
        } else {
            this.text = "Mission Failed. We'll get 'em next time.";
        }
        this.text = this.text + "\n" + "   Click to return to the main menu...";
        layout.setText(game.font, this.text);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, this.text, camera.viewportWidth/2 - layout.width/2, camera.viewportHeight/2 - layout.height/2);
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
        backgroundLogo.dispose();
    }

    public void toMenuScreen() {
        this.game.setScreen(new MenuScreen(this.game));
    }
}
