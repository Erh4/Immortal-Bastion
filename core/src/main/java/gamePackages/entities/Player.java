package gamePackages.entities;

import gamePackages.toolsManager.AnimationManager;

public class Player extends Entity {

    public Player(String spritePath, float posX, float posY, int width, int height, int maxHealth) {
        super(spritePath, posX, posY, width, height, maxHealth);
        this.IdleAnimation = new AnimationManager("character/stickman_idle.png", 8, 1, 24, 40, 1f/6f);
        this.LeftWalkAnimation = new AnimationManager("character/stickman_left_walk.png",  4, 1, 24, 40, 1/8f);
        this.RightWalkAnimation = new AnimationManager("character/stickman_right_walk.png", 4,1,24, 40, 1/8f);
        this.UpWalkAnimation = new AnimationManager("character/stickman_down_walk.png", 4, 1, 24, 40, 1/8f);
        this.DownWalkAnimation = new AnimationManager("character/stickman_down_walk.png", 4, 1, 24, 40, 1/8f);

        this.animationsList.put("Idle", this.IdleAnimation.getAnimation());
        this.animationsList.put("Left", this.LeftWalkAnimation.getAnimation());
        this.animationsList.put("Right", this.RightWalkAnimation.getAnimation());
        this.animationsList.put("Up", this.UpWalkAnimation.getAnimation());
        this.animationsList.put("Down", this.DownWalkAnimation.getAnimation());
    }

}
