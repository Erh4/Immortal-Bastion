package gamePackages.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import gamePackages.toolsManager.AnimationManager;
import gamePackages.toolsManager.CheckOverlapping;
import gamePackages.toolsManager.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a generic entity in the game, serving as a base class for specific entities such as players or monsters.
 *
 * This class provides attributes and methods for managing an entity's state, position, animations, interactions,
 * collisions, health, and combat-related properties.
 */
public class Entity {

    protected String spritePath;
    protected Sprite sprite;
    protected AnimationManager IdleAnimation;
    protected Animation<TextureRegion> animation;
    protected HashMap<String, Animation<TextureRegion>> animationsList;
    protected HashMap<String, HashMap<String, Animation<TextureRegion>>> animationsDico;
    protected String lastMovementAction;
    protected String animationName;
    protected Animation<TextureRegion> hitAnimation;


    protected float posX;
    protected float posY;
    protected float oldPosX;
    protected float oldPosY;
    protected int width;
    protected int height;
    protected float centerX;
    protected float centerY;
    protected List<Node> pathNodes;
    protected Circle collisionNodeCircle;


    protected ShapeRenderer shapeRenderer;
    protected Rectangle collisionRectangle;
    protected Rectangle interactionRectangle;
    protected boolean debugState;
    protected float detectionRadius;
    protected Circle detectionCircle;
    protected Rectangle attackRectangle;
    protected Circle attackRangeCircle;

    protected HashMap<String, Consumer<Float>> moveCommands;


    protected float elapsedAnimationTime;
    protected float elapsedHitAnimationTime;


    protected float maxHealth;
    protected float health;
    protected float damage;
    protected float dmg_reduction;
    protected float dmg_buff;
    protected float speed;
    public boolean attack;
    protected float meleeAttackRange;
    protected boolean playerDetected;
    protected boolean hitDetected;
    protected float resetAttackTiming;
    protected float elapsedTimeAttack;


    // Constructors
    /**
     * Default constructor.
     */
    public Entity() {}

    /**
     * Constructs an entity with the specified parameters.
     *
     * @param spritePath The path to the sprite representing the entity.
     * @param posX The x-coordinate of the entity's position.
     * @param posY The y-coordinate of the entity's position.
     * @param width The width of the entity.
     * @param height The height of the entity.
     * @param maxHealth The maximum health of the entity.
     */
    public Entity(String spritePath, float posX, float posY, int width, int height, float maxHealth) {
        this.spritePath = spritePath;

        initAnimations(spritePath);
        this.lastMovementAction = "Idle";
        this.animationName = lastMovementAction;
        this.hitAnimation = new AnimationManager("ui/hit.png", 0,3,16,16, 1/8f).getAnimation();

        initMoveCommands();

        this.posX = posX;
        this.posY = posY;
        this.oldPosX = posX;
        this.oldPosY = posY;
        this.width = width;
        this.height = height;
        this.centerX = posX+width/2f;
        this.centerY = posY+height/2f;
        this.pathNodes = new ArrayList<>();
        this.collisionNodeCircle = new Circle(getCenterX(), getCenterY(), 2);

        this.debugState = false;
        initShapeRenderer();
        initCollisionRectangle();
        initInteractionRectangle();
        initDetectionCircle();
        initAttackRectangle();
        this.attackRangeCircle = new Circle(getCenterX(), getCenterY(), 2);

        setMaxHealth(maxHealth);
        setHealth(maxHealth);
        setSpeed(0);
        setDmgReduction(0f);
        setDmgBuff(0f);


        this.attack = false;
        this.playerDetected = false;
        this.meleeAttackRange = 0;
        this.elapsedHitAnimationTime = 1;
        this.hitDetected = false;
        this.resetAttackTiming = 0.2f;
        this.elapsedTimeAttack = 0.5f;
        this.damage = 1;
    }


