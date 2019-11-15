package com.mozarellabytes.kroy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mozarellabytes.kroy.Screens.MenuScreen;
import com.mozarellabytes.kroy.Screens.SplashScreen;

public class Kroy extends Game {

	public SpriteBatch batch;
	public BitmapFont font;

	// Method called once when the application is created.
	@Override
	public void create () {
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		batch = new SpriteBatch();
		this.setScreen(new SplashScreen(this));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Magero.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 30;
		font = generator.generateFont(parameter);
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
