package com.mozarellabytes.kroy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class KroyMain extends ApplicationAdapter {

	// The SpriteBatch is a special class that is used to draw 2D images
	private SpriteBatch batch;
	private Texture logo;
	private Texture donutImage;
	private OrthographicCamera camera;
	private Sound errorSound;
	private Rectangle donut;
	private ShapeRenderer sr;

	// Method called once when the application is created.
	@Override
	public void create () {
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		logo = new Texture("logo.png");
		donutImage = new Texture("donut.png");
		errorSound = Gdx.audio.newSound(Gdx.files.internal("error.mp3"));

		Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

		camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

		donut = new Rectangle();
		donut.x = Gdx.graphics.getDisplayMode().width/2 - donutImage.getWidth()/4;
		donut.y = 200;
		donut.width = donutImage.getWidth()/2;
		donut.height = donutImage.getHeight()/2;

	}

	// Method called by the game loop from the application every time rendering should be performed. Game logic updates are usually also performed in this method.
	@Override
	public void render () {

		Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		// movement input for donut
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			Gdx.app.log("Location", String.valueOf(donut.x));
			if (donut.x > 0) {
				donut.x -= 200 * Gdx.graphics.getDeltaTime();
			} else {
				errorSound.play();
			}
		}

		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			Gdx.app.log("Location", String.valueOf(donut.x));
			if (donut.x + donut.width < camera.viewportWidth) {
				donut.x += 200 * Gdx.graphics.getDeltaTime();
			} else {
				errorSound.play();
			}
		}

		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			Gdx.app.log("Location", String.valueOf(donut.y));
			if (donut.y + donut.height < camera.viewportHeight) {
				donut.y += 200 * Gdx.graphics.getDeltaTime();
			} else {
				errorSound.play();
			}
		}

		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			Gdx.app.log("Location", String.valueOf(donut.y));
			if (donut.y > 0) {
				donut.y -= 200 * Gdx.graphics.getDeltaTime();
			} else {
				errorSound.play();
			}
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(logo, camera.viewportWidth/2 - logo.getWidth()/2, (float) (camera.viewportHeight*0.9 - logo.getHeight()*0.9), logo.getWidth(), logo.getHeight());
		batch.draw(donutImage, donut.x, donut.y, donut.width, donut.height);
		batch.end();
	}

	// Called when the application is destroyed. It is preceded by a call to pause().
	@Override
	public void dispose () {
		batch.dispose();
		logo.dispose();
	}
}
