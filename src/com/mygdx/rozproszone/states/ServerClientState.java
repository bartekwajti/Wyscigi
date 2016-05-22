/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.rozproszone.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.mygdx.rozproszone.Game;
import com.mygdx.rozproszone.GameStateManager;
import com.mygdx.rozproszone.network.Server;

/**
 *
 * @author Admin
 */
public class ServerClientState extends GameState {

    private BitmapFont font;
    
    private int selectedOption;
    private String[] options = {
        "Create server",
        "Join server",
        "Exit Game"
    };
    
    public ServerClientState(GameStateManager gsm) {
        super(gsm);
        
        selectedOption = 0;
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("kremlin.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 48;
        //parameter.color = Color.GREEN;
        font = generator.generateFont(parameter);
        
        generator.dispose();
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if(selectedOption < options.length - 1)
                ++selectedOption;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if(selectedOption > 0)
                --selectedOption;
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
           
            if(selectedOption == 0) {
                //start server
                Server server = new Server(4); //server for 4 players
                Thread th = new Thread(server);
                th.start();


                HostLobbyState lobbyState = new HostLobbyState(gsm,3,"localhost");
                gsm.set(lobbyState);
                dispose();
                //server.stop();
                /*PlayState playState = new PlayState(gsm);
                playState.setServer("localhost");
                gsm.set(playState);*/
//                dispose();
            }
            else if(selectedOption == 1) {
                // join server
                gsm.set(new JoinServerState(gsm,"localhost"));
                dispose();
            }
            else if(selectedOption == 2){
                System.exit(0);
            }
            
        }
        
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        
        for(int i = 0; i < options.length; ++i) {
            if(i == selectedOption)
                font.setColor(Color.WHITE);
            else
                font.setColor(Color.GREEN);
            
            font.draw(batch, options[i], Game.WIDTH/2-200, Game.HEIGHT-200-i*font.getLineHeight());
        }
        
        batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
    }
    
}
