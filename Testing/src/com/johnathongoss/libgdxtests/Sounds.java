package com.johnathongoss.libgdxtests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

	public static enum SoundPointer {SLASH_01, HIT_01}	
	
	public static Sound slash_01, hit_01;
	
	public static void Load(){
		
		slash_01 = Gdx.audio.newSound(Gdx.files.internal("sound/slash_01.ogg"));
		hit_01 = Gdx.audio.newSound(Gdx.files.internal("sound/hit_01.ogg"));
		
	}
	
	public static void PlaySound(SoundPointer SoundPointer){
		if (SoundPointer == Sounds.SoundPointer.HIT_01 && AppData.Prefs.isEnableSound())
			hit_01.play(1f);
		if (SoundPointer == Sounds.SoundPointer.SLASH_01 && AppData.Prefs.isEnableSound())
			slash_01.play(1f);	
	}
}
