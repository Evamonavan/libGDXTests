package com.johnathongoss.libgdxtests.examples;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ImageCache;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.entities.MyTimer;
import com.johnathongoss.libgdxtests.entities.SpeechBubble;
import com.johnathongoss.libgdxtests.screens.Examples;

public class Talking implements Screen{
	
	MyInputProcessor input = new MyInputProcessor();
	private MyGame game;
	private SpriteBatch batch;
	private Stage stage, stageui, stageBrick;
	private TextButton backButton;
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

	private Talker TBrian, TRon, TVeronica, TWorker, TBrick;	

	private Array<Speech> conversation;	
	private int lineIndex = 0;	

	MyTimer brickTimer;
	
	private String brickLines[] = {"That's a good one.", 
			"Brian Fantana.", "Brian.", "Veronica.",
			"I don't know what we're yelling about!",
			"Loud noises!",
			"Yeah, I stabbed a man in the heart.",
			"I killed a guy with a trident.",
			"I don't know.",
			"I ate a big, red candle.",
			"Cough. Look over here.",
			"I would like to extend to you an invitation to the pants party.",
			"Party with pants?",
			"All right. Let's go.",
			"Fantastic.",
			"I'm riding a furry tractor.",
			"I love... carpet.",
			"I love lamp.",
			"I love... desk.",
			"Bears can smell the menstruation.",
			"High pressure systems..."};


	private final Pool<SpeechBubble> speechBubblePool = new Pool<SpeechBubble>() {
		@Override
		protected SpeechBubble newObject() {
			return new SpeechBubble();
		}
	};

	public Talking (MyGame game){
		this.game = game;

		stage = new Stage();
		stageui = new Stage();
		stageBrick = new Stage();
	}

