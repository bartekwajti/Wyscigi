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

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
 */

public class OptionsLobbyState extends GameState implements Input.TextInputListener{

    public interface OptionsListener {
        public void setOptions(int laps, int lives);
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

    protected OptionsLobbyState(GameStateManager gsm, String ip, int lapsCount, int livesCount) {

        super(gsm);
        this.ip = ip;
        selectedOption = 0;
        enterLaps = false;
        enterLives = false;
        this.lapsCount = lapsCount;
        this.livesCount = livesCount;
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
                optionsListener.setOptions(lapsCount, livesCount);
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
            if (i == 0){
                font.draw(batch, options[i] + " " + lapsCount, Config.WIDTH/2-200, Config.HEIGHT-200-i*font.getLineHeight());
            }
            else if (i == 1){
                font.draw(batch, options[i] + " " + livesCount, Config.WIDTH/2-200, Config.HEIGHT-200-i*font.getLineHeight());
            }
            else{
                font.draw(batch, options[i], Config.WIDTH/2-200, Config.HEIGHT-200-i*font.getLineHeight());
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