    /**
     * Heals the entity by the specified amount.
     *
     * @param healAmount The amount of health to restore.
     *
     * If the healed amount exceeds the entity's maximum health, the health is capped at the maximum.
     */
    public void heal(float healAmount) {
        if (this.health + healAmount > this.maxHealth) {
            this.health = this.maxHealth;
        }
        else {
            this.health = this.health + healAmount;
        }
    }

    /**
     * Increases the entity's maximum health and adjusts the current health accordingly.
     *
     * @param maxHealth The amount to add to the entity's maximum health.
     *
     * This method adds the specified value to both the maximum health and current health of the entity.
     */
    public void upMaxHealth(float maxHealth) {
        setMaxHealth(this.maxHealth + maxHealth);
        this.health = this.health + maxHealth;
    }

    /**
     * Performs an attack on a list of potential victims.
     *
     * @param victims The list of entities to check for attack collisions.
     *
     * This method checks for overlaps between the entity's attack rectangle and the collision rectangles
     * of the victims. If an overlap is detected, the victim takes damage.
     */
    public void attack(List<Monster> victims) {
        for (Entity victim : victims) {
            if (this.attackRectangle.overlaps(victim.getCollisionRectangle())) {
                doDamage(victim);
            }
        }
    }


    /**
     * Performs an attack on a single victim.
     *
     * @param victim The entity to attack.
     *
     * This method applies damage to the specified victim.
     */
    public void attack(Entity victim) {
        doDamage(victim);
    }

    /**
     * Checks whether the player is detected within the entity's detection range.
     *
     * @param player The player entity to check.
     *
     * This method sets the `playerDetected` flag to true if the player's center coordinates
     * are within the detection circle of the entity.
     */
    public void checkPlayerDetection(Entity player) {
        this.playerDetected = CheckOverlapping.overlapsPointleCircle(this.detectionCircle, player.getCenterX(), player.getCenterY());
    }


    /**
     * Updates the position of the attack rectangle based on the specified attack direction.
     *
     * @param direction The direction of the attack. Possible values:
     *                  - "UpAttack" moves the rectangle above the entity.
     *                  - "DownAttack" moves the rectangle below the entity.
     *                  - "LeftAttack" moves the rectangle to the left of the entity.
     *                  - "RightAttack" moves the rectangle to the right of the entity.
     */
    public void updateAttackRectanglePosition(String direction) {
        switch (direction) {
            case "UpAttack":
                this.attackRectangle.setPosition(this.posX-this.meleeAttackRange/2f+this.width/2f, this.posY+this.height/2f);
                break;
            case "DownAttack":
                this.attackRectangle.setPosition(this.posX-this.meleeAttackRange/2f+this.width/2f, this.posY-this.meleeAttackRange+this.height/2f);
                break;
            case "LeftAttack":
                this.attackRectangle.setPosition(this.posX-this.meleeAttackRange+this.width/2f, this.posY+this.meleeAttackRange/2f);
                break;
            case "RightAttack":
                this.attackRectangle.setPosition(this.posX+this.width/2f, this.posY+this.meleeAttackRange/2f);
                break;
        }
    }


    /**
     * Applies damage to the entity and updates its state accordingly.
     *
     * @param damageAmount The amount of damage to apply.
     *
     * This method reduces the entity's health by the specified damage amount, accounting for damage reduction.
     * If the health drops to zero or below, the entity's health is set to zero.
     * The method also resets the hit animation timer and sets the hit detection flag.
     */
    public void takeDamages(float damageAmount) {
        if (this.health >= damageAmount) {
            dmg_reduction = getDmgReduction()/10;
            this.health = this.health - damageAmount*(1-dmg_reduction);
        }
        else {
            this.health = 0;
        }
        this.elapsedHitAnimationTime = 0f;
        this.hitDetected = true;
    }

    /**
     * Inflicts damage on the specified entity.
     *
     * @param entity The entity to receive the damage.
     *
     * This method applies damage to the target entity, considering the attacker's damage buff.
     * If the attacker has no damage buff, a default damage value of 1 is applied.
     */
    public void doDamage(Entity entity) {
        if (getDmgReduction() > 0) {
            entity.takeDamages(getDmgBuff());
        }
        else entity.takeDamages(getDamage());

    }

