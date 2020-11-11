package com.first.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.first.game.elements.CollectibleOrbs;
import com.first.game.sprites.Spaceman;
import com.first.game.util.Prefs;

public class PlayState extends State {
    private Spaceman spaceman;
    private CollectibleOrbs orbs;
    Array<Texture> textures = new Array<Texture>();
    private BitmapFont font;
    private Texture lifeTex, pauseTex, buttonTex;
    private static int score;
    private static int lives;
    private long startTime;
    private Music background;
    private AssetManager assetManager;
    private long background_audio_id;
    private Boolean AudioStarted = false;
    public Prefs prefs;
    private ShapeRenderer shapeRenderer;

    private static boolean PAUSED = false;

    public PlayState(GameStateManager gsm, OrthographicCamera cam, Viewport viewport, Stage stage) {
        super(gsm, cam, viewport, stage);

        score = 0;
        lives = 5;

        prefs = new Prefs();

        if (prefs.isSoundActive()) {
            background = Gdx.audio.newMusic(Gdx.files.internal("audio/background16.wav"));
            background.setLooping(true);
            background.play();
        }

        spaceman = new Spaceman(0, 50);
        lifeTex = new Texture(Gdx.files.internal("artwork/lifes.png"));
        pauseTex = new Texture(Gdx.files.internal("artwork/pause.png"));
        buttonTex = new Texture(Gdx.files.internal("artwork/button.png"));
        orbs = new CollectibleOrbs(cam);
        font = new BitmapFont(Gdx.files.internal("fonts/abel.fnt"), Gdx.files.internal("fonts/abel.png"), false);
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.15f);
        textures.add(new Texture("artwork/night_bg.png"));
        textures.get(textures.size - 1).setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        for (int i = 1; i <= 4; i++) {
            textures.add(new Texture("artwork/" + i + ".png"));
            textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
//        Gdx.app.debug("Stage:", stage.getActors().size + "");

        if (stage.getActors().size == 0) {
            ParallaxBackground parallaxBackground = new ParallaxBackground(textures, cam);
            parallaxBackground.setSize(cam.viewportWidth, cam.viewportHeight);
            parallaxBackground.setSpeed(1);
            stage.addActor(parallaxBackground);
        }

        startTime = System.currentTimeMillis();
        PAUSED = false;
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isTouched()) {

            Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            v = cam.unproject(v);


            if (PAUSED && (v.y > 42 && v.y < 56) && (v.x > 38 && v.x < 54)) {
                PAUSED = false;
                lives = 5;
                score = 0;

                gsm.set(new MenuState(gsm, cam, viewport, stage));
                dispose();
            }

            if (v.y > 80 && v.x > 90 && !PAUSED) {
                PAUSED = true;
            } else if (!PAUSED) {
                spaceman.fly();
            } else {
                PAUSED = false;
                stage.clear();
                ParallaxBackground parallaxBackground = new ParallaxBackground(textures, cam);
                parallaxBackground.setSize(cam.viewportWidth, cam.viewportHeight);
                parallaxBackground.setSpeed(1);
                stage.addActor(parallaxBackground);
            }
        } else {
            spaceman.descend();
        }

    }

    @Override
    public void update(float dt) {
        handleInput();

        if (!PAUSED) {
            cam.update();
            spaceman.update(dt);
            orbs.update(dt);
            CollisionCheckMain();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);

        stage.draw();

        sb.begin();
        sb.draw(spaceman.getTextureRegion(), spaceman.getPosition().x, spaceman.getPosition().y, 10, 10);
        orbs.render(sb);
        sb.end();

        sb.begin();
        font.draw(sb, "Score: " + getScore(), 30, 95);
        sb.end();

        sb.begin();

        sb.draw(lifeTex, 2, 90, 5, 7);
        font.draw(sb, "X " + lives, 8, 95);

        sb.end();

        if (PAUSED) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.5f));
            shapeRenderer.rect(0, 0, 100, 100);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            sb.begin();
            sb.draw(buttonTex, 45, 50, 20, 20);
//            font.draw(sb,"Touch Anywhere to Continue", 20,10);
//            font.draw(sb,"EXIT?", 45,50);
            sb.end();
            stage.clear();
            ParallaxBackground parallaxBackground = new ParallaxBackground(textures, cam);
            parallaxBackground.setSize(cam.viewportWidth, cam.viewportHeight);
            parallaxBackground.setSpeed(0);
            stage.addActor(parallaxBackground);
        } else {
            sb.begin();
            font.draw(sb, " Time : " + getPlayTime(), 55, 95);
            sb.draw(pauseTex, 92, 90, 5, 5);

            sb.end();
        }
    }

    @Override
    public void pause() {
        PAUSED = true;
    }

    @Override
    public void dispose() {
        orbs.dispose();
        font.dispose();
        spaceman.dispose();

        if (prefs.isSoundActive()) {
            background.stop();
            background.dispose();
        }

    }

    public void CollisionCheckMain() {
        int temp;
        temp = orbs.checkCollision(spaceman.getBoundingRectangle());

        if (temp == 1) {
            spaceman.reward();
            score += temp;
            if (score % 10 == 0) {
                lives += 1;
                orbs.levelUp();
            }
        } else if (temp == -1) {
            spaceman.punish();
            lives -= 1;
        }
        checkKillCondition();
    }

    public void checkKillCondition() {
        if (lives == 0) {
            //Killed, Restart
            gsm.set(new MenuState(gsm, cam, viewport, stage));
            dispose();
        }
    }

    private int getScore() {
        return score;
    }

    private String getPlayTime() {
        long minutes = 0, seconds = 0, units;
        String timeString;
        units = (System.currentTimeMillis() - startTime) / 1000;

//        hours = units / 3600;
        minutes = (units % 3600) / 60;
        seconds = units % 60;

        timeString = String.format("%02d : %02d", minutes, seconds);
        return timeString;
    }
}
