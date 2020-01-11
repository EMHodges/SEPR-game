package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.FireTruckType;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.Entities.FortressType;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Screens.GameScreen;

public class GUI {

    private final Kroy game;
    private final int selectedX, selectedY, selectedH, selectedW;
    private final GameScreen gameScreen;

    private final Rectangle homeButton;
    private final Texture homeButtonIdle;
    private final Texture homeButtonClicked;
    private Texture currentHomeTexture;

    private final Rectangle pauseButton;
    private final Texture pauseButtonIdle;
    private final Texture pauseButtonClicked;
    private Texture currentPauseTexture;

    private final Rectangle soundButton;
    private final Texture soundOnIdleTexture;
    private final Texture soundOffIdleTexture;
    private final Texture soundOnClickedTexture;
    private final Texture soundOffClickedTexture;
    private Texture currentSoundTexture;

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

        if (SoundFX.music_enabled) {
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }

        homeButton = new Rectangle(Gdx.graphics.getWidth() - 33, Gdx.graphics.getHeight() - 33, 30, 30);
        soundButton = new Rectangle(Gdx.graphics.getWidth() - 70, Gdx.graphics.getHeight() - 33, 30, 30);
        pauseButton = new Rectangle(Gdx.graphics.getWidth() - 107, Gdx.graphics.getHeight() - 33, 30, 30);
    }

    public void renderSelectedEntity(Object entity) {
        if (entity != null) {
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            renderSelectedEntityBackground();
            game.shapeRenderer.end();
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            // if fire truck is selected
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

    private void renderSelectedEntityBackground() {
        game.shapeRenderer.setColor(0, 0, 0, 0.5f);
        game.shapeRenderer.rect(selectedX, selectedY, 275, 275);
    }

    private void renderSelectedTruck(FireTruck truck) {
        // also render text stats along left side
        renderSelectedEntityBar(truck.getHP(), truck.getType().getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderSelectedEntityBar(truck.getReserve(), truck.getType().getMaxReserve(), Color.CYAN, Color.BLUE, 2);
        renderSelectedEntityText(truck);
    }

    private void renderSelectedFortress(Fortress fortress) {
        renderSelectedEntityBar(fortress.getHP(), fortress.getFortressType().getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderSelectedEntityText(fortress);
    }

    private void renderSelectedEntityText(FireTruck truck) {
        FireTruckType truckType = truck.getType();
        int newLine = 20;
        game.batch.begin();
        game.font26.draw(game.batch, truckType.getName(), this.selectedX + 10, this.selectedY + this.selectedH - 10);
        game.font19.draw(game.batch, "HP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50);
        game.font19.draw(game.batch, String.format("%.1f", truck.getHP()) + " / " + String.format("%.1f", truckType.getMaxHP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine);
        game.font19.draw(game.batch, "Reserve: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*2);
        game.font19.draw(game.batch, String.format("%.1f", truck.getReserve()) + " / " + String.format("%.1f", truckType.getMaxReserve()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*3);
        game.font19.draw(game.batch, "Speed: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*4);
        game.font19.draw(game.batch, String.format("%.1f", truckType.getSpeed()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*5);
        game.font19.draw(game.batch, "Range: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*6);
        game.font19.draw(game.batch, String.format("%.1f", truckType.getRange()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*7);
        game.font19.draw(game.batch, "AP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*8);
        game.font19.draw(game.batch, String.format("%.2f", truckType.getAP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*9);
        game.batch.end();
    }

    private void renderSelectedEntityText(Fortress fortress) {
        int newLine = 20;
        FortressType fortressType = fortress.getFortressType();
        game.batch.begin();
        game.font26.draw(game.batch, fortressType.getName(), this.selectedX + 10, this.selectedY + this.selectedH - 10);
        game.font19.draw(game.batch, "HP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50);
        game.font19.draw(game.batch, String.format("%.1f", fortress.getHP()) + " / " + String.format("%.1f", fortressType.getMaxHP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine);
        game.font19.draw(game.batch, "Range: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*2);
        game.font19.draw(game.batch, String.format("%.1f", fortressType.getRange()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*3);
        game.font19.draw(game.batch, "AP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*4);
        game.font19.draw(game.batch, String.format("%.2f", fortressType.getAP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*5);
        game.batch.end();
    }

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

    public void renderButtons() {
        game.batch.begin();
        game.batch.draw(currentSoundTexture, soundButton.x, soundButton.y, soundButton.width, soundButton.height);
        game.batch.draw(currentHomeTexture, homeButton.x, homeButton.y, homeButton.width, homeButton.height);
        game.batch.draw(currentPauseTexture, pauseButton.x, pauseButton.y, pauseButton.width, pauseButton.height);
        game.batch.end();
    }

    public void clickedHomeButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        currentHomeTexture = homeButtonClicked;
    }

    public void clickedSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffClickedTexture;
        } else {
            currentSoundTexture = soundOnClickedTexture;
        }
    }

    public void clickedPauseButton() {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_button_clicked.play();
        }
        if (gameScreen.getState().equals(GameScreen.PlayState.PLAY)) {
            currentPauseTexture = pauseButtonClicked;
        } else {
            currentPauseTexture = pauseButtonIdle;
        }
    }

    public Rectangle getHomeButton() {
        return this.homeButton;
    }

    public Rectangle getSoundButton() {
        return this.soundButton;
    }

    public Rectangle getPauseButton() {
        return this.pauseButton;
    }

    public void idleHomeButton() {
        currentHomeTexture = homeButtonIdle;
    }

    public void idlePauseButton() {
        currentPauseTexture = pauseButtonIdle;
    }

    public void idleSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }
    }

    public void changeSound() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOnIdleTexture;
            SoundFX.StopMusic();
        } else {
            currentSoundTexture = soundOffIdleTexture;
            SoundFX.PlayMusic();
        }
    }

    public void renderPauseScreenText() {
        game.batch.begin();
        game.font60.draw(game.batch, "GAME PAUSED", this.selectedX + 427, this.selectedY - 60);
        game.font26.draw(game.batch, "Press 'P' or the Pause button", this.selectedX + 417, this.selectedY - 140);
        game.font26.draw(game.batch, "to return to game", this.selectedX + 500, this.selectedY - 170);
        game.batch.end();
    }

}
