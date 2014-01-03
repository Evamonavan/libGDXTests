package com.johnathongoss.libgdx.examples;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.johnathongoss.libgdxtests.screens.Examples;
import com.johnathongoss.libgdxtests.tests.BlankTestScreen;
import com.johnathongoss.libgdxtests.utils.MyTimer;

public class PopCorns extends BlankTestScreen{	

	public PopCorns(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	MyTimer timer;
	private int numBalls;
	private float gravity;
	private float hardness;
	private float conservedEnergy;
	private Array<Corn> corns;

	@Override
	public void show(){
		shapeRenderer = new ShapeRenderer();
		timer = new MyTimer(MathUtils.random(2f, 6f)) {

			@Override
			protected void perform() {

				popTheCorn();
				timer.setCap(MathUtils.random(0f, 3f));

			}
		};

		timer.start();
		timer.setRepeating(true);

		numBalls = 50;
		testName = "Pop Corn Example |";
		
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

		gravity = -0.1f;
		hardness = 0.6f;
		conservedEnergy = 0.85f;
		friction = -0.75f;
		
		corns = new Array<Corn>();
		for (int i = 0; i < numBalls ; i++) {
			corns.add(new Corn(MathUtils.random(width), MathUtils.random(0, height/2), width/30 , i, corns));
			corns.get(i).setVelocity(0, 0);
			//balls.get(i).setTouchable(Touchable.disabled);
			stage.addActor(corns.get(i));
		}		
	}

	int tempInt;
	public float friction;
	public ShapeRenderer shapeRenderer;

	protected void popTheCorn() {

		tempInt = MathUtils.random(0, corns.size - 1);
		if (!corns.get(tempInt).popped){
			corns.get(tempInt).pop();
		}
	}

	@Override
	public void render(float delta) {	
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);	
		stage.act(delta);
		stage.draw();	
		stageui.act(delta);
		stageui.draw();	

		//updateText();
		renderText();

		if  (gravity > -0.8f)
			gravity -= delta;

		timer.update(delta);
		
		batchui.begin();
		renderTestName(batchui);
		batchui.end();
	}	

	@Override
	protected void updateText() {
		Text.clear();

		Text.add(timer.getCurrent() + "");		
		//Text.add(conservedEnergy + " |");

	}

	@Override
	protected void renderText() {
		

	}

	public class Corn extends Actor {
		public boolean popped;

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

		Color tempColor;
		public void changeColor() {
			tempColor = new Color(MathUtils.random(0f, 1f), MathUtils.random(0f, 1f), MathUtils.random(0f, 1f), MathUtils.random(0f, 1f));
			addAction(Actions.color(tempColor, 1.4f, Interpolation.swingOut));
			//setColor();

		}

		public void removeBall(Corn ball){

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

			//			shapeRenderer.begin(ShapeType.Line);
			//			shapeRenderer.scale(getScaleX(), getScaleY(), 0);
			//			shapeRenderer.setColor(Color.WHITE);
			//			shapeRenderer.rect(getX(), getY(), diameter, diameter);
			//			shapeRenderer.end();
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
}
