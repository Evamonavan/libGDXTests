package com.johnathongoss.libgdxtests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	
	public static BitmapFont font32, font24;	
	public static Skin skin;
	public static TextureAtlas spriteSheet;
	public static void Load(){			
		
		font32 = new BitmapFont(Gdx.files.internal("data/font_32.fnt"),
				Gdx.files.internal("data/font_32.png"), false);
		font24 = new BitmapFont(Gdx.files.internal("data/font_24.fnt"),
				Gdx.files.internal("data/font_24.png"), false);
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		spriteSheet = new TextureAtlas(Gdx.files.internal("textures/animations.atlas"), Gdx.files.internal("textures"));

	}
}
