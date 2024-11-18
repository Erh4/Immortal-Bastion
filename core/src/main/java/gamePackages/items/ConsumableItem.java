package gamePackages.items;

//import gamePackages.exceptions;


import gamePackages.entities.Player;

public class ConsumableItem extends Item {


    public ConsumableItem(String name, float stat, float posX, float posY, String spritePath) {
        super(name,stat,posX,posY,spritePath);

    }
    @Override
    public String getType(){ return "ConsumableItem"; }
    @Override
    public void use(Player player) {
        setName("None");
        // à faire quand joueurs blabla...
    }

}
