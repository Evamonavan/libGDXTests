package com.johnathongoss.libgdxtests.screens;

import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.johnathongoss.libgdxtests.AppData;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.MyInputProcessor;

public class OptionsScreen implements Screen{
	private MyGame game;
	private Stage stageui;
	float r, g, b;
	
	MyInputProcessor input = new MyInputProcessor(){

		@Override
		public boolean keyUp(int keycode) {
			if(keycode == Keys.BACK || 
					keycode == Keys.BACKSPACE ||
					keycode == Keys.ESCAPE){
				game.setScreen(new MainMenu(game));
			}
			return false;
		}
	};

	public OptionsScreen(MyGame game) {
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
		//Enable Ads
		game.showAds(true);

		stageui = new Stage();

		InputMultiplexer im = new InputMultiplexer(stageui, input);
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
		button.setPosition(game.getWidth() - game.getButtonWidth(), 0);
		stageui.addActor(button);

		final CheckBox cb_lpe = new CheckBox(" Limit Particle Effects", Assets.skin);	
		cb_lpe.setChecked(AppData.Prefs.isLimitParticles());
		cb_lpe.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean enabled = cb_lpe.isChecked();
				AppData.Prefs.setLimitParticles(enabled);
			}
		});	
		cb_lpe.setPosition(20, Gdx.app.getGraphics().getHeight() - cb_lpe.getHeight() - button.getHeight()*2);
		stageui.addActor(cb_lpe);

		final CheckBox cb_es = new CheckBox(" Enable sound", Assets.skin);	
		cb_es.setChecked(AppData.Prefs.isEnableSound());
		cb_es.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean enabled = cb_es.isChecked();
				AppData.Prefs.setEnableSound(enabled);
			}
		});	
		cb_es.setPosition(20, Gdx.app.getGraphics().getHeight() - cb_es.getHeight()*3 - button.getHeight()*2);
		stageui.addActor(cb_es);

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
		button.setPosition(20, cb_es.getY() - button.getHeight() - cb_es.getHeight());
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
	
}