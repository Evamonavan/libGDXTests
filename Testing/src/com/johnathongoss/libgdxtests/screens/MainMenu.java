package com.johnathongoss.libgdxtests.screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.tests.BlankTestScreen;
import com.johnathongoss.libgdxtests.tests.Box2D;
import com.johnathongoss.libgdxtests.tests.Box2DTest;
import com.johnathongoss.libgdxtests.tests.Camera2D;
import com.johnathongoss.libgdxtests.tests.Collision;
import com.johnathongoss.libgdxtests.tests.Particles;
import com.johnathongoss.libgdxtests.tests.SpeechBubbles;
import com.johnathongoss.libgdxtests.utils.Dice;
import com.johnathongoss.libgdxtests.utils.Timers;

public class MainMenu extends BlankTestScreen {

	TextButton exampleButton, utilButton;
	private Array<TextButton> buttons;
	public MainMenu(Game game) {
		super(game);				
		Text.add("0.5 |");
		Text.add("");
		Text.add("johnathongoss.com |");
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
		Gdx.input.setInputProcessor(stageui);
		buttons = new Array<TextButton>();
		
		exampleButton = new TextButton("Examples", skin);
		exampleButton.setHeight(BUTTON_HEIGHT);
		exampleButton.setWidth(BUTTON_WIDTH);		
		exampleButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Examples(game));
			}
		});	
		exampleButton.setPosition(width - BUTTON_WIDTH, height - BUTTON_HEIGHT);
		stageui.addActor(exampleButton);
		
		/**
		 * To Utils
		 */
		
		utilButton = new TextButton("Utils", skin);
		utilButton.setHeight(BUTTON_HEIGHT);
		utilButton.setWidth(BUTTON_WIDTH);		
		utilButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Utils(game));
			}
		});	
		utilButton.setPosition(width - BUTTON_WIDTH, height - BUTTON_HEIGHT*2);
		stageui.addActor(utilButton);
		
		/*
		 * Box 2D Test
		 */
		
		debugButton = new TextButton("Box2D", skin);
		debugButton.setHeight(BUTTON_HEIGHT);
		debugButton.setWidth(BUTTON_WIDTH);
		
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Box2D(game));
			}
		});		
		
		buttons.add(debugButton);
		
		/*
		 * Box 2D Test2
		 */
		
		debugButton = new TextButton("Box2D", skin);
		debugButton.setHeight(BUTTON_HEIGHT);
		debugButton.setWidth(BUTTON_WIDTH);
		
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Box2DTest(game));
			}
		});		
		
		//buttons.add(tempButton);
		
		/*
		 * Camera 2D Test
		 */
		
		debugButton = new TextButton("Camera 2D", skin);
		debugButton.setHeight(BUTTON_HEIGHT);
		debugButton.setWidth(BUTTON_WIDTH);
		
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Camera2D(game));
			}
		});		
		
		buttons.add(debugButton);		
		
		
		/*
		 * Collision Test
		 */
		
		debugButton = new TextButton("Collision", skin);
		debugButton.setHeight(BUTTON_HEIGHT);
		debugButton.setWidth(BUTTON_WIDTH);
		
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Collision(game));
			}
		});		
		
		buttons.add(debugButton);		
			
		/*
		 * Particles
		 */
		
		debugButton = new TextButton("Particles", skin);
		debugButton.setHeight(BUTTON_HEIGHT);
		debugButton.setWidth(BUTTON_WIDTH);
		
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Particles(game));
			}
		});		
		
		buttons.add(debugButton);
		
		/*
		 * Speech bubbles
		 */
		
		debugButton = new TextButton("Speech", skin);
		debugButton.setHeight(BUTTON_HEIGHT);
		debugButton.setWidth(BUTTON_WIDTH);
		
		debugButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new SpeechBubbles(game));
			}
		});		
		
		buttons.add(debugButton);	
		
		for (TextButton button : buttons){			
			stageui.addActor(button);			
		}		
	}	
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		exampleButton.setWidth(BUTTON_WIDTH);
		exampleButton.setHeight(BUTTON_HEIGHT);
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

}
