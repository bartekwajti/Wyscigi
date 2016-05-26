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
    }

    private BitmapFont font;
    private String ip;
    private int lapsCount;
    private int selectedOption;
    private String[] options = {
            "Laps",
            "Confirm"
    };

    private OptionsListener optionsListener;

    protected OptionsLobbyState(GameStateManager gsm, String ip) {

        super(gsm);
        this.ip = ip;
        selectedOption = 0;
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
                Gdx.input.getTextInput(this,"Enter number of Laps",Integer.toString(lapsCount),"");
            }
            else if(selectedOption == 1) {
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

            font.draw(batch, options[i], Game.WIDTH/2-200, Game.HEIGHT-200-i*font.getLineHeight());
        }

        batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
    }
    @Override
    public void input(String string) {
        try {
            lapsCount = Integer.parseInt(string);
        }
        catch (NumberFormatException nfe)
        {
            lapsCount = 3;
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
