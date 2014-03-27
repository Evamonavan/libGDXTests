package com.johnathongoss.libgdxtests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.johnathongoss.libgdxtests.screens.Loading;

public class MyGame extends Game{

	public CameraShaker CameraShaker = new CameraShaker();	

	private IActivityRequestHandler myRequestHandler;

	public MyGame(IActivityRequestHandler handler) {
		myRequestHandler = handler;
	}

	public void showAds(boolean showAds){

		if (AppData.DISABLE_ADS == true)
			showAds = false;

		if (myRequestHandler != null)
			myRequestHandler.showAds(showAds);
	}

	@Override
	public void create() {		

		setScreen(new Loading(this));
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

	public static class CameraShaker{

		float start_x;
		float start_y;
		float duration, intensity;
		private boolean isShaking = false, isRunning = false;
		OrthographicCamera cam;

		float currentTime = 0;

		public void Shake(OrthographicCamera cam, float duration, float intensity){
			this.cam = cam;
			this.duration = duration;
			this.intensity = intensity;
			currentTime = 0;

			start_x = this.cam.position.x;
			start_y = this.cam.position.y;

			isShaking  = true;			
			isRunning = true;
		}

		public void update(float delta){

			if (isRunning){
				currentTime += delta;

				if (currentTime > duration)
					isShaking = false;

				if (isShaking){
					cam.position.x += MathUtils.random(-intensity*cam.viewportWidth/800, intensity*cam.viewportWidth/800);
					cam.position.y += MathUtils.random(-intensity*cam.viewportHeight/800, intensity*cam.viewportHeight/800);
				}
				else{
					cam.position.x = start_x;
					cam.position.y = start_y;
					isRunning = false;
				}
			}
		}


	}

	public float getPixelNo() {
		return getWidth()*getHeight();
	}
}
