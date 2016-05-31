package com.mygdx.rozproszone.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.mygdx.rozproszone.Config;
import com.mygdx.rozproszone.GameStateManager;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
 */

public class JoinServerState extends GameState implements TextInputListener {
    
    BitmapFont font;
    private TextField ipInput;
    private Skin skin;
    private String ip = "localhost";
    private int selectedOption;
    private String[] options = {
            "Enter server IP",
            "Join game",
            "Back"
    };
    
    public JoinServerState(GameStateManager gsm, String ip) {

        super(gsm);
        this.ip = ip;
        this.selectedOption = 0;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Config.FILES_FONT));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
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

            if (selectedOption == 0) {
                Gdx.input.getTextInput(this,"Enter adress IP","localhost","");
            } else if (selectedOption == 1) {
                GameLobbyState lobbyState = new GameLobbyState(gsm,ip);

                gsm.set(lobbyState);
                dispose();
            } else if (selectedOption == 2) {
                gsm.set(new ServerClientState(gsm));
                dispose();
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

        font.setColor(Color.GREEN);
        font.draw(batch, "IP: " + ip, Config.WIDTH/2-200, Config.HEIGHT-50-font.getLineHeight());

        for(int i = 0; i < options.length; ++i) {
            if(i == selectedOption)
                font.setColor(Color.WHITE);
            else
                font.setColor(Color.GREEN);

            font.draw(batch, options[i], Config.WIDTH/2-200,Config.HEIGHT-200-i*font.getLineHeight());
        }
        
        batch.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void input(String string) {

        ip = string;
    }

    @Override
    public void canceled()  {

        ip = "localhost";
    }
    
}
