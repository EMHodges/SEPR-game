package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.MenuInputHandler;
import com.mozarellabytes.kroy.Utilities.ScreenHandler;
import com.mozarellabytes.kroy.Utilities.SoundFX;

public class MenuScreen implements Screen {

    private final Kroy game;
    public OrthographicCamera camera;
    private Texture backgroundImage;

    private Rectangle playButton;
    private Texture playIdleTexture;
    private Texture playClickedTexture;
    private Texture currentPlayTexture;

    private Rectangle controlsButton;
    private Texture controlsIdleTexture;
    private Texture controlsClickedTexture;
    private Texture currentControlsTexture;

    private Rectangle soundButton;
    private Texture soundOnIdleTexture;
    private Texture soundOffIdleTexture;
    private Texture soundOnClickedTexture;
    private Texture soundOffClickedTexture;
    private Texture currentSoundTexture;

    private MenuInputHandler ih;

    public MenuScreen(final Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        backgroundImage = new Texture(Gdx.files.internal("menuscreen_blank_2.png"), true);
        backgroundImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        playIdleTexture = new Texture(Gdx.files.internal("ui/start_idle.png"), true);
        playIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        playClickedTexture = new Texture(Gdx.files.internal("ui/start_clicked.png"), true);
        playClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        controlsIdleTexture = new Texture(Gdx.files.internal("ui/controls_idle.png"), true);
        controlsIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        controlsClickedTexture = new Texture(Gdx.files.internal("ui/controls_clicked.png"), true);
        controlsClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        soundOnIdleTexture = new Texture(Gdx.files.internal("ui/sound_on_idle.png"), true);
        soundOnIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffIdleTexture = new Texture(Gdx.files.internal("ui/sound_off_idle.png"), true);
        soundOffIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOnClickedTexture = new Texture(Gdx.files.internal("ui/sound_on_clicked.png"), true);
        soundOnClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffClickedTexture = new Texture(Gdx.files.internal("ui/sound_off_clicked.png"), true);
        soundOffClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        ih = new MenuInputHandler(this);

        if (SoundFX.music_enabled) {
            SoundFX.sfx_menu.setLooping(true);
            SoundFX.sfx_menu.play();
        }

        currentPlayTexture = playIdleTexture;
        currentControlsTexture = controlsIdleTexture;

        if (SoundFX.music_enabled) {
            currentSoundTexture = soundOnIdleTexture;
        } else {
            currentSoundTexture = soundOffIdleTexture;
        }

        playButton = new Rectangle();
        playButton.width = (float) (currentPlayTexture.getWidth()*0.75);
        playButton.height = (float) (currentPlayTexture.getHeight()*0.75);
        playButton.x = (int) (camera.viewportWidth/2 - playButton.width/2);
        playButton.y = (int) ((camera.viewportHeight/2 - playButton.height/2) * 0.8);

        controlsButton = new Rectangle();
        controlsButton.width = (float) (currentControlsTexture.getWidth()*0.75);
        controlsButton.height = (float) (currentControlsTexture.getHeight()*0.75);
        controlsButton.x = (int) (camera.viewportWidth/2 - controlsButton.width/2);
        controlsButton.y = (int) ((camera.viewportHeight/2 - controlsButton.height/2)*0.4);

        soundButton = new Rectangle();
        soundButton.width = 50;
        soundButton.height = 50;
        soundButton.x = camera.viewportWidth - soundButton.getWidth() - 5;
        soundButton.y = camera.viewportHeight - soundButton.getHeight() - 5;

        Gdx.input.setInputProcessor(ih);

    }

    public void toGameScreen() {
        game.setScreen(new GameScreen(game));
        this.dispose();
    }

    public Rectangle getPlayButton() {
        return playButton;
    }

    public Rectangle getControlsButton() {
        return controlsButton;
    }

    public Rectangle getSoundButton() {return soundButton; }

    public void clickedPlayButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        currentPlayTexture = playClickedTexture;
    }

    public void clickedControlsButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        currentControlsTexture = controlsClickedTexture;
    }

    public void clickedSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOnClickedTexture;
        } else {
            currentSoundTexture = soundOffClickedTexture;
        }
    }

    public void changeSound() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffIdleTexture;
            SoundFX.StopMusic();
        } else {
            currentSoundTexture = soundOnIdleTexture;
            SoundFX.PlayMenuMusic();
        }
    }

    public void idlePlayButton() {
        currentPlayTexture = playIdleTexture;
    }

    public void idleControlsButton() {
        currentControlsTexture = controlsIdleTexture;
    }

    public void idleSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOnIdleTexture;
        } else {
            currentSoundTexture = soundOffIdleTexture;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.batch.draw(currentPlayTexture, playButton.x, playButton.y, playButton.width, playButton.height);
        game.batch.draw(currentControlsTexture, controlsButton.x, controlsButton.y, controlsButton.width, controlsButton.height);
        game.batch.draw(currentSoundTexture, soundButton.x, soundButton.y, soundButton.width, soundButton.height);
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
        backgroundImage.dispose();
        currentPlayTexture.dispose();
        playClickedTexture.dispose();
        playIdleTexture.dispose();
        currentControlsTexture.dispose();
        controlsClickedTexture.dispose();
        controlsIdleTexture.dispose();
        currentSoundTexture.dispose();
        soundOnIdleButton.dispose();
        soundOnClickedButton.dispose();
        soundOffIdleButton.dispose();
        soundOffClickedButton.dispose();
        SoundFX.sfx_menu.stop();
    }

    public void toControlScreen(){ ScreenHandler.ToControls(game, this, "menu"); }
}
