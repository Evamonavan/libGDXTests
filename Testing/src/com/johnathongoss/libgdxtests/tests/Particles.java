package com.johnathongoss.libgdxtests.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdxtests.AppData;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.MyGame;
import com.johnathongoss.libgdxtests.ParticleCache;
import com.johnathongoss.libgdxtests.screens.MainMenu;

public class Particles extends BlankTestScreen {

	private  Array<PooledEffect> Effects;
	private TextButton switchButton;
	protected int index = 0, noParticles = 5;
	private String[] Names = {"Fire", "Frost", "Blood", "Bubbles", "Spark", "Firework"};
	private boolean continuous = false;
	private TextButton limitButton;
	private boolean limitReached = false;

	public Particles(MyGame game) {
		super(game);	

		testName = "Particles Test |";
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if (Effects.size < 5)
			limitReached = false;
		
		stage.act();
		stage.draw();
		batch.setProjectionMatrix(cam.combined);		
		batch.begin();
		for (PooledEffect effect : Effects){
			effect.draw(batch, delta);
			if (effect.isComplete()){
				Effects.removeValue(effect, true);
				effect.reset();
				effect.free();					
			}
		}
		renderTestName(batch);
		batch.end();

		updateText();
		batchui.begin();
		renderText();
		batchui.end();

		stageui.act();
		stageui.draw();
	}


	@Override
	public void show() {
		addBackButton();			
		InputMultiplexer im = new InputMultiplexer(stageui, stage, this);
		Gdx.input.setInputProcessor(im);		
		Gdx.input.setCatchBackKey(true);
		
		switchButton = new TextButton(Names[index], skin);
		switchButton.setHeight(BUTTON_HEIGHT);
		switchButton.setWidth(BUTTON_WIDTH);		
		switchButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				//clearParticles();				
				index ++;
				if (index > noParticles)
					index = 0;
				switchButton.setText(Names[index]);
			}
		});	
		switchButton.setPosition(width - BUTTON_WIDTH, height - BUTTON_HEIGHT);
		stageui.addActor(switchButton);

		limitButton = new TextButton("Cont.: " + continuous, skin);
		limitButton.setHeight(BUTTON_HEIGHT);
		limitButton.setWidth(BUTTON_WIDTH);		
		limitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				//clearParticles();

				continuous = !continuous;
				limitButton.setText("Cont.: " + continuous + "");
			}
		});	
		limitButton.setPosition(width - BUTTON_WIDTH, height - BUTTON_HEIGHT*2);
		stageui.addActor(limitButton);

		TextButton button = new TextButton("Clear", skin);
		button.setHeight(BUTTON_HEIGHT);
		button.setWidth(BUTTON_WIDTH);		
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {	
				clearParticles();
			}
		});	
		button.setPosition(width - BUTTON_WIDTH, height - BUTTON_HEIGHT*3);
		stageui.addActor(button);

		Effects = new Array<PooledEffect>();
	}

	protected void clearParticles() {
		Effects.clear();
		for (PooledEffect effect : Effects){				
			effect.reset();
			effect.free();	
		}		
	}

	@Override
	protected void updateText() {
		Text.clear();
		Text.add("Count: " + Effects.size + " |");
		if (limitReached ){
			Text.add("Limit reached |");
		}
	}

	@Override
	protected void renderText() {

		for (int i = 0; i < Text.size; i++){
			Assets.font24.drawMultiLine(batchui, Text.get(i), 0, height - BUTTON_HEIGHT*3 - i*24, width, HAlignment.RIGHT);

		}
	}

	public void createParticle(int index, float x, float y) {

		if (AppData.Prefs.isLimitParticles() && Effects.size > 19){
			limitReached = true;
		}
		else{
			if (!continuous){
				Effects.add(ParticleCache.getParticleEffect(index));

				for (int i = 0; i < Effects.get(Effects.size - 1).getEmitters().size; i++){
					Effects.get(Effects.size - 1).getEmitters().get(i).setContinuous(false);
				}
			}
			else if (continuous){
				Effects.add(ParticleCache.getParticleEffect(index));
				for (int i = 0; i < Effects.get(Effects.size - 1).getEmitters().size; i++){
					Effects.get(Effects.size - 1).getEmitters().get(i).setContinuous(true);

				}
			}

			Effects.get(Effects.size - 1).setPosition(x, height - y);
		}
	}

	@Override
	public void dispose(){
		super.dispose();		

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.BACK){
			game.setScreen(new MainMenu(game));
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		createParticle(index, screenX, screenY);	
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		createParticle(index, screenX, screenY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
