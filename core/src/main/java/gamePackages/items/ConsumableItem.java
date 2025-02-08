package gamePackages.items;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import gamePackages.entities.Player;
import gamePackages.exceptions.*;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents a consumable item in the game.
 *
 * The `ConsumableItem` class extends the `Item` class and introduces additional attributes
 * and behaviors specific to consumable items, such as quantity, effects, and timing.
 */
public class ConsumableItem extends Item {
    private int quantity = 0;
    private float value;
    protected float timing;
    protected float initialTiming;
    protected Rectangle rect;
    protected FreeTypeFontGenerator fontGenerator;
    protected FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    protected BitmapFont uiFont;
    protected String type;
    private Map<String,Boolean> subType = new HashMap<>();

    /**
     * Default constructor for creating an uninitialized consumable item.
     */
    public ConsumableItem() {};


    /**
     * Constructs a consumable item with the specified attributes.
     *
     * @param name The name of the item.
     * @param posX The X-coordinate of the item's position.
     * @param posY The Y-coordinate of the item's position.
     * @param spritePath The file path to the sprite texture for the item.
     * @param type The subtype of the item (e.g., HP, Speed).
     * @param value The value of the item's effect.
     * @throws IllegalItemException If the specified subtype is not valid.
     */
    public ConsumableItem(String name, float posX, float posY, String spritePath, String type, float value) throws IllegalItemException {
        super(name,posX,posY,spritePath);
        initMap();
        if (!subType.containsKey(type)) throw new IllegalItemException("Entrer un type existant... (voir constructeur de Player)");
        subType.put(type,true);
        setQuantity(1);
        setValue(value);

    }


    /**
     * Constructs a `ConsumableItem` with detailed parameters.
     *
     * @param name The name of the item.
     * @param posX The X-coordinate of the item's position.
     * @param posY The Y-coordinate of the item's position.
     * @param spritePath The file path to the sprite texture for the item.
     * @param type The subtype of the item (e.g., "HP", "Speed").
     * @param value The value of the item's effect.
     * @param quantity The initial quantity of the item.
     * @param timing The duration of the item's effect.
     */
    public ConsumableItem(String name, float posX, float posY, String spritePath, String type, float value, int quantity, float timing){
        // version où on précise la quantité (au cas où)
        super(name,posX,posY,spritePath);
        initMap();
        if (!subType.containsKey(type)) type ="HP";
        subType.put(type,true);
        setQuantity(quantity);
        setValue(value);
        setInitialTiming(timing);
        setTiming(timing);
        this.rect = new Rectangle(posX, posY, itemTexture.getWidth(), itemTexture.getHeight());
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Bokor-Regular.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 32;
        changeFontSize(24);
        fontParameter.borderColor = new Color(0.0F, 0.0F, 0.0F, 1.0F);
        fontParameter.borderWidth = 1;
        uiFont = fontGenerator.generateFont(fontParameter);
        uiFont.getData().setScale(0.4f);
    }
    /**
     * Initializes the subtype map with default values (all set to false).
     */
    private void initMap(){ //penser à gérer une omni-potion par exemple
        subType.put("HP",false);
        subType.put("Speed",false);
        subType.put("MaxHP",false);
        // peut-être à compléter ...
    }


    /**
     * Gets the type of the item.
     *
     * @return The type of the item, which is `"ConsumableItem"`.
     */
    @Override
    public String getType(){ return "ConsumableItem"; }


    /**
     * Gets the active subtype of the item.
     *
     * @return The active subtype of the item as a string.
     * @throws ZeroSubTypeException If no subtype is active.
     */
    public String getSubType() throws ZeroSubTypeException {
        for (String key : subType.keySet()){ // https://www.w3schools.com/java/java_ref_hashmap.asp
            if (subType.get(key)){
                return key;
            }
        }
        throw new ZeroSubTypeException("L'objet "+getName()+" n'a aucun subType...");

    }

    /**
     * Sets the value of the item's effect.
     *
     * @param value The value to set.
     */
    public void setValue(float value) { this.value = value; }

    /**
     * Gets the value of the item's effect.
     *
     * @return The value of the effect.
     */
    public float getValue() { return value; }

    /**
     * Gets the quantity of the item.
     *
     * @return The quantity of the item.
     */
    public int getQuantity() { return quantity;}

    /**
     * Increases the item's quantity by a specified amount.
     *
     * @param quantity The amount to add.
     */
    public void addQuantity(int quantity) { this.quantity += quantity; }

    /**
     * Sets the quantity of the item.
     *
     * @param quantity The new quantity.
     */
    public void setQuantity(int quantity) { this.quantity = quantity;}

    /**
     * Increments the item's quantity by 1.
     */
    public void increment() { quantity += 1; }

    /**
     * Decrements the item's quantity by 1.
     */
    public void decrement() { quantity -= 1; /*if (getQuantity() <=0) setName("None");*/}

    /**
     * Sets the initial timing for the item's effect.
     *
     * @param timing The initial timing value.
     */
    public void setInitialTiming(float timing) { this.initialTiming = timing;}

    /**
     * Gets the initial timing for the item's effect.
     *
     * @return The initial timing value.
     */
    public float getInitialTiming() { return initialTiming;}

    /**
     * Sets the remaining timing for the item's effect.
     *
     * @param timing The timing value to set.
     */
    public void setTiming(float timing) { this.timing = timing;}

    /**
     * Updates the remaining timing for the item's effect, decreasing it by the delta value.
     *
     * @param delta The time elapsed since the last update.
     */
    public void updateTiming(float delta) {
        this.timing -= delta;
        if (this.timing <= 0) {
            this.timing = 0;
        }
    }

    /**
     * Gets the remaining timing for the item's effect.
     *
     * @return The remaining timing value.
     */
    public float getTiming() { return timing; }

