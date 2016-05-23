package com.mygdx.rozproszone.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.rozproszone.Game;
import com.mygdx.rozproszone.GameStateManager;

/**
 * Created by Przemysław on 2016-05-22.
 */
public class GameLobbyState extends GameState {
    private BitmapFont font;
    private int lapsCounter;
    private String ip;
    private int selectedOption;
    private boolean isStarting;
    private String[] options = {
            "Back"
    };


    protected GameLobbyState(GameStateManager gsm, String ip) {
        super(gsm);
        this.ip = ip;
        selectedOption = 0;
        this.isStarting = false;
        this.lapsCounter = 3;
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

                // join server
                gsm.set(new JoinServerState(gsm,ip));
                dispose();

                /*P*/
//                dispose();
            }
        }
        /*
            lapsCounter = LobbyPacket.laps;
            isStarting = LobbyPacket.isStarting;

        */

        isStarting = true;
        if (isStarting)
        {
            PlayState playState = new PlayState(gsm, lapsCounter);
            playState.setLaps(lapsCounter);
            playState.setServer(ip);
            gsm.set(playState);
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
