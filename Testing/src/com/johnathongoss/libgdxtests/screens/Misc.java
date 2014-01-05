package com.johnathongoss.libgdxtests.screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdx.examples.FishTank;
import com.johnathongoss.libgdx.examples.FishTankBox2D;
import com.johnathongoss.libgdx.examples.PopCorns;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.misc.Dice;
import com.johnathongoss.libgdxtests.misc.Timers;
import com.johnathongoss.libgdxtests.tests.BlankTestScreen;
import com.johnathongoss.libgdxtests.tests.Camera2D;
import com.johnathongoss.libgdxtests.tests.Collision;

public class Misc extends BlankTestScreen implements InputProcessor{

	TextButton exampleButton;
	private Array<TextButton> buttons;
	public Misc(Game game) {
		super(game);				
		Text.add("Misc |");
		Text.add("bits and bobs |");
	}

	@Override
	public void render(float delta) {	
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);			
		stage.act(delta);
		stage.draw();	

		stageui.act(delta);
		stageui.draw();	
		
		renderText();		
	}	

	@Override
	public void show(){
		InputMultiplexer im = new InputMultiplexer(stageui, stage, this);
		Gdx.input.setInputProcessor(im);		
		Gdx.input.setCatchBackKey(true);

		backButton = new TextButton("Back", skin);
		backButton.setHeight(BUTTON_HEIGHT);
		backButton.setWidth(BUTTON_WIDTH);
		backButton.setPosition(width - BUTTON_WIDTH, height - BUTTON_HEIGHT);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MainMenu(game));
			}
		});	

		stageui.addActor(backButton);	


		buttons = new Array<TextButton>();

		/*
		 * Pop Corn
		 */

		debugButton = new TextButton("Timer", skin);
		debugButton.setHeight(BUTTON_HEIGHT);
		debugButton.setWidth(BUTTON_WIDTH);

		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Timers(game));
			}
		});		

		buttons.add(debugButton);

		/*
		 * Fish Tank
		 */

		debugButton = new TextButton("Dice", skin);
		debugButton.setHeight(BUTTON_HEIGHT);
		debugButton.setWidth(BUTTON_WIDTH);

		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Dice(game));
			}
		});		

		buttons.add(debugButton);

		for (TextButton button : buttons){			
			stage.addActor(button);			
		}
	}	

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		int row = 0;

		for (int i = 0; i < buttons.size; i++){

			buttons.get(i).setPosition(buttons.get(i).getWidth()*row, height - (i + 1)*buttons.get(i).getHeight());

			if (i == 8)
				row++;			
		}
	}

	@Override
	protected void renderText() {

		batchui.begin();
		for (int i = 0; i < Text.size; i++)
			Assets.font24.drawMultiLine(batchui, Text.get(i), 0, Text.size*24 - i*24, width, HAlignment.RIGHT);
		batchui.end();
	}

	@Override
	protected void updateText() {
		// TODO Auto-generated method stub

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
