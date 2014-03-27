package com.johnathongoss.libgdxtests.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ImageCache;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.MyInputProcessor;
import com.johnathongoss.libgdxtests.screens.MainMenu;

public class HexGridTest implements Screen{

	MyGame game;
	SpriteBatch batch;
	Stage stageui, stageaction, stagehexes;
	OrthographicCamera cam, cam_ui;
	HexGrid hexGrid;

	protected CameraController controller;
	protected GestureDetector gestureDetector;

	MyInputProcessor input = new MyInputProcessor(){

		@Override
		public boolean keyUp(int keycode) {
			if(keycode == Keys.BACK || 
					keycode == Keys.BACKSPACE ||
					keycode == Keys.ESCAPE){
				game.setScreen(new MainMenu(game));
			}
			return false;
		}
	};
	protected boolean debug = false;

	public HexGridTest(MyGame game) {
		this.game = game;		
		batch = new SpriteBatch();
		cam = new OrthographicCamera();
		cam_ui = new OrthographicCamera();
		hexGrid = new HexGrid(game.getWidth(), game.getHeight());		

		stageui = new Stage(game.getWidth(), game.getHeight(), true);
		stageaction = new Stage(game.getWidth(), game.getHeight(), true);
		stagehexes = new Stage(game.getWidth(), game.getHeight(), true);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		controller.update();

		stagehexes.act(delta);
		stagehexes.draw();

		stageaction.act(delta);
		stageaction.draw();

		stageui.act(delta);
		stageui.draw();		


		// Camera ------------- /
		//cam.position.x -= .01f;
		cam.update();


		cam_ui.update();
		batch.setProjectionMatrix(cam_ui.combined);
		batch.begin();
		//hexGrid.draw(batch);

		// UI --------------- /

		Assets.font24.drawMultiLine(batch, "Hex Grid Test |", 0, Assets.font24.getLineHeight(), game.getWidth(), HAlignment.RIGHT);

		//Assets.font24.drawMultiLine(batch, game.input.getX() + ", " + game.input.getY(), 0, game.getHeight(), game.getWidth(), HAlignment.RIGHT);
		//Assets.font24.drawMultiLine(batch, "Cam: " + cam.position.x + ", " + cam.position.y, 0, game.getHeight() - 24, game.getWidth(), HAlignment.RIGHT);

		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		game.showAds(true);

		/**
		 * Handling Input
		 */			

		cam.setToOrtho(false, game.getWidth(), game.getHeight());
		cam.position.set(hexGrid.getWidth(), hexGrid.getHeight()/2, 0);


		controller = new CameraController(hexGrid.getWidth() + (hexGrid.getWidth()/2 - game.getWidth()/2), hexGrid.getWidth() - (hexGrid.getWidth()/2 - game.getWidth()/2), hexGrid.getHeight()/2 + (hexGrid.getHeight()/2 - game.getHeight()/4), hexGrid.getHeight()/2 - (hexGrid.getHeight()/2 - game.getHeight()/4));
		// Camera needs to be moved (small screen)
		if (game.getWidth() < hexGrid.getWidth())
			controller.allowCameraPanning  = true;

		gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, controller);

		stagehexes.setCamera(cam);
		cam_ui.setToOrtho(false, game.getWidth(), game.getHeight());

		InputMultiplexer im = new InputMultiplexer(gestureDetector, stageui, stagehexes, input);		
		Gdx.input.setInputProcessor(im);

		//We dont want the back button to exit the app on this screen	
		Gdx.input.setCatchBackKey(true); 

		for (int q = 0; q < hexGrid.ARRAY_WIDTH; q++){

			for (int r = 0; r < hexGrid.ARRAY_HEIGHT; r++){
				stagehexes.addActor(hexGrid.getHex(q, r));
			}

		}	

