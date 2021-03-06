package com.mygdx.rozproszone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
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
    
    private BitmapFont font;
    
    private SpriteBatch batch;

    private TiledMap map;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera cam;
    
    private MapLayer layer0;
    private MapLayer layer1;
    private MapLayer layer2;
    
    private MapObjects collisionObjects;
    private MapObjects slowDustObjects;
    
    private Texture scoreBoardTexture;
    private Sprite scoreBoardSprite;

    private Texture winnerFlagTexture;
    private Sprite winnerFlagSprite;
    private boolean playerHasWon;
    private int winningID;

    public Level(){

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

        winnerFlagTexture = new Texture(Config.FILES_ENDING_FLAG);
        winnerFlagSprite = new Sprite(winnerFlagTexture);
        winnerFlagSprite.setPosition((Config.WIDTH - scoreBoardTexture.getWidth())/2.0f,Config.HEIGHT/2.0f);
        winningID = 0;
    }
    
    
    public void startView(){

        cam.update();

        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();

        this.batch.begin();
        this.scoreBoardSprite.draw(batch);

        if (playerHasWon){
            winnerFlagSprite.draw(batch);
            font.setColor(Config.COLORS[winningID]);
            font.draw(batch, "THE WINNER IS " + Config.PLAYERS_NAMES[winningID], (Config.WIDTH - scoreBoardTexture.getWidth())/2.0f  ,(Config.HEIGHT + winnerFlagSprite.getHeight())/2.0f);
            font.setColor(Color.WHITE);
            font.draw(batch,"RESTART GAME",(Config.WIDTH - scoreBoardTexture.getWidth())/2.0f ,Config.HEIGHT/2.0f - winnerFlagSprite.getHeight() - font.getXHeight());
        }
    }
    
    public void draw(Player player){

        player.getCarImage().draw(batch);
        drawPlayerLaps(player);
    }

    public void drawPlayerLaps(Player player){

        font.setColor(Config.COLORS[player.getID()]);
        font.draw(batch, Config.PLAYERS_NAMES[player.getID()], Config.PLAYER_NAME_POSITION_X ,Config.PLAYER_NAME_POSITION_Y - player.getID()*40);
        font.draw(batch,""+ player.getLaps(), Config.PLAYER_LAPS_POSITION_X  ,Config.PLAYER_LAPS_POSITION_Y - (player.getID())*40);
        font.draw(batch,""+ player.getLives(), Config.PLAYER_LIVES_POSITION_X ,Config.PLAYER_LIVES_POSITION_Y - (player.getID())*40);

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

    public void playerWinning(int id) {
        playerHasWon = true;
        winningID = id;
    }

    public void newGame() {
        playerHasWon = false;
    }
}
