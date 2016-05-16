package com.mygdx.rozproszone;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.rozproszone.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1366;
                config.height = 768;
                config.title = "Projekt - Przetwarzanie Rozproszone - Wyscigi";
                config.resizable = false;
                
                new LwjglApplication(new Game(), config);
	}
}