    /**
     * Updates the entity's position and recalculates associated attributes.
     *
     * This method:
     * - Updates the center coordinates of the entity.
     * - Adjusts the position of the sprite and collision-related rectangles and circles.
     * - Ensures all components are synchronized with the current position.
     */
    public void updatePosition() {
        this.setCenterX(this.posX+this.width/2f);
        this.setCenterY(this.posY+this.height/2f);
        this.sprite.setPosition(this.posX, this.posY);
        this.collisionRectangle.setPosition(this.posX, this.posY);
        this.interactionRectangle.setPosition(this.posX, this.posY);
        this.attackRectangle.setPosition(this.posX, this.posY+this.meleeAttackRange/2f);
        this.detectionCircle.setPosition(this.posX+this.width/2f, this.posY+this.height/2f);
        this.attackRangeCircle.setPosition(getCenterX(), getCenterY());
    }


    /**
     * Initializes movement commands and maps them to corresponding movement methods.
     *
     * This method creates a mapping of movement directions (e.g., "Up", "Down", "Left", "Right")
     * to their respective methods using method references.
     */
    public void initMoveCommands() {
        this.moveCommands = new HashMap<>();
        moveCommands.put("Up", this::goTop);
        moveCommands.put("Down", this::goBottom);
        moveCommands.put("Left", this::goLeft);
        moveCommands.put("Right", this::goRight);
        moveCommands.put("UpRight", this::goTopRight);
        moveCommands.put("UpLeft", this::goTopLeft);
        moveCommands.put("DownLeft", this::goBottomLeft);
        moveCommands.put("DownRight", this::goBottomRight);
    }


    /**
     * Moves the entity upward.
     *
     * @param delta Time elapsed since the last frame, used to calculate movement distance.
     *
     * Updates the entity's Y position and sets the last movement action to "Up".
     */
    public void goTop(float delta) {
        setOldPosX(this.posX);
        setOldPosY(this.posY);
        this.setPosY(this.posY + this.speed*delta);
        this.setLastMovementAction("Up");
        this.animationName = lastMovementAction;
    }

    /**
     * Moves the entity downward.
     *
     * @param delta Time elapsed since the last frame, used to calculate movement distance.
     *
     * Updates the entity's Y position and sets the last movement action to "Down".
     */
    public void goBottom(float delta) {
        setOldPosX(this.posX);
        setOldPosY(this.posY);
        this.setPosY(this.posY - this.speed*delta);
        this.setLastMovementAction("Down");
        this.animationName = lastMovementAction;
    }

    /**
     * Moves the entity to the left.
     *
     * @param delta Time elapsed since the last frame, used to calculate movement distance.
     *
     * Updates the entity's X position and sets the last movement action to "Left".
     */
    public void goLeft(float delta) {
        setOldPosX(this.posX);
        setOldPosY(this.posY);
        this.setPosX(this.posX - this.speed*delta);
        this.setLastMovementAction("Left");
        this.animationName = lastMovementAction;
    }

    /**
     * Moves the entity to the right.
     *
     * @param delta Time elapsed since the last frame, used to calculate movement distance.
     *
     * Updates the entity's X position and sets the last movement action to "Right".
     */
    public void goRight(float delta) {
        setOldPosX(this.posX);
        setOldPosY(this.posY);
        this.setPosX(this.posX + this.speed*delta);
        this.setLastMovementAction("Right");
        this.animationName = lastMovementAction;
    }

    /**
     * Moves the entity diagonally upward and to the left.
     *
     * @param delta Time elapsed since the last frame, used to calculate movement distance.
     *
     * Updates the entity's X and Y positions using a diagonal direction vector and sets
     * the last movement action to "UpLeft".
     */
    public void goTopLeft(float delta) {
        setOldPosY(this.posY);
        setOldPosX(this.posX);
        float dx = -1/(float)Math.sqrt(2);
        float dy = 1/(float)Math.sqrt(2);
        this.setPosY(this.posY + dy*this.speed*delta);
        this.setPosX(this.posX + dx*this.speed*delta);
        this.setLastMovementAction("UpLeft");
        this.animationName = "Left";
    }

