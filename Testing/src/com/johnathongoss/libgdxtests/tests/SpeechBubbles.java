package com.johnathongoss.libgdxtests.tests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ImageCache;
import com.johnathongoss.libgdxtests.screens.BlankScreen;
import com.johnathongoss.libgdxtests.screens.MainMenu;
import com.johnathongoss.libgdxtests.utils.MyTimer;

public class SpeechBubbles extends BlankScreen implements InputProcessor{

	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	private float width, height, BUTTON_WIDTH, BUTTON_HEIGHT;
	private String testName = "Speech Bubbles |";

	private String texts[] = {"What did the apple say to the tree?",
			"Don't do that!", "Watch it!", "Take your time then.",
			"Ah!", "Ouch!", "This piece of String is needlessly long! It's ridiculous!",
			"Stop touching that!", "", "Lorde's album is pretty good.", "You should check it out.",
			"...", "Did you see that ludicrous display last night?", "You'll break the screen doing that.",
			"A wild speech bubble appears!",
			"Leeeeeeeeeeeeeeeeeeeennnnnnnnnnnnnggggggggggttttthhhhhhhhhhhhhhh Teeeeeessssssssstttttt!"};

	public SpeechBubbles(Game game) {
		super(game);

		width = Gdx.app.getGraphics().getWidth();
		height = Gdx.app.getGraphics().getHeight();

		BUTTON_WIDTH = width/7;
		BUTTON_HEIGHT = height/8;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		checkSpeechBubbles();
		stage.draw();

		stageui.act(delta);
		stageui.draw();
		
		batchui.begin();
		Assets.font24.drawMultiLine(batchui, testName, 0, 24, width, HAlignment.RIGHT);
		batchui.end();
	}

	private void checkSpeechBubbles() {

		for (Actor sb : stage.getActors()){

			if (sb instanceof SpeechBubble && ((SpeechBubble) sb).isDead())
				stage.getActors().removeValue(sb, true);

		}
	}

	@Override
	public void show() {
		InputMultiplexer im = new InputMultiplexer(stageui, stage, this);
		Gdx.input.setInputProcessor(im);		

		TextButton backbutton = new TextButton("Main Menu", skin);
		backbutton.setBounds(0, height - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		backbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MainMenu(game));
			}
		});		
		stageui.addActor(backbutton);	

	}

	class SpeechBubble extends Actor{

		private NinePatch bubble;
		private MyTimer life;

		boolean dead = false;

		String text;
		BitmapFont font = Assets.font24;

		float padX = 10, padY = 35;

		SpeechBubble(String text, float x, float y, float width, float height, float time){

			this.text = text;			

			float sbWidth = font.getBounds(text).width + padX*2, 
					sbHeight = font.getBounds(text).height + padY*2;
			
			if (text == "" && sbWidth < 36)
				sbWidth = 36;	
			
			setWidth(sbWidth);
			setHeight(sbHeight);				
			
			setX(x);
			setY(y);		
			
			/**
			 * Keep in the screen
			 */
			
			if (getX() + sbWidth > Gdx.app.getGraphics().getWidth())
				setX(getX() - (sbWidth - (Gdx.app.getGraphics().getWidth() - getX())));
			
			if (getY() + sbHeight > Gdx.app.getGraphics().getHeight())
				setY(getY() - (sbHeight - (Gdx.app.getGraphics().getHeight() - getY())));

			bubble = ImageCache.CreatePatch("bubble");

			life = new MyTimer(time) {

				@Override
				protected void perform() {
					dead = true;

				}
			};
			life.start();

		}

		public void setPadding(float padX, float padY){

			this.padX = padX;
			this.padY = padY;

		}

		public void setFont(BitmapFont font){
			this.font = font;

		}
		public boolean isDead() {
			return dead;
		}

		@Override		
		public void draw(Batch batch, float parentAlpha){

			bubble.draw(batch, getX(), getY(), getWidth(), getHeight());
			font.drawMultiLine(batch, text, getX() + padX, getY() + getHeight() - padY, getWidth(), HAlignment.LEFT);
		}

		@Override
		public void act(float delta){
			super.act(delta);
			life.update(delta);

		}
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

		SpeechBubble sb = new SpeechBubble(texts[MathUtils.random(0, texts.length - 1)], screenX, height + -screenY, 100, 100, 2.5f);
		stage.addActor(sb);

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
