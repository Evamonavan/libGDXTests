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
import com.johnathongoss.libgdxtests.tests.Camera2D;
import com.johnathongoss.libgdxtests.tests.Collision;
import com.johnathongoss.libgdxtests.tests.Timers;

public class MainMenu extends BlankTestScreen {

	TextButton exampleButton;
	private Array<TextButton> buttons;
	public MainMenu(Game game) {
		super(game);				
		Text.add("0.3.1 |");
		Text.add("");
		Text.add("johnathongoss.com |");
	}
	
	@Override
	public void render(float delta) {	
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);			
		stage.act(delta);
		stage.draw();	
		
		renderText();		
	}	

	@Override
	public void show(){
		Gdx.input.setInputProcessor(stage);
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
		
		/*
		 * 2D Camera Test
		 */
		
		tempButton = new TextButton("Camera 2D", skin);
		tempButton.setHeight(BUTTON_HEIGHT);
		tempButton.setWidth(BUTTON_WIDTH);
		
		tempButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Camera2D(game));
			}
		});		
		
		buttons.add(tempButton);
		
		/*
		 * Collision Test
		 */
		
		tempButton = new TextButton("Collision", skin);
		tempButton.setHeight(BUTTON_HEIGHT);
		tempButton.setWidth(BUTTON_WIDTH);
		
		tempButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Collision(game));
			}
		});		
		
		buttons.add(tempButton);
		
		/*
		 * 2D Camera Test
		 */
		
		tempButton = new TextButton("Timers", skin);
		tempButton.setHeight(BUTTON_HEIGHT);
		tempButton.setWidth(BUTTON_WIDTH);
		
		tempButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new Timers(game));
			}
		});		
		
		buttons.add(tempButton);		
		
		for (TextButton button : buttons){			
			stage.addActor(button);			
		}
		
		stage.addActor(exampleButton);
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
