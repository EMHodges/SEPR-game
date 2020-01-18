package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Screens.GameScreen;

//import javax.jnlp.FileContents;

/**
 * This Class is responsible for displaying the majority of the GUI that the
 * user can see and interact with that are apart from the main function of
 * the game i.e. drawing the FireTruck's path. The GUI renders the buttons
 * visible in the top right corner whilst playing the game, along with
 * rendering the stats area in the top left corner when an entity is selected
 * by clicking on it on the map
 */
public class GUI {

    /** LibGdx game */
    private final Kroy game;

    /** Coordinates and dimensions of the stats box */
    private final int selectedX, selectedY, selectedH, selectedW;

    /** The screen where the buttons are rendered */
    private final GameScreen gameScreen;

    /** Rectangle containing the homeButton's coordinates, height and width */
    private final Rectangle homeButton;
    /** Texture of the homeButton when it is not being clicked on */
    private final Texture homeButtonIdle;
    /** Texture of the homeButton when it's being clicked */
    private final Texture homeButtonClicked;
    /** Texture of the homeButton that is rendered to the screen */
    private Texture currentHomeTexture;

    /** Rectangle containing the pauseButton's coordinates, height and width */
    private final Rectangle pauseButton;
    /** Texture of the pausebutton when it is not being clicked on */
    private final Texture pauseButtonIdle;
    /** Texture of the pauseButton when it's being clicked */
    private final Texture pauseButtonClicked;
    /** Texture of the pauseButton that is rendered to the screen */
    private Texture currentPauseTexture;

    /** Rectangle containing the infoButton's coordinates, height and width */
    private final Rectangle infoButton;
    /** Texture of the infoButton when it is not being clicked on */
    private final Texture infoButtonIdle;
    /** Texture of the infoButton when it's being clicked */
    private final Texture infoButtonClicked;
    /** Texture of the infoButton that is rendered to the screen */
    private Texture currentInfoTexture;

    /** Rectangle containing the soundButton's coordinates, height and width */
    private final Rectangle soundButton;
    /** Texture of the soundButton when music is off to turn the music on
     * when it is not being clicked */
    private final Texture soundOnIdleTexture;
    /** Texture of the soundButton when music is on to turn the music off
     * when it is not being clicked */
    private final Texture soundOffIdleTexture;
    /** Texture of the soundButton when music is off and the soundButton is
     * being clicked to turn the sound on*/
    private final Texture soundOnClickedTexture;
    /** Texture of the soundButton when music is on and the soundButton is
     * being clicked to turn the sound off */
    private final Texture soundOffClickedTexture;
    /** Texture of the soundButton that is rendered to the screen */
    private Texture currentSoundTexture;

    /** Camera to set the projection for the screen */
    private final OrthographicCamera pauseCamera;

    /** Constructor for GUI
     *
     * @param game          The Kroy game
     * @param gameScreen    Screen where these methods will be rendered
     */
    public GUI(Kroy game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.selectedH = 275;
        this.selectedW = 275;
        this.selectedX = 10;
        this.selectedY = Gdx.graphics.getHeight() - 10 - this.selectedH;

        homeButtonIdle = new Texture(Gdx.files.internal("ui/home_idle.png"), true);
        homeButtonIdle.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        homeButtonClicked = new Texture(Gdx.files.internal("ui/home_clicked.png"), true);
        homeButtonClicked.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        pauseButtonIdle = new Texture(Gdx.files.internal("ui/pause_idle.png"), true);
        pauseButtonIdle.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        pauseButtonClicked = new Texture(Gdx.files.internal("ui/pause_clicked.png"), true);
        pauseButtonClicked.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        infoButtonIdle = new Texture(Gdx.files.internal("ui/info_idle.png"), true);
        infoButtonIdle.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        infoButtonClicked = new Texture(Gdx.files.internal("ui/info_clicked.png"), true);
        infoButtonClicked.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        soundOnIdleTexture = new Texture(Gdx.files.internal("ui/sound_on_idle.png"), true);
        soundOnIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffIdleTexture = new Texture(Gdx.files.internal("ui/sound_off_idle.png"), true);
        soundOffIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOnClickedTexture = new Texture(Gdx.files.internal("ui/sound_on_clicked.png"), true);
        soundOnClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffClickedTexture = new Texture(Gdx.files.internal("ui/sound_off_clicked.png"), true);
        soundOffClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        currentHomeTexture = homeButtonIdle;
        currentPauseTexture = pauseButtonIdle;
        currentInfoTexture = infoButtonIdle;

        if (SoundFX.music_enabled) {
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }

        homeButton = new Rectangle(Gdx.graphics.getWidth() - 33, Gdx.graphics.getHeight() - 33, 30, 30);
        soundButton = new Rectangle(Gdx.graphics.getWidth() - 70, Gdx.graphics.getHeight() - 33, 30, 30);
        pauseButton = new Rectangle(Gdx.graphics.getWidth() - 107, Gdx.graphics.getHeight() - 33, 30, 30);
        infoButton = new Rectangle(Gdx.graphics.getWidth() - 144, Gdx.graphics.getHeight() - 33, 30, 30);

        pauseCamera = new OrthographicCamera();
        pauseCamera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
    }

