package com.johnathongoss.libgdxtests.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.entities.MyTimer;
import com.johnathongoss.libgdxtests.screens.Misc;
import com.johnathongoss.libgdxtests.tests.BlankTestScreen;

public class Timers extends BlankTestScreen{

	MyTimer timer;
	int count = 0;
	public Timers(MyGame game) {
		super(game);
		
		testName = "Timers Test |";
		timer = new MyTimer(3f) {			
			@Override
			protected void perform() {
				count++;
			}
		};
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act();		
		stage.draw();



		stageui.act();
		stageui.draw();

		batchui.begin();
		renderText();
		renderTestName(batchui);
		batchui.end();

		updateText();

		timer.update(delta);

	}

	@Override
	protected void renderText() {

		for (int i = 0; i < Text.size; i++){
			Assets.font32.drawMultiLine(batchui, Text.get(i), width/2, height/2 - 24*i, 0, HAlignment.CENTER);

		}

	}

	@Override
	public void show() {
		addBackButton();
		InputMultiplexer im = new InputMultiplexer(stageui, stage, this);
		Gdx.input.setInputProcessor(im);		
		Gdx.input.setCatchBackKey(true);
		
		/*
		 * Start
		 */		

		debugButton = new TextButton("Start", skin);
		debugButton.addListener(new ClickListener() {			

			@Override
			public void clicked(InputEvent event, float x, float y) {
				timer.start();
				//timer.reset();
				//count = 0;
			}

		});		
		buttons.add(debugButton);	
		
		/*
		 * Start
		 */		

		debugButton = new TextButton("Pause", skin);
		debugButton.addListener(new ClickListener() {			

			@Override
			public void clicked(InputEvent event, float x, float y) {
				timer.pause();
				//timer.reset();
				//count = 0;
			}

		});		
		buttons.add(debugButton);	
		
		/*
		 * Repeat
		 */		

		debugButton = new TextButton("Repeat", skin);
		debugButton.addListener(new ClickListener() {			

			@Override
			public void clicked(InputEvent event, float x, float y) {
				timer.setRepeating(!timer.isRepeating());
			}

		});		
		buttons.add(debugButton);
		
		/*
		 * Reset
		 */		

		debugButton = new TextButton("Reset", skin);
		debugButton.addListener(new ClickListener() {			

			@Override
			public void clicked(InputEvent event, float x, float y) {
				timer.reset();
				timer.pause();
				count = 0;
			}

		});		
		buttons.add(debugButton);				

		for (TextButton button : buttons)
			stageui.addActor(button);					

	}

	@Override
	protected void updateText() {
		Text.clear();
		Text.add("Times Completed: " + count);
		Text.add("Complete: " + timer.isComplete());	
		Text.add((int)timer.getCurrent() + "/" + (int)timer.getCap());	
		Text.add("Repeat: " + timer.isRepeating());	
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.BACK){
			game.setScreen(new Misc(game));
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
