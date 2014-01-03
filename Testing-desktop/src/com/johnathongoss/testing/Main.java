package com.johnathongoss.testing;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.johnathongoss.libgdxtests.Testing;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Testing";
		cfg.useGL20 = true;
		cfg.width = 440;
		cfg.height = 360;
		new LwjglApplication(new Testing(), cfg);
	}
}
