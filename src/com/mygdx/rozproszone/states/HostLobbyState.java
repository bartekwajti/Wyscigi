package com.mygdx.rozproszone.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.rozproszone.Game;
import com.mygdx.rozproszone.GameStateManager;
import com.mygdx.rozproszone.network.Server;

/**
 * Created by Przemysław on 2016-05-19.
 */
public class HostLobbyState extends GameState {

    private BitmapFont font;
    private int lapsCounter;
    private String ip;
    private int selectedOption;
    private String[] options = {
            "Start Game",
            "Options",
            "Back"
    };


    protected HostLobbyState(GameStateManager gsm, int lapsCounter, String ip) {
        super(gsm);
        this.ip = ip;
        selectedOption = 0;
        this.lapsCounter = lapsCounter;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("kremlin.ttf"));
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

            if(selectedOption == 0) {
                PlayState playState = new PlayState(gsm, lapsCounter);
                playState.setLaps(lapsCounter);
                playState.setServer(ip);
                gsm.set(playState);
//                dispose();
            }
            else if (selectedOption == 1){
                gsm.set(new OptionsLobbyState(gsm,ip));
            }
            else if(selectedOption == 2) {
                // join server
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
        font.draw(batch, "Laps Number: " + Integer.toString(lapsCounter), Game.WIDTH/2-200, Game.HEIGHT-80);

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
