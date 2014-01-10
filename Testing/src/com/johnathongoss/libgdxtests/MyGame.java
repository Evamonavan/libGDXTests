package com.johnathongoss.libgdxtests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MyGame extends Game{
	
	@Override
	public void create() {		

	}

	public float getWidth(){
		return Gdx.app.getGraphics().getWidth();		
	}
	public float getHeight(){
		return Gdx.app.getGraphics().getHeight();		
	}
	public float getButtonWidth(){

		return getWidth()/7;
	}
	public float getButtonHeight(){

		return getHeight()/8;
	}

}
