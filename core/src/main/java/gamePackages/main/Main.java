package gamePackages.main;

import com.badlogic.gdx.Game;
import gamePackages.screens.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
/**
 * The main entry point of the game application.
 *
 * The `Main` class extends the `Game` class from LibGDX, initializing the game and setting up the initial screen.
 */
public class Main extends Game {

    /**
     * Called when the application is created.
     *
     * This method initializes the game by setting the initial screen to a new instance of `GameScreen`.
     * Any exceptions during the screen initialization are caught and logged.
     */
    @Override
    public void create() {
        try {
            setScreen(new GameScreen());
        }
        catch (Exception e) {
            System.out.println("Internal Error"+e.getMessage());
        }

    }
}
