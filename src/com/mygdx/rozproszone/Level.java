/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.rozproszone;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 *
 * @author Daniel && Bartek && Przemysław
 */
public class Level {
    private SpriteBatch batch;
    private Texture levelTexture;
    private Sprite levelSprite;
    private TiledMap map;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera cam;
    private MapLayer layer0;
    private MapLayer layer1;
    private MapLayer layer2;
    MapObjects collisionObjects;
    MapObjects slowDustObjects;
    private Texture scoreBoardTexture;
    private Sprite scoreBoard;
    private Texture threeTexture;
    private Sprite three;
    private Texture twoTexture;
    private Sprite two;
    private Texture oneTexture;
    private Sprite one;
    float w;
    float h;
    int lapsPositionX;
    int lapsPositionY;


    public Level(String levelTextureName){
        this.w = 1366;
        this.h = 768;
        this.lapsPositionX = 250;
        this.lapsPositionY = 600;
        this.levelTexture=new Texture(levelTextureName);
        this.levelSprite=new Sprite(this.levelTexture,1366,768);
        batch = new SpriteBatch(); 
        map = new TmxMapLoader().load("assets//mapa.tmx");
        cam = new OrthographicCamera();
        cam.setToOrtho(false, w, h);
        cam.update();
        layer0 = map.getLayers().get(0);
        layer1 = map.getLayers().get(1);
        layer2 = map.getLayers().get(2);
        layer0.setVisible(true);
        layer1.setVisible(true);
        layer2.setVisible(true);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        collisionObjects= map.getLayers().get("Collision").getObjects();
        slowDustObjects= map.getLayers().get("SlowDust").getObjects();
        scoreBoardTexture = new Texture("ScoreBoard.jpg");
        scoreBoard = new Sprite(scoreBoardTexture);
        scoreBoard.setPosition(w-scoreBoardTexture.getWidth(),0);
        threeTexture = new Texture("Three.jpg");
        twoTexture = new Texture("Two.jpg");
        oneTexture = new Texture("One.jpg");
        three = new Sprite(threeTexture);
        two = new Sprite(twoTexture);
        one = new Sprite(oneTexture);
        three.setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY);
        two.setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY);
        one.setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY);

    }
    
    
    public void startView(){
         
       
        cam.update();
        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();
        this.batch.begin();
        this.scoreBoard.draw(batch);

        //this.levelSprite.draw(batch);
       
        
    }
    
    public void draw(Player player){
        player.getCarImage().draw(batch);
    }
    public void drawPlayerLaps(Player player, int displacement){
        int id = player.getID();
        int laps = player.getLaps();
        switch (laps){
            case 3:
                three.setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY -(displacement*30));
                this.three.draw(batch);
                break;
            case 2:
                two.setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                this.two.draw(batch);
                break;
            case 1:
                one.setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                this.one.draw(batch);
                break;
            case 0:

                break;
        }

        if (player.getLaps() == 3)
        {

        }
        else if (player.getLaps() == 2)
        {

        }
    }
        
    public void updateView(){
        batch.end();
    }    
   
    
    public MapObjects getCollisionObjects()
    {
        return this.collisionObjects;
    }
     public MapObjects getSlowDustObjects()
    {
        return this.slowDustObjects;
    }
             
}
