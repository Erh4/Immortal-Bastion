package gamePackages.entities;

import gamePackages.toolsManager.AnimationManager;

public class Clam_Monster extends Monster{
    public Clam_Monster() {}
    public Clam_Monster(float posX, float posY) {

        super("enemies/clam_monster_spritesheet.png", posX, posY, 24, 40, 5);
        String spritePath = "enemies/clam_monster_spritesheet.png";

        setDetectionRadius(120);
        this.setMeleeAttackRange(25);
        this.setResetAttackTiming(0.3f);
        this.animationsList.put("Idle", new AnimationManager(spritePath,0,7, 24,40,1/6f).getAnimation());
        this.animationsList.put("Left", new AnimationManager(spritePath,1,4, 24,40,1/6f).getAnimation());
        this.animationsList.put("Right", new AnimationManager(spritePath,2,4, 24,40,1/6f).getAnimation());
        this.animationsList.put("Up", new AnimationManager(spritePath,3,4, 24,40,1/8f).getAnimation());
        this.animationsList.put("Down", new AnimationManager(spritePath,4,4, 24,40,1/8f).getAnimation());
        this.animationsList.put("LeftAttack", new AnimationManager(spritePath,5,4, 24,40,1/20f).getAnimation());
        this.animationsList.put("RightAttack",new AnimationManager(spritePath,6,4, 24,40,1/20f).getAnimation());
        this.animationsList.put("UpAttack", new AnimationManager(spritePath,7,4, 24,40,1/20f).getAnimation());
        this.animationsList.put("DownAttack", new AnimationManager(spritePath,8,4, 24,40,1/20f).getAnimation());
        setDamage(1);
        setSpeed(80);
        setMonsterName("Clam_Monster");
    }

}
