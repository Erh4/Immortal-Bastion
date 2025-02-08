package gamePackages.entities;

import gamePackages.toolsManager.AnimationManager;

public class Clam_Boss extends Monster {
    public Clam_Boss() {}
    public Clam_Boss(float posX, float posY) {
        super("enemies/clam_boss_spritesheet.png", posX, posY, 48,80, 30);
        String spritePath = "enemies/clam_boss_spritesheet.png";
        setDetectionRadius(200);
        this.setMeleeAttackRange(60);
        this.setResetAttackTiming(0.3f);
        this.animationsList.put("Idle", new AnimationManager(spritePath,0,7, 48,80,1/6f).getAnimation());
        this.animationsList.put("Left", new AnimationManager(spritePath,1,4, 48,80,1/6f).getAnimation());
        this.animationsList.put("Right", new AnimationManager(spritePath,2,4, 48,80,1/6f).getAnimation());
        this.animationsList.put("Up", new AnimationManager(spritePath,3,4, 48,80,1/8f).getAnimation());
        this.animationsList.put("Down", new AnimationManager(spritePath,4,4, 48,80,1/8f).getAnimation());
        this.animationsList.put("LeftAttack", new AnimationManager(spritePath,5,4, 48,80,1/20f).getAnimation());
        this.animationsList.put("RightAttack",new AnimationManager(spritePath,6,4, 48,80,1/20f).getAnimation());
        this.animationsList.put("UpAttack", new AnimationManager(spritePath,7,4, 48,80,1/20f).getAnimation());
        this.animationsList.put("DownAttack", new AnimationManager(spritePath,8,4, 48,80,1/20f).getAnimation());
        setDamage(4);
        setSpeed(60);
        setMonsterName("Clam_Boss");
    }
}