    /**
     * Moves the entity diagonally upward and to the right.
     *
     * @param delta Time elapsed since the last frame, used to calculate movement distance.
     *
     * Updates the entity's X and Y positions using a diagonal direction vector and sets
     * the last movement action to "UpRight".
     */
    public void goTopRight(float delta) {
        setOldPosY(this.posY);
        setOldPosX(this.posX);
        float dx = 1/(float)Math.sqrt(2);
        float dy = 1/(float)Math.sqrt(2);
        this.setPosY(this.posY + dy*this.speed*delta);
        this.setPosX(this.posX + dx*this.speed*delta);
        this.setLastMovementAction("UpRight");
        this.animationName = "Right";
    }

    /**
     * Moves the entity diagonally downward and to the left.
     *
     * @param delta Time elapsed since the last frame, used to calculate movement distance.
     *
     * Updates the entity's X and Y positions using a diagonal direction vector and sets
     * the last movement action to "DownLeft".
     */
    public void goBottomLeft(float delta) {
        setOldPosY(this.posY);
        setOldPosX(this.posX);
        float dx = -1/(float)Math.sqrt(2);
        float dy = -1/(float)Math.sqrt(2);
        this.setPosY(this.posY + dy*this.speed*delta);
        this.setPosX(this.posX + dx*this.speed*delta);
        this.setLastMovementAction("DownLeft");
        this.animationName = "Left";
    }

    /**
     * Moves the entity diagonally downward and to the right.
     *
     * @param delta Time elapsed since the last frame, used to calculate movement distance.
     *
     * Updates the entity's X and Y positions using a diagonal direction vector and sets
     * the last movement action to "DownRight".
     */
    public void goBottomRight(float delta) {
        setOldPosY(this.posY);
        setOldPosX(this.posX);
        float dx = 1/(float)Math.sqrt(2);
        float dy = -1/(float)Math.sqrt(2);
        this.setPosY(this.posY + dy*this.speed*delta);
        this.setPosX(this.posX + dx*this.speed*delta);
        this.setLastMovementAction("DownRight");
        this.animationName = "Right";
    }


    /**
     * Initiates a punch action in the specified direction.
     *
     * @param direction The direction of the punch (e.g., "UpAttack", "DownAttack", etc.).
     *
     * This method sets the entity to an attack state, resets the animation timing,
     * and updates the current animation and movement action to reflect the punch direction.
     */
    public void punch(String direction) {
        this.attack = true;
        this.elapsedAnimationTime = 0;
        this.setLastMovementAction(direction);
        this.setAnimationName(lastMovementAction);
        this.elapsedTimeAttack = 0f;
    }

    /**
     * Resets the attack state if the attack animation has completed.
     *
     * If the elapsed animation time exceeds the reset attack timing, the entity's
     * attack state is set to false, and the animation is reset to "Idle".
     */
    public void resetAttack() {
        if (this.attack && this.elapsedAnimationTime > this.resetAttackTiming) {
            this.attack = false;
            this.elapsedAnimationTime = 0;
            this.setAnimationName("Idle");
            this.setLastMovementAction("Idle");
        }
    }


    /**
     * Renders the entity, updating its animations and attack state.
     *
     * @param batch The SpriteBatch used to draw the entity.
     * @param delta Time elapsed since the last frame, used for animation timing.
     *
     * This method:
     * - Updates the elapsed animation time.
     * - Resets the attack state if needed.
     * - Draws the entity's current animation frame.
     * - Renders the hit animation if the entity has been hit.
     */
    public void entityRender(SpriteBatch batch, float delta) {
        this.elapsedAnimationTime += delta;
        resetAttack();
        if (this.hitDetected && (this.elapsedHitAnimationTime < 0.2f)) {
            this.lastMovementAction = "Idle";
            this.animationName = "Idle";
        }
        boolean loopAnimation = !this.attack;
        this.setSprite(this.getCurrentAnimation().getKeyFrame(elapsedAnimationTime, loopAnimation));
        this.sprite.draw(batch);

        renderHitAnimation(batch, delta);

    }


