package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainClass implements ApplicationListener {
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture blue;
	Texture red;
	Texture gold;
	Rectangle b;
	Rectangle r;
	Rectangle g;
	Vector3 touchPos;
	Vector3 touchPos3;
	int death = 0;
	float h = 480;//inaltimea
	float w = 800;//latimea
	float lat = 64;//latimea majoritatilor texturilor
	float v = 300;//viteza bombs
	boolean pauseMen;
	Rectangle pausebutton,playbutton,replaybutton,touch,bonus;
	Texture pauseTexture,playTexture,replayTexture;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);
		death = 0;
		batch = new SpriteBatch();
		blue = new Texture("blue.png");
		red = new Texture("red.png");
		gold = new Texture("gold.png");
		pauseTexture = new Texture("pause.png");


		pausebutton = new Rectangle();
		pausebutton.x = w-70;
		pausebutton.y = h-70;
		pausebutton.width = 64;
		pausebutton.height = 64;


		b = new Rectangle();
		b.x = w/2-lat;
		b.y = 30;
		b.width = lat;
		b.height = 16;

		r = new Rectangle();
		r.x = w/2;
		r.y = 30;
		r.width = lat;
		r.height = 16;

		touchPos = new Vector3();

		touchPos3 = new Vector3();

		v = 500;

		pauseMen = true;

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);


		batch.begin();
		batch.draw(blue, b.x, b.y);
		batch.draw(red, r.x,r.y);


















		if(!pauseMen){

			batch.draw(pauseTexture, pausebutton.x, pausebutton.y);
			if(Gdx.input.isTouched()){
				camera.unproject(touchPos3.set(Gdx.input.getX(),Gdx.input.getY(),0));

				touch.x =touchPos3.x-8;
				touch.y = touchPos3.y-8;
				touch.width = 16;
				touch.height = 16;
				if(touch.overlaps(pausebutton)){
					pauseMen = true;
					death = 0;
				}


			}
		}






		batch.end();
	}

	@Override
	public void pause() {
		pauseMen=true;
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
}
