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

/** This screen shows the games controls including dragging the
 * fire truck to move it and pressing 'A' to attack the fortresses */

public class ControlsScreen implements Screen {

    private final Kroy game;

    /** The image displayed as the background behind the control screen */
    private Texture backgroundImage;

    /** The tile that shows the truck's path */
    private final Texture trailImage;

    /** The tile that shows the end of the truck's path */
    private final Texture trailEndImage;

    /** Sprite of a truck facing to the right */
    private final Texture truckRight;

    /** Sprite of a truck facing to the left */
    private final Texture truckLeft;

    /** Sprite of a fortress */
    private final Texture fortress;

    /** Camera to set the projection for the screen */
    public final OrthographicCamera camera;

    /** Rectangle containing the exit buttons coordinates */
    private final Rectangle exitButton;

    /** The HP of the fortress, helps animate the fortress */
    private int HP;

    /** Counter to reset the fortresses health bar */
    private int count;

    /** The name of the screen that called the control screen,
     * used to determine the background image */
    private final String screen;

    /** Width of the screen */
    private final float screenWidth;

    /** Height of the screen */
    private final float screenHeight;

    /** Screen that called the control screen - the screen
     * to return to after the control screen has been exited */
    private final Screen parent;

    /** Constructor for the Control screen
     * @param game LibGdx game
     * @param parent the screen that called the control screen
     * @param screen the name of the screen that called the control screen*/
    public ControlsScreen(Kroy game, Screen parent, String screen) {
        this.game = game;
        this.parent = parent;
        this.screen = screen;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        screenWidth = camera.viewportWidth;
        screenHeight = camera.viewportHeight;

        Gdx.input.setInputProcessor(new ControlScreenInputHandler(this));
        if (screen == "menu" ){
            backgroundImage = new Texture(Gdx.files.internal("menuscreen_blank_2.png"), true);
        } else if (screen == "game"){
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
    /** Renders the control screen including explaining how to move
     * the firetrucks and attack the fortresses
     *
     * @param delta The time in seconds since the last render. */
    public void render(float delta) {

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        drawBackgroundImage();
        drawFilledBackgroundBox();

        game.batch.begin();

        game.font50.draw(game.batch, "Control screen", screenWidth / 2.8f, screenHeight / 1.1678f);
        game.font25.draw(game.batch, "Flood the fortresses before the fortresses destroy your fire trucks to win", screenWidth / 12f,screenHeight / 1.29f);
        game.font33.draw(game.batch, "Moving the Fire Trucks", screenWidth / 8.33f, camera.viewportHeight * 0.6875f);
        game.font25.draw(game.batch, "Click on a truck and drag it", screenWidth / 7.692f,camera.viewportHeight * 0.6125f);
        game.font25.draw(game.batch, "This gives the truck a path:", screenWidth / 7.692f,camera.viewportHeight * 0.56875f);
        game.font25.draw(game.batch, "Unclick and the truck will", screenWidth / 7.692f,camera.viewportHeight * 0.3815f);
        game.font25.draw(game.batch, "follow the path", screenWidth / 7.692f,camera.viewportHeight * 0.34375f);

        game.batch.setColor(Color.CYAN);
        game.batch.draw(trailImage, 180,screenHeight / 2.2857f);
        game.batch.draw(trailImage, 230, screenHeight / 2.2857f);
        game.batch.draw(trailImage, 280, screenHeight / 2.2857f);
        game.batch.draw(trailImage, 330, screenHeight / 2.2857f);
        game.batch.draw(trailImage, 380, screenHeight / 2.2857f);
        game.batch.draw(trailImage, 430, screenHeight / 2.2857f);
        game.batch.draw(trailImage, 480, screenHeight / 2.2857f);
        game.batch.draw(trailEndImage, 480, screenHeight / 2.2857f);

        game.font25.draw(game.batch, "Or click and drag from the", 165,camera.viewportHeight - 590);
        game.font25.draw(game.batch, "end of the trucks path", 165,camera.viewportHeight - 625);

        game.batch.draw(trailImage, 380, camera.viewportHeight - 710);
        game.batch.draw(trailEndImage, 380, camera.viewportHeight - 710);

        game.batch.setColor(Color.RED);
        game.batch.draw(trailImage, 270, camera.viewportHeight - 710);
        game.batch.draw(trailEndImage, 270, camera.viewportHeight - 710);

        game.font33.draw(game.batch, "Attacking the fortresses", 680, camera.viewportHeight - 250);
        game.font25.draw(game.batch, "When a firetruck is within range ", 685,camera.viewportHeight - 310);
        game.font25.draw(game.batch, "of a fortress press A to attack", 685,camera.viewportHeight - 345);

        game.batch.setColor(Color.WHITE);
        game.batch.draw(truckRight, 172, camera.viewportHeight - 457);
        game.batch.draw(fortress,790, camera.viewportHeight - 670);
        game.batch.draw(truckLeft,1040, camera.viewportHeight - 610);

        game.batch.end();

        damageFortressHP();
        drawFortressHealthBar();

        renderExitButton();

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
        fortress.dispose();
        trailImage.dispose();
        trailEndImage.dispose();
        truckRight.dispose();
        truckLeft.dispose();
    }


    /** Changes the screen back to the screen that called the
     * control screen */
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

    private void drawBackgroundImage(){
        game.batch.begin();
        game.batch.draw(backgroundImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.batch.end();
    }

    private void drawFilledBackgroundBox(){
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.rect(50,50, camera.viewportWidth - 100 , camera.viewportHeight - 100, Color.BLACK, Color.BLACK,Color.BLACK, Color.BLACK);
        game.shapeRenderer.end();
    }

    /** Function causing damage to the fortress to deplete
     * it's health bar */
    public void damageFortressHP() {
        if (HP == 0) {
            HP = 200;
        } else {
            HP--;
        }
    }

    /** This draws the fortresses health bar */
    private void drawFortressHealthBar(){

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.rect(870,  camera.viewportHeight - 490, 35, 60);
        game.shapeRenderer.rect(875, camera.viewportHeight - 485, 24, 50, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        game.shapeRenderer.rect(875, camera.viewportHeight - 485, 24,  HP / 4, Color.RED, Color.RED, Color.RED, Color.RED);
        game.shapeRenderer.end();

        if (count <= 30) {
            drawFireTruckAttacking();
        } else if (count == 60){
            count = 0;
        }
        count++;
    }

    /** This draws the 'A' above the fire truck */
    private void drawFireTruckAttacking(){
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        game.shapeRenderer.rect(1055, camera.viewportHeight - 540, 30, 30);
        game.shapeRenderer.end();

        game.batch.begin();
        game.font33.draw(game.batch, "A", 1062, camera.viewportHeight - 515);
        game.batch.end();
    }


    /** Renders the exit button */
    private void renderExitButton(){
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.rect(1185,  camera.viewportHeight - 90, 30, 30, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        game.shapeRenderer.rect(1187,  camera.viewportHeight - 88, 26, 26, Color.RED, Color.RED, Color.RED, Color.RED);
        game.shapeRenderer.end();

        game.batch.begin();
        game.font33Red.draw(game.batch, "X", 1192, camera.viewportHeight - 65);
        game.batch.end();
    }

    public Rectangle getExitButton() {
        return this.exitButton;
    }




}