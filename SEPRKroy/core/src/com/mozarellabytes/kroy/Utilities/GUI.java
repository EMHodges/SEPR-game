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
import com.mozarellabytes.kroy.Screens.State;

public class GUI {

    private final Kroy game;
    private final int selectedX, selectedY, h, w;
    private final GameScreen gameScreen;

    private Rectangle homeButton;
    private final Texture homeButtonIdle;
    private final Texture homeButtonClicked;
    private Texture currentHomeTexture;

    private Rectangle pauseButton;
    private final Texture pauseButtonIdle;
    private final Texture pauseButtonClicked;
    private Texture currentPauseTexture;

    private Rectangle soundButton;
    private final Texture soundOnIdleTexture;
    private final Texture soundOffIdleTexture;
    private final Texture soundOnClickedTexture;
    private final Texture soundOffClickedTexture;
    private Texture currentSoundTexture;

    public GUI(Kroy game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.h = 275;
        this.w = 275;
        this.selectedX = 10;
        this.selectedY = Gdx.graphics.getHeight() - 10 - 275;

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
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            renderBackground();
            game.shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            // if fire truck is selected
            if (entity instanceof FireTruck) {
                FireTruck truck = (FireTruck) entity;
                renderTruck(truck);
            } else if (entity instanceof Fortress) {
                Fortress fortress = (Fortress) entity;
                renderFortress(fortress);
            }
            game.shapeRenderer.end();
        }
    }

    private void renderBackground() {
        game.shapeRenderer.setColor(0, 0, 0, 0.5f);
        game.shapeRenderer.rect(selectedX, selectedY, 275, 275);
    }

    private void renderTruck(FireTruck truck) {
        // also render text stats along left side
        renderBar(truck.getHP(), truck.getType().getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderBar(truck.getReserve(), truck.getType().getMaxReserve(), Color.CYAN, Color.BLUE, 2);
        renderText(truck);
    }

    private void renderFortress(Fortress fortress) {
        renderBar(fortress.getHP(), fortress.getFortressType().getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderText(fortress);
    }

    private void renderText(FireTruck truck) {
        FireTruckType truckType = truck.getType();
        int newLine = 20;
        game.batch.begin();
        game.font26.draw(game.batch, truckType.getName(), this.selectedX + 10, this.selectedY + this.h - 10);
        game.font19.draw(game.batch, "HP: ", this.selectedX + 15, this.selectedY + this.h - 50);
        game.font19.draw(game.batch, String.format("%.1f", truck.getHP()) + " / " + String.format("%.1f", truckType.getMaxHP()), this.selectedX + 20, this.selectedY + this.h - 50 - newLine);
        game.font19.draw(game.batch, "Reserve: ", this.selectedX + 15, this.selectedY + this.h - 50 - newLine*2);
        game.font19.draw(game.batch, String.format("%.1f", truck.getReserve()) + " / " + String.format("%.1f", truckType.getMaxReserve()), this.selectedX + 20, this.selectedY + this.h - 50 - newLine*3);
        game.font19.draw(game.batch, "Speed: ", this.selectedX + 15, this.selectedY + this.h - 50 - newLine*4);
        game.font19.draw(game.batch, String.format("%.1f", truckType.getSpeed()), this.selectedX + 20, this.selectedY + this.h - 50 - newLine*5);
        game.font19.draw(game.batch, "Range: ", this.selectedX + 15, this.selectedY + this.h - 50 - newLine*6);
        game.font19.draw(game.batch, String.format("%.1f", truckType.getRange()), this.selectedX + 20, this.selectedY + this.h - 50 - newLine*7);
        game.font19.draw(game.batch, "AP: ", this.selectedX + 15, this.selectedY + this.h - 50 - newLine*8);
        game.font19.draw(game.batch, String.format("%.2f", truckType.getAP()), this.selectedX + 20, this.selectedY + this.h - 50 - newLine*9);
        game.batch.end();
    }

    private void renderText(Fortress fortress) {
        int newLine = 20;
        FortressType fortressType = fortress.getFortressType();
        game.batch.begin();
        game.font26.draw(game.batch, fortressType.getName(), this.selectedX + 10, this.selectedY + this.h - 10);
        game.font19.draw(game.batch, "HP: ", this.selectedX + 15, this.selectedY + this.h - 50);
        game.font19.draw(game.batch, String.format("%.1f", fortress.getHP()) + " / " + String.format("%.1f", fortressType.getMaxHP()), this.selectedX + 20, this.selectedY + this.h - 50 - newLine);
        game.font19.draw(game.batch, "Range: ", this.selectedX + 15, this.selectedY + this.h - 50 - newLine*2);
        game.font19.draw(game.batch, String.format("%.1f", fortressType.getRange()), this.selectedX + 20, this.selectedY + this.h - 50 - newLine*3);
        game.font19.draw(game.batch, "AP: ", this.selectedX + 15, this.selectedY + this.h - 50 - newLine*4);
        game.font19.draw(game.batch, String.format("%.2f", fortressType.getAP()), this.selectedX + 20, this.selectedY + this.h - 50 - newLine*5);
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
        game.shapeRenderer.rect(this.selectedX + this.w - positionSpacer - outerSpacing - barSpacer, this.selectedY + outerSpacing, whiteW, this.h - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        game.shapeRenderer.rect(this.selectedX + this.w - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, whiteW - innerSpacing*2, barHeight, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
        game.shapeRenderer.rect(this.selectedX + this.w - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, whiteW - innerSpacing*2, value/maxValue*barHeight, progressColour, progressColour, progressColour, progressColour);
    }

    public void renderTruckBars(FireTruck truck, ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeMapRenderer.rect(truck.getPosition().x + 0.2f, truck.getPosition().y + 1.3f, 0.6f, 0.8f);
        shapeMapRenderer.rect(truck.getPosition().x + 0.266f, truck.getPosition().y + 1.4f, 0.2f, 0.6f, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
        shapeMapRenderer.rect(truck.getPosition().x + 0.266f, truck.getPosition().y + 1.4f, 0.2f, (float) truck.getReserve() / (float) truck.type.getMaxReserve() * 0.6f, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
        shapeMapRenderer.rect(truck.getPosition().x + 0.533f, truck.getPosition().y + 1.4f, 0.2f, 0.6f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(truck.getPosition().x + 0.533f, truck.getPosition().y + 1.4f, 0.2f, (float) truck.getHP() / (float) truck.type.getMaxHP() * 0.6f, Color.RED, Color.RED, Color.RED, Color.RED);
        shapeMapRenderer.end();
    }

    public void renderButtons(){
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
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        if (gameScreen.getState().equals(State.PLAY)){
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
        game.font26.draw(game.batch, "Press 'ESC' or the Pause button", this.selectedX + 417, this.selectedY - 140);
        game.font26.draw(game.batch, "to return to game", this.selectedX + 500, this.selectedY - 170);
        game.batch.end();
    }

}
