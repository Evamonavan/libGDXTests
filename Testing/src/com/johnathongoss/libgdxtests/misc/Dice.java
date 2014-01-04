package com.johnathongoss.libgdxtests.misc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.johnathongoss.libgdxtests.Assets;
import com.johnathongoss.libgdxtests.tests.BlankTestScreen;

public class Dice extends BlankTestScreen{
	
	int noDice = 1, diceLower = 1, diceUpper = 6;
	String result = "";
	public Dice(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		stageui.act(delta);
		stageui.draw();

		batch.begin();
		Assets.font32.drawMultiLine(batch, noDice + "d" + diceUpper + " |", 0, 32, width, HAlignment.RIGHT);
		Assets.font32.drawMultiLine(batch, result, 0, 64, width, HAlignment.CENTER);
		batch.end();		
	}

	@Override
	public void show() {
		addBackButton();
		TextButton tempB;

		/*
		 * Increase Dice
		 */		

		tempB = new TextButton("[+] Die", skin);
		tempB.setBounds(width - BUTTON_WIDTH, height - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		tempB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				noDice++;	
				if (noDice > 10)
					noDice = 10;
			}

		});			
		stageui.addActor(tempB);

		/*
		 * Decrease Dice
		 */		

		tempB = new TextButton("[-] Die", skin);
		tempB.setBounds(width - BUTTON_WIDTH*2, height - BUTTON_HEIGHT*1, BUTTON_WIDTH, BUTTON_HEIGHT);
		tempB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				noDice--;	
				if (noDice == 0)
					noDice = 1;
			}

		});			
		stageui.addActor(tempB);
		
		/*
		 * Increase Grade
		 */		

		tempB = new TextButton("[+] Grade", skin);
		tempB.setBounds(width - BUTTON_WIDTH, height - BUTTON_HEIGHT*2, BUTTON_WIDTH, BUTTON_HEIGHT);
		tempB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				diceUpper++;	
				if (diceUpper > 20)
					diceUpper = 20;
			}

		});			
		stageui.addActor(tempB);
		
		/*
		 * Decrease Grade
		 */		

		tempB = new TextButton("[-] Grade", skin);
		tempB.setBounds(width - BUTTON_WIDTH*2, height - BUTTON_HEIGHT*2, BUTTON_WIDTH, BUTTON_HEIGHT);
		tempB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				diceUpper--;	
				if (diceUpper < 2)
					diceUpper = 2;
			}

		});			
		stageui.addActor(tempB);

		/*
		 * Roll
		 */		

		tempB = new TextButton("Roll", skin);
		tempB.setBounds(width - BUTTON_WIDTH, height - BUTTON_HEIGHT*3, BUTTON_WIDTH, BUTTON_HEIGHT);
		tempB.addListener(new ClickListener() {		

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Array<Integer> rolls = new Array<Integer>();

				result = "Result: ";
				for (int i = 0; i < noDice; i++){
					rolls.add(MathUtils.random(diceLower, diceUpper));
				}
				rolls.sort();
				int sum = 0;
				for (int i = 0; i < rolls.size; i++){
					
					result += rolls.get(i);
					sum += rolls.get(i);
					if (i < noDice - 1)
						result += ", ";

				}
				
				result += "\nSum: " + sum;
			}

		});			
		stageui.addActor(tempB);

	}

	@Override
	protected void updateText() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void renderText() {
		// TODO Auto-generated method stub

	}

}