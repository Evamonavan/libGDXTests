package com.johnathongoss.libgdx.examples;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ImageCache;
import com.johnathongoss.libgdxtests.ParticleEffectsCache;
import com.johnathongoss.libgdxtests.screens.Examples;
import com.johnathongoss.libgdxtests.utils.MyTimer;

public class FishTank implements Screen {

	private Game game;
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	private MyTimer bubbleTimer;
	
	private float width, height, BUTTON_WIDTH, BUTTON_HEIGHT;
	private OrthographicCamera cam;
	private Stage stage, stageui;
	private SpriteBatch batch, batchui;
	
	public Array<Fish> fishes;
	public float gravity;
	public float friction;
	public float conservedEnergy;
	public double hardness;
	public float viscosity = 0.992f;
	Sprite tank, tank_shine;
	private  Array<PooledEffect> Effects;
	private Fish followedFish;

	private String testName;

	private TextButton backButton;
	public FishTank(Game game) {
		this.game = game;
		
		width = Gdx.app.getGraphics().getWidth();
		height = Gdx.app.getGraphics().getHeight();
		
		BUTTON_WIDTH = width/7;
		BUTTON_HEIGHT = height/8;
		
		tank = new Sprite(ImageCache.getTexture("tank"));
		tank.setPosition(0, 0);
		tank.setOrigin(0, 0);
		tank.setScale((float)Gdx.app.getGraphics().getWidth() / (float)tank.getRegionWidth(), (float)Gdx.app.getGraphics().getHeight() / (float)tank.getRegionHeight());

		tank_shine = new Sprite(ImageCache.getTexture("tank_shine"));
		tank_shine.setPosition(-20, -20);
		tank_shine.setOrigin(20, 20);
		tank_shine.setScale((float)Gdx.app.getGraphics().getWidth() / (float)tank.getRegionWidth(), (float)Gdx.app.getGraphics().getHeight() / (float)tank.getRegionHeight());

		testName = "Fish Tank Example |";
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		bubbleTimer.update(delta);
		
		if (following)
			followFish();

		batch.begin();
		tank.draw(batch);		
		batch.end();

		stage.act(delta);
		stage.draw();

		batch.begin();
		for (PooledEffect effect : Effects){
			effect.draw(batch, delta);
			if (effect.isComplete()){
				Effects.removeValue(effect, true);
				effect.reset();
				effect.free();					
			}
		}
		tank_shine.draw(batch);
		batch.end();

		stageui.act(delta);
		stageui.draw();

		batchui.begin();
		Assets.font24.drawMultiLine(batchui, testName, 0, 24, width, HAlignment.RIGHT);
		batchui.end();

		cam.update();
		batch.setProjectionMatrix(cam.combined);

	}

	private void followFish() {

		cam.zoom = 0.5f;
		cam.position.x = followedFish.getX();
		cam.position.y = followedFish.getY();

	}	

	@Override
	public void show() {	
		
		bubbleTimer = new MyTimer(1f) {
			
			@Override
			protected void perform() {
				createBubbles();
				setCap(MathUtils.random(0.5f, 1f));
				
			}
		};
		bubbleTimer.setRepeating(true);
		bubbleTimer.start();
		
		batch = new SpriteBatch();
		batchui = new SpriteBatch();
		
		stage = new Stage();
		stageui = new Stage();
		
		width = Gdx.app.getGraphics().getWidth();
		height = Gdx.app.getGraphics().getHeight();
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width, height);
		cam.update();   
		stage.setCamera(cam);
		
		InputMultiplexer im = new InputMultiplexer(stageui, stage);
		Gdx.input.setInputProcessor(im);	
		
		Effects = new Array<PooledEffect>();
		cam.zoom = 1.4f;
		gravity = 0;
		friction = -0.1f;
		hardness = 1f;
		conservedEnergy = 0.5f;
		viscosity = 0.99f;

