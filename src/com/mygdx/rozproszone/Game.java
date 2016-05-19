package com.mygdx.rozproszone;

import com.mygdx.rozproszone.states.ServerClientState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Game extends ApplicationAdapter {
    
    public static final int WIDTH = 1366;
    public static final int HEIGHT = 768;
    GameStateManager gsm;
    SpriteBatch batch;
    
    @Override
    public void create () {
      
        Gdx.gl.glClearColor(0, 0, 0, 1);
        gsm = new GameStateManager();
        gsm.push(new ServerClientState(gsm));
        
        batch = new SpriteBatch();
        
    }
    
    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
        
       
    }
    
}
