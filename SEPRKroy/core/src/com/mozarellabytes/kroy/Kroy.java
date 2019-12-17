package com.mozarellabytes.kroy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mozarellabytes.kroy.Screens.*;

public class Kroy extends Game {

	public SpriteBatch batch;
	public BitmapFont bigFont;
	public BitmapFont smallFont;

	// Method called once when the application is created.
	@Override
	public void create () {
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		batch = new SpriteBatch();

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Magero.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 30;
		bigFont = generator.generateFont(parameter);
		parameter.size = 19;
		smallFont = generator.generateFont(parameter);

		this.setScreen(new MenuScreen(this));

//		this.setScreen(new ControlsScreen(this));
		//this.setScreen(new SplashScreen(this));
	//	this.setScreen(new GameScreen(this));
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
		bigFont.dispose();
	}
}
