package com.johnathongoss.libgdxtests.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdxtests.ImageCache;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.screens.MainMenu;

public class Box2D extends BlankTestScreen implements GestureListener, InputProcessor{

	public Box2D(MyGame game) {
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
	private boolean showDebug = false;
	@Override  
	public void show() {  

		/*
		 * Toggle Debug
		 */		

		debugButton = new TextButton("Debug", skin);
		debugButton.setBounds(width - BUTTON_WIDTH, height - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		//debugButton.setPosition(width - BUTTON_WIDTH, height - BUTTON_HEIGHT);
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showDebug = !showDebug;				
			}

		});		
		stageui.addActor(debugButton);	

		bodies = new Array<Body>();
		addBackButton();         
		
		InputMultiplexer im = new InputMultiplexer(stageui, stage,new GestureDetector(this),  this);
		Gdx.input.setInputProcessor(im);		
		Gdx.input.setCatchBackKey(true);
		
		sprite = new Sprite(circle);

		batch = new SpriteBatch();
		debugRenderer = new Box2DDebugRenderer();
		cam = new OrthographicCamera();

		world = new World(new Vector2(0, -9.81f), true);	
		
		world.setContactListener(new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void endContact(Contact contact) {
				//contact.getFixtureA().getBody().getLinearVelocity().set(10, 10);
				//contact.getFixtureA().getBody().applyLinearImpulse(new Vector2(1, 1), new Vector2(0, 0), true);
				
				Vector2 dir = new Vector2();
				dir.x = MathUtils.random(-3f, 3f);
				dir.y = MathUtils.random(-1f, 1f);
				//contact.getFixtureA().getBody().setLinearVelocity(dir);
				//contact.getFixtureB().getBody().setTransform(position, angle)
				//contact.getFixtureB().getBody().setLinearVelocity(dir.scl(-1, -1));
				//contact.getFixtureB().getBody().setLinearVelocity(contact.getFixtureB().getBody().getLinearVelocity().add(contact.getFixtureA().getBody().getLinearVelocity()));
			}
			
			@Override
			public void beginContact(Contact contact) {
				//contact.getFixtureA().getBody().getLinearVelocity().set(1, 1);
				Gdx.app.log("", "Contact");
				
			}
		});
		
		
		// reusable construction objects
		BodyDef bodyDef = new BodyDef();
		// we also need an invisible zero size ground body
		// to which we can connect the mouse joint
		// BodyDef bodyDef = new BodyDef();
		groundBody = world.createBody(bodyDef);
		FixtureDef fixtureDef = new FixtureDef();

		// Bounds	
		createWall(new Vector2(0, 0), new Vector2(width, 0), 1, 0.2f, 0.8f, bodyDef, fixtureDef);
		createWall(new Vector2(0, 0), new Vector2(0, height), 1, 0.2f, 0.8f, bodyDef, fixtureDef);
		createWall(new Vector2(width, 0), new Vector2(width, height), 1, 0.2f, 0.8f, bodyDef, fixtureDef);
		createWall(new Vector2(0, height), new Vector2(width, height), 1, 0.2f, 0.8f, bodyDef, fixtureDef);

		for (int i = 0; i < 12; i++)
			createBall(MathUtils.random(0, width), MathUtils.random(0, height), MathUtils.random(width/15, width/30), 1f, 0f, .9f, bodyDef, fixtureDef);

		for (int i = 0; i < 3; i++)
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
		bodyDef.angularVelocity = 0;

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
		debugRenderer.dispose();
		world.dispose();
	}  
	Array<Body> bodies;
	protected TextureRegion circle = ImageCache.getTexture("circle");
	protected Sprite sprite;
	@Override  
	public void render(float delta) {    
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

		if (showDebug)
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

	/** ground body to connect the mouse joint to **/
	protected Body groundBody;

	/** our mouse joint **/
	protected MouseJoint mouseJoint = null;

	/** a hit body **/
	protected Body hitBody = null;

	/** temp vector **/
	protected Vector2 tmp = new Vector2();

	/** we instantiate this vector and the callback here so we don't irritate the GC **/
	Vector3 testPoint = new Vector3();
	QueryCallback callback = new QueryCallback() {
		@Override public boolean reportFixture (Fixture fixture) {
			// if the hit point is inside the fixture of the body
			// we report it
			if (fixture.testPoint(testPoint.x, testPoint.y)) {
				hitBody = fixture.getBody();
				return false;
			} else
				return true;
		}
	};

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// translate the mouse coordinates to world coordinates
		cam.unproject(testPoint.set(x, y, 0));
		// ask the world which bodies are within the given
		// bounding box around the mouse pointer
		hitBody = null;
		world.QueryAABB(callback, testPoint.x - 0.0001f, testPoint.y - 0.0001f, testPoint.x + 0.0001f, testPoint.y + 0.0001f);

		if (hitBody == groundBody) hitBody = null;

		// ignore kinematic bodies, they don't work with the mouse joint
		if (hitBody != null && hitBody.getType() == BodyType.KinematicBody) return false;

		// if we hit something we create a new mouse joint
		// and attach it to the hit body.
		if (hitBody != null) {
			MouseJointDef def = new MouseJointDef();
			def.bodyA = groundBody;
			def.bodyB = hitBody;
			def.collideConnected = true;
			def.target.set(testPoint.x, testPoint.y);
			def.maxForce = 1000.0f * hitBody.getMass();

			mouseJoint = (MouseJoint)world.createJoint(def);
			hitBody.setAwake(true);
		}

		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		Gdx.app.log("", "touchdown ");
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	/** another temporary vector **/
	Vector2 target = new Vector2(); 
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// if a mouse joint exists we simply update
		// the target of the joint based on the new
		// mouse coordinates
		if (mouseJoint != null) {
			cam.unproject(testPoint.set(x, y, 0));
			mouseJoint.setTarget(target.set(testPoint.x, testPoint.y));
		}
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
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
		Gdx.app.log("", "up");
		// if a mouse joint exists we simply destroy it
		if (mouseJoint != null) {
			world.destroyJoint(mouseJoint);
			mouseJoint = null;
		}
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
