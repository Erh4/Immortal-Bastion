package gamePackages.items;

import gamePackages.entities.Player;


/**
 * Represents an armor item in the game.
 *
 * The `Armor` class extends the base `Item` class and adds functionality specific
 * to armor, such as providing a damage reduction statistic (`armor_stat`).
 */
public class Armor extends Item{
    private final float armor_stat;

    /**
     * Constructs an `Armor` item with the specified attributes.
     *
     * @param name The name of the armor item.
     * @param posX The X-coordinate of the armor's position.
     * @param posY The Y-coordinate of the armor's position.
     * @param spritePath The file path to the sprite texture for the armor.
     * @param armor_stat The damage reduction value provided by the armor.
     */
    public Armor(String name, float posX, float posY, String spritePath,float armor_stat) {
        super(name, posX, posY, spritePath);
        this.armor_stat = armor_stat;
    }


    /**
     * Gets the armor statistic of the item.
     *
     * @return The armor stat, representing the damage reduction value.
     */
    public float getArmorStat() {return armor_stat;}


    /**
     * Gets the type of the item.
     *
     * @return A string representing the type of the item, which is `"armor"`.
     */
    @Override
    public String getType(){
        return "armor";
    }

    /**
     * Applies the armor's effect to the specified player.
     *
     * @param player The player on whom the armor is used.
     * @throws Exception If an error occurs while applying the armor's effect.
     *
     * This method toggles the player's damage reduction. If the player currently has no damage reduction,
     * the armor's stat is applied. If the player already has damage reduction, it is reset to zero.
     */
    @Override
    public void use(Player player) throws Exception{
        try {
            if (player.getDmgReduction() == 0f) player.setDmgReduction(armor_stat);
            else player.setDmgReduction(0f);
        }
        catch (NullPointerException e){
            System.out.println("Erreur avec la référence au joueur : "+ e);
        }
    }

}
