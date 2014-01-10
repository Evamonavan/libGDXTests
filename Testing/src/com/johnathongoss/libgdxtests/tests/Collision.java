package com.johnathongoss.libgdxtests.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
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
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.screens.MainMenu;

public class Collision extends BlankTestScreen {

	protected float spring = 0.03f;
	protected float gravity = -0.07f;
	protected float friction = -0.85f;
	protected float conservedEnergy = 0.95f;
	protected Array<Ball> balls;
	ShapeRenderer shapeRenderer;	

	protected float GravityLevel[] = {1f, 0.5f, 0.25f, 0.1f, 0.05f, 0.02f, 0.01f, 0, -0.01f, -0.02f, -0.05f, -0.1f, -0.25f, -0.5f, - 1f}; 
	protected int gravityPointer = 0;

	protected float HardnessLevel[] = {0.02f, 0.05f, 0.1f, 0.25f, 0.5f, 1f}; 
	protected int hardnessPointer = 0;

	protected float CEnergyLevel[] = {0.25f, 0.50f, 0.75f, 0.85f, 0.95f, 1, 1.01f, 1.02f, 1.04f, 1.06f, 1.08f, 1.1f}; 
	protected int cEnergyPointer = 0;
	protected int numBalls = 16;

	public Collision(MyGame game) {
		super(game);
		shapeRenderer = new ShapeRenderer();
	}

	public void removeBall(Ball ball){

		// remove from balls
		stage.getActors().removeValue(ball, true);
		Gdx.app.log("", "" + balls.removeValue(ball, true));

		// remove from ball arrays

		//		for (Ball b : balls)
		//			b.removeBall(ball);

	}

	@Override
	public void render(float delta) {	
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);	
		stage.act(delta);
		stage.draw();	
		stageui.act(delta);
		stageui.draw();	
		updateText();

		renderText();
	}

	@Override
	protected void renderText(){

		batchui.begin();
		Assets.font24.setColor(1, 1, 1, 0.8f);
		for (int i = 0; i < Text.size; i++){			
			Assets.font24.drawMultiLine(batchui, Text.get(i), 0, height - BUTTON_HEIGHT/3 - i*BUTTON_HEIGHT, width - BUTTON_WIDTH*2, HAlignment.RIGHT);
		}
		renderTestName(batchui);
		batchui.end();
	}

	@Override
	public void show() {
		testName = "Collision Test |";	
		addBackButton();
		InputMultiplexer im = new InputMultiplexer(stageui, stage, this);
		Gdx.input.setInputProcessor(im);		
		Gdx.input.setCatchBackKey(true);
		/*
		 * Randomise setup
		 */

		gravityPointer = MathUtils.random(GravityLevel.length - 8, GravityLevel.length - 1);
		gravity = GravityLevel[gravityPointer];

		hardnessPointer = MathUtils.random(HardnessLevel.length - 1);
		spring = HardnessLevel[hardnessPointer];

		cEnergyPointer = 4;
		conservedEnergy = CEnergyLevel[cEnergyPointer];				

		balls = new Array<Ball>();
		for (int i = 0; i < numBalls ; i++) {
			balls.add(new Ball(MathUtils.random(width), MathUtils.random(height), MathUtils.random(width/25, width/10), i, balls));
			balls.get(i).setVelocity(MathUtils.random(-1.2f, 1.2f), MathUtils.random(-1.2f, 1.2f));
			balls.get(i).changeColor();
			stage.addActor(balls.get(i));
		}

		/*
		 * Decrease Gravity
		 */		

		debugButton = new TextButton("[-] Gravity", skin);
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				increaseGravityLevel(-1);				
			}

		});		
		buttons.add(debugButton);	

		/*
		 * Decrease Hardness
		 */		

		debugButton = new TextButton("[-] Hardness", skin);
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				increaseHardnessLevel(-1);
			}
		});		
		buttons.add(debugButton);

		/*
		 * Decrease Conserved Energy
		 */		

		debugButton = new TextButton("[-] Con. Energy", skin);
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				increaseCEnergyLevel(-1);
			}
		});		
		buttons.add(debugButton);	

		/*
		 * Increase Gravity
		 */

		debugButton = new TextButton("[+] Gravity", skin);
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				increaseGravityLevel(1);
			}
		});		
		buttons.add(debugButton);

		/*
		 * Increase Hardness
		 */		

		debugButton = new TextButton("[+] Hardness", skin);
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				increaseHardnessLevel(1);
			}
		});		
		buttons.add(debugButton);

		/*
		 * Increase Conserve Energy
		 */		

		debugButton = new TextButton("[+] Con. Energy", skin);
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				increaseCEnergyLevel(1);
			}
		});		
		buttons.add(debugButton);

		/*
		 * New Ball
		 */		

		debugButton = new TextButton("Add Ball", skin);
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (balls.size < 60)
					addBall();				
			}

		});		
		buttons.add(debugButton);	

		for (TextButton button : buttons){			
			stageui.addActor(button);		
			button.setHeight(BUTTON_HEIGHT);
			button.setWidth(BUTTON_WIDTH);
		}

	}	

	protected void addBall() {

		balls.add(new Ball(MathUtils.random(width), MathUtils.random(height), MathUtils.random(width/25, width/10), balls.size, balls));
		balls.get(balls.size - 1).setVelocity(MathUtils.random(-1.2f, 1.2f), MathUtils.random(-1.2f, 1.2f));
		balls.get(balls.size - 1).changeColor();
		stage.addActor(balls.get(balls.size - 1));
	}

	protected void increaseCEnergyLevel(int amount) {
		cEnergyPointer += amount;

		if (cEnergyPointer > CEnergyLevel.length - 1)
			cEnergyPointer = CEnergyLevel.length - 1;
		else if (cEnergyPointer < 0)
			cEnergyPointer = 0;

		conservedEnergy = CEnergyLevel[cEnergyPointer];	

	}

	protected void increaseHardnessLevel(int amount) {
		hardnessPointer += amount;

		if (hardnessPointer > HardnessLevel.length - 1)
			hardnessPointer = HardnessLevel.length - 1;
		else if (hardnessPointer < 0)
			hardnessPointer = 0;

		spring = HardnessLevel[hardnessPointer];	

	}

	protected void increaseGravityLevel(int amount) {

		gravityPointer += amount;

		if (gravityPointer > GravityLevel.length - 1)
			gravityPointer = GravityLevel.length - 1;
		else if (gravityPointer < 0)
			gravityPointer = 0;

		gravity = GravityLevel[gravityPointer];	

	}

	@Override
	public void dispose() {
		super.dispose();
		shapeRenderer.dispose();
	}

	public class Ball extends Actor {
		public float diameter;
		float vx = 0;
		float vy = 0;
		int id;
		Color color = Color.WHITE;
		Array<Ball> others;

		public Ball(float xin, float yin, float din, int idin, Array<Ball> balls) {
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
					Fling(velocityX, velocityY);
				}

				public void tap(InputEvent event, float x, float y, int count, int button){
					changeColor();
					//Flip();
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

		public void removeBall(Ball ball){

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
			for (int i = id + 1; i < balls.size; i++) {
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
					double ax = (targetX - others.get(i).getXOffset()) * spring;
					double ay = (targetY - others.get(i).getYOffset()) * spring;
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
			
		}
	}
	@Override
	protected void updateText() {
		Text.clear();

		if (gravity != 0)
			Text.add(-gravity + " |");
		else
			Text.add(gravity + " |");
		Text.add(spring + " |");		
		Text.add(conservedEnergy + " |");

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.BACK){
			game.setScreen(new MainMenu(game));
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
