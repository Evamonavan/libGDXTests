package com.johnathongoss.libgdxtests.examples;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.entities.MyTimer;
import com.johnathongoss.libgdxtests.screens.Examples;

public class PopCorns implements Screen{		

	MyInputProcessor input = new MyInputProcessor();	
	
	MyGame game;
	private Stage stage, stageui;
	private SpriteBatch batchui;
	String testName;
	
	MyTimer timer;
	private int numBalls;
	private float gravity;
	private float hardness;
	private float conservedEnergy;
	private Array<Corn> corns;
	
	public float friction;
	public ShapeRenderer shapeRenderer;
	
	TextButton backButton;
	
	public PopCorns(MyGame game) {
		this.game = game;
		
		testName = "Pop Corn Example |";
		
		stage = new Stage();
		stageui = new Stage();
		batchui= new SpriteBatch();		
		
		shapeRenderer = new ShapeRenderer();
		timer = new MyTimer(MathUtils.random(2f, 6f)) {

			@Override
			protected void perform() {

				popTheCorn();
				timer.setCap(MathUtils.random(0f, 3f));

			}
		};
		
		backButton = new TextButton("Back", Assets.skin);
		corns = new Array<Corn>();
	}

	@Override
	public void show(){
		InputMultiplexer im = new InputMultiplexer(stageui, stage, input);
		Gdx.input.setInputProcessor(im);		
		Gdx.input.setCatchBackKey(true);		

		timer.start();
		timer.setRepeating(true);					
		
		backButton.setBounds(0, game.getHeight() - game.getButtonHeight(), game.getButtonWidth(), game.getButtonHeight());
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Examples(game));
			}
		});	

		stageui.addActor(backButton);	

		gravity = -0.1f;
		hardness = 0.6f;
		conservedEnergy = 0.85f;
		friction = -0.75f;		
		
		numBalls = 50;
		for (int i = 0; i < numBalls ; i++) {
			corns.add(new Corn(MathUtils.random(game.getWidth()), MathUtils.random(0, game.getHeight()/2), game.getWidth()/30 , i, corns));
			corns.get(i).setVelocity(0, 0);
			stage.addActor(corns.get(i));
		}		
	}	

	@Override
	public void render(float delta) {	
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);	
		stage.act(delta);
		stage.draw();	
		stageui.act(delta);
		stageui.draw();	

		if  (gravity > -0.8f)
			gravity -= delta;

		timer.update(delta);
		
		batchui.begin();
		Assets.font24.drawMultiLine(batchui, testName, 0, 24, game.getWidth(), HAlignment.RIGHT);
		batchui.end();
	}	
	
	private void popTheCorn() {

		int tempInt = MathUtils.random(0, corns.size - 1);
		if (!corns.get(tempInt).popped){
			corns.get(tempInt).pop();
		}
	}

	@Override
	public void resize(int width, int height) {}

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
		stage.dispose();
		stageui.dispose();
		batchui.dispose();
	}

	private class Corn extends Actor {
		public boolean popped;

		public void pop(){
			popped = true;

			setDiameter(MathUtils.random(game.getWidth()/18, game.getWidth()/12));
			setColor(1f, 0.8f, 0, 1);
			setVelocity(MathUtils.random(-9f, 9f), MathUtils.random(-9f, 9f));

		}

		public float diameter;
		float vx = 0;
		float vy = 0;
		int id;
		Array<Corn> others;		

		public Corn(float xin, float yin, float din, int idin, Array<Corn> balls) {
			setX(xin);
			setY(yin);
			diameter = din;
			id = idin;
			others = balls;	

			setBounds(getX() - diameter/2, getY() - diameter/2, diameter,diameter);
			setOrigin(diameter/2, diameter/2);
			setScale(1f);

			addListener(new ActorGestureListener(){
				public void fling(InputEvent event, float velocityX, float velocityY, int button){
					
				}

				public void tap(InputEvent event, float x, float y, int count, int button){

					if (!popped)
						pop();
				}
			});

			setTouchable(Touchable.enabled);


		} 
		
		public float getXOffset(){
			return getX() + diameter/2;			
		}
		
		public float getYOffset(){
			return getY() + diameter/2;			
		}

		void collide(float delta) {
			for (int i = id + 1; i < corns.size; i++) {
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
					others.get(i).vx += ax;
					others.get(i).vy += ay;
				}
			}   
		}

		void move(float delta) {
			vy += gravity;
			addX(vx);
			addY(vy);
			if (getXOffset() + diameter/2> game.getWidth()) {
				setX(game.getWidth() - diameter);
				vx *= friction; 
			}
			else if (getXOffset() - diameter/2 < 0) {
				setX(0);
				vx *= friction;
			}
			if (getYOffset() + diameter/2> game.getHeight()) {
				setY(game.getHeight() - diameter);
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
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
			shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
			shapeRenderer.scale(getScaleX(), getScaleY(), 0);
			shapeRenderer.setColor(getColor());
			shapeRenderer.circle(getXOffset(), getYOffset(), diameter/2);
			shapeRenderer.end();
			
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
			shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
			shapeRenderer.scale(getScaleX(), getScaleY(), 0);
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.circle(getXOffset(), getYOffset(), diameter/2);
			shapeRenderer.end();
		}

		@Override
		public void act(float delta){
			collide(delta);
			move(delta);
			for (int i = 0; i < getActions().size; i++) {
				Action action = getActions().get(i);
				if (action.act(delta) && i < getActions().size) {
					getActions().removeIndex(i);
					action.setActor(null);
					i--;
				}
			}
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
