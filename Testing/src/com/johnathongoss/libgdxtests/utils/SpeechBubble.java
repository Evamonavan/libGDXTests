package com.johnathongoss.libgdxtests.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ImageCache;

public class SpeechBubble extends Actor implements Poolable{

	private boolean usePic = false;
	private Sprite pic;
	private NinePatch bubble;
	private MyTimer life;
	private Actor followed;
	boolean alive = false, follow = false;

	String text;
	BitmapFont font = Assets.font24;

	float padX = 10, padY = 20;
	float speed = 0.07f;

	float yStretch;
	private float wrapWidth;
	private float picOffset;

	public SpeechBubble(){

		bubble = ImageCache.CreatePatch("bubble");		
	}

	public void init(String text, float x, float y){
		setUp(text, x, y);
		animate();		
	}

	public void init(String text, float x, float y, TextureRegion pic){	
		setUp(text, x, y);
		addPic(pic);
	}	
	
	private void setUp(String text, float x, float y) {
		alive  = true;
		this.text = text;			

		float sbWidth = font.getBounds(text).width + padX*2, 
				sbHeight = font.getBounds(text).height + padY*2;

		if (sbWidth < 50)
			sbWidth = 50;				

		yStretch = (float) Math.ceil(0.5f+(sbWidth/((float)Gdx.app.getGraphics().getWidth()/2f)));

		if (sbWidth > Gdx.app.getGraphics().getWidth()/2){
			sbWidth =  Gdx.app.getGraphics().getWidth()/2 + padX*2;
			sbHeight = font.getBounds(text).height*yStretch + padY*yStretch;
		}
		
		setWidth(sbWidth);
		setHeight(sbHeight);			
		
		wrapWidth = getWidth() - padX*2;

		setOrigin(getWidth()/2, getHeight()/2);

		setX(x - getWidth()/2);
		setY(y);		

		setLife(0.6f + (float)text.length()*speed);			
	}

	private void checkBounds() {

		if (follow){
			setX(followed.getX() - getWidth()/2);
			setY(followed.getY() + followed.getHeight());			
		}
		
		if (getX() + getWidth() > Gdx.app.getGraphics().getWidth())
			setX(getX() - (getWidth() - (Gdx.app.getGraphics().getWidth() - getX())));

		if (getY() + getHeight() > Gdx.app.getGraphics().getHeight())
			setY(getY() - (getHeight() - (Gdx.app.getGraphics().getHeight() - getY())));


		if (getX() < 0)
			setX(0);
		if (getY() < 0)
			setY(0);
	}

	public void setLife(float time){

		life = new MyTimer(time) {

			@Override
			protected void perform() {
				alive = false;

			}
		};
		life.reset();
		life.start();
	}

	@Override
	public void setColor(Color color){
		bubble.setColor(color);
		
	}

	public void setPadding(float padX, float padY){

		this.padX = padX;
		this.padY = padY;

	}

	public void setFont(BitmapFont font){
		this.font = font;

	}
	public boolean isAlive() {
		return alive;
	}
	@Override		
	public void draw(Batch batch, float parentAlpha){	
		
		bubble.draw(batch, getX(), getY(), getWidth(), getHeight());

		if (!usePic){

			font.drawWrapped(batch,
					text,
					getX() + padX, getY() + getHeight() - padY, wrapWidth);
		}
		else{
			batch.draw(pic, getX() + padX, getY() + picOffset, pic.getWidth(), pic.getHeight());
			font.drawWrapped(batch,
					text,
					getX() + padX*2 + pic.getWidth(), getY() + getHeight() - padY, wrapWidth);

		}
	}

	@Override
	public void act(float delta){
		super.act(delta);
		life.update(delta);		
		checkBounds();
		//typewriter.update(delta);		
	}

	private void addPic(TextureRegion texture) {

		usePic = true;
		pic = new Sprite(texture);
		pic.setSize(80, 80);
		
		if (getHeight() < 100)
			setHeight(100);		
		
		picOffset = getHeight() - 80 - padX;
		
		setWidth(getWidth() + pic.getWidth() + padX);	
		
		animate();
	}

	private void animate() {
		addAction(Actions.sequence(Actions.sizeTo(getWidth(), getHeight()*0.75f), Actions.sizeTo(getWidth(), getHeight(), 0.20f, Interpolation.bounceOut)));

	}

	@Override
	public void reset() {
		alive = false;
		setX(0);
		setY(0);
		setWidth(0);
		setHeight(0);
		wrapWidth = 0;
		text = "";
		usePic = false;
		life.reset();
		life.pause();
		follow = false;
		followed = null;
	}

	public void setFollow(Actor actor) {
		follow = true;		
		followed = actor;	
		checkBounds();
	}
}