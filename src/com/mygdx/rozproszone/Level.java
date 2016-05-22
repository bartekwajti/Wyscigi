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
 * @author Daniel && Bartlomiej && Przemysław
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

    private Texture textureNumbers[];
    private Sprite  spriteNumbers[];
    float w;
    float h;
    int lapsPositionX;
    int lapsPositionY;


    public Level(String levelTextureName){
        String[] numberFileNames ={
                "One.jpg",
                "Two.jpg",
                "Three.jpg",
                "Four.jpg",
                "Five.jpg",
                "Six.jpg",
                "Seven.jpg",
                "Eight.jpg",
                "Nine.jpg",
                "MoreThanNine.jpg"
        };
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
        textureNumbers = new Texture[10];
        spriteNumbers = new Sprite[10];
        for (int i =0;i<10;i++)
        {
            textureNumbers[i] = new Texture(numberFileNames[i]);
            spriteNumbers[i] = new Sprite(textureNumbers[i]);
            spriteNumbers[i].setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY);
        }
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
            case 9:
                spriteNumbers[laps-1].setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                spriteNumbers[laps-1].draw(batch);
                break;
            case 8:
                spriteNumbers[laps-1].setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                spriteNumbers[laps-1].draw(batch);
                break;
            case 7:
                spriteNumbers[laps-1].setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                spriteNumbers[laps-1].draw(batch);
                break;
            case 6:
                spriteNumbers[laps-1].setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                spriteNumbers[laps-1].draw(batch);
                break;
            case 5:
                spriteNumbers[laps-1].setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                spriteNumbers[laps-1].draw(batch);
                break;
            case 4:
                spriteNumbers[laps-1].setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                spriteNumbers[laps-1].draw(batch);
                break;
            case 3:
                spriteNumbers[laps-1].setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                spriteNumbers[laps-1].draw(batch);
                break;
            case 2:
                spriteNumbers[laps-1].setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                spriteNumbers[laps-1].draw(batch);
                break;
            case 1:
                spriteNumbers[laps-1].setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                spriteNumbers[laps-1].draw(batch);
                break;
            case 0:

                break;
            default:
                spriteNumbers[9].setPosition(w-scoreBoardTexture.getWidth()+lapsPositionX,lapsPositionY - (displacement*30));
                spriteNumbers[9].draw(batch);
                break;
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
