package gamePackages.controls;

import com.badlogic.gdx.Game;
import gamePackages.entities.Clam_Monster;
import gamePackages.entities.Monster;
import gamePackages.entities.Player;
import gamePackages.items.Inventory;
import gamePackages.ui.InventoryUI;

import java.util.List;


/**
 * An abstract base class implementing the Command interface, providing default behavior for commands.
 *
 * This class serves as a base for concrete command implementations and provides default
 * "unknown command" behavior for all overloaded `execute` methods.
 * Subclasses can override these methods to define specific command behavior.
 */
public abstract class BaseCommand implements Command{

    /**
     * Default implementation for commands targeting the player.
     * Prints "Unknown command" to indicate the command is not implemented.
     *
     * @param player The player on whom the command would be executed.
     */
    @Override
    public void execute(Player player) {
        System.out.println("Unknown command");
    }

    /**
     * Default implementation for commands targeting the player and a list of monsters.
     * Prints "Unknown command" to indicate the command is not implemented.
     *
     * @param player The player involved in the command execution.
     * @param monster A list of monsters that may be affected by the command.
     */
    @Override
    public void execute(Player player, List<Monster> monster) {
        System.out.println("Unknown command");
    }

    /**
     * Default implementation for commands targeting the inventory UI.
     * Prints "Unknown command" to indicate the command is not implemented.
     *
     * @param inventoryUI The inventory UI component on which the command would be executed.
     */
    @Override
    public void execute(InventoryUI inventoryUI) {
        System.out.println("Unknown command");
    }
}
