package com.johnathongoss.libgdxtests.examples;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ImageCache;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.ParticleCache;
import com.johnathongoss.libgdxtests.entities.MyTimer;
import com.johnathongoss.libgdxtests.screens.Examples;

public class FishTank implements Screen {

	MyInputProcessor input = new MyInputProcessor();

	/*
	 * Essentials
	 */

	private MyGame game;
	private String testName;
	private OrthographicCamera cam;
	private Stage stage, stageui;
	private SpriteBatch batch, batchui;

	/*
	 * Assets
	 */

	private TextButton backButton;
	private MyTimer bubbleTimer;	// TODO Make this Gdx.timer
	private  Array<PooledEffect> Effects;
	Sprite tank, tank_shine;

	public Array<Fish> fishes;
	public float gravity;
	public float friction;
	public float conservedEnergy;
	public double hardness;
	public float viscosity = 0.992f;	

	private Fish followedFish;

	public FishTank(MyGame game) {
		this.game = game;

		/*
		 * Initiate Variables
		 */
		
		testName = "Fish Tank Example |";

		batch = new SpriteBatch();
		batchui = new SpriteBatch();

		stage = new Stage();
		stageui = new Stage();
		cam = new OrthographicCamera();

		Effects = new Array<PooledEffect>();

		fishes = new Array<Fish>();		

		tank = new Sprite(ImageCache.getTexture("tank"));		
		tank_shine = new Sprite(ImageCache.getTexture("tank_shine"));				
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		bubbleTimer.update(delta);

		if (following)
			followFish();

		cam.update();
		batch.setProjectionMatrix(cam.combined);

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
		Assets.font24.drawMultiLine(batchui, testName, 0, 24, game.getWidth(), HAlignment.RIGHT);
		batchui.end();		
	}	

	@Override
	public void show() {	
		InputMultiplexer im = new InputMultiplexer(stageui, stage, input);
		Gdx.input.setInputProcessor(im);		
		Gdx.input.setCatchBackKey(true);		

		tank.setPosition(0, 0);
		tank.setOrigin(0, 0);
		tank.setScale(game.getWidth() / (float)tank.getRegionWidth(), (float)Gdx.app.getGraphics().getHeight() / (float)tank.getRegionHeight());

		tank_shine.setPosition(-20, -20);
		tank_shine.setOrigin(20, 20);
		tank_shine.setScale(game.getWidth() / (float)tank.getRegionWidth(), (float)Gdx.app.getGraphics().getHeight() / (float)tank.getRegionHeight());

		bubbleTimer = new MyTimer(1f) {

			@Override
			protected void perform() {
				createBubbles();
				setCap(MathUtils.random(0.4f, 1f));

			}
		};
		bubbleTimer.setRepeating(true);
		bubbleTimer.start();		

		cam.setToOrtho(false, game.getWidth(), game.getHeight());
		cam.update();   
		stage.setCamera(cam);		

		cam.zoom = 1.4f;
		gravity = 0;
		friction = -0.1f;
		hardness = 1f;
		conservedEnergy = 0.5f;
		viscosity = 0.99f;

		backButton = new TextButton("Back", Assets.skin);
		backButton.setBounds(0, game.getHeight() - game.getButtonHeight(), game.getWidth(), game.getButtonHeight());
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Examples(game));
			}
		});	

		stageui.addActor(backButton);			

		for (int i = 0; i < 8; i++) {
			fishes.add(new Fish(MathUtils.random(game.getWidth()), MathUtils.random(game.getHeight()), MathUtils.random(game.getWidth()/15, game.getWidth()/10), i, fishes));
			fishes.get(i).setVelocity(MathUtils.random(-1.2f, 1.2f), MathUtils.random(-1.2f, 1.2f));
			fishes.get(i).changeColor();
			stage.addActor(fishes.get(i));
		}
		
		//TODO check fish positions and prevent overlap bug
	}
	
	private void followFish() {
		cam.zoom = 0.5f;
		cam.position.x = followedFish.getX();
		cam.position.y = followedFish.getY();
	}	

	public void createBubbles() {

		Effects.add(ParticleCache.getParticleEffect(ParticleCache.BUBBLES));
		Effects.get(Effects.size - 1).setPosition(MathUtils.random(50, game.getWidth() - 50), 0);

	}
	private boolean following = false;

	private void resetCamera(){
		cam.position.x = game.getWidth()/2;
		cam.position.y = game.getHeight()/2;
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
			animate();

			vx += MathUtils.random(-2f*power, 2f*power);
			vy += MathUtils.random(-0.6f*power, 0.6f*power);				

		}
		private void animate() {
			if (getScaleX() == 1f){
				addAction(Actions.sequence(Actions.scaleTo(.4f, 1f, 0.15f), Actions.scaleTo(1f, 1f, 0.15f)));
			}
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
					changeDirection(2f);
					others.get(i).changeDirection(1f);
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

			if (getXOffset() + diameter/2 > game.getWidth()) {
				animate();
				setX(game.getWidth() - diameter);
				vx *= friction; 
			}
			else if (getXOffset() - diameter/2 < 0) {
				animate();
				setX(0);
				vx *= friction;
			}
			if (getYOffset() + diameter/2> game.getHeight()) {
				animate();
				setY(game.getHeight() - diameter);
				vy *= friction; 
			} 
			else if (getYOffset() - diameter/2 < 0) {
				animate();
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
		stage.setCamera(cam);		
		batch.setProjectionMatrix(cam.combined);

		stage.setViewport(width, height, true);
		stageui.setViewport(width, height, true);

		backButton.setBounds(0, height - game.getButtonHeight(), game.getButtonWidth(), game.getButtonHeight());

		tank.setScale((float)Gdx.app.getGraphics().getWidth() / (float)tank.getRegionWidth(), (float)Gdx.app.getGraphics().getHeight() / (float)tank.getRegionHeight());
		tank_shine.setScale((float)Gdx.app.getGraphics().getWidth() / (float)tank.getRegionWidth(), (float)Gdx.app.getGraphics().getHeight() / (float)tank.getRegionHeight());
	}

	@Override
	public void hide() {
		dispose();
	}

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
	}
	
	private class MyInputProcessor implements InputProcessor{

		@Override
		public boolean keyDown(int keycode) {
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {

			if(keycode == Keys.BACK || 
					keycode == Keys.BACKSPACE ||
					keycode == Keys.ESCAPE){
				
				game.setScreen(new Examples(game));
			}

			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			return false;
		}
	}
}
