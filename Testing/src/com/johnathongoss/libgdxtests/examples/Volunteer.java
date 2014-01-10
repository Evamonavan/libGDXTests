package com.johnathongoss.libgdxtests.examples;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.EffectsCache;
import com.johnathongoss.libgdxtests.ImageCache;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.Sounds;
import com.johnathongoss.libgdxtests.entities.DamageImage;
import com.johnathongoss.libgdxtests.entities.Effect;
import com.johnathongoss.libgdxtests.entities.SpeechBubble;
import com.johnathongoss.libgdxtests.screens.Examples;
import com.johnathongoss.libgdxtests.screens.MainMenu;

public class Volunteer implements Screen{
	MyInputProcessor input = new MyInputProcessor();
	MyGame game;
	OrthographicCamera cam;
	Stage stage, stageui;
	SpriteBatch batch;
	private TextButton backButton;

	Jeremy jeremy;

	int damage = -1;

	private int weaponIndex = 0;
	String[] weaponNames = {"Sword", "Heal"};

	private Array<PooledEffect> Effects;
	
	private final Pool<SpeechBubble> speechBubblePool = new Pool<SpeechBubble>() {
        @Override
        protected SpeechBubble newObject() {
                return new SpeechBubble();
        }
    };

	public Volunteer(MyGame game){
		this.game = game;
		cam = new OrthographicCamera();		
		stage = new Stage();
		stageui = new Stage();
		batch = new SpriteBatch();

		Effects = new Array<PooledEffect>();

		jeremy = new Jeremy();
		jeremy.setPosition(game.getWidth()/2, game.getHeight()/2);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		
		checkSpeechBubbles();
		for(Actor effect : stage.getActors()){
			
			if (effect instanceof Effect && !((Effect) effect).isAlive())
				stage.getActors().removeValue(effect, true);
		}
		
		checkDamageImages(); //TODO no need for poolable
		batch.begin();
		for (PooledEffect effect : Effects){
			effect.draw(batch, delta);
			if (effect.isComplete()){
				Effects.removeValue(effect, true);
				effect.reset();
				effect.free();					
			}
		}
		batch.end();

		stage.act(delta);
		stage.draw();	

		stageui.act(delta);
		stageui.draw();
	}
	
	private void checkSpeechBubbles() {

		for (Actor sb : stageui.getActors()){

			if (sb instanceof SpeechBubble && !((SpeechBubble) sb).isAlive()){
				stageui.getActors().removeValue(sb, true);
                speechBubblePool.free((SpeechBubble) sb);	
			}

		}
	}

	@Override
	public void resize(int width, int height) {
		backButton.setBounds(0, height - game.getButtonHeight(), game.getButtonWidth(), game.getButtonHeight());
	}

