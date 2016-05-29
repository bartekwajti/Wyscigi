package com.mygdx.rozproszone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.Color;


/**
 *
 * @author Daniel && Bartlomiej && Przemysław
 */

public class Level {
    
    private BitmapFont font;
    
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
    private Sprite scoreBoardSprite;

    private Texture numbersTexture[];
    private Sprite  numbersSprite[];

    int lapsPositionX;
    int lapsPositionY;



    public Level(String levelTextureName){

        this.lapsPositionX = 250;
        this.lapsPositionY = 600;

        this.levelTexture=new Texture(levelTextureName);
        this.levelSprite=new Sprite(this.levelTexture,Config.WIDTH,Config.HEIGHT);

        batch = new SpriteBatch(); 
        map = new TmxMapLoader().load(Config.FILES_MAP);

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Config.WIDTH, Config.HEIGHT);
        cam.update();

        layer0 = map.getLayers().get(0);
        layer1 = map.getLayers().get(1);
        layer2 = map.getLayers().get(2);
        layer0.setVisible(true);
        layer1.setVisible(true);
        layer2.setVisible(true);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Config.FILES_FONT));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 26;
        font = generator.generateFont(parameter);

        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        collisionObjects= map.getLayers().get("Collision").getObjects();
        slowDustObjects= map.getLayers().get("SlowDust").getObjects();

        scoreBoardTexture = new Texture(Config.FILES_SCOREBOARD);
        scoreBoardSprite = new Sprite(scoreBoardTexture);
        scoreBoardSprite.setPosition(Config.WIDTH - scoreBoardTexture.getWidth(),0);

        numbersTexture = new Texture[10];
        numbersSprite = new Sprite[10];

        for (int i =0;i<10;i++){

            numbersTexture[i] = new Texture(Config.NUMBER_FILE_NAMES[i]);
            numbersSprite[i] = new Sprite(numbersTexture[i]);
            numbersSprite[i].setPosition(Config.WIDTH - scoreBoardTexture.getWidth() + lapsPositionX,lapsPositionY);
        }
    }
    
    
    public void startView(){

        cam.update();

        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();

        this.batch.begin();
        this.scoreBoardSprite.draw(batch);
    }
    
    public void draw(Player player){

        player.getCarImage().draw(batch);
    }

    public void drawPlayerLaps(Player player){

        int laps = player.getLaps();
        int displacement = player.getID();

        switch (displacement) {
            case 0:
                font.setColor(Color.BLACK);
                font.draw(batch, "Black " + laps + " Lives " + player.getLives(), Config.WIDTH - scoreBoardTexture.getWidth() + lapsPositionX - 200.0f, lapsPositionY - displacement * 30 + font.getXHeight());
                break;
            case 1:
                font.setColor(Color.GOLD);
                font.draw(batch, "Yellow " + laps + " Lives " + player.getLives(), Config.WIDTH - scoreBoardTexture.getWidth() + lapsPositionX - 200.0f, lapsPositionY - displacement * 30 + font.getXHeight());
                break;
            case 2:
                font.setColor(Color.FIREBRICK);
                font.draw(batch, "Red " + laps + " Lives " + player.getLives(), Config.WIDTH - scoreBoardTexture.getWidth() + lapsPositionX - 200.0f, lapsPositionY - displacement * 30 + font.getXHeight());
                break;
            case 3:
                font.setColor(Color.SKY);
                font.draw(batch, "Blue " + laps + " Lives " + player.getLives(), Config.WIDTH - scoreBoardTexture.getWidth() + lapsPositionX - 200.0f, lapsPositionY - displacement * 30 + font.getXHeight());
                break;
            default:
                font.setColor(Color.WHITE);
                font.draw(batch, "DEFAULT", Config.WIDTH - scoreBoardTexture.getWidth() + lapsPositionX - 200.0f, lapsPositionY - displacement * 30 + font.getXHeight());
                break;
        }
    }
        
    public void updateView(){

        batch.end();
    }    
   
    
    public MapObjects getCollisionObjects(){

        return this.collisionObjects;
    }

     public MapObjects getSlowDustObjects(){

        return this.slowDustObjects;
    }
             
}
