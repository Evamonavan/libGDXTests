package com.johnathongoss.libgdxtests.tests;

import net.dermetfan.utils.libgdx.Typewriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.johnathongoss.libgdxtests.Assets;

public class TypewriterTest implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;
    private String text = "This is a long multiline string. Some text that you can read through in a RPG, that scrolls like in the pokemon games or something";

    // create a default Typewriter
    private Typewriter typewriter = new Typewriter();

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = Assets.font24;
        typewriter.getInterpolator().setInterpolation(Interpolation.linear);

        // set some custom cursors
        typewriter.getAppender().set(new CharSequence[] {"", ".", "..", "..."}, 1.5f / 4f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.drawWrapped(batch,
            // update the time and get the interpolated CharSequence with cursor
            typewriter.updateAndType(text, delta),
            0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

}