	@Override
	public void show() {
		InputMultiplexer im = new InputMultiplexer(stage, stageui, input);
		Gdx.input.setInputProcessor(im);		
		Gdx.input.setCatchBackKey(true);	

		cam.setToOrtho(false, game.getWidth(), game.getHeight());
		cam.update();   
		stage.setCamera(cam);
		cam.zoom = 0.4f;
		stage.addActor(jeremy);

		/*
		 * Back Button
		 */

		backButton = new TextButton("Back", Assets.skin);
		backButton.setBounds(0, game.getHeight() - game.getButtonHeight(), game.getButtonWidth(), game.getButtonHeight());
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Examples(game));
			}
		});	

		stageui.addActor(backButton);	

		/*
		 * Change Weapon Button
		 */

		final TextButton weaponButton = new TextButton(weaponNames[weaponIndex], Assets.skin);
		weaponButton.setBounds(game.getWidth() - game.getButtonWidth(), game.getHeight() - game.getButtonHeight(), game.getButtonWidth(), game.getButtonHeight());
		weaponButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponIndex++;
				if (weaponIndex >= weaponNames.length)
					weaponIndex = 0;
				weaponButton.setText(weaponNames[weaponIndex]);
				
				addTesterSpeech();
				
			}
		});	

		stageui.addActor(weaponButton);	
		
		addTesterSpeech();
	}

	protected void addTesterSpeech() {
		SpeechBubble sb = speechBubblePool.obtain();
		sb.setColor(new Color(0.5f, 0.5f, 0.5f,  1f));
		switch (weaponIndex){
		
		case 0:{
			sb.init("This might hurt a bit mate.", game.getWidth(), 0);
			break;
		}
		case 1:{
			sb.init("Hang in there Jeremy!", game.getWidth(), 0);
			break;
		}
		
		}	
		
		stageui.addActor(sb);
		
	}

	private void checkDamageImages(){

		for (Actor di : stage.getActors()){

			if (di instanceof DamageImage && !((DamageImage) di).isAlive()){
				stage.getActors().removeValue(di, true);
				EffectsCache.free((DamageImage) di);	
			}
		}		
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
		stage.dispose();
		stageui.dispose();
		batch.dispose();		
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
			//			damage = MathUtils.random(-5, 5);
			//			DamageImage di = EffectsCache.getDamageEffect();
			//
			//			di.init(damage, jeremy.getX(), jeremy.getY());
			//			stage.addActor(di);
			
			attackWithWeapon();
			

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

	class Jeremy extends Actor{		

		private Animation animation;
		private TextureRegion currentFrame;
		private int hp_max = 30, hp_current = 30;

		public Jeremy(){	
			animation = new Animation(1f, Assets.spriteSheet.createSprites("man_die"));		
			currentFrame = animation.getKeyFrame(0f);
			
			setBounds(0, 0, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
		}

		@Override
		public void draw(Batch batch, float parentAlpha){			
			batch.draw(currentFrame, getX(), getY());

			Assets.font24.drawMultiLine(batch, hp_current + "/" + hp_max, getX(), getY(), getWidth(), HAlignment.CENTER);
		}
		
		@Override
		public float getX() {
			// TODO Auto-generated method stub
			return super.getX();
		}

		@Override
		public void setPosition(float x, float y) {			
			super.setPosition(x, y);
		}

		public void addHP(int amount){

			hp_current += amount;

			if (hp_current > hp_max)
				hp_current = hp_max;

			if (hp_current < 0)
				hp_current = 0;
		}
		
		@Override
		public void act(float delta) {			
			super.act(delta);
			
			if (hp_current <= 3)
				currentFrame = animation.getKeyFrame(5f);
			else if (hp_current <= 8)
				currentFrame = animation.getKeyFrame(4f);
			else if (hp_current <= 15)
				currentFrame = animation.getKeyFrame(3f);
			else if (hp_current <= 22)
				currentFrame = animation.getKeyFrame(2f);
			else if (hp_current <= 26)
				currentFrame = animation.getKeyFrame(1f);
			else
				currentFrame = animation.getKeyFrame(0f);
			
		}
	}

	abstract class WeaponEffect extends Actor{

		protected Animation animation;
		private TextureRegion currentFrame;
		private float time = 0;
		protected Jeremy actor;
		protected int damage;
		protected boolean alive = false;
		protected Color color = Color.WHITE;

		@Override
		public void draw(Batch batch, float parentAlpha) {	
			// TODO sort out offset / origin
			batch.setColor(color);
			if (alive)
				batch.draw(currentFrame, actor.getX(), actor.getY());
			batch.setColor(Color.WHITE);
		}

		@Override
		public void act (float delta) {
			super.act(delta);				
			time += delta;		
			
			currentFrame = animation.getKeyFrame(time);	

			if (alive && animation.isAnimationFinished(time)){
				perform();
				alive = false;
			}
		}

		public void setActor(Jeremy actor){
			this.actor = actor;
		}
		public void setDamage(int damage){
			this.damage = damage;
		}
		public void setAnimation(Animation animation){
			this.animation = animation;
		}
		public boolean isAlive(){
			return alive;
		}

		protected abstract void perform();
	}
	
	class Heal extends WeaponEffect{

		Heal(Jeremy actor, int damage){
			alive = true;
			color = new Color(0, 1f, 0.5f, 1f);
			this.damage = damage;			
			this.actor = actor;
			animation = new Animation(0.1f, Assets.spriteSheet.createSprites("fx_sparkle"), Animation.NORMAL);
			
		}

		@Override
		protected void perform() {
			DamageImage di = EffectsCache.getDamageEffect();
			di.init(this.damage, actor.getX() + actor.getWidth()/2, actor.getY() + actor.getHeight()/3);
			stage.addActor(di);
			actor.addHP(this.damage);
		}
	}

	class Sword extends WeaponEffect{

		Sword(Jeremy actor, int damage){
			
			alive = true;
			this.damage = damage;			
			this.actor = actor;
			animation = new Animation(0.05f, Assets.spriteSheet.createSprites("fx_slash"), Animation.NORMAL);
			Sounds.PlaySound(Sounds.SoundPointer.SLASH_01);
		}

		@Override
		protected void perform() {
			DamageImage di = EffectsCache.getDamageEffect();
			di.init(this.damage, actor.getX() + actor.getWidth()/2, actor.getY() + actor.getHeight()/3);
			stage.addActor(di);
			actor.addHP(this.damage);
			Sounds.PlaySound(Sounds.SoundPointer.HIT_01);
		}
	}

	public void attackWithWeapon() {
		switch (weaponIndex){
		case 0:{
			stage.addActor(new Sword(jeremy, MathUtils.random(-2, -1)));			
			break;
			
		}		
		case 1:{
			
			stage.addActor(new Heal(jeremy, MathUtils.random(1, 2)));			
			break;
		}		
		
		}		
	}
}
