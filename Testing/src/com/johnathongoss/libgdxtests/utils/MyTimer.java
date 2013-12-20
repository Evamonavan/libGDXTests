package com.johnathongoss.libgdxtests.utils;

public abstract class MyTimer{

	float current = 0, cap = 1f;

	boolean ticking = false, complete = false, repeating = false;

	public MyTimer(float cap){
		this.cap = cap;
	}

	public void setRepeating(boolean repeating) {
		this.repeating = repeating;
	}
	
	public void setCap(float cap) {
		this.cap = cap;
	}

	public void reset(){

		current = 0;
		complete = false;

	}

	public void start(){

		ticking = true;

	}

	public void pause(){

		ticking = false;

	}

	public float getCurrent(){
		return current;
	}

	public float getCap(){
		return cap;
	}

	public boolean isComplete() {
		return complete;
	}

	public void update(float delta){

		if (ticking)
			current += delta;

		if (current >= cap)
			complete = true;

		if (complete){

			if (ticking)
				perform();
			pause();
			if (repeating){
				reset();
				start();
			}
			else
				pause();
		}

	}

	protected abstract void perform();

	public boolean isRepeating() {

		return repeating;
	}


}
