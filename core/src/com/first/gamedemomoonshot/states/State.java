package com.first.gamedemomoonshot.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class State {
    protected com.first.gamedemomoonshot.states.GameStateManager gsm;

    protected OrthographicCamera cam;
    protected Viewport viewport;
    protected Stage stage;
    protected State(GameStateManager gsm, OrthographicCamera cam, Viewport viewport, Stage stage){
        this.gsm = gsm;
        this.cam = cam;
        this.viewport = viewport;
        this.stage = stage;
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void pause();
    public abstract void dispose();
}
