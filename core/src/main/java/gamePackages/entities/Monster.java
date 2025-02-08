package gamePackages.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import gamePackages.toolsManager.AnimationManager;
import gamePackages.toolsManager.CheckOverlapping;
import gamePackages.toolsManager.Node;
import gamePackages.toolsManager.PathfindingManager;

/**
 * Represents a monster entity in the game, extending the base `Entity` class.
 *
 * The `Monster` class inherits the attributes and behaviors of `Entity` and introduces
 * additional properties specific to monsters, such as a name and a distance threshold (`epsilonDistance`).
 */
public class Monster extends Entity{

    //protected MonsterUI monsterUI;
    protected float epsilonDistance;
    protected String monsterName;
    /**
     * Default constructor for creating an uninitialized monster.
     */
    public Monster() {}

    /**
     * Constructs a monster with the specified parameters.
     *
     * @param spritePath The path to the sprite representing the monster.
     * @param posX The X-coordinate of the monster's position.
     * @param posY The Y-coordinate of the monster's position.
     * @param width The width of the monster.
     * @param height The height of the monster.
     * @param maxHealth The maximum health of the monster.
     *
     * This constructor initializes the monster's properties by calling the parent `Entity` constructor.
     */
    public Monster(String spritePath, float posX, float posY, int width, int height, float maxHealth) {
        super(spritePath, posX, posY, width, height, maxHealth);
    }


    /**
     * Sets the name of the monster.
     *
     * @param monsterName The name to assign to the monster.
     */
    public void setMonsterName(String monsterName) {this.monsterName = monsterName;}

    /**
     * Gets the name of the monster.
     *
     * @return The name of the monster.
     */
    public String getMonsterName() {return this.monsterName;}


    /**
     * Renders the monster by calling the base entity rendering method.
     *
     * @param batch The SpriteBatch used to draw the monster.
     * @param delta Time elapsed since the last frame, used for animation timing.
     * @param camera The camera used to render the monster's perspective.
     */
    public void monsterRender(SpriteBatch batch, float delta, OrthographicCamera camera) {
        entityRender(batch, delta);
    }


    /**
     * Moves the monster towards a target entity using pathfinding and updates its position.
     *
     * @param delta Time elapsed since the last frame, used for movement calculations.
     * @param target The target entity the monster is moving toward.
     * @param PathfindingMap The pathfinding manager used to calculate the movement path.
     */
    public void movToTarget(float delta, Entity target, PathfindingManager PathfindingMap) {
        //movmentHandler1(delta, target);
        //System.out.println("____________________________________");
        this.collisionNodeCircle.setPosition(getCenterX(), getCenterY());

        int xTempDest = (int)target.getCenterX()/16;
        int yTempDest = (int)target.getCenterY()/16;

        int xTempSrc = Math.round(getCenterX()/16);
        int yTempSrc = Math.round(getCenterY()/16);
        //System.out.println("TempDest : (" + xTempDest+","+yTempDest+")");

        Node nextNode = getPathNodes().getLast();

        float tempDistance = getSpeed()*delta;
        //System.out.println("Node : (" + (nextNode.getX()-(int)(PathfindingMap.getMaxCol()/2f))+","+(nextNode.getY()-(int)((PathfindingMap.getMaxRow()-2)/2f)-1)+")");
        float targetX = xTempDest+nextNode.getX()-(int)(PathfindingMap.getMaxCol()/2f);
        float targetY = nextNode.getY()+yTempDest-(int)((PathfindingMap.getMaxRow()-2)/2f)-1;

        targetX *= 16;
        targetY *= 16;


        collisionNodeCircle.setRadius(tempDistance);
        collisionNodeCircle.setPosition(getCenterX(), getCenterY());
        if (targetX == xTempSrc*16 && targetY == yTempSrc*16) {
            setOnIdleState();
        }
        else {
            movmentHandler1(delta, targetX, targetY);
            this.epsilonDistance = tempDistance * 2;
        }

        //System.out.println("Monster: ("+posX + "," + posY+")");
        //System.out.println("Target: ("+targetX + "," + targetY+")");

    }

    /**
     * Handles movement logic to move the monster towards a target in the bottom-left direction.
     *
     * @param delta Time elapsed since the last frame, used for movement calculations.
     * @param targetX The target X-coordinate.
     * @param targetY The target Y-coordinate.
     */
    protected void movmentHandler1(float delta, float targetX, float targetY) {
        if (targetX+epsilonDistance < getCenterX() && targetY+epsilonDistance < getCenterY()) {//Aller en bas à gauche
            //System.out.println("BottomLeft");
            goBottomLeft(delta);
        }
        else {
            movmentHandler2(delta, targetX, targetY);
        }
    }

