package gamePackages.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import gamePackages.entities.Player;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameScreen implements Screen, InputProcessor {
    protected SpriteBatch batch;
    protected Texture image;


    protected OrthographicCamera camera;
    protected FitViewport viewport;
    protected Player player;
    protected float elapsedTime;
    @Override
    public void show() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        player = new Player("character/beren.png", 0, 0, 32, 48, 100);
        player.setSpeed(150);
        camera = new OrthographicCamera();
        camera.setToOrtho(false,0,0);
        camera.zoom = 0.3f;
        viewport = new FitViewport(640,480,camera);
        viewport.apply();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);
        input(delta);

        batch.begin();
        batch.draw(image,0,0,640,480);
        this.player.entityRender(batch, elapsedTime);
        batch.end();
        player.entityHitboxRenderer(camera);
        player.updatePosition();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        viewport.update(width, height);
    }

    public boolean scrolled(float amountX, float amountY) {
        // amountY donne la direction du défilement : positif pour vers le bas, négatif pour vers le haut
        if (amountY < 0 && camera.zoom>0.3f) { camera.zoom -= 0.1f; }
        else if (amountY > 0 && camera.zoom<1f) {camera.zoom += 0.1f; }
        return true;
    }

    public void movCamera() {
        camera.position.x = player.getPosX()+12;
        camera.position.y = player.getPosY()+21;
        camera.update();

    }

    public void input(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.goTop(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.goBottom(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.goLeft(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.goRight(delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            player.setShowHitboxRectangle();
        }
        movCamera();
    }


    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }
    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }


    // Vous pouvez laisser les autres méthodes non implémentées si vous n'en avez pas besoin
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
}
