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
	Texture baloon;
	Texture menuBg;
	Texture bonusimg;
	Texture logoimg;
	Texture logotextD;
	Texture logotextU;
	Texture logotextO;
	Texture cemetery;
	Sound babah;
	Sound duo;
	Sound smile;
	Music muz;
	Rectangle b;
	Rectangle r;
	Rectangle g;
	Vector3 touchPos;
	Vector3 touchPos2;
	Vector3 touchPos3;
	Array<Rectangle> bombs;



	long lastbomb;

	float h = 480;//inaltimea
	float w = 800;//latimea
	float lat = 64;//latimea majoritatilor texturilor
	float v = 300;//viteza bombs
	float v2 = 170;//viteza players
	double per = 1000000000;//periodicitate pentru bombe
	int score = 0;
	boolean pauseMen;//pentru meniul de pauza
	int death = 0;//starea daca ai pierdut
	int deathscore = 0;//starea daca ai pornit pentru prima data jocul, pentru pauseMenu
	String scroreName;
	BitmapFont bitFont;//pentru afisarea cifrelor
	Rectangle pausebutton,playbutton,replaybutton,touch,bonus;
	Texture pauseTexture,playTexture,replayTexture;

	int i = 0;//periodicitate bonus


	@Override
	public void create () {
		death = 0;

		deathscore = 0;
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);

		//crearea obiectelor de fiecare tip
		batch = new SpriteBatch();
		blue = new Texture("blue.png");
		bombimg = new Texture("face.png");
		baloon = new Texture("baloons.png");
		red = new Texture("red.png");
		gold = new Texture("gold.png");
		menuBg = new Texture("menuBG.png");
		pauseTexture = new Texture("pause.png");
		playTexture = new Texture("play.png");
		replayTexture = new Texture("resume.png");
		bonusimg = new Texture("bonusimg.png");
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
		touch = new Rectangle();
		//sound
		smile = Gdx.audio.newSound(Gdx.files.internal("smile.wav"));
		babah = Gdx.audio.newSound(Gdx.files.internal("babah.mp3"));
		duo = Gdx.audio.newSound(Gdx.files.internal("2duo.mp3"));
		muz = Gdx.audio.newMusic(Gdx.files.internal("muz.mp3"));
		//setMuz
		muz.setLooping(true);
		muz.play();
		//dimensiuni butom play
		playbutton = new Rectangle();
		playbutton.x = w/2-90;
		playbutton.y = h/2-90;
		playbutton.width = 180;
		playbutton.height = 180;
		//dimensiuni butom replay
		replaybutton = new Rectangle();
		replaybutton.x =w/2+100;
		replaybutton.y = h/4-50;
		replaybutton.width = 128;
		replaybutton.height = 128;
		//dimensiuni bonus faces
		bonus= new Rectangle();
		bonus.x = MathUtils.random(0,800-64);
		bonus.y = h;
		bonus.width = 64;
		bonus.height = 64;
		//periodicitatea bonus-urilor
		i = 100;
		//dimensiuni blue instrument
		b = new Rectangle();
		b.x = w/2-lat;
		b.y = 30;
		b.width = lat;
		b.height = 16;
		//dimensiuni red instrument
		r = new Rectangle();
		r.x = w/2;
		r.y = 30;
		r.width = lat;
		r.height = 16;
		//dimensiuni atrubutelor impreuna(gold)
		g = new Rectangle();
		g.x = w/2-lat;
		g.y = 30;
		g.width = lat*2;
		g.height = 16;
		//initializarea Vectorilor
		touchPos = new Vector3();
		touchPos2 = new Vector3();
		touchPos3 = new Vector3();
		//setarea vitezelor, pereodicitate
		v = 500;
		v2 = 170;
		per = 2000000000;
		//score
		score = 0;
		scroreName = "0";
		bitFont = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);;

		//pause menu deodata
		pauseMen = true;
		//lista de rectangluri pentru bombe
		bombs = new Array<Rectangle>();
		bombspawn();

	}

	@Override
	public void resize(int width, int height) {

	}

	//crearea bombelor
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

		//startDraw
		batch.begin();

		//pentru diferentierea dintre pause-meniuri
		if(score!=0){deathscore = 1;}
		//marirea vitezei bombelor la scor
		if((score%200==0)&&(score!=0)){
			v2+=30;
			if(per>=1000000)per=per/1.5;
		}
		//bonus
		if((score>i)&&(score!=0)&&(!pauseMen)){

			if((bonus.overlaps(r))||(bonus.overlaps(b))){
				smile.play();
				bonus.y = h+20;
				score += 300;
				i=score+100;
				bonus.x = MathUtils.random(0,800-64);
			}
			if(bonus.y<=0){
				bonus.y = h+20;
				i=score+100;
				bonus.x = MathUtils.random(0,800-64);
			}
			bonus.y -= 100 * Gdx.graphics.getDeltaTime();
		}
		if((score>i)&&(score!=0)){
			batch.draw(bonusimg, bonus.x, bonus.y);
		}



		//bombe Iterator
		for(Rectangle bomb:bombs){
			batch.draw(bombimg,bomb.x,bomb.y);
		}

		if((TimeUtils.nanoTime()-lastbomb>per)&&(!pauseMen))bombspawn();

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

		//drow 2 instrumente
		batch.draw(blue, b.x, b.y);
		batch.draw(red, r.x,r.y);



		//touch controll
		if((Gdx.input.isTouched())&&(!pauseMen)){
			if((Gdx.input.isTouched(0))&&(!Gdx.input.isTouched(1))){
				camera.unproject(touchPos.set(Gdx.input.getX(0),Gdx.input.getY(0),0));
				if(touchPos.x<w/2){
					b.x -= v * Gdx.graphics.getDeltaTime()*2;
				}
				if(touchPos.x>w/2){
					r.x += v * Gdx.graphics.getDeltaTime()*2;
				}
			}
			if((Gdx.input.isTouched(1))&&(!Gdx.input.isTouched(0))){
				camera.unproject(touchPos.set(Gdx.input.getX(1),Gdx.input.getY(1),0));
				if(touchPos.x<w/2){
					b.x -= v * Gdx.graphics.getDeltaTime()*2;
				}
				if(touchPos.x>w/2){
					r.x += v * Gdx.graphics.getDeltaTime()*2;
				}

			}
			if((Gdx.input.isTouched(1))&&(Gdx.input.isTouched(0))){
				camera.unproject(touchPos.set(Gdx.input.getX(0),Gdx.input.getY(0),0));
				camera.unproject(touchPos2.set(Gdx.input.getX(1),Gdx.input.getY(1),0));
				if((touchPos.x<w/2)&&(touchPos2.x<w/2)){
					b.x -= v * Gdx.graphics.getDeltaTime()*2;
				}
				if((touchPos.x>w/2)&&(touchPos2.x>w/2)){
					r.x += v * Gdx.graphics.getDeltaTime()*2;
				}
				if(((touchPos.x<w/2)&&(touchPos2.x>w/2))||((touchPos.x>w/2)&&(touchPos2.x<w/2))){
					b.x -= v * Gdx.graphics.getDeltaTime()*2;
					r.x += v * Gdx.graphics.getDeltaTime()*2;
				}


			}
		}
		//keyControll blue
		if ((Gdx.input.isKeyPressed(Input.Keys.LEFT))&&(!pauseMen)) b.x -= v * Gdx.graphics.getDeltaTime();
		if ((!Gdx.input.isKeyPressed(Input.Keys.LEFT))&&(!pauseMen)) b.x += v * Gdx.graphics.getDeltaTime();
		//limitele ecran
		if (b.x < 0) b.x = 0;
		if (b.x > w / 2 - 64) b.x = w / 2 - 64;
		//keyControll red
		if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT))&&(!pauseMen)) r.x += v * Gdx.graphics.getDeltaTime();
		if ((!Gdx.input.isKeyPressed(Input.Keys.RIGHT))&&(!pauseMen)) r.x -= v * Gdx.graphics.getDeltaTime();
		//limitele ecran
		if (r.x < w / 2) r.x = w / 2;
		if (r.x > w - lat) r.x = w - lat;

		//goldDisplay, contor++
		if(((b.x == w/2-64)&&(r.x == w/2))&&(!pauseMen)){

			batch.draw(gold, g.x,g.y);
			duo.play();
			duo.loop(0);
			score++;
		}
		//display Score
		scroreName = ""+score;
		batch.draw(baloon, 5, h-64-10);
		bitFont.setColor(1.0f,0.65f,0.0f,1.0f);
		bitFont.getData().setScale(0.5f);
		bitFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		bitFont.getSpaceWidth();
		bitFont.draw(batch,scroreName,64/2-23,h-64-20);

		//PouseButton
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

		//PauseMenu
		if(pauseMen){
			muz.pause();
			batch.draw(menuBg, 0, 0);
			batch.draw(logoimg,w/2-32,40);
			batch.draw(logotextD,w/2-100,360);
			batch.draw(logotextU,w/2-40,360);
			batch.draw(logotextO,w/2+20,360);
			//doar la prima lansare
			if(death != 2)batch.draw(playTexture, playbutton.x, playbutton.y);
			//daca game over
			if(death == 2){
				batch.draw(cemetery,200,h/2-30);
				scroreName = "Your Score: "+score;
				bitFont.setColor(0.45f, 0.29f, 0.14f,0.75f);
				bitFont.getData().setScale(0.55f);

				bitFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
				bitFont.draw(batch,scroreName,w/2-256,h/2-60);//score in cazul in care am pierdut
			}
			//sau pause sau gameover
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
				}
			}
		}
		//sfirsitul desenarii
		batch.end();
	}
	@Override
	public void pause() {
		//activarea PauseMeni-ului la iesirea din program
		pauseMen=true;
	}
	@Override
	public void resume() {
	}
	@Override
	public void dispose() {

	}
}