		TextButton button = new TextButton("Back", Assets.skin);
		button.setWidth(Gdx.app.getGraphics().getWidth()/7);
		button.setHeight(Gdx.app.getGraphics().getHeight()/8);				
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				game.setScreen(new MainMenu(game));
			}
		});	
		button.setPosition(0, Gdx.app.getGraphics().getHeight() - button.getHeight()*2);
		stageui.addActor(button);

		button = new TextButton("Debug", Assets.skin);
		button.setWidth(Gdx.app.getGraphics().getWidth()/7);
		button.setHeight(Gdx.app.getGraphics().getHeight()/8);				
		button.addListener(new ClickListener() {			

			@Override
			public void clicked(InputEvent event, float x, float y) {

				debug  = !debug;
			}
		});	
		button.setPosition(game.getWidth() - game.getButtonWidth(), Gdx.app.getGraphics().getHeight() - button.getHeight()*2);
		stageui.addActor(button);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();
		stageui.dispose();
		stageaction.dispose();
		stagehexes.dispose();

	}

	protected class CameraController implements GestureListener { 

		public boolean allowCameraPanning = false;
		public float velX, velY;
		boolean flinging = false;
		float initialScale = 1;

		float boundX, boundX2, boundY, boundY2;

		public CameraController(float bx1, float bx2, float by1, float by2) {
			boundX = bx1;
			boundX2 = bx2;
			boundY = by1;
			boundY2 = by2;
		}

		public boolean touchDown (float x, float y, int pointer, int button) {
			flinging = false;
			initialScale = cam.zoom;
			return false;
		}

		@Override
		public boolean tap (float x, float y, int count, int button) {
			Gdx.app.log("GestureDetectorTest", "tap at " + x + ", " + y + ", count: " + count);
			return false;
		}

		@Override
		public boolean longPress (float x, float y) {
			Gdx.app.log("GestureDetectorTest", "long press at " + x + ", " + y);
			return false;
		}

		@Override
		public boolean fling (float velocityX, float velocityY, int button) {
			Gdx.app.log("GestureDetectorTest", "fling " + velocityX + ", " + velocityY);
			flinging = true;
			if (allowCameraPanning){
				velX = cam.zoom * velocityX * 0.5f;
				velY = cam.zoom * velocityY * 0.5f;
			}
			return false;
		}

		@Override
		public boolean pan (float x, float y, float deltaX, float deltaY) {
			if (allowCameraPanning) // no need if screen size big enough (check in Show())
				cam.position.add(-checkBoundsX(deltaX) * cam.zoom, checkBoundsY(deltaY) * cam.zoom, 0);
			return false;
		}

		private float checkBoundsX(float deltaX) {
			if (cam.position.x >= boundX && deltaX < 0){
				velX = 0;
				cam.position.x = boundX;
				return 0;
			}

			if (cam.position.x <= boundX2 && deltaX > 0){
				velX = 0;
				cam.position.x = boundX2;
				return 0;
			}

			return deltaX;
		}
		private float checkBoundsY(float deltaY) {

			if (cam.position.y >= boundY && deltaY > 0){
				velY = 0;
				cam.position.y = boundY;
				return 0;
			}

			if (cam.position.y <= boundY2 && deltaY < 0){
				velX = 0;
				cam.position.y = boundY2;
				return 0;
			}

			return deltaY;
		}

		@Override
		public boolean panStop (float x, float y, int pointer, int button) {
			Gdx.app.log("GestureDetectorTest", "pan stop at " + x + ", " + y);
			return false;
		}

		@Override
		public boolean zoom (float originalDistance, float currentDistance) {
			float ratio = originalDistance / currentDistance;

			cam.zoom = initialScale * ratio;
			if (cam.zoom < 1f)
				cam.zoom = 1f;
			if (cam.zoom > 1.7f)
				cam.zoom = 1.7f;

			return false;
		}

		public Vector2 getNormalized(Vector2 v) {
			float l = v.len();
			if (l == 0)
				return new Vector2();
			else
				return new Vector2(v.x / l, v.y / l);
		}

		@Override
		public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {

			return false;
		}

		public void update () {				

			//updateBounds();

			if (Gdx.input.isKeyPressed(Keys.S))
				cam.zoom += 0.03;
			if (Gdx.input.isKeyPressed(Keys.W))
				cam.zoom -= 0.03;			

			if (flinging) {
				velX *= 0.92f;
				velY *= 0.92f;			

				cam.position.add(-checkBoundsX(velX) * Gdx.graphics.getDeltaTime(), checkBoundsY(velY) * Gdx.graphics.getDeltaTime(), 0);

				if (Math.abs(velX) < 0.01f) velX = 0;
				if (Math.abs(velY) < 0.01f) velY = 0;	

			}
		}

		public void setBounds(float x1, float x2, float y1, float y2) {
			boundX = x1;
			boundX2 = x2;
			boundY = y1;
			boundY2 = y2;

		}
	}

	public class HexGrid {

		// Grid size is currently max of 75. This should be adequate. Can be altered.
		private final int Q = 8, R = 7;
		public final int TOP_RIGHT = 0, RIGHT = 1, BOTTOM_RIGHT = 2, BOTTOM_LEFT = 3, LEFT = 4, TOP_LEFT = 5;
		public final int		ARRAY_HEIGHT = R, ARRAY_WIDTH = Q + R/2;
		private final Hex[][] Hexes = new Hex[ARRAY_WIDTH][ARRAY_HEIGHT];	

		float x, y; //center of the HexGrid. Usually game.width/2, game.height/2

		public int[][] neighbors = {	

				{ 0, +1}, //TOP RIGHT
				{+1,  0}, //RIGHT
				{+1, -1}, //BOTTOM RIGHT
				{ 0, -1}, //BOTTOM LEFT
				{-1,  0}, //LEFT
				{-1, +1}  //TOP LEFT
		};

		public HexGrid(float gameWidth, float gameHeight) {

			for (int q = 0; q < ARRAY_WIDTH; q++){

				for (int r = 0; r < ARRAY_HEIGHT; r++){

					Hexes[q][r] = new Hex(q, r);

					//Flag hexes that arent needed

					if ((q == 0 && (r != 6)) ||
							(q == 1 && 	(r == 0 || r == 1 || r == 2 || r == 3)) ||
							(q == 2 && (r == 0 || r == 1)) ||
							(q == 10 && (r != 0)) ||
							(q == 9 && (r == 3 || r == 4 || r == 5 || r == 6)) ||
							(q == 8 && (r == 5 || r == 6))
							){
						Hexes[q][r].setUsed(false);
					}
				}			
			}
		}	
		public float getWidth(){

			return Q*Hex.width + Hex.width/2;

		}
		public float getHeight(){

			return (R-1)*Hex.height*3/4;

		}
		public Hex getHex(int q, int r){

			return Hexes[q][r];

		}

		public Hex getNeighbor(int q, int r, int direction){

			int[] d = neighbors[direction];

			// If no such hex exists ie it is out of bounds,
			// return null

			try {

				return Hexes[q + d[0]][r + d[1]];

			} catch (Exception e) {

				return null;
			}
		}

		public void draw(SpriteBatch batch){

			for (int q = ARRAY_WIDTH - 1; q >= 0; q--){

				for (int r = ARRAY_HEIGHT - 1; r >= 0; r--){
					Hexes[q][r].draw(batch, 1);
				}			
			}
		}

		class Hex extends Actor{

			//	Hex
			//	picwidth = 96
			//	picheight = 96
			//	size = height/2
			//	vert distance = height*3/4 = 72
			//	hor dist = width

			public static final int TOP_RIGHT = 0, RIGHT = 1, BOTTOM_RIGHT = 2,
					BOTTOM_LEFT = 3, LEFT = 4, TOP_LEFT = 5;

			public int q, r;   // Grid position
			public float x, y;  // Pixel position

			private boolean isUsed = true;
			private boolean isSelected = false;

			public static final float width = 96, height = 96, size = width/2, vert_dist = height*3/4, hor_dist = width/2;

			public Hex(final int q, final int r){
				this.q = q;
				this.r = r;
				setBounds(getX(), getY() + 24, width, 48); //TODO centre hitbox of hex

				addListener(new ActorGestureListener(){

					public void fling(InputEvent event, float velocityX, float velocityY, int button){

					}

					public void tap(InputEvent event, float x, float y, int count, int button){
						Gdx.app.log("Hex", "Tapped [" + q + ", " + r + "]" );
						isSelected = !isSelected;
						//flipNeighbors();
					}
				});
			}

			protected void flipNeighbors() {

				for (int i = 0; i < 6; i++){

					try {
						Hexes[q + neighbors[i][0]][r + neighbors[i][1]].isSelected = !Hexes[q + neighbors[i][0]][r + neighbors[i][1]].isSelected;

					} catch (Exception e) {
						Gdx.app.log("Error", "Accessed Hex not in array.");
					}
				}

			}

			public boolean isUsed() {

				return isUsed ;
			}

			public void setUsed(boolean used){
				isUsed = used;		

				if (isUsed == false)
					setTouchable(Touchable.disabled);
			}

			public float getX(){

				return (float) (size*1.16f*Math.sqrt(3f) * (q + r/2f)) - width/2; //to centre [0, 0] at x = 0, y = 0. Useful in some algorithms

			}

			public float getY(){

				return (float) (size*3f/2f * r) - height/2; //to centre [0, 0] at x = 0, y = 0. Useful in some algorithms

			}

			@Override
			public void act(float delta) {
				// TODO Auto-generated method stub
				super.act(delta);
			}

			@Override
			public void draw(Batch batch, float parentAlpha) {
				if (isUsed()){

					if (!isSelected)
						batch.draw(ImageCache.getTexture("hex"), getX(), getY());		
					else
						batch.draw(ImageCache.getTexture("hex_selected"), getX(), getY());		

				}

				if (debug)
					Assets.font24.drawMultiLine(batch, "[" + q + ", " + r + "]", getX() , getY() + 60, width, HAlignment.CENTER);

			}

			public int getCubeX(){		
				return q;				
			}
			public int getCubeY(){
				return -q - r;
			}
			public int getCubeZ(){
				return r;
			}
			public int getAxialQ(){
				return q;
			}
			public int getAxialR(){
				return r;
			}
		}
	}

}
