package com.johnathongoss.libgdx.examples;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.johnathongoss.libgdxtests.screens.Examples;
import com.johnathongoss.libgdxtests.utils.SpeechBubble;

public class Conversation implements Screen {
	private Game game;
	private SpriteBatch batch;
	private Stage stage, stageui;
	private float width, height, BUTTON_WIDTH, BUTTON_HEIGHT;
	private TextButton backButton;
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

	private Talker TBrian, TRon, TVeronica, TWorker;	

	private Array<Speech> conversation;	
	private int lineIndex = 0;	
	
	 private final Pool<SpeechBubble> speechBubblePool = new Pool<SpeechBubble>() {
	        @Override
	        protected SpeechBubble newObject() {
	                return new SpeechBubble();
	        }
	    };

	public Conversation (Game game){
		this.game = game;

		width = Gdx.app.getGraphics().getWidth();
		height = Gdx.app.getGraphics().getHeight();

		BUTTON_WIDTH = width/7;
		BUTTON_HEIGHT = height/8;

		stage = new Stage();
		stageui = new Stage();
	}

	@Override
	public void show() {

		Gdx.input.setInputProcessor(stageui);

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

		conversation = new Array<Speech>();

		TBrian = new Talker(ImageCache.getTexture("brian"), new Color(0.1f, 0.45f, 1f, 1f), width/4f, height/2f);
		TRon = new Talker(ImageCache.getTexture("ron"), new Color(0.9f, 0.65f, 0.1f, 1f), width*(3f/4f), height/2f);
		TVeronica = new Talker(ImageCache.getTexture("veronica"), new Color(1f, 0.1f, 0.8f, 1f), width/2f, height/4f);
		TWorker = new Talker(ImageCache.getTexture("worker"), new Color(0.5f, 0.35f, 0.3f, 1f), width/2f, height*(3f/4f));

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

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);	
		
		batch.begin();
		Assets.font24.drawMultiLine(batch, "Conversation Example |", 0, 24, width, HAlignment.RIGHT);
		batch.end();
		
		stage.act(delta);
		checkSpeechBubbles();
		stage.draw();	
		
		stageui.act(delta);
		stageui.draw();
	}

	private void checkSpeechBubbles() {

		for (Actor sb : stage.getActors()){

			if (sb instanceof SpeechBubble && !((SpeechBubble) sb).isAlive()){
				stage.getActors().removeValue(sb, true);
				speechBubblePool.free((SpeechBubble) sb);
				nextLine();
			}

		}
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;

		BUTTON_WIDTH = width/7;
		BUTTON_HEIGHT = height/8;	

		backButton.setBounds(width - BUTTON_WIDTH, height - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		stage.setViewport(width, height, true);
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
			sb.setColor(talker.getColor());
			stage.addActor(sb);					
		}	
	}

	class Talker{

		Color color;	
		float x, y;
		TextureRegion pic;
		
		
		Talker(TextureRegion pic, Color color, float x, float y){
			this.pic = pic;
			this.x = x;
			this.y = y;

			this.color = color;		
		}

		public TextureRegion getPic() {			
			return pic;
		}

		public float getX(){
			return x;			
		}

		public float getY(){
			return y;	
		}

		public Color getColor() {			
			return color;
		}

		public void setColor(Color color){

			this.color = color;

		}

	}
}
