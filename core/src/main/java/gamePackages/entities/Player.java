package gamePackages.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import gamePackages.items.ConsumableItem;
import gamePackages.items.Inventory;
import gamePackages.items.Item;
import gamePackages.toolsManager.AnimationManager;
import gamePackages.exceptions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the player entity in the game, extending the base `Entity` class.
 *
 * The `Player` class introduces additional attributes and behaviors specific to the player, such as
 * inventory management, dodge mechanics, and screen-related parameters.
 */
public class Player extends Entity {

    /*protected Inventory playerInventory;*/
    protected Inventory playerInventory;

    protected boolean dodgeState;
    protected float dodgeTime;

    protected HashMap<String, ConsumableItem> listOfEffect;

    protected Rectangle loadingRectangle;
    protected float screenWidth;
    protected float screenHeight;

    protected float oldSpeed;
    protected float oldMaxHP;
    protected float oldHP;

    /**
     * Constructs a `Player` object with the specified attributes.
     *
     * @param spritePath The path to the sprite sheet for the player.
     * @param posX The X-coordinate of the player's position.
     * @param posY The Y-coordinate of the player's position.
     * @param width The width of the player.
     * @param height The height of the player.
     * @param maxHealth The maximum health of the player.
     *
     * This constructor initializes the player's animations, collision properties, inventory,
     * and screen-related attributes. It also sets default values for dodge mechanics and movement speed.
     */
    public Player(String spritePath, float posX, float posY, int width, int height, float maxHealth){
        super(spritePath, posX, posY, width, height, maxHealth);
        try {
            this.animationsList.put("Idle", new AnimationManager(spritePath, 0, 8, 24, 40, 1f/6f).getAnimation());
        } catch (Exception e) {this.animationsList.put("Idle", new AnimationManager("default.png", 0, 1, 32, 32, 1f/6f).getAnimation());}
        try {
            this.animationsList.put("Idle", new AnimationManager(spritePath, 0, 8, 24, 40, 1f/6f).getAnimation());
            this.animationsList.put("Left", new AnimationManager(spritePath,  3, 4, 24, 40, 1/8f).getAnimation());
            this.animationsList.put("Right", new AnimationManager(spritePath, 4,4,24, 40, 1/8f).getAnimation());
            this.animationsList.put("Up", new AnimationManager(spritePath, 2, 4, 24, 40, 1/8f).getAnimation());
            this.animationsList.put("Down", new AnimationManager(spritePath, 1, 4, 24, 40, 1/8f).getAnimation());
            this.animationsList.put("RightAttack", new AnimationManager(spritePath, 7, 4, 24, 40, 1/20f).getAnimation());
            this.animationsList.put("LeftAttack", new AnimationManager(spritePath, 8, 4, 24, 40, 1/20f).getAnimation());
            this.animationsList.put("DownAttack", new AnimationManager(spritePath, 5, 4, 24, 40, 1/20f).getAnimation());
            this.animationsList.put("UpAttack", new AnimationManager(spritePath, 6, 4, 24, 40, 1/20f).getAnimation());
            this.animationsList.put("DodgeUp", new AnimationManager(spritePath, 10, 4, 24, 40, 1/10f).getAnimation());
            this.animationsList.put("DodgeDown", new AnimationManager(spritePath, 9, 4, 24, 40, 1/10f).getAnimation());
            this.animationsList.put("DodgeRight", new AnimationManager(spritePath, 12, 4, 24, 40, 1/10f).getAnimation());
            this.animationsList.put("DodgeLeft", new AnimationManager(spritePath, 11, 4, 24, 40, 1/10f).getAnimation());
        } catch (Exception e) {System.out.println("Image file path not found or error in animation frames parameters");}


        setCollisionRectangle(new Rectangle(posX, posY, 18, 20));
        this.setMeleeAttackRange(30);
        dodgeState = false;
        dodgeTime = 0f;
        setSpeed(150);
        setOldSpeed(getSpeed());
        setOldMaxHP(getMaxHealth());
        setOldHP(getHealth());
        setResetAttackTiming(0.2f);
        playerInventory = new Inventory();

        listOfEffect = new HashMap<>();
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.loadingRectangle = new Rectangle(getCenterX()-this.screenWidth/2f+50, getCenterY()-this.screenHeight/2f+50, this.screenWidth-100, this.screenHeight-100);
    }

