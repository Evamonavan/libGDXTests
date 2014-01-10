package com.johnathongoss.libgdxtests.entities;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Effect extends Actor implements Poolable {

	protected boolean alive = false;
	@Override
	public void reset() {
		alive = false;		
	}
	
	public boolean isAlive(){
		return alive;
	}

}