    /**
     * Renders the hit animation if the entity has been recently hit.
     *
     * @param batch The SpriteBatch used to draw the animation.
     * @param delta Time elapsed since the last frame, used for animation timing.
     *
     * This method:
     * - Draws the hit animation sprite if the hit animation timer is active.
     * - Increments the hit animation timer.
     * - Resets the hit detection flag once the animation completes.
     */
    protected void renderHitAnimation(SpriteBatch batch, float delta) {
        if (this.elapsedHitAnimationTime < 0.2f) {
            Sprite tmpSprite = new Sprite(this.hitAnimation.getKeyFrame(elapsedHitAnimationTime, false));
            tmpSprite.setPosition(this.getPosX()+this.getWidth()-16, this.getPosY()+this.getHeight()-12);
            tmpSprite.draw(batch);
            this.elapsedHitAnimationTime+= delta;
        }
        else if (this.hitDetected) {
            this.hitDetected = false;
        }
    }


    /**
     * Initializes the entity's animations using the specified sprite path.
     *
     * @param spritePath The path to the sprite sheet used for animations.
     *
     * This method:
     * - Initializes the animation dictionary.
     * - Sets up the idle animation and sets it as the default animation.
     * - Populates the animation list with the idle animation.
     */
    public void initAnimations(String spritePath) {
        initAnimationsDico();

        setIdleAnimation(spritePath);
        setAnimation(this.IdleAnimation.getAnimation());
        initAnimationsList();
        this.animationsList.put("Idle", this.IdleAnimation.getAnimation());
    }


    /**
     * Initializes the animation list for the entity.
     *
     * This method creates an empty `HashMap` to store animations mapped by their names.
     */
    public void initAnimationsList() {this.animationsList = new HashMap<>();}

    /**
     * Initializes the animation dictionary for the entity.
     *
     * This method creates an empty `HashMap` to categorize animations by states and actions.
     */
    public void initAnimationsDico() { this.animationsDico = new HashMap<>();}

    /**
     * Initializes the shape renderer for the entity.
     *
     * This method creates a new `ShapeRenderer` instance, used for debugging or rendering visual shapes.
     */
    public void initShapeRenderer() {this.shapeRenderer = new ShapeRenderer();}

    /**
     * Initializes the collision rectangle for the entity.
     *
     * This method creates a new `Rectangle` positioned at the entity's coordinates with the entity's width
     * and half of its height.
     */
    public void initCollisionRectangle() {this.collisionRectangle = new Rectangle(posX, posY, width, height/2f);}

    /**
     * Initializes the interaction rectangle for the entity.
     *
     * This method creates a new `Rectangle` positioned at the entity's coordinates with the entity's
     * full width and height.
     */
    public void initInteractionRectangle() {this.interactionRectangle = new Rectangle(posX, posY, width, height);}

    /**
     * Initializes the detection circle for the entity.
     *
     * This method creates a new `Circle` centered at the entity's position with the specified detection radius.
     */
    public void initDetectionCircle() {this.detectionCircle = new Circle(this.posX+this.width/2f, this.posY+this.height/2f, this.detectionRadius);}

    /**
     * Initializes the attack rectangle for the entity.
     *
     * This method creates a new `Rectangle` positioned at the entity's coordinates with the specified melee attack range.
     */
    public void initAttackRectangle() {this.attackRectangle = new Rectangle(posX, posY, this.meleeAttackRange, this.meleeAttackRange);}

    /**
     * Sets the sprite for the entity using the specified texture region.
     *
     * @param textureRegion The texture region to use for the sprite.
     *
     * This method creates a new `Sprite` from the given texture region and positions it at the entity's coordinates.
     */
    public void setSprite(TextureRegion textureRegion) {
        this.sprite = new Sprite(textureRegion);
        this.sprite.setPosition(this.posX, this.posY);
    }

