package com.mozarellabytes.kroy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class KroyMain extends ApplicationAdapter {

	// The SpriteBatch is a special class that is used to draw 2D images
	private SpriteBatch batch;
	private Texture img;
	private Texture donutImage;
	private OrthographicCamera camera;

	private Rectangle donut;

	// Method called once when the application is created.
	@Override
	public void create () {
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		img = new Texture("logo.png");
		donutImage = new Texture("donut.png");

		camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width/2, Gdx.graphics.getDisplayMode().height/2);

		donut = new Rectangle();
		donut.x = Gdx.graphics.getDisplayMode().width/2 - donutImage.getWidth()/2;
		donut.y = 20;
		donut.width = 32;
		donut.height = 32;

		Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width/2, Gdx.graphics.getDisplayMode().height/2);
		Gdx.app.log("Log", "this is a log message");
	}

	// Method called by the game loop from the application every time rendering should be performed. Game logic updates are usually also performed in this method.
	@Override
	public void render () {
		Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(img, Gdx.graphics.getWidth()/2 - img.getWidth()/2,Gdx.graphics.getHeight()/2 - img.getHeight()/2);
		batch.draw(donutImage, donut.x, donut.y, camera.viewportWidth/10, camera.viewportHeight/10);
		batch.end();

		// movement input for donut
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) donut.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) donut.x += 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) donut.y += 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) donut.y -= 200 * Gdx.graphics.getDeltaTime();

	}

	// Called when the application is destroyed. It is preceded by a call to pause().
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
