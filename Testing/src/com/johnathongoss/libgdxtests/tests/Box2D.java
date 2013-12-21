package com.johnathongoss.libgdxtests.tests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2D extends BlankTestScreen{

	public Box2D(Game game) {
		super(game);
		
		testName = "Box2D Test |";
		// TODO Auto-generated constructor stub
	}

	World world; 
	Box2DDebugRenderer debugRenderer; 
	// Box2DRenderer renderer;

	static final float BOX_STEP=1/60f;  
	static final int BOX_VELOCITY_ITERATIONS=6;  
	static final int BOX_POSITION_ITERATIONS=2;  
	static final float WORLD_TO_BOX=0.01f;  
	static final float BOX_WORLD_TO=100f;  
	@Override  
	public void show() {       
		cam.position.x = cam.viewportWidth*0.01f/2;
		cam.position.y = cam.viewportHeight*0.01f/2;
		cam.zoom = 0.01f;
		world = new World(new Vector2(0, -10), true);  
		
		addBackButton();         
		addInput();
		createScene();         
		debugRenderer = new Box2DDebugRenderer();          

	}  

	private void createScene() {
				
		createWall(new Vector2(0, 0), cam.viewportWidth, 0, 0.0f);
		createWall(new Vector2(0, cam.viewportHeight), cam.viewportWidth, 0f, 0.5f);
		createWall(new Vector2(0, 0), 0,  cam.viewportHeight, 0.5f);
		createWall(new Vector2(cam.viewportWidth, 0), 0,  cam.viewportHeight, 0.5f);

		for (int i = 0; i < 16; i++){			
			createBall(
					MathUtils.random(0, cam.viewportWidth), 
					MathUtils.random(0, cam.viewportHeight), 
					MathUtils.random(width/30, width/16), 
					0.8f, 	//Density
					0.15f, 	//Friction
					0.82f); 	//Restitution
		}

	}

	private void createBall(float x, float y, float radius,
			float density, float friction, float restitution) {
		//Dynamic Body  
		BodyDef bodyDef = new BodyDef();  
		bodyDef.type = BodyType.DynamicBody;  
		bodyDef.position.set(x*WORLD_TO_BOX, y*WORLD_TO_BOX);  
		Body body = world.createBody(bodyDef);  
		
		CircleShape dynamicCircle = new CircleShape();  
		dynamicCircle.setRadius(radius*WORLD_TO_BOX);  
		FixtureDef fixtureDef = new FixtureDef();  
		fixtureDef.shape = dynamicCircle;  
		fixtureDef.density = density;  
		fixtureDef.friction = friction;  
		fixtureDef.restitution = restitution;  
		body.createFixture(fixtureDef); 
		
	}

	private void createWall(Vector2 pos, float width, float height, float density) {
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.set(pos.scl(WORLD_TO_BOX));  
		Body groundBody = world.createBody(groundBodyDef);  
		PolygonShape groundBox = new PolygonShape();  
		groundBox.setAsBox(width*WORLD_TO_BOX, height*WORLD_TO_BOX);  
		groundBody.createFixture(groundBox, density); 
		
	}

	@Override  
	public void dispose() {  
	}  
	@Override  
	public void render(float delta) {    
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);  
		 
		world.step(delta, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);  
		debugRenderer.render(world, cam.combined); 
		stageui.act(delta);
		stageui.draw();
		
		batchui.begin();
		renderTestName(batchui);
		batchui.end();
	}  

	@Override
	protected void updateText() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void renderText() {
		// TODO Auto-generated method stub

	}

}