    /**
     * Gets the player's inventory.
     *
     * @return The player's inventory object.
     */
    public Inventory getPlayerInventory() {
        return playerInventory;
    }

    /**
     * Checks if the player is in a dodge state.
     *
     * @return True if the player is dodging, false otherwise.
     */
    public boolean isDodgeState() {
        return dodgeState;
    }


    /**
     * Updates the player's position, resolving collisions with walls.
     *
     * @param walls A list of rectangles representing wall positions in the game world.
     */
    public void updatePosition(List<Rectangle> walls) {
        this.collisionRectangle.setPosition(this.posX, this.posY);

        for (Rectangle wall : walls) {

            // Ce code permet une meilleure fluidité dans les collisions mais est mal écrit quand il y a une double collision
            /*if (this.collisionRectangle.overlaps(wall)) {

                Rectangle tempRect = this.collisionRectangle;
                tempRect.setPosition(this.oldPosX, this.posY);
                boolean changeX = false;
                boolean changeY = false;
                if (!tempRect.overlaps(wall)) {
                    changeX = true;
                }
                tempRect.setPosition(this.posX, this.oldPosY);
                if (!tempRect.overlaps(wall)) {
                    changeY = true;
                }
                if (changeX) {
                    setPosX(this.oldPosX);
                }
                if (changeY) {
                    setPosY(this.oldPosY);
                }
                break;
            }*/

            //Code moins fluide pour les deplacements mais meilleurs pour ne pas passer à travers des murs
            if (this.collisionRectangle.overlaps(wall)) {
                setPosY(oldPosY);
                setPosX(oldPosX);
            }
        }

        this.collisionRectangle.setPosition(this.posX+4, this.posY+4);
        this.setCenterX(this.posX+this.width/2f);
        this.setCenterY(this.posY+this.height/2f);
        this.sprite.setPosition(this.posX, this.posY);
        this.interactionRectangle.setPosition(this.posX, this.posY);
        this.attackRectangle.setPosition(this.posX, this.posY+this.meleeAttackRange/2f);
        this.detectionCircle.setPosition(this.posX+this.width/2f, this.posY+this.height/2f);

    }


    /**
     * Initiates a dodge action for the player, temporarily increasing speed.
     *
     * @param delta Time elapsed since the last frame, used for timing the dodge.
     */
    public void dodge(float delta) {
        if (!dodgeState) {
            dodgeState = true;
            setOldSpeed(getSpeed());
            dodgeTime = delta;
            setSpeed(1000);
        }
        dodgeTime += delta;

        if (dodgeTime > 0.1f) {
            //setSpeed(oldSpeed);
            dodgeState = false;
            dodgeTime = 0f;
            setSpeed(getOldSpeed());
        }
        else {
            if (lastMovementAction.equals("Idle")) {lastMovementAction = "Down";}
            //System.out.println(lastMovementAction);
            if (moveCommands.containsKey(lastMovementAction)) {moveCommands.get(lastMovementAction).accept(delta);}

            switch (lastMovementAction) {
                case "Up":animationName = "DodgeUp";
                    break;
                case "Down":animationName = "DodgeDown";
                    break;
                case "Left", "UpLeft", "DownLeft":animationName = "DodgeLeft";
                    break;
                case "Right", "UpRight", "DownRight":animationName = "DodgeRight";
                    break;
                default:animationName = "Idle";
            }
        }
    }

    /**
     * Adds an effect to the player's active effects list.
     *
     * @param item The consumable item representing the effect.
     */
    public void addEffect(ConsumableItem item) {
        listOfEffect.put(item.getName(), item);
    }

    /**
     * Gets the player's list of active effects.
     *
     * @return A map of effect names to consumable items currently affecting the player.
     */
    public HashMap<String, ConsumableItem> getListOfEffect() {
        return listOfEffect;
    }