    /**
     * Creates a deep copy of the consumable item.
     *
     * @return A new `ConsumableItem` object with identical attributes.
     */
    public ConsumableItem deepCopy(){
        try {
        //System.out.println("Deep Copy Type >" +getCurrSubType());
        return new ConsumableItem(name, posX, posY, spritePath, getCurrSubType(), value, quantity, timing);

        } catch (Exception e) {};
        return new ConsumableItem(name, posX, posY, spritePath, type, value, quantity, timing);
    }

    /**
     * Gets the current active subtype of the item.
     *
     * @return The active subtype of the item as a string.
     * @throws ZeroSubTypeException If no subtype is active.
     * @throws IllegalItemException If multiple subtypes are active.
     */
    public String getCurrSubType() throws ZeroSubTypeException, IllegalItemException{
        String temp_subtype = "NULL";

        if (!subType.containsValue(true)) throw new ZeroSubTypeException("Il n'y a aucun sous-type ...");
        for (String key : subType.keySet()){
            if (subType.get(key)){
                if (temp_subtype.equals("NULL")) temp_subtype = key;
                else throw new IllegalItemException("L'item "+getName()+" contient >1 subtype ...");
            }
        }
        return temp_subtype;
    }

    /**
     * Uses the consumable item, applying its effect to the player.
     *
     * @param player The player using the item.
     * @throws ZeroSubTypeException If the item has no active subtype.
     * @throws IllegalItemException If the item has multiple active subtypes or no quantity.
     */
    @Override
    public void use(Player player) throws ZeroSubTypeException, IllegalItemException {
        try {

            if (quantity<=0) throw new IllegalItemException(getName()+" est vide ...");

            String temp_subtype = getCurrSubType();
            //System.out.println(temp_subtype);
            switch (temp_subtype) {
                case "HP":
                    player.heal(getValue());
                    break;
                case "Speed":
                    player.setOldSpeed(player.getSpeed());
                    int temp_value = (int) getValue(); // setSpeed n'accepte que des int
                    player.setSpeed(player.getSpeed() * (1+temp_value/100f));
                    break;
                case "MaxHP":
                    player.setOldMaxHP(player.getMaxHealth());
                    //System.out.println("Potion value "+getValue());
                    player.upMaxHealth(getValue());
                    break;
            }
            decrement();

        }
        catch (NullPointerException e){
            System.out.println("Erreur avec la référence au joueur : "+ e);
        }
        //Player.
    }


    /**
     * Consumes the item and applies its effect to the player, adding it to the player's active effects.
     *
     * @param player The player consuming the item.
     * @throws ZeroSubTypeException If the item has no active subtype.
     * @throws IllegalItemException If the item has multiple active subtypes or no quantity.
     */
    public void consumItem(Player player) throws ZeroSubTypeException, IllegalItemException {
        if (!player.getListOfEffect().containsKey(this.getName())) {
            this.use(player);
            //System.out.println(this.getName());
            player.addEffect(this);
            this.setTiming(this.getInitialTiming());
            if (this.getQuantity() <= 0) {
                player.getPlayerInventory().removeEquipedConsumableItem(this);
                player.getPlayerInventory().deleteItem(this.getName());
            }
        }
    }


    /**
     * Reverses the effect of the item on the player.
     *
     * @param player The player on whom the effect is reversed.
     * @throws ZeroSubTypeException If the item has no active subtype.
     * @throws IllegalItemException If the item has multiple active subtypes.
     */
    public void reverseEffect(Player player) throws ZeroSubTypeException, IllegalItemException {
        try {
            //if (quantity<=0) throw new IllegalItemException(getName()+" est vide ...");
            String temp_subtype = getCurrSubType();
            switch (temp_subtype) {
                case "Speed":
                    /*float temp_value = getValue(); // setSpeed n'accepte que des int
                    player.setSpeed(player.getSpeed() / (1+temp_value/100f));*/

                    //System.out.println("Old Speed "+player.getOldSpeed());
                    player.setSpeed(player.getOldSpeed());
                    break;
                case "MaxHP":
                    //System.out.println("Old HP : " + player.getOldMaxHP());
                    player.setMaxHealth(player.getOldMaxHP());
                    //System.out.println("Max Health on reverse : " + player.getMaxHealth());
                    /*
                    float temp = player.getMaxHealth()-getValue();
                    player.setMaxHealth(temp);*/

                    if (player.getHealth() > player.getMaxHealth()) {
                        player.setHealth(player.getMaxHealth());
                    }

                    break;
            }
            //decrement();
        }
        catch (NullPointerException e){
            System.out.println("Erreur avec la référence au joueur : "+ e);
        }
        //Player.
    }

    /**
     * Gets the rectangle representing the item's bounding box.
     *
     * @return The bounding box rectangle.
     */
    public Rectangle getRectangle() {
        return this.rect;
    }


    /**
     * Changes the size of the font used for UI text.
     *
     * @param size The new font size.
     */
    public void changeFontSize(int size) {
        fontParameter.size = size;
        uiFont = fontGenerator.generateFont(fontParameter);
    }

    /**
     * Changes the color of the font used for UI text.
     *
     * @param color The new font color.
     */
    public void changeFontColor(Color color) {
        fontParameter.color = color;
        uiFont = fontGenerator.generateFont(fontParameter);
    }

    /**
     * Renders the consumable item on the screen.
     *
     * @param batch The SpriteBatch used for rendering.
     * @param camera The camera used to adjust the projection matrix.
     */
    public void renderItem(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.draw(itemSprite, posX, posY, 24,24);
        uiFont.draw(batch, "x "+quantity, posX+ rect.width/2, posY+ rect.height);
    }



}
