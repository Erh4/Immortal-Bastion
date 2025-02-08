package gamePackages.items;

import gamePackages.entities.Player;


/**
 * Represents a weapon item in the game.
 *
 * The `Weapon` class extends the base `Item` class and adds functionality specific
 * to weapons, such as providing a damage buff.
 */
public class Weapon extends Item{
    private final float damage;


    /**
     * Constructs a `Weapon` item with the specified attributes.
     *
     * @param name The name of the weapon.
     * @param posX The X-coordinate of the weapon's position.
     * @param posY The Y-coordinate of the weapon's position.
     * @param spritePath The file path to the sprite texture for the weapon.
     * @param damage The damage buff provided by the weapon.
     */
    public Weapon(String name, float posX, float posY, String spritePath, float damage){
        super(name, posX, posY, spritePath);
        this.damage = damage;
    }


    /**
     * Gets the damage value of the weapon.
     *
     * @return The damage buff provided by the weapon.
     */
    public float getDamage(){return damage;}


    /**
     * Gets the type of the item.
     *
     * @return A string representing the type of the item, which is `"weapon"`.
     */
    @Override
    public String getType(){
        return "weapon";
    }

    /**
     * Applies the weapon's effect to the specified player.
     *
     * @param player The player on whom the weapon is used.
     * @throws Exception If an error occurs while applying the weapon's effect.
     *
     * This method toggles the player's damage buff. If the player currently has no damage buff,
     * the weapon's damage value is applied. If the player already has a damage buff, it is reset to zero.
     */
    @Override
    public void use(Player player) throws Exception{
        try {
            if (player.getDmgBuff() == 0f) {
                player.setDmgBuff(damage);
            } else player.setDmgBuff(0f);
        }
        catch (NullPointerException e){
            throw new NullPointerException("Erreur lors de la référence au joueur ..."); // même comportement que Armor normalement
        }

    }

}
