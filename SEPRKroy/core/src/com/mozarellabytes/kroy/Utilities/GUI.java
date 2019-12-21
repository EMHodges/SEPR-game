package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Screens.State;

public class GUI {

    private Kroy game;
    private ShapeRenderer renderer;
    private int x, y, w, h;
    private GameScreen gameScreen;

    private Rectangle homeButton;
    private Texture homeButtonIdle;
    private Texture homeButtonClicked;
    private Texture currentHomeTexture;

    private Rectangle pauseButton;
    private Texture pauseButtonIdle;
    private Texture pauseButtonClicked;
    private Texture currentPauseTexture;

    private Rectangle soundButton;
    private Texture soundOnIdleTexture;
    private Texture soundOffIdleTexture;
    private Texture soundOnClickedTexture;
    private Texture soundOffClickedTexture;
    private Texture currentSoundTexture;

    public GUI(Kroy game, GameScreen gameScreen, int w, int h) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.renderer = new ShapeRenderer();
        this.x = 10;
        this.y = Gdx.graphics.getHeight() - 10 - h;
        this.w = w;
        this.h = h;

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

        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }

        homeButton = new Rectangle();
        homeButton.width = 30;
        homeButton.height = 30;
        homeButton.x = Gdx.graphics.getWidth() - homeButton.width - 3;
        homeButton.y = Gdx.graphics.getHeight() - homeButton.height - 3;

        pauseButton = new Rectangle();
        pauseButton.width = 30;
        pauseButton.height = 30;
        pauseButton.x = Gdx.graphics.getWidth() - pauseButton.width - 77;
        pauseButton.y = Gdx.graphics.getHeight() - pauseButton.height - 3;

        soundButton = new Rectangle();
        soundButton.width = 30;
        soundButton.height = 30;
        soundButton.x = Gdx.graphics.getWidth() - homeButton.width - 40;
        soundButton.y = Gdx.graphics.getHeight() - homeButton.height - 3;

    }

    public void render(Object entity) {
        if (entity != null) {
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderBackground();
            renderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            // if fire truck is selected
            if (entity instanceof FireTruck) {
                FireTruck truck = (FireTruck) entity;
                renderTruck(truck);
            } else if (entity instanceof Fortress) {
                Fortress fortress = (Fortress) entity;
                renderFortress(fortress);
            }
            renderer.end();
        }
    }

    private void renderBackground() {
        renderer.setColor(0, 0, 0, 0.5f);
        renderer.rect(x, y, w, h);
    }

    private void renderTruck(FireTruck truck) {
        // also render text stats along left side
        renderBar(truck.getHP(), truck.getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderBar(truck.getReserve(), truck.getMaxReserve(), Color.CYAN, Color.BLUE, 2);
        renderText(truck);
    }

    private void renderFortress(Fortress fortress) {
        renderBar(fortress.getHP(), fortress.getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderText(fortress);
    }

    private void renderText(FireTruck truck) {
        int newLine = 20;
        game.batch.begin();
        game.bigFont.draw(game.batch, truck.getName(), this.x + 10, this.y + this.h - 10);
        game.smallFont.draw(game.batch, "HP: ", this.x + 15, this.y + this.h - 50);
        game.smallFont.draw(game.batch, String.format("%.1f", truck.getHP()) + " / " + String.format("%.1f", truck.getMaxHP()), this.x + 20, this.y + this.h - 50 - newLine);
        game.smallFont.draw(game.batch, "Reserve: ", this.x + 15, this.y + this.h - 50 - newLine*2);
        game.smallFont.draw(game.batch, String.format("%.1f", truck.getReserve()) + " / " + String.format("%.1f", truck.getMaxReserve()), this.x + 20, this.y + this.h - 50 - newLine*3);
        game.smallFont.draw(game.batch, "Speed: ", this.x + 15, this.y + this.h - 50 - newLine*4);
        game.smallFont.draw(game.batch, String.format("%.1f", truck.getSpeed()), this.x + 20, this.y + this.h - 50 - newLine*5);
        game.smallFont.draw(game.batch, "Range: ", this.x + 15, this.y + this.h - 50 - newLine*6);
        game.smallFont.draw(game.batch, String.format("%.1f", truck.getRange()), this.x + 20, this.y + this.h - 50 - newLine*7);
        game.smallFont.draw(game.batch, "AP: ", this.x + 15, this.y + this.h - 50 - newLine*8);
        game.smallFont.draw(game.batch, String.format("%.2f", truck.getAP()), this.x + 20, this.y + this.h - 50 - newLine*9);
        game.batch.end();
    }

    private void renderText(Fortress fortress) {
        int newLine = 20;
        game.batch.begin();
        game.bigFont.draw(game.batch, fortress.getName(), this.x + 10, this.y + this.h - 10);
        game.smallFont.draw(game.batch, "HP: ", this.x + 15, this.y + this.h - 50);
        game.smallFont.draw(game.batch, String.format("%.1f", fortress.getHP()) + " / " + String.format("%.1f", fortress.getMaxHP()), this.x + 20, this.y + this.h - 50 - newLine);
        game.smallFont.draw(game.batch, "Range: ", this.x + 15, this.y + this.h - 50 - newLine*2);
        game.smallFont.draw(game.batch, String.format("%.1f", fortress.getRange()), this.x + 20, this.y + this.h - 50 - newLine*3);
        game.smallFont.draw(game.batch, "AP: ", this.x + 15, this.y + this.h - 50 - newLine*4);
        game.smallFont.draw(game.batch, String.format("%.2f", fortress.getAP()), this.x + 20, this.y + this.h - 50 - newLine*5);
        game.batch.end();
    }

    private void renderBar(float value, float maxValue, Color progressColour, Color backgroundColour, int position) {
        int whiteW = 50;
        int outerSpacing = 10;
        int innerSpacing = 5;
        int spaceForText = 35;
        int barHeight = this.h - outerSpacing*2 - innerSpacing*2 - spaceForText;
        int positionSpacer = position * whiteW;
        int barSpacer = 0;
        if (position > 1) barSpacer = 5;
        renderer.rect(this.x + this.w - positionSpacer - outerSpacing - barSpacer, this.y + outerSpacing, whiteW, this.h - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        renderer.rect(this.x + this.w - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.y + outerSpacing + innerSpacing, whiteW - innerSpacing*2, barHeight, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
        renderer.rect(this.x + this.w - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.y + outerSpacing + innerSpacing, whiteW - innerSpacing*2, value/maxValue*barHeight, progressColour, progressColour, progressColour, progressColour);
    }

    public void renderButtons(){
        renderSoundButton();
        renderHomeButton();
        renderPauseButton();
    }


    public void renderHomeButton() {
        game.batch.begin();
        game.batch.draw(currentHomeTexture, homeButton.x, homeButton.y, homeButton.width, homeButton.height);
        game.batch.end();
    }

    public void clickedHomeButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        currentHomeTexture = homeButtonClicked;
    }

    public void idleHomeButton() { currentHomeTexture = homeButtonIdle; }

    public Rectangle getHomeButton(){ return this.homeButton; }


    public void renderSoundButton(){
        game.batch.begin();
        game.batch.draw(currentSoundTexture, soundButton.x, soundButton.y, soundButton.width, soundButton.height);
        game.batch.end();
    }

    public void clickedSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffClickedTexture;
        } else {
            currentSoundTexture = soundOnClickedTexture;
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

    public void idleSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }
    }

    public Rectangle getSoundButton(){ return this.soundButton; }

    public void renderPauseButton() {
        game.batch.begin();
        game.batch.draw(currentPauseTexture, pauseButton.x, pauseButton.y, pauseButton.width, pauseButton.height);
        game.batch.end();
    }

    public void clickedPauseButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        if (gameScreen.getState().equals(State.PLAY)){
            currentPauseTexture = pauseButtonClicked;
        } else {
            currentPauseTexture = pauseButtonIdle;
        }
    }



    public void idlePauseButton() { currentPauseTexture = pauseButtonIdle; }

    public Rectangle getPauseButton(){ return this.pauseButton; }

    public void renderPauseScreenText(){
        game.batch.begin();
        game.biggestFont.draw(game.batch, "GAME PAUSED", this.x + 427, this.y - 60);
        game.bigFont.draw(game.batch, "Press 'P' or the Pause button", this.x + 417, this.y - 140);
        game.bigFont.draw(game.batch, "to return to game", this.x + 500, this.y - 170);
        game.batch.end();
    }

}
