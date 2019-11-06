package com.mozarellabytes.kroy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mozarellabytes.kroy.Screen.MainMenuScreen;

public class Kroy extends Game {

	public SpriteBatch batch;
	public BitmapFont font;

	// Method called once when the application is created.
	@Override
	public void create () {
		//Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.getData().setScale(2);
		this.setScreen(new MainMenuScreen(this));
	}

	// Method called by the game loop from the application every time rendering should be performed. Game logic updates are usually also performed in this method.
	@Override
	public void render () {
		super.render();
	}

	// Called when the application is destroyed. It is preceded by a call to pause().
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
