package gamePackages.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import gamePackages.toolsManager.AnimationManager;

import java.util.HashMap;

public class Entity {

    protected Texture entityTexture;
    protected Sprite sprite;

    protected float posX;
    protected float posY;
    protected int width;
    protected int height;
    protected Rectangle collisionRectangle;
    protected Rectangle interactionRectangle;

    protected ShapeRenderer shapeRenderer;
    protected boolean showHitboxRectangle;

    protected AnimationManager IdleAnimation;
    protected AnimationManager LeftWalkAnimation;
    protected AnimationManager RightWalkAnimation;
    protected AnimationManager UpWalkAnimation;
    protected AnimationManager DownWalkAnimation;

    protected Animation<TextureRegion> animation;
    protected HashMap<String, Animation<TextureRegion>> animationsList;

    protected HashMap<String, HashMap<String, Animation<TextureRegion>>> animationsDico;

    protected int maxHealth;
    protected int health;



    protected float speed;
    protected String lastMovementAction;

    public Entity(String spritePath, float posX, float posY, int width, int height) {
        this.entityTexture = new Texture(spritePath);
        this.sprite = new Sprite(this.entityTexture);
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.maxHealth = 9999999;
        this.health = 9999999;
        this.collisionRectangle = new Rectangle(posX, posY, width, height);
        this.interactionRectangle = new Rectangle(posX, posY, width, height);
        this.speed = 0;
        this.lastMovementAction = "";
    }
    public Entity(String spritePath, float posX, float posY) {
        this.entityTexture = new Texture(spritePath);
        this.sprite = new Sprite(this.entityTexture);
        this.posX = posX;
        this.posY = posY;
        this.width = 0;
        this.height = 0;
        this.maxHealth = 9999999;
        this.health = 9999999;
        this.collisionRectangle = new Rectangle(posX, posY, 0, 0);
        this.interactionRectangle = new Rectangle(posX, posY, 0, 0);
        this.speed = 0;
        this.lastMovementAction = "";
    }
    public Entity(String spritePath, float posX, float posY, int width, int height, int maxHealth) {
        /*this.entityTexture = new Texture(spritePath);
        this.sprite = new Sprite(this.entityTexture);*/
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.collisionRectangle = new Rectangle(posX, posY, width, height);
        this.interactionRectangle = new Rectangle(posX, posY, width, height);
        this.shapeRenderer = new ShapeRenderer();
        this.showHitboxRectangle = false;

        this.speed = 0;
        this.lastMovementAction = "Idle";


        this.animationsDico = new HashMap<>();

        this.IdleAnimation = new AnimationManager("character/stickman_idle.png", 8, 1, 24, 40, 1f/6f);
        this.LeftWalkAnimation = new AnimationManager("character/stickman_idle.png", 8, 1, 24, 40, 1f/6f);
        this.RightWalkAnimation = new AnimationManager("character/stickman_idle.png", 8, 1, 24, 40, 1f/6f);
        this.UpWalkAnimation = new AnimationManager("character/stickman_idle.png", 8, 1, 24, 40, 1f/6f);
        this.DownWalkAnimation = new AnimationManager("character/stickman_idle.png", 8, 1, 24, 40, 1f/6f);


        this.animation = this.IdleAnimation.getAnimation();
        this.animationsList = new HashMap<>();
        this.animationsList.put("Idle", this.IdleAnimation.getAnimation());
        this.animationsList.put("Left", this.LeftWalkAnimation.getAnimation());
        this.animationsList.put("Right", this.RightWalkAnimation.getAnimation());
        this.animationsList.put("Up", this.UpWalkAnimation.getAnimation());
        this.animationsList.put("Down", this.DownWalkAnimation.getAnimation());




    }
    public Entity(String spritePath) {
        this.entityTexture = new Texture(spritePath);
        this.sprite = new Sprite(this.entityTexture);
        this.posX = 0;
        this.posY = 0;
        this.width = 0;
        this.height = 0;
        this.maxHealth = 9999999;
        this.health = 9999999;
        this.collisionRectangle = new Rectangle(this.posX, this.posY, this.width, this.height);
        this.interactionRectangle = new Rectangle(this.posX, this.posY, this.width, this.height);
        this.speed = 0;
        this.lastMovementAction = "";
    }

    protected void setSprite(Texture texture) {
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(this.posX, this.posY);
    }

    protected void setSprite(TextureRegion textureRegion) {
        this.sprite = new Sprite(textureRegion);
        this.sprite.setPosition(this.posX, this.posY);
    }

    protected void setPosX(float posX) {
        this.posX = posX;
    }
    protected void setPosY(float posY) {
        this.posY = posY;
    }
    protected void setWidth(int width) { this.width = width; }
    protected void setHeight(int height) { this.height = height; }
    protected void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    protected void setHealth(int health) { this.health = health; }
    public void setSpeed(int speed) { this.speed = speed; }
    protected void setCollisionRectangle(Rectangle collisionRectangle) { this.collisionRectangle = collisionRectangle; }
    protected void setInteractionRectangle(Rectangle interactionRectangle) { this.interactionRectangle = interactionRectangle; }
    protected void setLastMovementAction(String lastMovementAction) { this.lastMovementAction = lastMovementAction; }
    public void setShowHitboxRectangle() { this.showHitboxRectangle = !this.showHitboxRectangle; }

    public Sprite getSprite() { return this.sprite; }
    public float getPosX() { return this.posX; }
    public float getPosY() { return this.posY; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public int getMaxHealth() { return this.maxHealth; }
    public int getHealth() { return this.health; }
    protected float getSpeed() { return this.speed; }
    public Rectangle getCollisionRectangle() { return this.collisionRectangle; }
    public Rectangle getInteractionRectangle() { return this.interactionRectangle; }
    public String getLastMovementAction() { return this.lastMovementAction; }
    public boolean getShowHitboxRectangle () { return  this.showHitboxRectangle; }


    public Animation<TextureRegion> getCurrentAnimation() {
        //if (lastMovementAction.equals("default"))
            return this.animationsList.get(this.lastMovementAction);
        }


    public void updatePosition() {
        this.sprite.setPosition(this.posX, this.posY);
        this.collisionRectangle.setPosition(this.posX, this.posY);
        this.interactionRectangle.setPosition(this.posX, this.posY);
    }


    public void goTop(float delta) {
        this.setPosY(this.posY + this.speed*delta);
        this.setLastMovementAction("Up");
    }

    public void goBottom(float delta) {
        this.setPosY(this.posY - this.speed*delta);
        this.setLastMovementAction("Down");
    }

    public void goLeft(float delta) {
        this.setPosX(this.posX - this.speed*delta);
        this.setLastMovementAction("Left");
    }

    public void goRight(float delta) {
        this.setPosX(this.posX + this.speed*delta);
        this.setLastMovementAction("Right");
    }

    public void entityRender(SpriteBatch batch, float elapsedTime) {
        this.setSprite(this.getCurrentAnimation().getKeyFrame(elapsedTime, true));
        this.sprite.draw(batch);
        this.lastMovementAction = "Idlefj205857";
    }

    public void entityHitboxRenderer(OrthographicCamera camera) {

        if (this.showHitboxRectangle) {
            this.shapeRenderer.setProjectionMatrix(camera.combined);
            this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            this.shapeRenderer.setColor(1f, 0f, 0f, 1f);
            this.shapeRenderer.rect(this.posX, this.posY, this.width, this.height);
            this.shapeRenderer.end();
        }

    }
}
