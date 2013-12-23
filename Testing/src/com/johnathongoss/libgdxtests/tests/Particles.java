package com.johnathongoss.libgdxtests.tests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.ParticleEffectsCache;
import com.johnathongoss.libgdxtests.screens.Examples;

public class Particles extends BlankTestScreen {

	private  Array<PooledEffect> Effects;
	private MyInputProcessor inputProcessor;
	private TextButton switchButton;
	protected int index = 0;

	public Particles(Game game) {
		super(game);	

		inputProcessor = new MyInputProcessor();
		testName = "Particles |";
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

		batch.begin();
		for (PooledEffect effect : Effects){
			//effect.getEmitters().first().setPosition(effect.getEmitters().first().getX() + 2, effect.getEmitters().first().getY() + 2);
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
		addInput();		

		switchButton = new TextButton("Particle " + index, skin);
		switchButton.setHeight(BUTTON_HEIGHT);
		switchButton.setWidth(BUTTON_WIDTH);		
		switchButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				index ++;
				if (index > 3)
					index = 0;
				switchButton.setText("Particle " + index);
			}
		});	
		switchButton.setPosition(width - BUTTON_WIDTH, height - BUTTON_HEIGHT);
		stageui.addActor(switchButton);


		InputMultiplexer multiplexer = new InputMultiplexer(stageui, inputProcessor);
		Gdx.input.setInputProcessor(multiplexer);

		Effects = new Array<PooledEffect>();
	}

	@Override
	protected void updateText() {
		Text.clear();
		Text.add("Count: " + Effects.size + " |");

	}

	@Override
	protected void renderText() {

		for (int i = 0; i < Text.size; i++){

			Assets.font24.drawMultiLine(batchui, Text.get(i), 0, height - BUTTON_HEIGHT - i*24, width, HAlignment.RIGHT);

		}

	}

	public class MyInputProcessor implements InputProcessor {
		@Override
		public boolean keyDown (int keycode) {
			return false;
		}

		@Override
		public boolean keyUp (int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped (char character) {
			return false;
		}

		@Override
		public boolean touchDown (int x, int y, int pointer, int button) {
			createParticle(index, x, y);			

			return false;
		}

		@Override
		public boolean touchUp (int x, int y, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchDragged (int x, int y, int pointer) {
			createParticle(index, x, y);
			return false;
		}

		@Override
		public boolean scrolled (int amount) {
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	public void createParticle(int index, float x, float y) {

			Effects.add(ParticleEffectsCache.getParticleEffect(index));
			Effects.get(Effects.size - 1).setPosition(x, height - y);
			Gdx.app.log("Particles", "Particle Created");		
	}

	@Override
	public void dispose(){
		super.dispose();		

	}
}