    /**
     * Sets the idle animation for the entity using the specified sprite path.
     *
     * @param spritePath The path to the sprite sheet for the idle animation.
     *
     * This method attempts to create a new `AnimationManager` for the idle animation.
     * If an error occurs, a default sprite sheet is used as a fallback.
     */
    public void setIdleAnimation(String spritePath) {
        try {this.IdleAnimation = new AnimationManager(spritePath,0, 1, 24, 40, 1f/6f);}

        catch (Exception e) {this.IdleAnimation = new AnimationManager("default.png",0, 1, 32, 32, 1f/6f);}
    }

    /**
     * Sets the current animation for the entity.
     *
     * @param animation The animation to set as the current animation.
     */
    public void setAnimation(Animation<TextureRegion> animation) {this.animation = animation;}

    /**
     * Sets the last movement action performed by the entity.
     *
     * @param MovementAction The name of the last movement action (e.g., "Idle", "Up", "Down").
     */
    public void setLastMovementAction(String MovementAction) {this.lastMovementAction = MovementAction;}

    /**
     * Sets the current animation name for the entity.
     *
     * @param animationName The name of the animation to set (e.g., "Idle", "Walk", "Attack").
     */
    public void setAnimationName(String animationName) { this.animationName = animationName; }

    /**
     * Sets the entity's state to idle.
     *
     * This method sets the last movement action and animation name to "Idle".
     */
    public void setOnIdleState() {
        this.setLastMovementAction("Idle");
        this.setAnimationName("Idle");
    }


    /**
     * Sets the X-coordinate of the entity's position.
     *
     * @param posX The new X-coordinate.
     */
    public void setPosX(float posX) {this.posX = posX;}

    /**
     * Sets the Y-coordinate of the entity's position.
     *
     * @param posY The new Y-coordinate.
     */
    public void setPosY(float posY) {this.posY = posY;}

    /**
     * Sets the previous X-coordinate of the entity's position.
     *
     * @param posX The previous X-coordinate.
     */
    public void setOldPosX(float posX) {this.oldPosX = posX;}

    /**
     * Sets the previous Y-coordinate of the entity's position.
     *
     * @param posY The previous Y-coordinate.
     */
    public void setOldPosY(float posY) {this.oldPosY = posY;}

    /**
     * Sets the width of the entity.
     *
     * @param width The new width.
     */
    public void setWidth(int width) { this.width = width; }

    /**
     * Sets the height of the entity.
     *
     * @param height The new height.
     */
    public void setHeight(int height) { this.height = height; }

    /**
     * Sets the X-coordinate of the entity's center.
     *
     * @param centerX The new X-coordinate of the center.
     */
    public void setCenterX(float centerX) { this.centerX = centerX;}

    /**
     * Sets the Y-coordinate of the entity's center.
     *
     * @param centerY The new Y-coordinate of the center.
     */
    public void setCenterY(float centerY) { this.centerY = centerY;}

    /**
     * Sets the movement path for the entity.
     *
     * @param pathNodes The list of nodes defining the movement path.
     */
    public void setPathNodes(List<Node> pathNodes) {this.pathNodes = pathNodes;}

    /**
     * Toggles the debug display state of the entity.
     *
     * This method switches the `debugState` flag between true and false.
     */
    public void setDebugDisplayState() { this.debugState = !this.debugState; }

    /**
     * Sets the detection radius for the entity and updates the detection circle.
     *
     * @param radius The new detection radius.
     */
    public void setDetectionRadius(float radius) { this.detectionRadius = radius;this.detectionCircle.radius = radius;}

    /**
     * Sets the collision rectangle for the entity.
     *
     * @param collisionRectangle The new collision rectangle.
     */
    public void setCollisionRectangle(Rectangle collisionRectangle) { this.collisionRectangle = collisionRectangle; }

    /**
     * Sets the interaction rectangle for the entity.
     *
     * @param interactionRectangle The new interaction rectangle.
     */
    public void setInteractionRectangle(Rectangle interactionRectangle) { this.interactionRectangle = interactionRectangle; }

