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
    private Rectangle bigDonut;
    private Texture donutImage;
    private Rectangle smallDonut;
    private OrthographicCamera camera;
    private int points;
    private float w;

    GameScreen(Kroy game) {
        this.game = game;

        errorSound = Gdx.audio.newSound(Gdx.files.internal("error.mp3"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        donutImage = new Texture(Gdx.files.internal("donut2.png"));

        bigDonut = new Rectangle();
        bigDonut.width = (float) donutImage.getWidth()/2;
        bigDonut.height = (float) donutImage.getHeight()/2;
        bigDonut.x = camera.viewportWidth/2 - bigDonut.getWidth();
        bigDonut.y = camera.viewportHeight/2 - bigDonut.getHeight();

        GlyphLayout layout = new GlyphLayout();
        String item = "Points: ";
        layout.setText(game.font, item);
        w = layout.width;

        smallDonut = new Rectangle();
        smallDonut.width = (float) donutImage.getWidth()/4;
        smallDonut.height = (float) donutImage.getHeight()/4;
        smallDonut.x = 100;
        smallDonut.y = 100;

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
        game.batch.draw(donutImage, smallDonut.x, smallDonut.y, smallDonut.width, smallDonut.height);
        game.batch.draw(donutImage, bigDonut.x, bigDonut.y, bigDonut.width, bigDonut.height);
        game.font.draw(game.batch, "Points: " + points, 10, camera.viewportHeight - 10);
        game.batch.end();

        if (bigDonut.overlaps(smallDonut)) {
            points++;
            smallDonut.x = (float) ((Math.random() * ((camera.viewportWidth - smallDonut.width) + 1)) + 0);
            smallDonut.y = (float) ((Math.random() * ((camera.viewportHeight - smallDonut.height) + 1)) + 0);;
            eat();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (bigDonut.x > 0) {
                bigDonut.x -= 200 * Gdx.graphics.getDeltaTime() * 4;
            } else {
                errorSound.play();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (bigDonut.x + bigDonut.width < camera.viewportWidth) {
                bigDonut.x += 200 * Gdx.graphics.getDeltaTime() * 4;
            } else {
                errorSound.play();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (bigDonut.y + bigDonut.height < camera.viewportHeight) {
                bigDonut.y += 200 * Gdx.graphics.getDeltaTime() * 4;
            } else {
                errorSound.play();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (bigDonut.y > 0) {
                bigDonut.y -= 200 * Gdx.graphics.getDeltaTime() * 4;
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

    private void eat() {
        bigDonut.width += 10;
        bigDonut.height += 10;
        points++;
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
