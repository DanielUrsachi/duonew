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
	Texture menuBg;
	Texture logoimg;
	Texture logotextD;
	Texture logotextU;
	Texture logotextO;
	Texture cemetery;
	Rectangle b;
	Rectangle r;
	Rectangle g;
	Texture bonusimg;
	Vector3 touchPos;
	Vector3 touchPos3;
	Sound babah;
	Sound duo;
	Sound smile;
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

		menuBg = new Texture("menuBG.png");
		pauseTexture = new Texture("pause.png");
		playTexture = new Texture("play.png");
		replayTexture = new Texture("resume.png");
		logoimg = new Texture("logoimg.png");
		logotextD = new Texture("logoD.png");
		logotextU = new Texture("logoU.png");
		logotextO = new Texture("logoO.png");
		cemetery = new Texture("cemetery.png");
		pausebutton = new Rectangle();
		pausebutton.x = w-70;
		pausebutton.y = h-70;
		pausebutton.width = 64;
		pausebutton.height = 64;


		playbutton = new Rectangle();
		playbutton.x = w/2-90;
		playbutton.y = h/2-90;
		playbutton.width = 180;
		playbutton.height = 180;

		replaybutton = new Rectangle();
		replaybutton.x =w/2+100;
		replaybutton.y = h/4-50;
		replaybutton.width = 128;
		replaybutton.height = 128;

		smile = Gdx.audio.newSound(Gdx.files.internal("smile.wav"));
		babah = Gdx.audio.newSound(Gdx.files.internal("babah.mp3"));
		duo = Gdx.audio.newSound(Gdx.files.internal("2duo.mp3"));
		muz = Gdx.audio.newMusic(Gdx.files.internal("muz.mp3"));

		muz.setLooping(true);
		muz.play();


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
















		scroreName = ""+score;
		batch.draw(baloon, 5, h-64-10);
		bitFont.setColor(1.0f,0.65f,0.0f,1.0f);
		bitFont.getData().setScale(0.5f);
		bitFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		bitFont.getSpaceWidth();
		bitFont.draw(batch,scroreName,64/2-23/*-(score/1000*10)*/,h-64-20)
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
		if(pauseMen){
			muz.pause();
			batch.draw(menuBg, 0, 0);
			batch.draw(logoimg,w/2-32,40);
			batch.draw(logotextD,w/2-100,360);
			batch.draw(logotextU,w/2-40,360);
			batch.draw(logotextO,w/2+20,360);
			if(death != 2)batch.draw(playTexture, playbutton.x, playbutton.y);
			if(death == 2){
				batch.draw(cemetery,200,h/2-30);
				scroreName = "Your Score: "+score;
				bitFont.setColor(0.45f, 0.29f, 0.14f,0.75f);
				bitFont.getData().setScale(0.55f);

				bitFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
				bitFont.draw(batch,scroreName,w/2-256,h/2-60);//score in cazul in care am pierdut
			}
			if(deathscore == 1)batch.draw(replayTexture, replaybutton.x, replaybutton.y);
			if(Gdx.input.isTouched()){
				camera.unproject(touchPos3.set(Gdx.input.getX(),Gdx.input.getY(),0));

				touch.x =touchPos3.x-8;
				touch.y = touchPos3.y-8;
				touch.width = 16;
				touch.height = 16;
				if((touch.overlaps(playbutton))&&(death != 2)){
					muz.play();
					pauseMen = false;
				}
				if((touch.overlaps(replaybutton))&&(deathscore == 1)){
					muz.play();
					bonus.y = h+20;//pentru bonus
					i = 100;//pentru bonus

					Iterator<Rectangle> iter2 = bombs.iterator();
					while (iter2.hasNext()){
						Rectangle bomb = iter2.next();


						iter2.remove();

					}
					v = 500;
					v2 = 170;
					per = 2000000000;

					pauseMen = false;


					score = 0;
					death = 2;
					//


					//

					//create();
					//iter.remove();

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
