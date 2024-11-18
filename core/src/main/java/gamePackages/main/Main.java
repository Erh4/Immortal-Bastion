package gamePackages.main;

import com.badlogic.gdx.Game;
import gamePackages.screens.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());

    }
}
