package com.johnathongoss.libgdxtests.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.johnathongoss.libgdxtests.AppData;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ImageCache;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.ParticleCache;
import com.johnathongoss.libgdxtests.Sounds;

public class Loading implements Screen {

	private MyGame game;
	private SpriteBatch batch;
	private OrthographicCamera cam;
	public Loading(MyGame game) {
		this.game = game;
		
		cam = new OrthographicCamera();
		batch = new SpriteBatch();		
		batch.setProjectionMatrix(cam.combined);
		
		// TODO AssetsManager
		
		Assets.Load();		
		ImageCache.load();
		ParticleCache.Load();
		AppData.Prefs.Load();	
		Sounds.Load();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(AppData.Prefs.getBackgroundR(), AppData.Prefs.getBackgroundG(), AppData.Prefs.getBackgroundB(), 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);									
		
		batch.begin();
		Assets.font32.draw(batch, "LOADING...", 64, 64);
		batch.end();
		
		game.setScreen(new MainMenu(game));
	}	

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		batch.dispose();
	}

}
