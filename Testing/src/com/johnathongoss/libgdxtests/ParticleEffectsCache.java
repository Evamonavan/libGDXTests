package com.johnathongoss.libgdxtests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

public class ParticleEffectsCache {

	public static final int FIRE = 0, POISON = 1, BLOOD = 2;

	private static ParticleEffectPool pool_fire, pool_poison, pool_blood;

	private static ParticleEffect 	fx_fire, fx_poison, fx_blood;



	public static void Load(){

		fx_fire = new ParticleEffect();
		fx_fire.load(Gdx.files.internal("fx/burn.p"), Gdx.files.internal("fx"));			

		pool_fire = new ParticleEffectPool(fx_fire, 0, 150);
		
		fx_poison = new ParticleEffect();
		fx_poison.load(Gdx.files.internal("fx/poison.p"), Gdx.files.internal("fx"));			

		pool_poison = new ParticleEffectPool(fx_poison, 0, 150);
		
		fx_blood = new ParticleEffect();
		fx_blood.load(Gdx.files.internal("fx/blood.p"), Gdx.files.internal("fx"));			

		pool_blood = new ParticleEffectPool(fx_blood, 0, 150);
	}

	static PooledEffect effect;
	public static PooledEffect getParticleEffect(int type){

		switch (type){
		case FIRE: {
			effect = pool_fire.obtain();			
			break;
		}
		case POISON: {
			effect = pool_poison.obtain();			
			break;
		}
		case BLOOD: {
			effect = pool_blood.obtain();			
			break;
		}

		}
		effect.start();
		return effect;
	}
}
