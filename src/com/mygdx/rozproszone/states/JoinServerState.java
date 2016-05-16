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

/**
 *
 * @author Admin
 */
public class JoinServerState extends GameState implements TextInputListener {
    
    BitmapFont font;
    private TextField ipInput;
    private Skin skin;
    private String ip = "localhost";
    
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
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("kremlin.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.color = Color.GREEN;
        font = generator.generateFont(parameter);
        
        generator.dispose();
        //ipInput.setDisabled(false);
        //Gdx.input.setInputProcessor(this);
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
            Gdx.input.getTextInput(this, "Enter server ip", "localhost", "");
        else if(Gdx.input.isKeyJustPressed(Input.Keys.S))
        {
            PlayState playState = new PlayState(gsm);
            playState.setServer(ip);
            gsm.set(playState);
            dispose();
        }
               
    }

    @Override
    public void update(float dt) {
        handleInput();
        
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        
        font.draw(batch, "Press ENTER to type, S to start", Game.WIDTH/2-200, Game.HEIGHT-100-font.getLineHeight());
        font.draw(batch, "IP: " + ip, Game.WIDTH/2-200, Game.HEIGHT-200-font.getLineHeight());
        
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
