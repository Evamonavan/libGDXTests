package com.johnathongoss.libgdxtests;

import com.badlogic.gdx.Game;
import com.johnathongoss.libgdxtests.screens.Loading;

public class Testing extends Game {
		
	@Override
	public void create() {		
			
		setScreen(new Loading(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