    /**
     * Sets the maximum health of the entity.
     *
     * @param maxHealth The new maximum health value.
     */
    public void setMaxHealth(float maxHealth) { this.maxHealth = maxHealth; }

    /**
     * Sets the current health of the entity.
     *
     * @param health The new health value.
     */
    public void setHealth(float health) { this.health = health; }

    /**
     *Sets the current damage amount of the entity
     *
     * @param damage The new damage amount
     */
    public void setDamage(float damage) { this.damage = damage; }

    /**
     * Sets the damage reduction factor for the entity.
     *
     * @param dmg_reduction The new damage reduction value (e.g., 0.2 for 20% reduction).
     */
    public void setDmgReduction(float dmg_reduction) { this.dmg_reduction = dmg_reduction; }

    /**
     * Sets the damage buff for the entity.
     *
     * @param dmg_buff The new damage buff value.
     */
    public void setDmgBuff(float dmg_buff){ this.dmg_buff = dmg_buff; }

    /**
     * Sets the movement speed of the entity.
     *
     * @param speed The new speed value.
     */
    public void setSpeed(float speed) { this.speed = speed; }

    /**
     * Sets the melee attack range for the entity and updates related attributes.
     *
     * @param meleeAttackRange The new melee attack range.
     *
     * This method updates:
     * - The radius of the attack range circle.
     * - The size of the attack rectangle.
     */
    public void setMeleeAttackRange(float meleeAttackRange) {
        this.meleeAttackRange = meleeAttackRange;
        this.attackRangeCircle.setRadius(meleeAttackRange);
        attackRectangle.setSize(meleeAttackRange, meleeAttackRange);
    }

    /**
     * Sets whether the entity has been hit.
     *
     * @param hitDetected True if the entity is hit, false otherwise.
     */
    public void setHitDetected(boolean hitDetected) { this.hitDetected = hitDetected; }

    /**
     * Sets the time required to reset the entity's attack state.
     *
     * @param timing The new reset attack timing.
     */
    public void setResetAttackTiming(float timing) {this.resetAttackTiming = timing;}

    /**
     * Sets the elapsed time since the last attack.
     *
     * @param time The new elapsed attack time.
     */
    public void setElapsedTimeAttack(float time) {this.elapsedTimeAttack=time;}


    /**
     * Gets the sprite representing the entity.
     *
     * @return The sprite object.
     */
    public Sprite getSprite() { return this.sprite; }

    /**
     * Gets the last movement action performed by the entity.
     *
     * @return The name of the last movement action (e.g., "Idle", "Up").
     */
    public String getLastMovementAction() { return this.lastMovementAction; }

    /**
     * Gets the name of the current animation being played.
     *
     * @return The name of the current animation (e.g., "Idle", "Walk").
     */
    public String getAnimationName() { return this.animationName; }

    /**
     * Gets the current animation for the entity based on its animation name.
     *
     * @return The current animation object. Returns the "Idle" animation if no match is found.
     */
    public Animation<TextureRegion> getCurrentAnimation() {
        if (this.animationsList.containsKey(this.animationName)) {
            return this.animationsList.get(this.animationName); }
        return this.animationsList.get("Idle");
    }


    /**
     * Gets the current X-coordinate of the entity.
     *
     * @return The X-coordinate of the entity.
     */
    public float getPosX() { return this.posX; }

    /**
     * Gets the current Y-coordinate of the entity.
     *
     * @return The Y-coordinate of the entity.
     */
    public float getPosY() { return this.posY; }

    /**
     * Gets the previous X-coordinate of the entity.
     *
     * @return The previous X-coordinate of the entity.
     */
    public float getOldPosX() { return this.oldPosX; }

    /**
     * Gets the previous Y-coordinate of the entity.
     *
     * @return The previous Y-coordinate of the entity.
     */
    public float getOldPosY() { return this.oldPosY; }

    /**
     * Gets the width of the entity.
     *
     * @return The width of the entity.
     */
    public int getWidth() { return this.width; }

