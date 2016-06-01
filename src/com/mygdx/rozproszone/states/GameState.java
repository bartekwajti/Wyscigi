package com.mygdx.rozproszone.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.rozproszone.GameStateManager;

import java.net.UnknownHostException;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
 */

public abstract class GameState {
    
    protected GameStateManager gsm;
    
    protected GameState(GameStateManager gsm) {

        this.gsm = gsm;
    }
    
    public abstract void handleInput() throws UnknownHostException;
    public abstract void update(float dt) throws UnknownHostException;
    public abstract void render(SpriteBatch batch);
    public abstract void dispose();
}
