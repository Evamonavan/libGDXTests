package com.johnathongoss.libgdxtests.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleEffectActor extends Actor{

	ParticleEffect effect;

	public ParticleEffectActor(ParticleEffect effect) {
		this.effect = effect;
	}
	
	@Override
	public void draw(Batch batch, float alpha){
		effect.draw(batch); //define behavior when stage calls Actor.draw()
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		effect.update(delta);		
		//effect.start();
		effect.setPosition(getX(), getY());
	}

	public ParticleEffect getEffect() {
		return effect;
	}

}
