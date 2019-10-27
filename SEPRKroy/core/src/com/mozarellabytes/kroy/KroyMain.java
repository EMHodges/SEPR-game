package com.mozarellabytes.kroy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class KroyMain extends ApplicationAdapter {
	// The SpriteBatch is a special class that is used to draw 2D images
	private SpriteBatch batch;
	private Texture img;
	private Texture bucketImage;
	private OrthographicCamera camera;

	private Rectangle bucket;

	// Method called once when the application is created.
	@Override
	public void create () {
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		img = new Texture("logo.png");
		bucketImage = new Texture("bucket.jpg");

		camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 32;
		bucket.height = 32;

		Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		Gdx.app.log("Log", "this is a log message");
	}

	// Method called by the game loop from the application every time rendering should be performed. Game logic updates are usually also performed in this method.
	@Override
	public void render () {
		camera.update();
		Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(img, Gdx.graphics.getWidth()/2 - img.getWidth()/2,Gdx.graphics.getHeight()/2 - img.getHeight()/2);
		batch.draw(bucketImage, Gdx.graphics.getWidth()/2 - bucketImage.getWidth()/2, 100, camera.viewportWidth/10, camera.viewportHeight/10);
		batch.end();
	}

	// Called when the application is destroyed. It is preceded by a call to pause().
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
