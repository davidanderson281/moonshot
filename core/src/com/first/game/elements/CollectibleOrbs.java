package com.first.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.first.game.Moonshot;

import java.util.Random;

public class CollectibleOrbs {

    private static int ORB_COUNT = 10;
    private static int DARK_COUNT = 1;
    private static int ORB_RADIUS = 1;
    private static int ORB_SPEED = 14;
    private Array<Orb> orbs;
    private Array<Dark> darks;
    private Orb orb;
    private Dark dark;
    private Array<Integer> column;
    private Integer NumSteps = 6;

    private ShapeRenderer shapeRenderer;
    public CollectibleOrbs(OrthographicCamera cam){
        orbs = new Array<Orb>();
        darks = new Array<Dark>();
        column = new Array<Integer>();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        int step = 100/NumSteps;

        for(int i = step; i< (step*NumSteps) ; i = i + step){
            column.add(i);
        }

        for(int i = 0; i < ORB_COUNT; i++) {
            orb = new Orb();
            orbs.add(orb);
        }
        for(int i = 0; i<DARK_COUNT; i++) {
            dark = new Dark();
            darks.add(dark);

        }
    }

    public void update(float delta){
        for(Orb orb: orbs){
            orb.update(delta);

        }
        for(Dark dark : darks){
            dark.update(delta);
        }
    }

    public void levelUp(){
        dark = new Dark();
        darks.add(dark);
        ORB_SPEED +=2;
    }

    public void render(SpriteBatch sb){
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);

        for(Orb orb:orbs){
            sb.draw(orb.getOrbTexture(),orb.position.x,orb.position.y,2,4);
//            shapeRenderer.circle(orb.position.x,orb.position.y,5);
        }
        for(Dark dark : darks){
            sb.draw(dark.getDarkTexture(),dark.position.x,dark.position.y,2,4);
//            shapeRenderer.circle(dark.position.x,dark.position.y,5);
        }
//        shapeRenderer.end();
    }
    public void dispose(){
        orb.dispose();
        dark.dispose();
    }

    public int checkCollision(Rectangle stork){
        int ret=0;
        for(Orb orb: orbs){
            if(stork.contains(orb.getOrbCircle())){
                orb.reposition();
                ret= 1;
            }
        }
        for (Dark dark:darks){
            if(stork.contains(dark.getDarkCircle())){
                dark.reposition();
                ret = -1;
            }
        }
        return ret;
    }




    private class Orb{
        private Vector2 position;
        private Random rand;
        private Circle orbCircle;
        private Texture texture;

        public Orb(){
            rand = new Random();
            position = new Vector2(rand.nextInt(Moonshot.WIDTH) + Moonshot.WIDTH,rand.nextInt(Moonshot.HEIGHT));
            orbCircle = new Circle(position.x,position.y,ORB_RADIUS);
            orbCircle.setPosition(position);
            texture = new Texture(Gdx.files.internal("particle/orbSprite.png"));

        }

        public Texture getOrbTexture(){
            return texture;
        }

        public void update(float dt){
            if(position.x< -32){
                position.x = rand.nextInt(Moonshot.WIDTH) + Moonshot.WIDTH;
                position.y = column.get(rand.nextInt(NumSteps - 1));
            }
            else
            {
                position.x-= ORB_SPEED*dt;
            }
            orbCircle.set(position.x,position.y,ORB_RADIUS);
        }
        public void dispose(){
            texture.dispose();
        }
        public Circle getOrbCircle(){return orbCircle;}

        public void reposition(){
            position.x = rand.nextInt(100) + 100;
            position.y = column.get(rand.nextInt(NumSteps - 1));
        }
    }

    private class Dark{
        private Vector2 position;
        private Random rand;
        private Texture texture;
        private Circle darkCircle;
        public Dark(){
            rand = new Random();
            position = new Vector2(rand.nextInt(Moonshot.WIDTH) + Moonshot.WIDTH,rand.nextInt(Moonshot.HEIGHT));
            darkCircle = new Circle(position.x,position.y,ORB_RADIUS);
            texture = new Texture(Gdx.files.internal("particle/darkSprite.png"));
        }


        public void update(float dt){
            if(position.x< -32){
                position.x = rand.nextInt(Moonshot.WIDTH) + Moonshot.WIDTH;
                position.y = column.get(rand.nextInt(NumSteps - 1));
            }
            else
            {
                position.x-= ORB_SPEED*dt;
            }
            darkCircle.set(position.x,position.y,ORB_RADIUS);
        }
        public void dispose(){
            texture.dispose();
        }
        public Texture getDarkTexture(){return texture;}
        public Circle getDarkCircle(){return darkCircle;}

        public void reposition(){
            position.x = rand.nextInt(Moonshot.WIDTH) + 100;
            position.y = column.get(rand.nextInt(NumSteps - 1));
        }
    }
}
