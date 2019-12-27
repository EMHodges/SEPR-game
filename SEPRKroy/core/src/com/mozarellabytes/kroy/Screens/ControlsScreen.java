package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.*;

import java.awt.*;

public class ControlsScreen implements Screen {

    private final Kroy game;

    private Texture backgroundImage;
    private final Texture trailImage;
    private final Texture trailEndImage;
    private final Texture truckRight;
    private final Texture truckLeft;
    private final Texture fortress;

    public final OrthographicCamera camera;

    private final Rectangle exitButton;

    private int HP;
    private int count;
    private final String screen;

    private final Screen parent;

    public ControlsScreen(Kroy game, Screen parent, String screen) {
        this.game = game;
        this.parent = parent;
        this.screen = screen;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        Gdx.input.setInputProcessor(new ControlScreenInputHandler(this));
        if (screen == null ? "menu" == null : screen.equals("menu")){
            backgroundImage = new Texture(Gdx.files.internal("menuscreen_blank_2.png"), true);
        } else if (screen == null ? "game" == null : screen.equals("game")){
            backgroundImage = new Texture(Gdx.files.internal("images/YorkMapEdit.png"), true);
        }

        backgroundImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        trailImage = new Texture(Gdx.files.internal("sprites/firetruck/trail.png"), true);
        trailEndImage = new Texture(Gdx.files.internal("sprites/firetruck/trailEnd.png"), true);

        truckRight = new Texture(Gdx.files.internal("sprites/firetruck/right.png"), true);
        truckLeft = new Texture(Gdx.files.internal("sprites/firetruck/left.png"), true);

        fortress = new Texture(Gdx.files.internal("sprites/fortress/fortress.png"), true);

        HP = 200;
        count = 0;

        exitButton = new Rectangle();
        exitButton.x = 1185;
        exitButton.y = (int) (camera.viewportHeight - 90);
        exitButton.width = 30;
        exitButton.height = 30;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.3f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.batch.end();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.rect(50,50, camera.viewportWidth - 100 , camera.viewportHeight - 100, Color.BLACK, Color.BLACK,Color.BLACK, Color.BLACK);
        game.shapeRenderer.end();

        game.batch.begin();

        game.font50.draw(game.batch, "Control screen", 430, camera.viewportHeight - 115);
        game.font25.draw(game.batch, "Flood the fortresses before the fortresses destroy your fire trucks to win", 110,camera.viewportHeight - 180);
        game.font33.draw(game.batch, "Moving the Fire Trucks", 135, camera.viewportHeight - 250 );
        game.font25.draw(game.batch, "Click on a truck and drag it", 165,camera.viewportHeight - 310);
        game.font25.draw(game.batch, "This gives the truck a path:", 165,camera.viewportHeight - 345);
        game.font25.draw(game.batch, "Unclick and the truck will", 165,camera.viewportHeight - 495);
        game.font25.draw(game.batch, "follow the path", 165,camera.viewportHeight - 525);

        game.batch.setColor(Color.CYAN);
        game.batch.draw(trailImage, 180,camera.viewportHeight - 450);
        game.batch.draw(trailImage, 230, camera.viewportHeight - 450);
        game.batch.draw(trailImage, 280, camera.viewportHeight - 450);
        game.batch.draw(trailImage, 330, camera.viewportHeight - 450);
        game.batch.draw(trailImage, 380, camera.viewportHeight - 450);
        game.batch.draw(trailImage, 430, camera.viewportHeight - 450);
        game.batch.draw(trailImage, 480, camera.viewportHeight - 450);
        game.batch.draw(trailImage, 480, camera.viewportHeight - 450);

        game.font25.draw(game.batch, "Or click and drag from the", 165,camera.viewportHeight - 590);
        game.font25.draw(game.batch, "end of the trucks path", 165,camera.viewportHeight - 625);

        game.batch.draw(trailImage, 380, camera.viewportHeight - 710);
        game.batch.draw(trailEndImage, 380, camera.viewportHeight - 710);

        game.batch.setColor(Color.RED);
        game.batch.draw(trailImage, 270, camera.viewportHeight - 710);
        game.batch.draw(trailEndImage, 270, camera.viewportHeight - 710);

        game.batch.setColor(Color.WHITE);
        game.batch.draw(truckRight, 172, camera.viewportHeight - 457);
        game.batch.draw(fortress,790, camera.viewportHeight - 670);
        game.batch.draw(truckLeft,1040, camera.viewportHeight - 610);

        game.font33.draw(game.batch, "Attacking the fortresses", 680, camera.viewportHeight - 250);
        game.font25.draw(game.batch, "When a firetruck is within range ", 685,camera.viewportHeight - 310);
        game.font25.draw(game.batch, "of a fortress press A to attack", 685,camera.viewportHeight - 345);

        game.batch.end();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.rect(870,  camera.viewportHeight - 490, 35, 60);
        game.shapeRenderer.rect(875, camera.viewportHeight - 485, 24, 50, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        game.shapeRenderer.rect(875, camera.viewportHeight - 485, 24,  HP / 4, Color.RED, Color.RED, Color.RED, Color.RED);
        game.shapeRenderer.end();

        if (count <= 30) {

            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            game.shapeRenderer.rect(1055, camera.viewportHeight - 540, 30, 30);
            game.shapeRenderer.end();

            game.batch.begin();
            game.font33.draw(game.batch, "A", 1062, camera.viewportHeight - 515);
            game.batch.end();
            damageHP();
        }

        count++;

        if (count == 60){
            count = 0;
        }

        // Hopefully have a 'back' button instead of this
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.rect(1185,  camera.viewportHeight - 90, 30, 30, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        game.shapeRenderer.rect(1187,  camera.viewportHeight - 88, 26, 26, Color.RED, Color.RED, Color.RED, Color.RED);
        game.shapeRenderer.end();

        game.batch.begin();
        game.font33Red.draw(game.batch, "X", 1192, camera.viewportHeight - 65);
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
        fortress.dispose();
        trailImage.dispose();
        trailEndImage.dispose();
        truckRight.dispose();
        truckLeft.dispose();
    }

    public void damageHP() {
        if (HP == 0) {
            HP = 200;
        } else {
            HP -= 1;
        }
    }

    public void changeScreen() {
        if (this.screen.equals("game")) {
            GUI gui = new GUI(game, (GameScreen) parent);
            Gdx.input.setInputProcessor(new GameInputHandler((GameScreen) parent, gui));
            this.game.setScreen(parent);
        } else if (this.screen.equals("menu")){
            Gdx.input.setInputProcessor(new MenuInputHandler((MenuScreen)parent));
            this.game.setScreen(parent);
        }
    }

    public Rectangle getExitButton() {
        return this.exitButton;
    }

}