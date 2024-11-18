package gamePackages.toolsManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationManager {
    protected Texture spritesSheet;
    protected Animation<TextureRegion> animation;
    protected TextureRegion[] animationFrames;


    public AnimationManager(String spritesSheetPath, int cols, int rows, int frameWidth, int frameHeight, float frameDuration) {
        this.spritesSheet = new Texture(spritesSheetPath);
        TextureRegion[][] frames = new TextureRegion(this.spritesSheet).split(frameWidth, frameHeight);
        this.animationFrames = new TextureRegion[cols * rows];

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.animationFrames[index] = frames[i][j];
                index++;
            }
        }

        this.animation = new Animation<>(frameDuration, this.animationFrames);
    }

    public Animation<TextureRegion> getAnimation() {
        return this.animation;
    }
}
