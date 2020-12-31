package com.first.gamedemomoonshot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HighScoreState extends State {
    private Texture background, crossIcon;
    private BitmapFont headerFont;

    public HighScoreState(GameStateManager gsm, OrthographicCamera cam, Viewport viewport, Stage stage) {
        super(gsm,cam, viewport, stage);

        headerFont = new BitmapFont(Gdx.files.internal("fonts/abel.fnt"), Gdx.files.internal("fonts/abel.png"), false);
        headerFont.getData().setScale(0.3f);
        headerFont.setColor(Color.WHITE);
        headerFont.setUseIntegerPositions(false);
        background = new Texture("artwork/main_landing.png");
        crossIcon = new Texture("artwork/cross_icon.png");
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched() ) {
            Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            v = cam.unproject(v);

            if (v.y > 92 && v.x > 92) {
                gsm.set(new MenuState(gsm, cam, viewport, stage));
                dispose();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(background,0,0, 100,100);
        sb.draw(crossIcon,94, 94, 5, 5);

        headerFont.draw(sb,"Coming soon", 25,90);

        sb.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void dispose() {
        background.dispose();
        headerFont.dispose();
    }
}