    /**
     * Updates the player's active effects, removing expired effects and reversing their impact.
     *
     * @param delta Time elapsed since the last frame, used for timing effect durations.
     */
    public void updateEffects(float delta) {
        ArrayList<String> delKey = new ArrayList<>();
        //System.out.println(getListOfEffect());
        for (ConsumableItem effect : listOfEffect.values()) {
            effect.updateTiming(delta);
            if (effect.getTiming() == 0) {
                delKey.add(effect.getName());
                try {
                    effect.reverseEffect(this);
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        List<String> keys = new ArrayList<>();
        for (String key : delKey) {
            listOfEffect.forEach( (k,v) -> {if (v.getName().equals(key)) keys.add(k);} ); // https://www.w3schools.com/java/ref_hashmap_foreach.asp
            keys.forEach((k)->listOfEffect.remove(k));
        }

    }

    /**
     * Renders the player, including animations for movement, attacks, and dodging.
     *
     * @param batch The SpriteBatch used to draw the player.
     * @param delta Time elapsed since the last frame, used for animation timing.
     */
    public void playerRender(SpriteBatch batch, float delta) {
        this.elapsedAnimationTime += delta;
        resetAttack();
        boolean loopAnimation = !this.attack || dodgeState;
        this.setSprite(this.getCurrentAnimation().getKeyFrame(elapsedAnimationTime, loopAnimation));
        this.sprite.draw(batch);
    }


    /**
     * Updates the player's loading rectangle to reflect its current position on the screen.
     */
    public void updateLoadingRectangle() {
        this.loadingRectangle.setPosition(getCenterX()-this.screenWidth/2f+50, getCenterY()-this.screenHeight/2f+50);
    }

    /**
     * Gets the loading rectangle of the player.
     *
     * @return The loading rectangle, defining the visible area around the player.
     */
    public Rectangle getLoadingRectangle() {
        return this.loadingRectangle;
    }


    /**
     * Sets the player's previous maximum health.
     *
     * @param maxHP The value to set as the player's old maximum health.
     */
    public void setOldMaxHP(float maxHP) {this.oldMaxHP = maxHP;}


    /**
     * Sets the player's previous speed.
     *
     * @param speed The value to set as the player's old speed.
     */
    public void setOldSpeed(float speed) {this.oldSpeed = speed;}

    /**
     * Gets the player's previous speed.
     *
     * @return The player's old speed value.
     */
    public float getOldSpeed() {return this.oldSpeed;}


    /**
     * Gets the player's previous maximum health.
     *
     * @return The player's old maximum health value.
     */
    public float getOldMaxHP(){return this.oldMaxHP;}

    /**
     * Sets the player's inventory.
     *
     * @param inventory The inventory to set for the player.
     */
    public void setPlayerInventory(Inventory inventory) {
        this.playerInventory = inventory;
    }

    /**
     * Sets the player's previous health.
     *
     * @param hp The value to set as the player's old health.
     */
    public void setOldHP(float hp) {this.oldMaxHP = hp;}


    /**
     * Gets the player's previous health.
     *
     * @return The player's old health value.
     */
    public float getOldHP() {return this.oldMaxHP;}


    /**
     * Creates a deep copy of the player, including inventory and active effects.
     *
     * @return A new `Player` object with identical properties and effects.
     */
    public Player deepCopy(){
        //System.out.println("============================================================");
        //System.out.println("Old max HP " + getOldMaxHP());
        Player playerCopy = new Player(spritePath, posX, posY, width, height, getMaxHealth());

        //playerCopy.setHealth(getHealth());
        //System.out.println("Max Health On Save >>>>>>>>>>>>>>>>>>>>>>>>>>>> " + maxHealth);
        playerCopy.setHealth(getHealth());
        playerCopy.setOldHP(getHealth());
        playerCopy.setSpeed(getSpeed());
        playerCopy.setDamage(damage);
        playerCopy.setOldSpeed(getOldSpeed());
        playerCopy.setOldMaxHP(getOldMaxHP());

        playerCopy.setPlayerInventory(playerInventory.deepCopy());
        for (ConsumableItem item : listOfEffect.values()) {
            ConsumableItem tempItem = item.deepCopy();
            tempItem.setTiming(item.getTiming());
            playerCopy.listOfEffect.put(tempItem.getName(), tempItem);
        }
        return playerCopy;

    }

}
