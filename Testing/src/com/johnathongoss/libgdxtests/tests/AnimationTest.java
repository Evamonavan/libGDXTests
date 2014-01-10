package com.johnathongoss.libgdxtests.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.screens.MainMenu;

public class AnimationTest implements Screen, InputProcessor{

	MyGame game;
	private String testName = "Animation Test |";

	SpriteBatch batch;
	Stage stage, stageui;
	OrthographicCamera cam;
	Walker man;

	public AnimationTest(MyGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);	

		stage.act(delta);
		stage.draw();

		batch.begin();
		man.act(delta);
		man.draw(batch, 1f);
		Assets.font24.drawMultiLine(batch, testName, 0, 24, Gdx.app.getGraphics().getWidth(), HAlignment.RIGHT);
		Assets.font24.setColor(1, 1, 1, 0.8f);
		Assets.font24.drawMultiLine(batch, "FPS: " + Gdx.app.getGraphics().getFramesPerSecond(), 0, 24, Gdx.app.getGraphics().getWidth(), HAlignment.LEFT);
		Assets.font24.setColor(1, 1, 1, 1f);
		batch.end();

		stageui.act(delta);
		stageui.draw();

	}

	@Override
	public void resize(int width, int height) {
		stageui.setViewport(width, height);

	}

	@Override
	public void show() {		
		batch = new SpriteBatch();
		stage = new Stage();
		stageui = new Stage();

		cam = new OrthographicCamera();
		cam.setToOrtho(false, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
		cam.update();   
		stage.setCamera(cam);		

		man = new Walker("man");
		man.setPosition(new Vector2(Gdx.app.getGraphics().getWidth()/2, Gdx.app.getGraphics().getHeight()/2));
		
		InputMultiplexer im = new InputMultiplexer(stageui, stage, this);
		Gdx.input.setInputProcessor(im);		
		Gdx.input.setCatchBackKey(true);

		TextButton button = new TextButton("Back", Assets.skin);
		button.setWidth(Gdx.app.getGraphics().getWidth()/7);
		button.setHeight(Gdx.app.getGraphics().getHeight()/8);				
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				game.setScreen(new MainMenu(game));
			}
		});	
		button.setPosition(0, Gdx.app.getGraphics().getHeight() - button.getHeight());
		stageui.addActor(button);
	}

	@Override
	public void hide() {
		dispose();

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		stageui.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.BACK || 
				keycode == Keys.BACKSPACE ||
				keycode == Keys.ESCAPE){
			game.setScreen(new MainMenu(game));
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		man.setTarget(new Vector2(screenX, Gdx.app.getGraphics().getHeight() - screenY));

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		man.setTarget(new Vector2(screenX, Gdx.app.getGraphics().getHeight() - screenY));

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

	class Walker extends Actor{

		Vector2 position;
		Vector2 target;
		Vector2 velocity;		
		
		Animation aWalk_d, aWalk_u, aWalk_l, aWalk_r;
		private Array<Sprite> sprites;
		private boolean moving = false;
		private float walkSpeed = 1.5f;
		private TextureRegion currentFrame;
		private float time = 0;
		Walker(String spriteName){			

			/** Load animations */
			// TODO createSprites needs to be cached
			sprites = Assets.spriteSheet.createSprites(spriteName + "_d");				
			aWalk_d = new Animation(0.1f/walkSpeed , sprites, Animation.LOOP);

			sprites = Assets.spriteSheet.createSprites(spriteName + "_u");				
			aWalk_u = new Animation(0.1f/walkSpeed , sprites, Animation.LOOP);			

			sprites = Assets.spriteSheet.createSprites(spriteName + "_l");				
			aWalk_l = new Animation(0.1f/walkSpeed , sprites, Animation.LOOP);	

			sprites = Assets.spriteSheet.createSprites(spriteName + "_r");				
			aWalk_r = new Animation(0.1f/walkSpeed , sprites, Animation.LOOP);	

			position = new Vector2();
			target = new Vector2();
			velocity = new Vector2();
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {			

			// TODO sort out offset / origin
			batch.draw(currentFrame, position.x - 32, position.y);

			//Assets.font24.draw(batch, moving  + "", position.x, position.y + 48);
			//Assets.font24.draw(batch, target.x + "," + target.y, position.x, position.y + 24);
			//Assets.font24.draw(batch, velocity.x + "," + velocity.y, position.x, position.y);
			//Assets.font24.draw(batch, position.x + "," + position.y, position.x, position.y - 24);
		}

		@Override
		public void act (float delta) {
			super.act(delta);	
			
			time += delta;
			
			/** Which animation logic. */
			if (velocity.y > 0 && Math.abs(velocity.y) >= Math.abs(velocity.x))
				currentFrame = aWalk_u.getKeyFrame(time);	
			else if (velocity.y < 0 && Math.abs(velocity.y) >= Math.abs(velocity.x))
				currentFrame = aWalk_d.getKeyFrame(time);
			else if (velocity.x > 0 )
				currentFrame = aWalk_r.getKeyFrame(time);
			else if (velocity.x < 0)
				currentFrame = aWalk_l.getKeyFrame(time);			
			else
				currentFrame = aWalk_d.getKeyFrame(time);

			/** Still frame. */
			if (!moving)
				currentFrame = aWalk_d.getKeyFrame(0);

			/** Meeting target logic */
			if (velocity.x > 0 && position.x > target.x)
				velocity.x = 0;

			if (velocity.x < 0 && position.x < target.x)
				velocity.x = 0;

			if (velocity.y > 0 && position.y > target.y)
				velocity.y = 0;

			if (velocity.y < 0 && position.y < target.y)
				velocity.y = 0;	

			/** If the magnitude of velocity is 0, it's not moving. */
			if (velocity.len() == 0){
				moving = false; 
				time = 0;
			}
			else
				moving = true;

			if (moving)
				position.add(velocity);
		}
		
		public Vector2 getPosition() {
			return position;
		}
		public void setPosition(Vector2 position){			
			this.position = position;
						
		}

		public void setTarget(Vector2 target){
			this.target = target;

			Vector2 temp = new Vector2(target);

			/** Standardise the movement speed. Movement to unit vector*/
			temp.scl(1/temp.sub(position).len());

			/** Scale to walkSpeed */
			velocity = temp.scl(walkSpeed);			
		}
	}
}
