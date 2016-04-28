package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MainClass implements ApplicationListener {
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture blue;
	Texture red;
	Texture gold;
	Texture bombimg;
	Rectangle b;
	Rectangle r;
	Rectangle g;
	Texture bonusimg;
	Vector3 touchPos;
	Vector3 touchPos3;
	Sound babah;
	Music muz;
	int death = 0;
	float h = 480;//inaltimea
	float w = 800;//latimea
	float lat = 64;//latimea majoritatilor texturilor
	float v = 300;//viteza bombs
	float v2 = 170;//viteza players
	double per = 1000000000;
	int score = 0;
	String scroreName;
	BitmapFont bitFont;
	boolean pauseMen;
	Rectangle pausebutton,playbutton,replaybutton,touch,bonus;
	Texture pauseTexture,playTexture,replayTexture;
	Array<Rectangle> bombs;
	int deathscore = 0;

	long lastbomb;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);
		death = 0;
		deathscore = 0;
		batch = new SpriteBatch();
		blue = new Texture("blue.png");
		red = new Texture("red.png");
		gold = new Texture("gold.png");
		pauseTexture = new Texture("pause.png");
		bombimg = new Texture("face.png");


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
		muz.setLooping(true);
		muz.play();

		score = 0;
		scroreName = "0";
		bitFont = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);;

		pauseMen = true;
		bombs = new Array<Rectangle>();
		bombspawn();

	}

	@Override
	public void resize(int width, int height) {

	}

	private void bombspawn(){
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0,800-64);
		raindrop.y = 480;
		raindrop.width=64;
		raindrop.height = 90;
		bombs.add(raindrop);
		lastbomb= TimeUtils.nanoTime();
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


		for(Rectangle bomb:bombs){
			batch.draw(bombimg,bomb.x,bomb.y);
		}

		if((TimeUtils.nanoTime()-lastbomb>per)&&(!pauseMen))bombspawn();

		if(score!=0){deathscore = 1;}
		if((score%200==0)&&(score!=0)){
			v2+=50;
			if(per<=1000000)per=per/1.5;
		}


		Iterator<Rectangle> iter = bombs.iterator();
		while ((iter.hasNext())&&(pauseMen==false)){
			Rectangle bomb = iter.next();
			bomb.y-=v2*Gdx.graphics.getDeltaTime();
			if(bomb.y+64<0) iter.remove();
			if((bomb.overlaps(b))||(bomb.overlaps(r))) {
				babah.play();

				iter.remove();
				pauseMen=true;
				death = 2;
				///death = true;

			}
		};


		if ((Gdx.input.isKeyPressed(Input.Keys.LEFT))&&(!pauseMen)) b.x -= v * Gdx.graphics.getDeltaTime();
		if ((!Gdx.input.isKeyPressed(Input.Keys.LEFT))&&(!pauseMen)) b.x += v * Gdx.graphics.getDeltaTime();


		if (b.x < 0) b.x = 0;
		if (b.x > w / 2 - 64) b.x = w / 2 - 64;

		//red

		if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT))&&(!pauseMen)) r.x += v * Gdx.graphics.getDeltaTime();
		if ((!Gdx.input.isKeyPressed(Input.Keys.RIGHT))&&(!pauseMen)) r.x -= v * Gdx.graphics.getDeltaTime();

		if (r.x < w / 2) r.x = w / 2;
		if (r.x > w - lat) r.x = w - lat;

















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
