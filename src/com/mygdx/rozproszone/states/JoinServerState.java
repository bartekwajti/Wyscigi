/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.mygdx.rozproszone.Game;
import com.mygdx.rozproszone.GameStateManager;
import com.mygdx.rozproszone.network.Server;

/**
 *
 * @author Admin
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
    
    public JoinServerState(GameStateManager gsm) {
        super(gsm);
        /*skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        ipInput = new TextField("", skin);
        ipInput.setPosition(100, 100);
        ipInput.setMessageText("Enter server ip");
        
        ipInput.setTextFieldListener(new TextFieldListener() {
			public void keyTyped (TextField textField, char key) {
				if (key == '\n') textField.getOnscreenKeyboard().show(false);
			}
		});
        */
        this.selectedOption = 0;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("kremlin.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        //parameter.color = Color.GREEN;
        font = generator.generateFont(parameter);
        
        generator.dispose();
        //ipInput.setDisabled(false);
        //Gdx.input.setInputProcessor(this);
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
                //start server
                Gdx.input.getTextInput(this,"Enter adress IP","localhost","");
//                dispose();
            } else if (selectedOption == 1) {
                // join server
                GameLobbyState lobbyState = new GameLobbyState(gsm);

                gsm.set(lobbyState);

                /*PlayState playState = new PlayState(gsm);
                playState.setServer(ip);
                gsm.set(playState);*/
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
        font.draw(batch, "IP: " + ip, Game.WIDTH/2-200, Game.HEIGHT-50-font.getLineHeight());

        for(int i = 0; i < options.length; ++i) {
            if(i == selectedOption)
                font.setColor(Color.WHITE);
            else
                font.setColor(Color.GREEN);

            font.draw(batch, options[i], Game.WIDTH/2-200, Game.HEIGHT-200-i*font.getLineHeight());
        }

        //font.draw(batch, "Press ENTER to type, S to start", Game.WIDTH/2-200, Game.HEIGHT-100-font.getLineHeight());

        
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
    public void canceled() {
        ip = "localhost";
    }
    
}