    /**
     * Handles movement logic to move the monster towards a target in the top-left direction.
     *
     * @param delta Time elapsed since the last frame, used for movement calculations.
     * @param targetX The target X-coordinate.
     * @param targetY The target Y-coordinate.
     */
    protected void movmentHandler2(float delta, float targetX, float targetY) {
        if (targetX+epsilonDistance < getCenterX() && targetY-epsilonDistance > getCenterY()) { //Aller en haut à gauche
            //System.out.println("TopLeft");
            goTopLeft(delta);
        }
        else {
            movmentHandler3(delta, targetX, targetY);
        }
    }

    /**
     * Handles movement logic to move the monster towards a target in the bottom-right direction.
     *
     * @param delta Time elapsed since the last frame, used for movement calculations.
     * @param targetX The target X-coordinate.
     * @param targetY The target Y-coordinate.
     */
    protected void movmentHandler3(float delta, float targetX, float targetY) {
        if (targetX-epsilonDistance > getCenterX() && targetY+epsilonDistance < getCenterY()) { //Aller en bas à droite
            //System.out.println("BottomRight");
            goBottomRight(delta);
        }
        else {
            movmentHandler4(delta, targetX, targetY);
        }
    }

    /**
     * Handles movement logic to move the monster towards a target in the top-right direction.
     *
     * @param delta Time elapsed since the last frame, used for movement calculations.
     * @param targetX The target X-coordinate.
     * @param targetY The target Y-coordinate.
     */
    protected void movmentHandler4(float delta, float targetX, float targetY) {
        if (targetX-epsilonDistance > getCenterX() && targetY-epsilonDistance > getCenterY()) { //Aller en haut à droite
            //System.out.println("TopRight");
            goTopRight(delta);
        }
        else {
            movmentHandler5(delta, targetX, targetY);
        }
    }


    /**
     * Handles movement logic to move the monster towards a target.
     *
     * @param delta Time elapsed since the last frame, used for movement calculations.
     * @param targetX The target X-coordinate.
     * @param targetY The target Y-coordinate.
     */
    protected void movmentHandler5(float delta, float targetX, float targetY) {


        if (targetX < this.getCenterX() && targetY+epsilonDistance > getCenterY() && targetY-epsilonDistance < getCenterY()) { //Aller à gauche
            //System.out.println("Left");
            goLeft(delta);
        }
        if (targetX > this.getCenterX() && targetY+epsilonDistance > getCenterY() && targetY-epsilonDistance < getCenterY()) { //Aller à droite
            //System.out.println("Right");
            goRight(delta);
        }

        if (targetY < this.getCenterY() && targetX+epsilonDistance > getCenterX() && targetX-epsilonDistance < getCenterX()) { //Aller en bas
            //System.out.println("Bottom");
            goBottom(delta);
        }
        if (targetY > this.getCenterY() && targetX+epsilonDistance > getCenterX() && targetX-epsilonDistance < getCenterX()) { //Aller en haut
            //System.out.println("Top");
            goTop(delta);
        }

    }

    /**
     * Updates the monster's position and movement state based on the player's location.
     *
     * @param delta Time elapsed since the last frame, used for movement calculations.
     * @param target The player entity the monster is tracking.
     * @param PathfindingMap The pathfinding manager used for movement calculations.
     */
    public void UpdateToPlayerPosition(float delta, Entity target, PathfindingManager PathfindingMap) {
        if (getPlayerDetected()) {
            if (!this.getCollisionRectangle().overlaps(target.getCollisionRectangle()) && !attack) {
                //if (!CheckOverlapping.overlapsRectCircle(target.getCollisionRectangle(), attackRangeCircle)&& !attack) {
                movToTarget(delta, target, PathfindingMap);
                updatePosition();
            }
        }

        else {
            setOnIdleState();
        }
    }

    /**
     * Creates a deep copy of the monster with the same attributes and animations.
     *
     * @return A new `Monster` instance with identical properties.
     */
    public Monster deepCopy() {
        Monster monsterCopy = new Monster(spritePath, posX, posY, width, height, maxHealth);
        monsterCopy.setHealth(health);
        monsterCopy.setSpeed(speed);
        monsterCopy.setDamage(damage);
        monsterCopy.setDetectionRadius(detectionRadius);
        monsterCopy.setMeleeAttackRange(meleeAttackRange);
        monsterCopy.setResetAttackTiming(resetAttackTiming);
        monsterCopy.setMonsterName(monsterName);
        for (String anim : animationsList.keySet()){
            monsterCopy.animationsList.put(anim, animationsList.get(anim));
        }
        return monsterCopy;
    }
}
