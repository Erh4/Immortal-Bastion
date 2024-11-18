package gamePackages.items;

import com.badlogic.gdx.graphics.Texture;
import gamePackages.entities.Player;

public abstract class Item {
    protected final float stat;
    protected String name;
    protected float posX;
    protected float posY;
    protected final Texture itemTexture;

    public Item(String name, float stat, float posX, float posY, String spritePath) {
        this.name = name;
        this.stat = stat;
        this.posX = posX;
        this.posY = posY;
        this.itemTexture = new Texture(spritePath);
    }
    public void setName(String name) { this.name = name; }
    public abstract String getType();
    public float getStat() { return stat; }
    public String getName() { return name; }
    public abstract void use(Player player);
}
