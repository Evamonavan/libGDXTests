package com.johnathongoss.libgdxtests.screens;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ImageCache;
import com.johnathongoss.libgdxtests.ParticleEffectsCache;


public class Loading implements Screen {
	/** ALWAYS */

	private Game game;
	private OrthographicCamera camera;
	private SpriteBatch batch;

	public Loading(Game game) {
		this.game = game;
		
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 736);

		Assets.Load();
		ImageCache.load();
		ParticleEffectsCache.Load();
	}
	
	@Override
	public void render(float delta) {
	//	Gdx.gl.glClearColor(.356f, .607f, .819f, 1f);
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);		
		
		batch.begin();
		//batch.draw(ImageCache.getTexture("loading"), 0, 0);
		Assets.font32.draw(batch, "LOADING...", 64, 64);
		batch.end();

		camera.update();
	
		//game.setScreen(new Battle_Chess(game));
		//game.setScreen(new Battle_Dominance(game));
		game.setScreen(new MainMenu(game));

	}	

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {


	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

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

		batch.dispose();

	}

}
