package com.first.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.first.game.util.Prefs;

public class MenuState extends State{
    private Texture background;
    private Texture playStork;
    private Texture stop;
    private BitmapFont font;
    private Prefs prefs;

    private static Boolean PAUSED = false;

    public MenuState(GameStateManager gsm, OrthographicCamera cam, Viewport viewport, Stage stage) {
        super(gsm, cam, viewport, stage);

        prefs = new Prefs();
        font = new BitmapFont(Gdx.files.internal("fonts/abel.fnt"), Gdx.files.internal("fonts/abel.png"), false);
        font.getData().setScale(0.15f);
        font.setUseIntegerPositions(false);

        background = new Texture("artwork/main_landing2.png");
        playStork = new Texture("artwork/spaceman.png");
        stop = new Texture("artwork/stop.png");
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            v = cam.unproject(v);
            // numbers are percents
            if(v.y > 1 && v.y < 12) {
                if(v.x > 10 && v.x < 20) {
                    // High score
                } else if (v.x > 30 && v.x < 40) {
                    // Second button
                } else if (v.x > 45 && v.x < 55) {
                    // Play button
                    gsm.set(new PlayState(gsm, cam, viewport, stage));
                    dispose();
                } else if (v.x > 65 && v.x < 70) {
                    prefs.setSound(!prefs.isSoundActive());
                } else if (v.x > 80 && v.x < 90) {
                    gsm.set(new InfoState(gsm, cam, viewport, stage));
                    dispose();
                }
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
        sb.draw(background, 0 , 0, 100, 100);
        sb.draw(playStork, 18 , 30, 65, 65);
        if (!prefs.isSoundActive()) {
            sb.draw(stop, 63, 4, 12, 17);
        }
        font.draw(sb, "Press play to start", 30, 30);
        sb.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void dispose() {
        background.dispose();
        playStork.dispose();
        font.dispose();
    }
}
