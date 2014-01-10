package com.johnathongoss.libgdxtests;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.johnathongoss.libgdxtests.entities.DamageImage;
import com.johnathongoss.libgdxtests.entities.Effect;

public class EffectsCache {

	public static final int DAMAGE = 0;

	private static final Pool<DamageImage> damagePool = new Pool<DamageImage>() {
        @Override
        protected DamageImage newObject() {
                return new DamageImage();
        }
    };
	
	public static void Load(){

		
	}
	
	public static DamageImage getDamageEffect() {
		return damagePool.obtain();
	}

	public static void free(Effect effect) {
		
		if (effect instanceof DamageImage)
			damagePool.free((DamageImage) effect);
		
	}	
	
	class Slash extends Effect{
		
		Animation animation;		
		
		public void Perform(Actor actor, int damage){
			
			animation = new Animation(0.1f, Assets.spriteSheet.createSprites("fx_slash"), Animation.NORMAL);
			
		}
		
	}
}
