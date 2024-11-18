package gamePackages.items;

import gamePackages.entities.Player;

public class Armor extends Item{

    public Armor(String name, float stat, float posX, float posY, String spritePath){
        super(name, stat, posX, posY, spritePath);
    }

    @Override
    public String getType(){
        return "armor";
    }
    @Override
    public void use(Player player){
        // à faire quand player sera dev.
    }

}