		backButton = new TextButton("Examples", skin);
		backButton.setHeight(BUTTON_HEIGHT);
		backButton.setWidth(BUTTON_WIDTH);
		backButton.setPosition(width - BUTTON_WIDTH, height - BUTTON_HEIGHT);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Examples(game));
			}
		});	

		stageui.addActor(backButton);	

		fishes = new Array<Fish>();

		for (int i = 0; i < 10; i++) {
			fishes.add(new Fish(MathUtils.random(width), MathUtils.random(height), MathUtils.random(width/15, width/10), i, fishes));
			fishes.get(i).setVelocity(MathUtils.random(-1.2f, 1.2f), MathUtils.random(-1.2f, 1.2f));
			fishes.get(i).changeColor();
			stage.addActor(fishes.get(i));
		}
	}


	public void createBubbles() {

		Effects.add(ParticleEffectsCache.getParticleEffect(ParticleEffectsCache.BUBBLES));
		Effects.get(Effects.size - 1).setPosition(MathUtils.random(50, width - 50), 0);

	}
	private boolean following = false;

	private void resetCamera(){
		cam.position.x = width/2;
		cam.position.y = height/2;
		cam.zoom = 1.4f;		
	}

	public class Fish extends Actor {

		MyTimer timer;
		Fish thisFish;
		public float diameter;
		float vx = 0;
		float vy = 0;
		int id;
		Color color = Color.WHITE;
		Array<Fish> others;
		Sprite sprite = new Sprite(ImageCache.getTexture("fish"));
		
		private float ySca = 1f;
		public Fish(float xin, float yin, float din, int idin, Array<Fish> fishes) {
			thisFish = this;
			timer = new MyTimer(MathUtils.random(1f, 8f)) {				

				@Override 
				protected void perform() {
					changeDirection(1f);				

					timer.setCap(MathUtils.random(3.5f, 9f));
				}

			};
			timer.start();
			timer.setRepeating(true);

			setX(xin);
			setY(yin);
			diameter = din;
			id = idin;
			others = fishes;	

			setBounds(getX() - diameter/2, getY() - diameter/2, diameter,diameter);
			setOrigin(diameter/2, diameter/2);
			setScale(1f);

			addListener(new ActorGestureListener(){
				public void fling(InputEvent event, float velocityX, float velocityY, int button){

				}

				public void tap(InputEvent event, float x, float y, int count, int button){
					if (!following){
						followedFish = thisFish;
						following = true;
					}
					else if (following && thisFish != followedFish){

						followedFish = thisFish;
						following = true;

					}

					else if (following && thisFish == followedFish){
						resetCamera();
						following = false;
					}
				}
			});

			setTouchable(Touchable.enabled);
		} 
		private void changeDirection(float power) {

			vx += MathUtils.random(-2f*power, 2f*power);
			vy += MathUtils.random(-0.6f*power, 0.6f*power);				

		}
		private void calcRotation() {
			float rot = MathUtils.atan2(vy, vx)*180/MathUtils.PI;

			if (rot < 0)
				rot += 360;			
		
			if (rot > 90 && rot < 270)
				ySca = -1f;
			else
				ySca = 1f;
			
			setRotation(rot);

		}
		Color tempColor;
		public void changeColor() {			
			setColor(new Color(MathUtils.random(0f, 1f), MathUtils.random(0f, 1f), MathUtils.random(0f, 1f), 1f));
		}

		public void Fling(float velocityX, float velocityY) {
			vx = velocityX*0.01f;
			vy = velocityY*0.01f;
		}		

		public float getXOffset(){
			return getX() + diameter/2;			
		}
		public float getYOffset(){
			return getY() + diameter/2;			
		}

		void collide(float delta) {
			for (int i = id + 1; i < fishes.size; i++) {
				float dx = others.get(i).getXOffset() - getXOffset();
				float dy = others.get(i).getYOffset()  - getYOffset();
				double distance = Math.sqrt(dx*dx + dy*dy);
				float minDist = others.get(i).diameter/2 + diameter/2;
				if (distance < minDist) { 
					changeDirection(1.5f);
					others.get(i).changeDirection(1.5f);
					vx *= conservedEnergy;
					vy *= conservedEnergy;
					others.get(i).vx *= conservedEnergy;
					others.get(i).vy *= conservedEnergy;
					double angle = Math.atan2(dy, dx);
					double targetX = getXOffset() + Math.cos(angle) * minDist;
					double targetY = getYOffset() + Math.sin(angle) * minDist;
					double ax = (targetX - others.get(i).getXOffset()) * hardness;
					double ay = (targetY - others.get(i).getYOffset()) * hardness;
					vx -= ax;
					vy -= ay;
				}
			}   
		}

		void move(float delta) {
			vy *= viscosity ;
			vx *= viscosity;

			if (vx > 2f)
				vx = 2f;
			if (vy > 2f)
				vy = 2f;

			addX(vx);
			addY(vy);

			if (getXOffset() + diameter/2> width) {
				setX(width - diameter);
				vx *= friction; 
			}
			else if (getXOffset() - diameter/2 < 0) {
				setX(0);
				vx *= friction;
			}
			if (getYOffset() + diameter/2> height) {
				setY(height - diameter);
				vy *= friction; 
			} 
			else if (getYOffset() - diameter/2 < 0) {
				setY(0);
				vy *= friction;
			}
		}

		public void addX(float amount){			
			setX(getX() + amount);			
		}
		public void addY(float amount){			
			setY(getY() + amount);			
		}
		
		@Override
		public void draw(Batch batch, float alpha) {
			batch.setColor(getColor());
			batch.draw(sprite, getX(), getY(), 
					getOriginX(), getOriginY(), 				
					getWidth(), getHeight(), 
					-getScaleX(), ySca*getScaleY(),
					getRotation());
			batch.setColor(Color.WHITE);			
		}

		@Override
		public void act(float delta){
			super.act(delta);
			timer.update(delta);
			collide(delta);
			move(delta);
			calcRotation();
		}

		public void setVelocity(float vx, float vy) {
			this.vx = vx;
			this.vy = vy;

		}

		public void setDiameter(float diameter) {			
			this.diameter = diameter;
			setBounds(getX() - diameter/2, getY() - diameter/2, diameter,diameter);
			setOrigin(diameter/2, diameter/2);
			setScale(1f);

		}
	}

	@Override
	public void resize(int width, int height) {
		this.width = Gdx.app.getGraphics().getWidth();
		this.height = Gdx.app.getGraphics().getHeight();
		BUTTON_WIDTH = width/7;
		BUTTON_HEIGHT = height/8;
		stage.setCamera(cam);
		Gdx.gl.glViewport(0, 0, width, height);	
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		stage.setViewport(width, height, false);
		stageui.setViewport(width, height, false);
		
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		batch.dispose();
		batchui.dispose();
		stage.dispose();
		stageui.dispose();
		game.dispose();		
	}

}
