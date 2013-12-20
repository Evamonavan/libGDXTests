package com.johnathongoss.libgdxtests.screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdx.examples.FishTank;
import com.johnathongoss.libgdx.examples.PopCorns;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.tests.BlankTestScreen;
import com.johnathongoss.libgdxtests.tests.Camera2D;
import com.johnathongoss.libgdxtests.tests.Collision;
import com.johnathongoss.libgdxtests.tests.Timers;

public class Examples extends BlankTestScreen {

	TextButton exampleButton;
	private Array<TextButton> buttons;
	public Examples(Game game) {
		super(game);				
		Text.add("Examples |");
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
		
		addBackButton();
		
		buttons = new Array<TextButton>();
		
		/*
		 * Pop Corn
		 */
		
		tempButton = new TextButton("Pop Corn", skin);
		tempButton.setHeight(BUTTON_HEIGHT);
		tempButton.setWidth(BUTTON_WIDTH);
		
		tempButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new PopCorns(game));
			}
		});		
		
		buttons.add(tempButton);
		
		/*
		 * Fish Tank
		 */
		
		tempButton = new TextButton("Fish Tank", skin);
		tempButton.setHeight(BUTTON_HEIGHT);
		tempButton.setWidth(BUTTON_WIDTH);
		
		tempButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new FishTank(game));
			}
		});		
		
		buttons.add(tempButton);
				
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

}
