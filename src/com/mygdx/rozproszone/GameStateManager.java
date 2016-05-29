package com.mygdx.rozproszone;

import com.mygdx.rozproszone.states.GameState;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Stack;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
 */

public class GameStateManager {
    
    private Stack<GameState> states;
    
    public GameStateManager() {

        states = new Stack<GameState>();
    }
    
    public void push(GameState gameState) {

        states.push(gameState);
    }
    
    public void pop() {

        states.pop();
    }
    
    public void set(GameState gameState) {

        states.pop();
        states.push(gameState);
    }
    
    public void update(float dt) {

        states.peek().update(dt);
    }
    
    public void render(SpriteBatch batch) {

        states.peek().render(batch);
    }

    public void dispose() {

        states.peek().dispose();
        pop();
    }
    
}
