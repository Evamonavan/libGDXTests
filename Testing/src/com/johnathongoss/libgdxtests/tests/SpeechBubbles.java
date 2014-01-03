package com.johnathongoss.libgdxtests.tests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ImageCache;
import com.johnathongoss.libgdxtests.screens.BlankScreen;
import com.johnathongoss.libgdxtests.screens.MainMenu;
import com.johnathongoss.libgdxtests.utils.SpeechBubble;

public class SpeechBubbles extends BlankScreen implements InputProcessor{

	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	private float width, height, BUTTON_WIDTH, BUTTON_HEIGHT;
	private String testName = "Speech Bubbles |";
	private boolean usePics = false;
	
    // SpeechBubble pool.
    private final Pool<SpeechBubble> speechBubblePool = new Pool<SpeechBubble>() {
        @Override
        protected SpeechBubble newObject() {
                return new SpeechBubble();
        }
    };

	private String texts[] = {"Did you see that ludicrous display last night?", "What did the apple say to the tree?",
			"Don't do that!", "Watch it!", "Take your time then.",
			"Ah!", "Ouch!", "This piece of String is needlessly long! It's ridiculous!",
			"Stop touching that!", "", "Lorde's album is pretty good.", "You should check it out.",
			"...", "You'll break the screen doing that.",
			"A wild speech bubble appears!",
	"Leeeeeeeeeeeeeeee eeeennnnnnnnnn nnngggggggggg ttttthhhhhhhhh hhhhhh Teeeeeess sssssssttt ttt!"};

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
		Assets.font24.drawMultiLine(batchui, "Free: " + speechBubblePool.getFree() + " |", 0, 48, width, HAlignment.RIGHT);
		Assets.font24.drawMultiLine(batchui, testName, 0, 24, width, HAlignment.RIGHT);
		batchui.end();
	}

	private void checkSpeechBubbles() {

		for (Actor sb : stage.getActors()){

			if (sb instanceof SpeechBubble && !((SpeechBubble) sb).isAlive()){
				stage.getActors().removeValue(sb, true);
                speechBubblePool.free((SpeechBubble) sb);	
			}

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

		backbutton = new TextButton("Toggle Pics", skin);
		backbutton.setBounds(width - BUTTON_WIDTH, height - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		backbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				usePics = !usePics;
			}
		});		
		stageui.addActor(backbutton);
	}	
	
	@Override
	public void dispose(){
		super.dispose();
		skin.dispose();		
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		SpeechBubble sb = speechBubblePool.obtain();
		
		if (usePics)
			sb.init(texts[MathUtils.random(0, texts.length - 1)], screenX, height + -screenY, ImageCache.getTexture("background"));
		else
			sb.init(texts[MathUtils.random(0, texts.length - 1)], screenX, height + -screenY);
		
		sb.setColor(new Color(MathUtils.random(0, 1f), MathUtils.random(0, 1f), MathUtils.random(0, 1f), 1f));
		stage.addActor(sb);

		return false;
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
