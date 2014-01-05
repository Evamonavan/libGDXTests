package com.johnathongoss.libgdxtests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class AppData {

	public static class Prefs{
		private static Preferences prefs;

		private static String 	limitParticles = "limPart",
				backgroundR = "b_r",
				backgroundG = "b_g",
				backgroundB = "b_b";

		public static boolean isLimitParticles(){
			return prefs.getBoolean(limitParticles, true);			
		}
		public static float getBackgroundR() {

			return prefs.getFloat(backgroundR, 0.0f);	
		}
		public static float getBackgroundG() {

			return prefs.getFloat(backgroundG, 0.0f);	
		}
		public static float getBackgroundB() {

			return prefs.getFloat(backgroundB, 0.0f);	
		}


		public static void setLimitParticles(boolean limit){
			prefs.putBoolean(limitParticles, limit);
		}		
		
		public static void setBackgroundR(float r) {
			prefs.putFloat(backgroundR, r);
			
		}
		public static void setBackgroundG(float g) {
			prefs.putFloat(backgroundG, g);
			
		}
		public static void setBackgroundB(float b) {
			prefs.putFloat(backgroundB, b);
			
		}
		
		public static void Save(){
			prefs.flush();

		}		
		public static void Load(){			
			prefs = Gdx.app.getPreferences("AppPreferences");

		}
	}
}
