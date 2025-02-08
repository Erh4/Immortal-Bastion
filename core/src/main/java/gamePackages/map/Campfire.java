package gamePackages.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import gamePackages.entities.Player;


/**
 * Represents a campfire in the game world, which serves as a point of interaction for the player.
 *
 * The `Campfire` class provides methods for rendering the campfire, its interaction hitbox,
 * and displaying interaction prompts to the player.
 */
public class Campfire {

    protected float posX, posY;
    protected Rectangle interactionRectangle;
    protected ShapeRenderer shapeRenderer;
    protected Texture campfireTexture;
    protected Sprite campfireSprite;
    protected Sprite interactionSprite;

    protected FreeTypeFontGenerator fontGenerator;
    protected FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    protected BitmapFont uiFont;

    /**
     * Constructs a `Campfire` at the specified position.
     *
     * @param posX The X-coordinate of the campfire.
     * @param posY The Y-coordinate of the campfire.
     *
     * This constructor initializes the campfire's position, interaction rectangle, texture,
     * sprite, and UI elements for displaying interaction prompts.
     */
    public Campfire(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        this.campfireTexture = new Texture("maps/RF_Catacombs_v1.0/campfire.png");
        this.campfireSprite = new Sprite(campfireTexture);
        this.interactionRectangle = new Rectangle(posX-25,posY-25,100,100);
        this.shapeRenderer = new ShapeRenderer();
        this.interactionSprite = new Sprite(new Texture("ui/keys/interaction.png"));
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Bokor-Regular.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 24;
        fontParameter.borderColor = new Color(0.0F, 0.0F, 0.0F, 1.0F);
        fontParameter.borderWidth = 1;
        uiFont = fontGenerator.generateFont(fontParameter);
        uiFont.getData().setScale(0.5f);
    }

    /**
     * Gets the X-coordinate of the campfire's position.
     *
     * @return The X-coordinate of the campfire.
     */
    public float getPosX() {
        return posX;
    }

    /**
     * Sets the X-coordinate of the campfire's position.
     *
     * @param posX The new X-coordinate of the campfire.
     */
    public void setPosX(float posX) {
        this.posX = posX;
    }

    /**
     * Gets the Y-coordinate of the campfire's position.
     *
     * @return The Y-coordinate of the campfire.
     */
    public float getPosY() {
        return posY;
    }

    /**
     * Sets the Y-coordinate of the campfire's position.
     *
     * @param posY The new Y-coordinate of the campfire.
     */
    public void setPosY(float posY) {
        this.posY = posY;
    }

    /**
     * Gets the rectangle representing the campfire's interaction zone.
     *
     * @return The interaction rectangle.
     */
    public Rectangle getInteractionRectangle() {
        return interactionRectangle;
    }

    /**
     * Renders the campfire sprite at its position.
     *
     * @param batch  The `SpriteBatch` used for rendering.
     * @param camera The `OrthographicCamera` used to adjust the projection matrix.
     */
    public void renderCampfire(SpriteBatch batch, OrthographicCamera camera) {
        batch.draw(campfireSprite, posX, posY);
    }

    /**
     * Renders the campfire's interaction hitbox for debugging purposes.
     *
     * @param camera The `OrthographicCamera` used to adjust the projection matrix.
     */
    public void renderCampfireHitbox(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1,1,1,1);
        shapeRenderer.rect(posX-25,posY-25,100,100);
        shapeRenderer.end();
    }

    /**
     * Renders the interaction prompt if the player is within the interaction zone.
     *
     * @param batch  The `SpriteBatch` used for rendering.
     * @param player The player whose interaction rectangle is checked.
     */
    public void renderInteraction(SpriteBatch batch, Player player) {
        if (player.getInteractionRectangle().overlaps(interactionRectangle)) {
            batch.draw(interactionSprite, posX, posY+campfireSprite.getHeight(),16,16);
            uiFont.draw(batch, "SAVE", posX+18, posY+13+campfireSprite.getHeight());
        }
    }

}
