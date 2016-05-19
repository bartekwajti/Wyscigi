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
 * @author Daniel
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
   
    
    public Level(String levelTextureName){
        float w = 1366;
        float h = 768;
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
        
    }
    
    
    public void startView(){
         
       
           cam.update();
        tiledMapRenderer.setView(cam);
         tiledMapRenderer.render();
          this.batch.begin();
        
        //this.levelSprite.draw(batch);
       
        
    }
    
    public void draw(Player player){
        player.getCarImage().draw(batch);
       

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
