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
public class OptionsLobbyState extends GameState implements Input.TextInputListener{

    public interface OptionsListener {
        public void onLapsSet(int laps);
        public void onLivesSet(int lives);
    }
    private boolean enterLaps;
    private boolean enterLives;
    private BitmapFont font;
    private String ip;
    private int lapsCount;
    private int livesCount;
    private int selectedOption;
    private String[] options = {
            "Laps",
            "Lives",
            "Confirm"
    };

    private OptionsListener optionsListener;

    protected OptionsLobbyState(GameStateManager gsm, String ip) {

        super(gsm);
        this.ip = ip;
        selectedOption = 0;
        enterLaps = false;
        enterLives = false;
        this.lapsCount = 3;
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
                enterLaps = true;
                Gdx.input.getTextInput(this,"Enter number of Laps",Integer.toString(lapsCount),"");

            }
            if(selectedOption == 1) {
                enterLives = true;
                Gdx.input.getTextInput(this,"Enter number of Lives",Integer.toString(livesCount),"");
            }
            else if(selectedOption == 2) {
                // go back to HostLobbyState
                //gsm.set(new HostLobbyState(gsm, this.lapsCount, ip));
                optionsListener.onLapsSet(lapsCount);
                gsm.pop();
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

        for(int i = 0; i < options.length; ++i) {
            if(i == selectedOption)
                font.setColor(Color.WHITE);
            else
                font.setColor(Color.GREEN);
            if (selectedOption == 0){
                font.draw(batch, options[i] + " " + lapsCount, Game.WIDTH/2-200, Game.HEIGHT-200-i*font.getLineHeight());
            }
            else if (selectedOption == 1){
                font.draw(batch, options[i] + " " + livesCount, Game.WIDTH/2-200, Game.HEIGHT-200-i*font.getLineHeight());
            }
        }

        batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
    }
    @Override
    public void input(String string) {
        if (enterLaps) {
            try {
                lapsCount = Integer.parseInt(string);
                enterLaps = false;
            } catch (NumberFormatException nfe) {
                lapsCount = 3;
            }
        }
        else if (enterLives){
            try {
                livesCount = Integer.parseInt(string);
                enterLives = false;
            } catch (NumberFormatException nfe) {
                livesCount = 3;
            }
        }
    }

    @Override
    public void canceled() {
        lapsCount = 3;
    }

    public void setOptionsListener(OptionsListener optionsListener) {
        this.optionsListener = optionsListener;
    }
}
