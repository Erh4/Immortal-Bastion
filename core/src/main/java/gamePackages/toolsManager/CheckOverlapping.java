package gamePackages.toolsManager;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import static java.lang.Math.*;

/**
 * Utility class for checking geometric overlaps and collisions.
 *
 * The `CheckOverlapping` class provides static methods to determine
 * whether specific geometric shapes (rectangles, circles, points) overlap.
 */
public class CheckOverlapping {
    /**
     * Checks if a rectangle and a circle overlap.
     *
     * @param rect The `Rectangle` object representing the rectangular area.
     * @param circ The `Circle` object representing the circular area.
     * @return `true` if the rectangle and circle overlap, `false` otherwise.
     *
     * This method calculates the closest point on the rectangle to the circle's center
     * and determines if the distance between them is less than or equal to the circle's radius.
     */
    public static boolean overlapsRectCircle(Rectangle rect, Circle circ){
        float closestX = clamp(circ.x, rect.x, rect.x+rect.width);
        float closestY = clamp(circ.y, rect.y, rect.y+rect.height);

        float distanceX = circ.x-closestX;
        float distanceY = circ.y-closestY;

        float distanceSquared = distanceX*distanceX + distanceY*distanceY;


        return distanceSquared <= circ.radius * circ.radius;
    }

    /**
     * Checks if a point lies within a circle.
     *
     * @param circ    The `Circle` object representing the circular area.
     * @param centerX The X-coordinate of the point to check.
     * @param centerY The Y-coordinate of the point to check.
     * @return `true` if the point lies within the circle, `false` otherwise.
     *
     * This method calculates the distance between the circle's center and the point,
     * and compares it to the circle's radius.
     */
    public static boolean overlapsPointleCircle(Circle circ, float centerX, float centerY){
        float distanceX = circ.x-centerX;
        float distanceY = circ.y-centerY;
        float distanceSquared = distanceX*distanceX + distanceY*distanceY;
        return distanceSquared <= circ.radius * circ.radius;
    }
}
