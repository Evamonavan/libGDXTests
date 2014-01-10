package com.johnathongoss.libgdxtests.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ImageCache;

public class DamageImage extends Effect{

	TextureRegion image = ImageCache.getTexture("damage_icon");
	BitmapFont font = Assets.font32;
	String damage;

	Color imageColor = new Color(0.8f, 0.3f, 0f, 1f);
	Timer timer;

	public DamageImage(){
		timer = new Timer();
	}

	public void init(int damage, float x, float y){
		alive = true;

		if (damage > 0){
			this.damage = "+" + damage;
			setColor(0, 1f, 0.5f, 1f);
			image = ImageCache.getTexture("heal_icon");
		}
		else if (damage < 0){
			this.damage = damage + "";
			setColor(1f, 0.1f, 0.1f, 1f);			
		}
		else{
			this.damage = damage + "";
			setColor(1f, 1f, 1f, 1f);
		}

		timer.scheduleTask(new Task() {

			@Override
			public void run() {
				kill();

			}
		}, 0.8f);
		timer.start();

		setX(x);
		setY(y);
		setWidth(image.getRegionWidth());
		setHeight(image.getRegionHeight());
		setOrigin(getWidth()/2, getHeight()/2);

		addAction(Actions.sequence(Actions.scaleTo(0.25f, 0.25f, 0.0f, Interpolation.elasticIn), Actions.scaleTo(1f, 1f, 0.8f, Interpolation.elasticOut)));
	}

	protected void kill() {
		addAction(Actions.fadeOut(1f));

	}

	@Override
	public void act(float delta){
		super.act(delta);	

		if (getColor().a == 0)
			alive = false;
	}

	@Override		
	public void draw(Batch batch, float parentAlpha){	
		batch.setColor(getColor());
		batch.draw(image, getX() - image.getRegionWidth()/2, getY() - image.getRegionHeight()/2,
				getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

		font.setColor(1f, 1f, 1f, getColor().a);
		font.drawMultiLine(batch, damage + "", getX(), getY() + font.getLineHeight()/2, 0, HAlignment.CENTER);
		font.setColor(1, 1, 1, 1);
		batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void reset() {
		timer = new Timer();
		alive  = false;
		setColor(1f, 1f, 1f, 1f);
	}
}
