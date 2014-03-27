package com.johnathongoss.testing;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.johnathongoss.libgdxtests.IActivityRequestHandler;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.Testing;

public class Main implements IActivityRequestHandler{
	
	 private static Main application;
	public static void main(String[] args) {
		if (application == null) {
            application = new Main();
        }
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Testing";
		cfg.useGL20 = true;
		cfg.width = 1280; //xperia ray 854
		cfg.height = 720; // xperia ray 480
		new LwjglApplication(new MyGame(application), cfg);
	}

	@Override
	public void showAds(boolean show) {
		// TODO Auto-generated method stub
		
	}
}
