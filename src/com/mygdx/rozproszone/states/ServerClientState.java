package com.mygdx.rozproszone.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.mygdx.rozproszone.Config;
import com.mygdx.rozproszone.GameStateManager;
import com.mygdx.rozproszone.network.Server;

import java.net.UnknownHostException;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
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

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Config.FILES_FONT));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 48;
        font = generator.generateFont(parameter);
        
        generator.dispose();
    }

    @Override
    public void handleInput() throws UnknownHostException {

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

                Server server = new Server(Config.NUMBER_OF_PLAYERS_IN_GAME);
                Thread th = new Thread(server);
                th.start();

                HostLobbyState lobbyState = new HostLobbyState(gsm,Config.NUMBER_OF_LAPS,"localhost",Config.NUMBER_OF_LIVES);
                gsm.set(lobbyState);
                dispose();

            }
            else if(selectedOption == 1) {

                gsm.set(new JoinServerState(gsm,"localhost"));
                dispose();
            }
            else if(selectedOption == 2){
                System.exit(0);
            }
            
        }
        
    }

    @Override
    public void update(float dt) throws UnknownHostException {

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
            
            font.draw(batch, options[i], Config.WIDTH/2-200, Config.HEIGHT-200-i*font.getLineHeight());
        }
        
        batch.end();
    }

    @Override
    public void dispose() {

        font.dispose();
    }
    
}
