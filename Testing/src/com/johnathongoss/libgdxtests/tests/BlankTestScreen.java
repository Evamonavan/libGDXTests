package com.johnathongoss.libgdxtests.tests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.screens.BlankScreen;
import com.johnathongoss.libgdxtests.screens.MainMenu;

public abstract class BlankTestScreen extends BlankScreen {
	
	protected Array<String> Text;	
	protected Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	protected float BUTTON_WIDTH = Gdx.app.getGraphics().getWidth()/7;
	protected float BUTTON_HEIGHT = Gdx.app.getGraphics().getHeight()/8;
	protected TextButton backButton;
	protected boolean showBackButton = false;
	protected Array<TextButton> buttons = new Array<TextButton>();
	protected String testName = "";

	public BlankTestScreen(final MyGame game) {
		super(game);

		Text = new Array<String>();
	}	

	public abstract void render(float delta);
	
	public abstract void show();
	
	protected void renderTestName(SpriteBatch batch){
		Assets.font24.setColor(1, 1, 1, 0.8f);
		Assets.font24.drawMultiLine(batch, "FPS: " + Gdx.app.getGraphics().getFramesPerSecond(), 0, 24, width, HAlignment.LEFT);
		Assets.font24.setColor(1, 1, 1, 1);
		Assets.font24.drawMultiLine(batch, testName , 0, 24, width, HAlignment.RIGHT);		
	}
	
	protected abstract void updateText();
	protected TextButton debugButton;
	protected void addBackButton(){

		showBackButton = true;

		if (showBackButton){
			backButton = new TextButton("Back", skin);
			backButton.setHeight(BUTTON_HEIGHT);
			backButton.setWidth(BUTTON_WIDTH);
			backButton.setPosition(0, height - BUTTON_HEIGHT);
			backButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					game.setScreen(new MainMenu(game));
				}
			});	

			stageui.addActor(backButton);	
		}

	}

	protected abstract void renderText();

	

	@Override
	public void resize(int width, int height){
		super.resize(width, height);
		BUTTON_WIDTH = Gdx.app.getGraphics().getWidth()/7;
		BUTTON_HEIGHT = Gdx.app.getGraphics().getHeight()/8;
		int row = 0, column = 0;
		float tempX = width - BUTTON_WIDTH, tempY = height - BUTTON_HEIGHT;
		for (int i = 0; i < buttons.size; i++){

			buttons.get(i).setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
			buttons.get(i).setPosition(tempX - BUTTON_WIDTH*column, tempY - BUTTON_HEIGHT*row);

			row++;

			if (row == 3){
				column++;
				row = 0;
				
				if (column == 2){
					column = 0;
					row = 3;
				}
				
			}
		}

		if (showBackButton)
			backButton.setPosition(0, height - backButton.getHeight());

	}

}
