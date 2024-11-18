package gamePackages.items;

import gamePackages.entities.Player;

public class Weapon extends Item{

    public Weapon(String name, float stat, float posX, float posY, String spritePath){
        super(name, stat, posX, posY, spritePath);
    }

    @Override
    public String getType(){
        return "weapon";
    }
    @Override
    public void use(Player player){
        //
    }

}
