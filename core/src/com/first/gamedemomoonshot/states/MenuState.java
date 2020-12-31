package com.first.gamedemomoonshot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.first.gamedemomoonshot.util.Prefs;

public class MenuState extends State{
    private final Texture background, playStork, stop, buttonTex;
    private final BitmapFont font;
    private final Prefs prefs;

    public MenuState(GameStateManager gsm, OrthographicCamera cam, Viewport viewport, Stage stage) {
        super(gsm, cam, viewport, stage);

        prefs = new Prefs();
        font = new BitmapFont(Gdx.files.internal("fonts/abel.fnt"), Gdx.files.internal("fonts/abel.png"), false);
        font.getData().setScale(0.15f);
        font.setUseIntegerPositions(false);

        background = new Texture("artwork/main_landing.png");
        playStork = new Texture("artwork/spaceman.png");
        stop = new Texture("artwork/stop.png");
        buttonTex = new Texture(Gdx.files.internal("artwork/button.png"));
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            v = cam.unproject(v);
            if(v.y > 3 && v.y < 15) {
                if(v.x > 5 && v.x < 20) {
                    // How to play button
                    gsm.set(new HowToState(gsm, cam, viewport, stage));
                    dispose();
                } else if (v.x > 24 && v.x < 39) {
                    // High score button
                    gsm.set(new HighScoreState(gsm, cam, viewport, stage));
                    dispose();
                } else if (v.x > 43 && v.x < 58) {
                    // Play button
                    gsm.set(new PlayState(gsm, cam, viewport, stage));
                    dispose();
                } else if (v.x > 62 && v.x < 77) {
                    // Sound button
                    prefs.setSound(!prefs.isSoundActive());
                } else if (v.x > 80 && v.x < 95) {
                    // About button
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
        sb.draw(buttonTex, 5, 3, 15, 12);
        sb.draw(buttonTex, 24, 3, 15, 12);
        sb.draw(buttonTex, 43, 3, 15, 12);
        sb.draw(buttonTex, 62, 3, 15, 12);
        sb.draw(buttonTex, 80, 3, 15, 12);
        font.draw(sb,"Help", 8,10.5f);
        font.draw(sb,"Scores", 25,10.5f);
        font.draw(sb,"Play", 47, 10.5f);
        font.draw(sb,"Sound", 64,10.5f);
        font.draw(sb,"About", 82.5f,10.5f);
        if (!prefs.isSoundActive()) {
            sb.draw(stop, 63, 4, 13, 10);
        }
//        font.draw(sb, "Press play to start", 30, 25);
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