    /**
     * Gets the height of the entity.
     *
     * @return The height of the entity.
     */
    public int getHeight() { return this.height; }

    /**
     * Gets the X-coordinate of the center of the entity.
     *
     * @return The X-coordinate of the entity's center.
     */
    public float getCenterX() { return this.centerX; }

    /**
     * Gets the Y-coordinate of the center of the entity.
     *
     * @return The Y-coordinate of the entity's center.
     */
    public float getCenterY() { return this.centerY; }

    /**
     * Gets the movement path of the entity as a list of nodes.
     *
     * @return The list of nodes representing the movement path.
     */
    public List<Node> getPathNodes() { return this.pathNodes; }

    /**
     * Gets the collision node circle of the entity.
     *
     * @return The collision node circle.
     */
    public Circle getCollisionNodeCircle() {
        return this.collisionNodeCircle;
    }

    /**
     * Gets the shape renderer for the entity.
     *
     * @return The shape renderer.
     */
    public ShapeRenderer getShapeRenderer() {return this.shapeRenderer;}

    /**
     * Gets the debug display state of the entity.
     *
     * @return True if debug display is enabled, false otherwise.
     */
    public boolean getDebugDisplayState() {return this.debugState;}

    /**
     * Gets the detection radius of the entity.
     *
     * @return The detection radius.
     */
    public float getDetectionRadius() { return this.detectionRadius; }

    /**
     * Gets the collision rectangle of the entity.
     *
     * @return The collision rectangle.
     */
    public Rectangle getCollisionRectangle() { return this.collisionRectangle; }

    /**
     * Gets the interaction rectangle of the entity.
     *
     * @return The interaction rectangle.
     */
    public Rectangle getInteractionRectangle() { return this.interactionRectangle; }

    /**
     * Gets the attack rectangle of the entity.
     *
     * @return The attack rectangle.
     */
    public Rectangle getAttackRectangle() {return this.attackRectangle;}


    /**
     * Gets the attack range circle of the entity.
     *
     * @return The attack range circle.
     */
    public Circle getAttackRangeCircle() {return this.attackRangeCircle;}

    /**
     * Gets the maximum health of the entity.
     *
     * @return The maximum health value.
     */
    public float getMaxHealth() { return this.maxHealth; }

    /**
     * Gets the current health of the entity.
     *
     * @return The current health value.
     */
    public float getHealth() { return this.health; }

    /**
     * Gets the movement speed of the entity.
     *
     * @return The speed value.
     */
    public float getSpeed() { return this.speed; }

    /**
     * Gets the current damage amount of the entity
     *
     * @return The damage amount
     */
    public float getDamage() {return this.damage;}

    /**
     * Checks if the entity is dead.
     *
     * @return True if the entity's health is 0, false otherwise.
     */
    public boolean isDead(){ return this.health == 0; }

    /**
     * Gets the melee attack range of the entity.
     *
     * @return The melee attack range.
     */
    public float getMeleeAttackRange(){ return this.meleeAttackRange; }

    /**
     * Gets the melee attack range of the entity.
     *
     * @return The melee attack range.
     */
    public float getDmgReduction(){ return dmg_reduction; }

    /**
     * Gets the damage buff value of the entity.
     *
     * @return The damage buff value.
     */
    public float getDmgBuff(){ return dmg_buff; }

    /**
     * Checks if the player is detected by the entity.
     *
     * @return True if the player is detected, false otherwise.
     */
    public boolean getPlayerDetected() {return playerDetected;}

    /**
     * Checks if the entity has been hit.
     *
     * @return True if the entity has been hit, false otherwise.
     */
    public boolean getHitDetected() {return hitDetected;}

    /**
     * Gets the timing required to reset the attack state of the entity.
     *
     * @return The reset attack timing value.
     */
    public float getResetAttackTiming() {return this.resetAttackTiming;}

    /**
     * Gets the elapsed time since the entity's last attack.
     *
     * @return The elapsed attack time.
     */
    public float getElapsedTimeAttack(){return this.elapsedTimeAttack;}
}
