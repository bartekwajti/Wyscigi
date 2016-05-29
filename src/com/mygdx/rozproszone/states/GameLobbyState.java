package com.mygdx.rozproszone.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.rozproszone.Config;
import com.mygdx.rozproszone.Game;
import com.mygdx.rozproszone.GameStateManager;
import com.mygdx.rozproszone.network.Client;
import com.mygdx.rozproszone.network.Server;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
 */

public class GameLobbyState extends GameState implements Client.LobbyListener {
    private BitmapFont font;
    private int lapsCounter;
    private String ip;
    private int selectedOption;
    private boolean isStarting;
    private int livesCounter;
    private String[] options = {
            "Back"
    };

    Client client;

    protected GameLobbyState(GameStateManager gsm, String ip) {
        super(gsm);
        this.ip = ip;
        selectedOption = 0;
        this.isStarting = false;
        //this.lapsCounter = 3;
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("kremlin.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        font = generator.generateFont(parameter);

        generator.dispose();

        client =  new Client(ip, Server.PORT);
        client.setLobbyListener(this);

        this.lapsCounter = client.getSetupGamePacket().lapsCount;
        this.livesCounter = client.getSetupGamePacket().lives;

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

        //isStarting = true;
        if (isStarting)
        {
            PlayState playState = new PlayState(gsm);
            playState.setLaps(lapsCounter);
            playState.setLives(livesCounter);
            client.setPacketProvider(playState);
            playState.setServer(ip);
            playState.setClient(client);

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
        font.draw(batch, "Laps Number: " + Integer.toString(lapsCounter), Config.WIDTH/2-200, Config.HEIGHT-60);
        font.draw(batch, "Lives Number: " + Integer.toString(livesCounter), Config.WIDTH/2-200, Config.HEIGHT-110);


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

    @Override
    public void onGameStart() {
        isStarting = true;
    }

    @Override
    public void onLapsCountChanged(int lapsCount) {
        this.lapsCounter = lapsCount;
    }

    @Override
    public void onLivesCountChanged(int livesCount) {
        this.livesCounter = livesCount;
    }
}
