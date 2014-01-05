package com.johnathongoss.libgdxtests.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.johnathongoss.libgdxtests.AppData;
import com.johnathongoss.libgdxtests.Assets;

public class OptionsScreen implements Screen, InputProcessor{
	private Game game;

	private Stage stageui;

	float r, g, b;

	public OptionsScreen(Game game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stageui.act(delta);
		stageui.draw();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		stageui = new Stage();

		InputMultiplexer im = new InputMultiplexer(stageui, this);
		Gdx.input.setInputProcessor(im);		
		Gdx.input.setCatchBackKey(true);

		TextButton button = new TextButton("Save", Assets.skin);
		button.setWidth(Gdx.app.getGraphics().getWidth()/7);
		button.setHeight(Gdx.app.getGraphics().getHeight()/8);				
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AppData.Prefs.Save();
				game.setScreen(new MainMenu(game));
			}
		});	
		button.setPosition(0, Gdx.app.getGraphics().getHeight() - button.getHeight());
		stageui.addActor(button);

		final CheckBox cb = new CheckBox(" Limit Particle Effects |", Assets.skin);	
		cb.setChecked(AppData.Prefs.isLimitParticles());
		cb.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean enabled = cb.isChecked();
				AppData.Prefs.setLimitParticles(enabled);
			}
		});	
		cb.setPosition(Gdx.app.getGraphics().getWidth() - cb.getWidth(), Gdx.app.getGraphics().getHeight() - cb.getHeight() - button.getHeight());
		stageui.addActor(cb);
		
		r = AppData.Prefs.getBackgroundR();
		g = AppData.Prefs.getBackgroundG();
		b = AppData.Prefs.getBackgroundB();
		
		button = new TextButton("Background", Assets.skin);
		button.setWidth(Gdx.app.getGraphics().getWidth()/7);
		button.setHeight(Gdx.app.getGraphics().getHeight()/8);				
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				

				r += 0.1f;
				if (r > 0.3f)
					r = 0;
				
				g += 0.1f;
				if (g > 0.3f)
					g = 0;
				b += 0.1f;
				if (b > 0.3f)
					b = 0;

				AppData.Prefs.setBackgroundR(r); 
				AppData.Prefs.setBackgroundG(g);
				AppData.Prefs.setBackgroundB(b);

				Gdx.gl.glClearColor(r, g, b, 1f);
			}
		});	
		button.setPosition(Gdx.app.getGraphics().getWidth() - button.getWidth(), cb.getY() - button.getHeight() - cb.getHeight());
		stageui.addActor(button);
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
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