	@Override
	public void show() {

		InputMultiplexer im = new InputMultiplexer(stageui, stage, input);
		Gdx.input.setInputProcessor(im);		
		Gdx.input.setCatchBackKey(true);

		backButton = new TextButton("Back", skin);
		backButton.setBounds(0, game.getHeight() - game.getButtonHeight(), game.getButtonWidth(), game.getButtonHeight());
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Examples(game));
			}
		});	

		stageui.addActor(backButton);

		conversation = new Array<Speech>();

		TBrian = new Talker("brick", ImageCache.getTexture("brian"), new Color(0.1f, 0.45f, 1f, 0.8f), game.getWidth()/4f, game.getHeight()/2f);
		TRon = new Talker("brick", ImageCache.getTexture("ron"), new Color(0.9f, 0.65f, 0.1f, 0.8f), game.getWidth()*(3f/4f), game.getHeight()/2f);
		TVeronica = new Talker("brick", ImageCache.getTexture("veronica"), new Color(1f, 0.1f, 0.8f, 0.8f), game.getWidth()/2f, game.getHeight()/4f);
		TWorker = new Talker("brick", ImageCache.getTexture("worker"), new Color(0.5f, 0.35f, 0.3f, 0.8f), game.getWidth()/2f, game.getHeight()*(3f/4f));

		TBrick = new Talker("brick", ImageCache.getTexture("brick"), new Color(0.3f, 0.3f, 0.3f, 0.8f), MathUtils.random(0, game.getWidth()), MathUtils.random(0, game.getHeight()));
		
		// TODO Brick extends Talker class?
		brickTimer = new MyTimer(MathUtils.random(0f, 0f)) {
			
			@Override
			protected void perform() {
				TBrick.setTarget(new Vector2(MathUtils.random(0, game.getWidth()), MathUtils.random(0, game.getHeight())));
				brickTimer.setCap(MathUtils.random(4f, 10f));
			}
		};
		brickTimer.setRepeating(true);
		brickTimer.start();
		
		stage.addActor(TBrick);
		stage.addActor(TBrian);
		stage.addActor(TRon);
		stage.addActor(TVeronica);
		stage.addActor(TWorker);

		cAdd(TBrian, "Well,");
		cAdd(TBrian, "I'll give this little cookie an hour before we're doing the no-pants dance.");
		cAdd(TBrian, "Time to musk up.");
		cAdd(TRon, "Wow.");
		cAdd(TRon, "Never ceases to amaze me.");
		cAdd(TRon, "What cologne are you gonna go with?");
		cAdd(TRon, "London Gentleman or...");
		cAdd(TRon, "Wait.");
		cAdd(TRon, "No, no, no.");
		cAdd(TRon, "Hold on.");
		cAdd(TRon, "Blackbeard's Delight?");
		cAdd(TBrian, "No.");
		cAdd(TBrian, "She gets a special cologne.");
		cAdd(TBrian, "It's called Sex Panther by Odeon.");
		cAdd(TBrian, "It's illegal in nine countries.");
		cAdd(TBrian, "Yep.");
		cAdd(TBrian, "It's made with bits of real panther.");
		cAdd(TBrian, "So you know it's good.");
		cAdd(TRon, "It's quite pungent.");
		cAdd(TBrian, "Oh yeah.");
		cAdd(TRon, "It's a formidable scent.");
		cAdd(TRon, "It stings the nostrils.");		
		cAdd(TRon, "In a good way.");
		cAdd(TBrian, "Yeah.");
		cAdd(TRon, "Brian,");
		cAdd(TRon, "I'm gonna be honest with you.");
		cAdd(TRon, "That smells like pure gasoline.");	
		cAdd(TBrian, "They've done studies, you know?");
		cAdd(TBrian, "60% of the time, it works every time.");
		cAdd(TRon, "That doesn't make any sense.");	
		cAdd(TBrian, "Well,");
		cAdd(TBrian, "let's go see if we can make this little kitty purr.");
		cAdd(TBrian, "Hey, sweet cheeks.");
		cAdd(TBrian, "Got an invite I'd like to extend your way.");
		cAdd(TVeronica, "My God.");
		cAdd(TVeronica, "What is that smell?");
		cAdd(TVeronica, "Oh!");
		cAdd(TBrian, "That's the smell of desire, my lady.");
		cAdd(TVeronica, "God no.");
		cAdd(TVeronica, "It smells like -");
		cAdd(TVeronica, "- like a used diaper filled with Indian food.");
		cAdd(TVeronica, "Oh!");
		cAdd(TVeronica, "Excuse me.");
		cAdd(TBrian, "You know,");
		cAdd(TBrian, "desire smells like that to some people.");
		cAdd(TWorker, "What is that?");
		cAdd(TWorker, "It smells like a turd covered in burnt hair!");
		cAdd(TBrian, "Oh.");

		batch = new SpriteBatch();

		lineIndex = 0;

		nextLine();
	}	

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);	

		batch.begin();
		Assets.font24.drawMultiLine(batch, "Talking Example |", 0, 24, game.getWidth(), HAlignment.RIGHT);		
		batch.end();
		
		stage.act(delta);
		checkSpeechBubbles();
		stage.draw();			
		
		stageBrick.act(delta);
		stageBrick.draw();

		stageui.act(delta);
		stageui.draw();
		
		brickTimer.update(delta);
	}
	
	private void cAdd(Talker t, String text) {

		conversation.add(new Speech(t, text));

	}

	private void nextLine() {

		conversation.get(lineIndex).Say(stage);	

		lineIndex++;

		if (lineIndex == conversation.size){
			lineIndex = 0;
		}
	}

	private void checkSpeechBubbles() {		
		
		for (Actor sb : stage.getActors()){

			if (sb instanceof SpeechBubble && !((SpeechBubble) sb).isAlive()){
				stage.getActors().removeValue(sb, true);
				speechBubblePool.free((SpeechBubble) sb);
				nextLine();
			}

		}
		
		for (Actor sb : stageBrick.getActors()){

			if (sb instanceof SpeechBubble && !((SpeechBubble) sb).isAlive()){
				stageBrick.getActors().removeValue(sb, true);
				speechBubblePool.free((SpeechBubble) sb);
			}

		}
	}

	@Override
	public void resize(int width, int height) {		

		backButton.setBounds(0, height - game.getButtonHeight(), game.getButtonWidth(), game.getButtonHeight());
		//stage.setViewport(width, height, true);
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
		stageBrick.dispose();
		skin.dispose();
	}

	class Speech{

		Talker talker;
		String text;

		public Speech(Talker talker, String text){
			this.talker = talker;
			this.text = text;			
		}

		public void Say(Stage stage){

			SpeechBubble sb = speechBubblePool.obtain();
			sb.init(text, talker.getX(), talker.getY(), talker.getPic());
			sb.setFollow(talker);
			sb.setColor(talker.getColor());
			stage.addActor(sb);					
		}	
	}

	class Talker extends Actor{

		Color color;
		TextureRegion pic;

		Vector2 position;
		Vector2 target;
		Vector2 velocity;		

		Animation aWalk_d, aWalk_u, aWalk_l, aWalk_r;
		private Array<Sprite> sprites;
		private boolean moving = false;
		private float walkSpeed = 1.5f;
		private TextureRegion currentFrame;
		private String name;
		private float time = 0;

		Talker(String name, TextureRegion pic, Color color, float x, float y){
			this.pic = pic;
			this.name = name;			

			sprites = Assets.spriteSheet.createSprites(name + "_d");				
			aWalk_d = new Animation(0.1f/walkSpeed , sprites, Animation.LOOP);

			sprites = Assets.spriteSheet.createSprites(name + "_u");				
			aWalk_u = new Animation(0.1f/walkSpeed , sprites, Animation.LOOP);			

			sprites = Assets.spriteSheet.createSprites(name + "_l");				
			aWalk_l = new Animation(0.1f/walkSpeed , sprites, Animation.LOOP);	

			sprites = Assets.spriteSheet.createSprites(name + "_r");				
			aWalk_r = new Animation(0.1f/walkSpeed , sprites, Animation.LOOP);	

			position = new Vector2();
			target = new Vector2();
			velocity = new Vector2();

			setPosition(new Vector2(x, y));
			setHeight(64);
			this.color = color;		
		}

		public float getX(){
			return position.x;

		}
		public float getY(){
			return position.y;

		}

		public TextureRegion getPic() {			
			return pic;
		}

		public Color getColor() {			
			return color;
		}
		
		public String getName(){
			return name;
		}

		public void setColor(Color color){
			this.color = color;
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

		@Override
		public void draw(Batch batch, float parentAlpha){
			batch.draw(currentFrame, position.x - 32, position.y);

		}

		@Override
		public void act(float delta){
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
			stageBrick.clear();
			SpeechBubble sb = speechBubblePool.obtain();
			sb.init(brickLines[MathUtils.random(0, brickLines.length - 1)], TBrick.getX(), TBrick.getY() + TBrick.getHeight(), TBrick.getPic());
			sb.setColor(TBrick.getColor());
			sb.setFollow(TBrick);
			stageBrick.addActor(sb);
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
