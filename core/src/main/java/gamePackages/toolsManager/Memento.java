package gamePackages.toolsManager;

import gamePackages.entities.Clam_Monster;
import gamePackages.entities.Monster;
import gamePackages.entities.Player;
import gamePackages.items.ConsumableItem;


/**
 * Represents a Memento for saving and restoring the state of game entities.
 *
 * The `Memento` class is used to capture and store the state of different
 * entities (Player, Monster, ConsumableItem) to allow restoration at a later time.
 */
public class Memento {

    protected Player playerMemento;
    protected Monster monsterMemento;
    protected ConsumableItem consumableMemento;


    /**
     * Constructs a `Memento` to save the state of a `Player`.
     *
     * @param player The `Player` object whose state is to be saved.
     */
    public Memento(Player player) {
        playerMemento = player;
    }

    /**
     * Constructs a `Memento` to save the state of a `Monster`.
     *
     * @param clam The `Monster` object whose state is to be saved.
     */
    public Memento(Monster clam) {
        monsterMemento = clam;
    }

    /**
     * Constructs a `Memento` to save the state of a `ConsumableItem`.
     *
     * @param consumable The `ConsumableItem` object whose state is to be saved.
     */
    public Memento(ConsumableItem consumable) {
        consumableMemento = consumable;
    }

    /**
     * Gets the saved state of the `Player`.
     *
     * @return The `Player` object stored in the memento.
     */
    public Player getPlayerMemento() {
        return playerMemento;
    }


    /**
     * Gets the saved state of the `Monster`.
     *
     * @return The `Monster` object stored in the memento.
     */
    public Monster getClamMemento() {
        return monsterMemento;
    }

    /**
     * Gets the saved state of the `ConsumableItem`.
     *
     * @return The `ConsumableItem` object stored in the memento.
     */
    public ConsumableItem getConsumableMemento() {
        return consumableMemento;
    }
}
