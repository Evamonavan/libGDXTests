package com.johnathongoss.libgdx.examples;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdx.examples.PopCorns.Corn;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ImageCache;
import com.johnathongoss.libgdxtests.tests.BlankTestScreen;
import com.johnathongoss.libgdxtests.tests.Camera2D;
import com.johnathongoss.libgdxtests.tests.Collision;
import com.johnathongoss.libgdxtests.tests.Collision.Ball;
import com.johnathongoss.libgdxtests.utils.MyTimer;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class FishTank extends BlankTestScreen {

	public ShapeRenderer shapeRenderer;
	public Array<Fish> fishes;
	public float gravity;
	public float friction;
	public float conservedEnergy;
	public double hardness;
	public float viscosity = 0.992f;
	Sprite tank, tank_shine;
	
	public FishTank(Game game) {
		super(game);		
		tank = new Sprite(ImageCache.getTexture("tank"));
		tank.setPosition(0, 0);
		tank.setOrigin(0, 0);
		tank.setScale((float)Gdx.app.getGraphics().getWidth() / (float)tank.getRegionWidth(), (float)Gdx.app.getGraphics().getHeight() / (float)tank.getRegionHeight());

		tank_shine = new Sprite(ImageCache.getTexture("tank_shine"));
		tank_shine.setPosition(0, 0);
		tank_shine.setOrigin(0, 0);
		tank_shine.setScale((float)Gdx.app.getGraphics().getWidth() / (float)tank.getRegionWidth(), (float)Gdx.app.getGraphics().getHeight() / (float)tank.getRegionHeight());

		
		testName = "Fish Tank Example |";
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();
		tank.draw(batch);
		batch.end();
		stage.act(delta);
		stage.draw();

		batch.begin();
		tank_shine.draw(batch);
		batch.end();
		stageui.act(delta);
		stageui.draw();

		batchui.begin();
		renderTestName(batchui);
		batchui.end();

		controller.update();
		cam.update();
		batch.setProjectionMatrix(cam.combined);

	}

	@Override
	protected void updateText() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void renderText() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		shapeRenderer = new ShapeRenderer();
		cam.zoom = 1.4f;
		gravity = 0;
		friction = -0.1f;
		hardness = 1f;
		conservedEnergy = 0.5f;
		viscosity = 0.99f;

		addCameraControl(width, 0, height, 0);
		addBackButton();

		fishes = new Array<Fish>();
		for (int i = 0; i < 10; i++) {
			fishes.add(new Fish(MathUtils.random(width), MathUtils.random(height), MathUtils.random(width/15, width/10), i, fishes));
			fishes.get(i).setVelocity(MathUtils.random(-1.2f, 1.2f), MathUtils.random(-1.2f, 1.2f));
			fishes.get(i).changeColor();
			stage.addActor(fishes.get(i));
		}
	}

	public class Fish extends Actor {
		public boolean popped;

		MyTimer timer;

		public void pop(){
			popped = true;

			setDiameter(MathUtils.random(width/18, width/12));
			setColor(1f, 0.8f, 0, 1);
			setVelocity(MathUtils.random(-9f, 9f), MathUtils.random(-9f, 9f));

		}

		public float diameter;
		float vx = 0;
		float vy = 0;
		int id;
		Color color = Color.WHITE;
		Array<Fish> others;
		TextureRegion texture = ImageCache.getTexture("fish");
		public Fish(float xin, float yin, float din, int idin, Array<Fish> balls) {
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
			others = balls;	

			setBounds(getX() - diameter/2, getY() - diameter/2, diameter,diameter);
			setOrigin(diameter/2, diameter/2);
			setScale(1f);

			addListener(new ActorGestureListener(){
				public void fling(InputEvent event, float velocityX, float velocityY, int button){

				}

				public void tap(InputEvent event, float x, float y, int count, int button){
					changeDirection(2f);
				}
			});

			setTouchable(Touchable.enabled);


		} 
		private void changeDirection(float power) {

			//setVelocity(MathUtils.random(-vx *MathUtils.random(-1.3f, 1.3f), vx *MathUtils.random(-1.3f, 1.3f)), MathUtils.random(-vy *MathUtils.random(-1.3f, 1.3f), vy * MathUtils.random(-1.3f, 1.3f)));

			vx += MathUtils.random(-2f*power, 2f*power);
			vy += MathUtils.random(-0.6f*power, 0.6f*power);

		}
		Color tempColor;
		public void changeColor() {			
			setColor(new Color(MathUtils.random(0f, 1f), MathUtils.random(0f, 1f), MathUtils.random(0f, 1f), 1f));
		}

		public void removeBall(Fish ball){

			Gdx.app.log("", others.removeValue(ball, true) + "");


			for (int i = 0; i < others.size; i++)
				if (others.get(i) == ball)
					Gdx.app.log("", "" + others.removeValue(others.get(i), true));
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
					//others.get(i).vx += ax;
					//others.get(i).vy += ay;
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
		private void Flip (){

			if (getActions().size <= 0){

				float toScaleX;
				
				if (getScaleX() > 0)
					toScaleX = -1f;
				else
					toScaleX = 1f;

				addAction(Actions.scaleTo(toScaleX, 1f, 0.3f, Interpolation.exp5Out));

			}
		}
		@Override
		public void draw(Batch batch, float alpha) {
			batch.setColor(getColor());
			batch.draw(texture, getX(), getY(), 
					getOriginX(), getOriginY(), 				
					getWidth(), getHeight(), 
					getScaleX(), getScaleY(),
					getRotation());
			batch.setColor(Color.WHITE);
			//Assets.font24.draw(batch, "" + vx, getX(), getY());			

			//			shapeRenderer.begin(ShapeType.Filled);
			//			shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
			//			shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
			//			shapeRenderer.scale(getScaleX(), getScaleY(), 0);
			//			shapeRenderer.setColor(getColor());
			//			shapeRenderer.circle(getXOffset(), getYOffset(), diameter/2);
			//			shapeRenderer.end();
			//
			//			shapeRenderer.begin(ShapeType.Line);
			//			shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
			//			shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
			//			shapeRenderer.scale(getScaleX(), getScaleY(), 0);
			//			shapeRenderer.setColor(Color.BLACK);
			//			shapeRenderer.circle(getXOffset(), getYOffset(), diameter/2);
			//			shapeRenderer.end();

		}

		@Override
		public void act(float delta){
			timer.update(delta);
			collide(delta);
			move(delta);
			
			if (vx > 0.15 && getScaleX() > 0)
				Flip();
			else if (vx < -0.15 && getScaleX() < 0)
				Flip();
			
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

}
