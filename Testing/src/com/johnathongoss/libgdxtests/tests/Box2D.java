package com.johnathongoss.libgdxtests.tests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdxtests.ImageCache;

public class Box2D extends BlankTestScreen implements InputProcessor{

	public Box2D(Game game) {
		super(game);

		testName = "Box2D Test |";
	}

	World world; 
	Box2DDebugRenderer debugRenderer; 

	static final float BOX_STEP = 1/60f;  
	static final int BOX_VELOCITY_ITERATIONS = 6;  
	static final int BOX_POSITION_ITERATIONS = 2;  
	static final float WORLD_TO_BOX = 0.02f;  
	static final float BOX_WORLD_TO = 50f;  
	@Override  
	public void show() {  
		bodies = new Array<Body>();
		addBackButton();         
		InputMultiplexer im = new InputMultiplexer(stageui, stage, this);
		Gdx.input.setInputProcessor(im);
		sprite = new Sprite(circle);

		batch = new SpriteBatch();
		debugRenderer = new Box2DDebugRenderer();
		cam = new OrthographicCamera();

		world = new World(new Vector2(0, -9.81f), true);	

		// reusable construction objects
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();

		// Bounds	
		createWall(new Vector2(0, 0), new Vector2(width, 0), 1, 0.2f, 0.8f, bodyDef, fixtureDef);
		createWall(new Vector2(0, 0), new Vector2(0, height), 1, 0.2f, 0.8f, bodyDef, fixtureDef);
		createWall(new Vector2(width, 0), new Vector2(width, height), 1, 0.2f, 0.8f, bodyDef, fixtureDef);
		createWall(new Vector2(0, height), new Vector2(width, height), 1, 0.2f, 0.8f, bodyDef, fixtureDef);

		for (int i = 0; i < 15; i++)
			createBall(MathUtils.random(0, width), MathUtils.random(0, height), MathUtils.random(width/15, width/30), 1f, 0f, .9f, bodyDef, fixtureDef);

		for (int i = 0; i < 10; i++)
			creatBox(MathUtils.random(0, width), MathUtils.random(0, height), MathUtils.random(width/7, height/10), MathUtils.random(width/7, height/10), 1, 0.3f, .8f, bodyDef, fixtureDef);
	}  

	private void creatBox(float x, float y, float w, float h,
			float density, float friction, float restitution, BodyDef bodyDef, FixtureDef fixtureDef) {
		bodyDef.position.set(WORLD_TO_BOX*x, WORLD_TO_BOX*y);
		bodyDef.angularVelocity = 0;

		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(WORLD_TO_BOX*w/2, WORLD_TO_BOX*h/2);

		fixtureDef.shape = boxShape;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		Sprite sprite = new Sprite(ImageCache.getTexture("background"));
		sprite.setSize(WORLD_TO_BOX*w, WORLD_TO_BOX*h);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
				
		Body box = world.createBody(bodyDef);
		
		box.setUserData(sprite);
		box.createFixture(fixtureDef);

		boxShape.dispose();		
	}

	private void createScene(BodyDef bodyDef, FixtureDef fixtureDef) {

		//createWall(new Vector2(0, 0), cam.viewportWidth, 0, 0.0f);
		//createWall(new Vector2(0, cam.viewportHeight), cam.viewportWidth, 0f, 0);
		//createWall(new Vector2(0, 0), 0,  cam.viewportHeight, 0.5f);
		//createWall(new Vector2(cam.viewportWidth, 0), 0,  cam.viewportHeight, 0);

		for (int i = 0; i < 25; i++){			
			createBall(
					MathUtils.random(0, 3), 
					MathUtils.random(0, 3), 
					MathUtils.random(0.2f, 1f), 
					0.8f, 	//Density
					0.10f, 	//Friction
					0.82f,
					bodyDef,
					fixtureDef); 	//Restitution
		}

	}

