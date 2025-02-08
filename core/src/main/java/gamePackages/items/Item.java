package gamePackages.items;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import gamePackages.entities.Player;


/**
 * Represents a generic item in the game.
 *
 * The `Item` class serves as a base class for all specific item types. It includes basic
 * attributes such as name, position, sprite, and texture, and defines abstract methods
 * for behavior that must be implemented by subclasses.
 */
public abstract class Item {
    // si "None" pose des problèmes, ajouter un etat booléen "deprecated"
    protected String name;
    protected float posX;
    protected float posY;
    protected Texture itemTexture;
    protected Sprite itemSprite;
    protected String spritePath;

    /**
     * Default constructor for creating an uninitialized item.
     */
    public Item() {};


    /**
     * Constructs an item with the specified attributes.
     *
     * @param name The name of the item.
     * @param posX The X-coordinate of the item's position.
     * @param posY The Y-coordinate of the item's position.
     * @param spritePath The file path to the sprite texture for the item.
     *
     * This constructor initializes the item's attributes and creates its texture and sprite.
     */
    public Item(String name, float posX, float posY, String spritePath) {
        this.spritePath = spritePath;
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.itemTexture = new Texture(spritePath);
        this.itemSprite = new Sprite(itemTexture);

    }

    /**
     * Sets the name of the item.
     *
     * @param name The name to assign to the item.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the type of the item.
     *
     * @return A string representing the item's type.
     *
     * Subclasses must implement this method to define their specific type.
     */
    public abstract String getType();

    /**
     * Gets the name of the item.
     *
     * @return The name of the item.
     */
    public String getName() { return name; }

    /**
     * Gets the texture of the item.
     *
     * @return The item's texture.
     */
    public Texture getItemTexture() { return itemTexture; }

    /**
     * Gets the sprite of the item.
     *
     * @return The item's sprite.
     */
    public Sprite getItemSprite() { return itemSprite; }

    /**
     * Uses the item, applying its effects to the specified player.
     *
     * @param player The player on whom the item is used.
     * @throws Exception If an error occurs while using the item.
     *
     * Subclasses must implement this method to define the specific behavior of the item.
     */
    public abstract void use(Player player) throws Exception;
}
