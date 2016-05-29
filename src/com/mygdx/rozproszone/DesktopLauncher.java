package com.mygdx.rozproszone;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
 */

public class DesktopLauncher {

	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		        config.width = Config.WIDTH;
                config.height = Config.HEIGHT;
                config.title = Config.GAME_NAME;
                config.resizable = false;
                
                new LwjglApplication(new Game(), config);
	}
}