    /**
     * Renders the health and (when applicable) reserve bars
     * along with the custom attributes that the entity
     * possesses
     *
     * @param entity    The entity that has been clicked on
     */
    public void renderSelectedEntity(Object entity) {
        if (entity != null) {
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            renderSelectedEntityBackground();
            game.shapeRenderer.end();
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if (entity instanceof FireTruck) {
                FireTruck truck = (FireTruck) entity;
                renderSelectedTruck(truck);
            } else if (entity instanceof Fortress) {
                Fortress fortress = (Fortress) entity;
                renderSelectedFortress(fortress);
            }
            game.shapeRenderer.end();
        }
    }

    /**
     * Renders the dark background behind the stats area
     */
    private void renderSelectedEntityBackground() {
        game.shapeRenderer.setColor(0, 0, 0, 0.5f);
        game.shapeRenderer.rect(selectedX, selectedY, selectedW, selectedH);
    }

    /**
     * Calls the methods which render the attributes and
     * health/reserve bars of a truck in the stats area
     *
     * @param truck the FireTruck that owns the stats
     *              that are being displayed
     */
    private void renderSelectedTruck(FireTruck truck) {
        renderSelectedEntityBar(truck.getHP(), truck.getType().getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderSelectedEntityBar(truck.getReserve(), truck.getType().getMaxReserve(), Color.CYAN, Color.BLUE, 2);
        renderSelectedEntityText(truck);
    }

    /**
     * Calls the methods which render the attributes and
     * health bar of a fortress in the stats area
     *
     * @param fortress  the Fortress that owns the stats
     *                  thta are being displayed
     */
    private void renderSelectedFortress(Fortress fortress) {
        renderSelectedEntityBar(fortress.getHP(), fortress.getFortressType().getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderSelectedEntityText(fortress);
    }

    /**
     * Renders the attributes in a vertical layout
     * of the FireTruck
     *
     * @param truck the FireTruck that owns the stats
     *              that are being displayed
     */
    private void renderSelectedEntityText(FireTruck truck) {
        int newLine = 20;
        game.batch.begin();
        game.font26.draw(game.batch, truck.getType().getName(), this.selectedX + 10, this.selectedY + this.selectedH - 10);
        game.font19.draw(game.batch, "HP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50);
        game.font19.draw(game.batch, String.format("%.1f", truck.getHP()) + " / " + String.format("%.1f", truck.getType().getMaxHP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine);
        game.font19.draw(game.batch, "Reserve: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*2);
        game.font19.draw(game.batch, String.format("%.1f", truck.getReserve()) + " / " + String.format("%.1f", truck.getType().getMaxReserve()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*3);
        game.font19.draw(game.batch, "Speed: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*4);
        game.font19.draw(game.batch, String.format("%.1f", truck.getType().getSpeed()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*5);
        game.font19.draw(game.batch, "Range: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*6);
        game.font19.draw(game.batch, String.format("%.1f", truck.getType().getRange()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*7);
        game.font19.draw(game.batch, "AP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*8);
        game.font19.draw(game.batch, String.format("%.2f", truck.getType().getAP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*9);
        game.batch.end();
    }

    /**
     * Renders the attributes in a vertical layout
     * of the Fortress
     *
     * @param fortress  the Fortress that owns the stats
     *                  that are being displayed
     */
    private void renderSelectedEntityText(Fortress fortress) {
        int newLine = 20;
        game.batch.begin();
        game.font26.draw(game.batch, fortress.getFortressType().getName(), this.selectedX + 10, this.selectedY + this.selectedH - 10);
        game.font19.draw(game.batch, "HP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50);
        game.font19.draw(game.batch, String.format("%.1f", fortress.getHP()) + " / " + String.format("%.1f", fortress.getFortressType().getMaxHP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine);
        game.font19.draw(game.batch, "Range: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*2);
        game.font19.draw(game.batch, String.format("%.1f", fortress.getFortressType().getRange()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*3);
        game.font19.draw(game.batch, "AP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*4);
        game.font19.draw(game.batch, String.format("%.2f", fortress.getFortressType().getAP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*5);
        game.batch.end();
    }

    /**
     * Renders the stat bars which are currently used to
     * show the health/reserve of trucks and health of
     * fortresses. The integers inside the method that
     * have values set to them are customisable to get
     * the desired layout/formatting of the bars
     *
     * @param value             the value towards the goal
     * @param maxValue          the goal
     * @param progressColour    the colour of the value bar
     * @param backgroundColour  the colour behind the value bar
     * @param position          the 'bar number' to allow multiple
     *                          bars along side each other
     *                          (1 to infinity)
     */
    private void renderSelectedEntityBar(float value, float maxValue, Color progressColour, Color backgroundColour, int position) {
        int whiteW = 50;
        int outerSpacing = 10;
        int innerSpacing = 5;
        int spaceForText = 35;
        int barHeight = this.selectedH - outerSpacing*2 - innerSpacing*2 - spaceForText;
        int positionSpacer = position * whiteW;
        int barSpacer = 0;
        if (position > 1) barSpacer = 5;
        game.shapeRenderer.rect(this.selectedX + this.selectedW - positionSpacer - outerSpacing - barSpacer, this.selectedY + outerSpacing, whiteW, this.selectedH - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        game.shapeRenderer.rect(this.selectedX + this.selectedW - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, whiteW - innerSpacing*2, barHeight, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
        game.shapeRenderer.rect(this.selectedX + this.selectedW - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, whiteW - innerSpacing*2, value/maxValue*barHeight, progressColour, progressColour, progressColour, progressColour);
    }

    /** Renders the buttons to the game screen */
    public void renderButtons() {
        game.batch.begin();
        game.batch.draw(currentSoundTexture, soundButton.x, soundButton.y, soundButton.width, soundButton.height);
        game.batch.draw(currentHomeTexture, homeButton.x, homeButton.y, homeButton.width, homeButton.height);
        game.batch.draw(currentPauseTexture, pauseButton.x, pauseButton.y, pauseButton.width, pauseButton.height);
        game.batch.draw(currentInfoTexture, infoButton.x, infoButton.y, infoButton.width, infoButton.height);
        game.batch.end();
    }

    /** Sets the homeButton texture to homeButtonClicked while the homeButton
     * is being clicked on */
    public void clickedHomeButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        currentHomeTexture = homeButtonClicked;
    }

    /** Sets the infoButton texture to "Idle" if the previous was "Clicked",
     * else it sets it to "Clicked" */
    public void clickedInfoButton() {
        if (currentInfoTexture == infoButtonIdle) {
            currentInfoTexture = infoButtonClicked;
        } else {
            currentInfoTexture = infoButtonIdle;
        }
    }

    /** Sets the soundButton texture to either soundOffClickedTexture or
     * soundOnClickedTexture while the soundButton is being clicked on */
    public void clickedSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffClickedTexture;
        } else {
            currentSoundTexture = soundOnClickedTexture;
        }
    }

    /** Sets the pauseButton texture that is rendered to the screen and pauses
     * and unpauses the game */
    public void clickedPauseButton() {

        if (gameScreen.getState().equals(GameScreen.PlayState.PLAY)) {
            currentPauseTexture = pauseButtonClicked;
            if (SoundFX.music_enabled) {
                SoundFX.sfx_pause.play();
            }
        } else {
            currentPauseTexture = pauseButtonIdle;
            if (SoundFX.music_enabled) {
                SoundFX.sfx_unpause.play();
            }
        }
    }

    /** Sets the homeButton texture that is rendered to the screen */
    public void idleHomeButton() {
        currentHomeTexture = homeButtonIdle;
    }

    /** Sets the pauseButton texture that is rendered to the screen */
    public void idlePauseButton() {
        currentPauseTexture = pauseButtonIdle;
    }

    public void idleInfoButton() {
        currentInfoTexture = infoButtonIdle;
    }

    /** Sets the soundButton texture that is rendered to the screen */
    public void idleSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }
    }

    /** Toggles the sound, called if 'S' key or the sound button
     * is pressed */
    public void changeSound() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOnIdleTexture;
            SoundFX.stopMusic();
        } else {
            currentSoundTexture = soundOffIdleTexture;
            SoundFX.playGameMusic();
        }
    }

    /** Renders the text to the screen when the game is paused */
    public void renderPauseScreenText() {
        GlyphLayout layout = new GlyphLayout();
        String pauseText1 =  "Game paused \n";
        String pauseText2 =  "Press 'P' or the Pause button \n To return to game";
        layout.setText(game.font26, pauseText1);
        layout.setText(game.font26, pauseText2);

        game.batch.setProjectionMatrix(pauseCamera.combined);
        game.batch.begin();
        game.font50.draw(game.batch, pauseText1, pauseCamera.viewportWidth/2 - layout.width/2.7f, pauseCamera.viewportHeight/1.8f - layout.height/2);
        game.font26.draw(game.batch, pauseText2, pauseCamera.viewportWidth/2 - layout.width/2, pauseCamera.viewportHeight/2.3f - layout.height/2);
        game.batch.end();
    }

    public Rectangle getHomeButton() { return this.homeButton; }

    public Rectangle getSoundButton() { return this.soundButton; }

    public Rectangle getPauseButton() { return this.pauseButton; }

    public Rectangle getInfoButton() { return this.infoButton; }

}
