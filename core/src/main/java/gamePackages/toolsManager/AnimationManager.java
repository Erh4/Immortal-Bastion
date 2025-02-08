package gamePackages.toolsManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Manages animations created from a spritesheet.
 *
 * The `AnimationManager` class simplifies the process of extracting animation frames
 * from a spritesheet and creating an `Animation` object for use in a game.
 */
public class AnimationManager {
    protected Texture spritesSheet;
    protected Animation<TextureRegion> animation;
    protected TextureRegion[] animationFrames;

    /**
     * Constructs an `AnimationManager` using a spritesheet and animation parameters.
     *
     * @param spritesSheetPath The file path to the spritesheet texture.
     * @param lineNumber       The row in the spritesheet to use for the animation (0-indexed).
     * @param framesNumber     The number of frames in the animation.
     * @param frameWidth       The width of each frame in pixels.
     * @param frameHeight      The height of each frame in pixels.
     * @param frameDuration    The duration of each frame in seconds.
     *
     * This constructor splits the spritesheet into individual frames and
     * creates an animation using the specified parameters.
     */
    public AnimationManager(String spritesSheetPath, int lineNumber, int framesNumber, int frameWidth, int frameHeight, float frameDuration) {


        this.spritesSheet = new Texture(spritesSheetPath);
        TextureRegion[][] frames = new TextureRegion(this.spritesSheet).split(frameWidth, frameHeight);
        this.animationFrames = new TextureRegion[framesNumber];

        for (int i = 0; i < framesNumber; i++) {

            int j = i%(this.spritesSheet.getWidth()/frameWidth);
            this.animationFrames[i] = frames[lineNumber][j];
            }

        this.animation = new Animation<>(frameDuration, this.animationFrames);

        //System.out.println("Animation done");
    }

    /**
     * Gets the animation created from the spritesheet.
     *
     * @return An `Animation` object containing the frames and duration.
     */
    public Animation<TextureRegion> getAnimation() {
        return this.animation;
    }
}