	private void createBall(float x, float y, float radius,
			float density, float friction, float restitution, BodyDef bodyDef, FixtureDef fixtureDef) {

		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.x = x*WORLD_TO_BOX;
		bodyDef.position.y = y*WORLD_TO_BOX;
		bodyDef.angularVelocity = -10;

		CircleShape ballShape = new CircleShape();
		ballShape.setRadius(radius*WORLD_TO_BOX);

		fixtureDef.shape = ballShape;
		fixtureDef.density = 2.5f;
		fixtureDef.restitution = .8f;
		fixtureDef.friction = .25f;
		Sprite sprite = new Sprite(ImageCache.getTexture("circle"));
		sprite.setSize(radius*2*WORLD_TO_BOX, radius*2*WORLD_TO_BOX);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		
		
		
		Body ball = world.createBody(bodyDef);
	
		ball.setUserData(sprite);
		ball.createFixture(fixtureDef);
		ballShape.dispose();

		//		CircleShape dynamicCircle = new CircleShape();  
		//		dynamicCircle.setRadius(radius*WORLD_TO_BOX); 
		//
		//		FixtureDef fixtureDef = new FixtureDef();  		
		//		fixtureDef.shape = dynamicCircle;  
		//		fixtureDef.density = density;  
		//		fixtureDef.friction = friction;  
		//		fixtureDef.restitution = restitution;  
		//
		//		BodyDef bodyDef = new BodyDef();  
		//		bodyDef.type = BodyType.DynamicBody;  
		//		bodyDef.position.set(x*WORLD_TO_BOX, y*WORLD_TO_BOX);  
		//		Body body = world.createBody(bodyDef);  	
		//
		//		body.createFixture(fixtureDef).setUserData(new Box2DSprite(sprite)); 

	}

	private void createWall(Vector2 v1, Vector2 v2,float density, float friction, float restitutuion, BodyDef bodyDef, FixtureDef fixtureDef) {
		bodyDef.type = BodyType.StaticBody;

		EdgeShape groundShape = new EdgeShape();
		groundShape.set(v1.x*WORLD_TO_BOX, v1.y*WORLD_TO_BOX, v2.x*WORLD_TO_BOX, v2.y*WORLD_TO_BOX);

		fixtureDef.shape = groundShape;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitutuion;		

		world.createBody(bodyDef).createFixture(fixtureDef);

		groundShape.dispose();		

	}

	@Override  
	public void dispose() {  
		super.dispose();

		batch.dispose();
		debugRenderer.dispose();
		world.dispose();
	}  
	Array<Body> bodies;
	protected TextureRegion circle = ImageCache.getTexture("circle");
	protected Sprite sprite;
	@Override  
	public void render(float delta) {    
		
		//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);  
		//		 
		//		world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);  		
		//		debugRenderer.render(world, cam.combined); 
		//		
		//		batch.begin();
		//		Box2DSprite.draw(batch, world);				
		//		batch.end();
		//		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.getBodies(bodies);
		batch.begin();
		for (Body body : bodies)
			if (body.getUserData() != null && body.getUserData() instanceof Sprite){
				Sprite sprite = (Sprite)body.getUserData();
				sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getHeight()/2);
				sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
				sprite.draw(batch);
				
			}
		
		batch.end();

		debugRenderer.render(world, cam.combined);

		stageui.act(delta);
		stageui.draw();

		batchui.begin();
		renderTestName(batchui);
		batchui.end();
		
		world.step(delta, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
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
	public void resize(int width, int height) {
		super.resize(width, height);
		cam.viewportWidth = width;
		//Gdx.app.log("", "" + cam.viewportWidth);
		cam.viewportHeight = height;
		cam.position.x = WORLD_TO_BOX*width/2;
		cam.position.y = WORLD_TO_BOX* height/2;		
		cam.zoom = WORLD_TO_BOX;
		cam.update();

		batch.setProjectionMatrix(cam.combined);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Gdx.app.log("", "Down");
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
