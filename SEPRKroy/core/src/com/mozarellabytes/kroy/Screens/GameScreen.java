package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.GameInputHandler;
import com.mozarellabytes.kroy.Utilities.MenuInputHandler;

public class GameScreen implements Screen {

    private Kroy game;
    private TiledMap map;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private GameInputHandler ih;

    public GameScreen(Kroy game) {
        this.game = game;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 40, 24);
        camera.update();

        map = new TmxMapLoader().load("maps/YorkMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 48f);

        ih = new GameInputHandler(this);
        Gdx.input.setInputProcessor(ih);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();
        game.batch.begin();
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
        map.dispose();
    }
}
