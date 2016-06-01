package com.mygdx.rozproszone;
import com.badlogic.gdx.graphics.Color;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
 */

public class Config {

    //Window parameters

    public static final int WIDTH = 1366;
    public static final int HEIGHT = 768;
    public static final String GAME_NAME = "Projekt - Przetwarzanie Rozproszone - Wyscigi";

    //SERVER parameters

    public static final int NUMBER_OF_PLAYERS_IN_GAME = 4;
    public static final int NUMBER_OF_LIVES = 3;
    public static final int NUMBER_OF_LAPS = 3;

    //Client parameters



    //Game parameters

    public static final String[] PLAYERS_NAMES ={
            "BLACK",
            "YELLOW",
            "RED",
            "BLUE",
    };

    public static final Color [] COLORS = {
            Color.BLACK,
            Color.YELLOW,
            Color.FIREBRICK,
            Color.BLUE,

    };

    public static final Float STARTING_POSITION_X = 150.0f;
    public static final Float STARTING_POSITION_Y = 400.0f;
    
    public static final int PLAYER_NAME_POSITION_X=1040;
    public static final int PLAYER_NAME_POSITION_Y=640;
    public static final int PLAYER_LAPS_POSITION_X=1200;
    public static final int PLAYER_LAPS_POSITION_Y=640;
    public static final int PLAYER_LIVES_POSITION_X=1300;
    public static final int PLAYER_LIVES_POSITION_Y=640;


    public static final Float[] PLAYER_STARTING_POSITION_X ={
            STARTING_POSITION_X,
            STARTING_POSITION_X +60.0f,
            STARTING_POSITION_X,
            STARTING_POSITION_X +60.0f
    };

    public static final Float[] PLAYER_STARTING_POSITION_Y ={
            STARTING_POSITION_Y ,
            STARTING_POSITION_Y ,
            STARTING_POSITION_Y -80.0f,
            STARTING_POSITION_Y -80.0f,
    };
    
    //Game State parameters


    //File names

    public static final String FILES_FONT ="FasterOne-Regular.ttf";
    public static final String FILES_MAP="assets//mapa.tmx";
    public static final String FILES_SCOREBOARD ="ScoreBoard.jpg";
    public static final String FILES_ENDING_FLAG = "endRaceFlag.png";

    public static final String[] PLAYER_IMAGE_FILE_NAMES ={
            "player1.png",
            "player2.png",
            "player3.png",
            "player4.png",
    };

    public static final String[] PLAYER_CRASH_IMAGE_FILE_NAMES ={
            "player1Crash.png",
            "player2Crash.png",
            "player3Crash.png",
            "player4Crash.png",
    };

}
