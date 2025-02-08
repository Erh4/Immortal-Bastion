package gamePackages.controls;

import gamePackages.entities.Clam_Monster;
import gamePackages.entities.Monster;
import gamePackages.entities.Player;
import gamePackages.ui.InventoryUI;

import java.util.List;


/**
 * Represents a command pattern interface for executing various game actions.
 *
 * This interface defines overloaded methods for executing commands with different targets,
 * allowing for flexible interactions with game entities and UI components.
 */
public interface Command {

    /**
     * Executes a command targeting the player.
     *
     * @param player The player on whom the command is executed.
     */
    public void execute(Player player);

    /**
     * Executes a command targeting the player and a list of monsters.
     *
     * @param player The player involved in the command execution.
     * @param monsters A list of monsters that may be affected by the command.
     */
    public void execute(Player player, List<Monster> monsters);

    /**
     * Executes a command targeting the inventory UI.
     *
     * @param inventoryUI The inventory UI component on which the command is executed.
     */
    public void execute(InventoryUI inventoryUI);
}
