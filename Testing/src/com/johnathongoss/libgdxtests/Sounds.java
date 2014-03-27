package com.johnathongoss.libgdxtests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

	public static enum SoundPointer {SLASH_01, HIT_01, HEAL_01, POPCORN_POP}	
	public static enum MusicPointer {MICROWAVE}
	
	public static Sound slash_01, hit_01, heal_01, popcorn_pop;
	
	public static void Load(){
		
		slash_01 = Gdx.audio.newSound(Gdx.files.internal("sound/slash_01.ogg"));
		hit_01 = Gdx.audio.newSound(Gdx.files.internal("sound/hit_01.ogg"));
		heal_01 = Gdx.audio.newSound(Gdx.files.internal("sound/heal_01.ogg"));
		popcorn_pop = Gdx.audio.newSound(Gdx.files.internal("sound/popcorn_pop.ogg"));
	}
	
	public static void PlaySound(SoundPointer SoundPointer){
		if (SoundPointer == Sounds.SoundPointer.HIT_01 && AppData.Prefs.isEnableSound())
			hit_01.play(1f);
		if (SoundPointer == Sounds.SoundPointer.SLASH_01 && AppData.Prefs.isEnableSound())
			slash_01.play(1f);	
		if (SoundPointer == Sounds.SoundPointer.HEAL_01 && AppData.Prefs.isEnableSound())
			heal_01.play(1f);	
		if (SoundPointer == Sounds.SoundPointer.POPCORN_POP && AppData.Prefs.isEnableSound())
			popcorn_pop.play(1f);
	}
	
	public static void PlayMusic(MusicPointer MusicPointer, boolean repeat){
		if (MusicPointer == Sounds.MusicPointer.MICROWAVE && AppData.Prefs.isEnableSound()){
			//popcorn_microwave.setLooping(repeat);
			//popcorn_microwave.play();
		}
	}

	public static void ClearSounds() {
		
		//popcorn_microwave.stop();
		
	}
}
