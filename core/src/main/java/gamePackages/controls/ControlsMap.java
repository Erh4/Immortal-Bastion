package gamePackages.controls;

import com.badlogic.gdx.Input;
import gamePackages.entities.Clam_Monster;
import gamePackages.entities.Entity;
import gamePackages.entities.Monster;
import gamePackages.entities.Player;
import gamePackages.items.ConsumableItem;
import gamePackages.items.Inventory;
import gamePackages.ui.InventoryUI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Maps keyboard input to commands for controlling the player and game interactions.
 *
 * This class uses a mapping between sets of key codes and corresponding command objects
 * to execute specific actions based on user input.
 */
public class ControlsMap{
    /**
     * A static mapping of key combinations to commands.
     * The keys are sets of integer key codes, and the values are command implementations.
     */
    public static Map<Set<Integer>, Command> keyMappings;
    public float delta;

    /**
     * Constructs a `ControlsMap` for the given player.
     *
     * @param player The player to be controlled by the mapped commands.
     *
     * This constructor initializes the key-command mapping with predefined key combinations for:
     * - Movement in all cardinal and diagonal directions (W, A, S, D combinations).
     * - Dodging (SPACE key).
     * Each mapping executes a specific action on the player, using the current delta for timing.
     */
    public ControlsMap(Player player) {
        delta = 0;
        keyMappings = new HashMap<>();

        //Directions
        keyMappings.put(Set.of(Input.Keys.W, Input.Keys.D), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.goTopRight(delta);}
        });
        keyMappings.put(Set.of(Input.Keys.W, Input.Keys.A), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.goTopLeft(delta);}
        });
        keyMappings.put(Set.of(Input.Keys.S, Input.Keys.D), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.goBottomRight(delta);}
        });
        keyMappings.put(Set.of(Input.Keys.S, Input.Keys.A), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.goBottomLeft(delta);}
        });
        keyMappings.put(Set.of(Input.Keys.W), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.goTop(delta);
            }
        });
        keyMappings.put(Set.of(Input.Keys.A), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.goLeft(delta);
            }
        });
        keyMappings.put(Set.of(Input.Keys.S), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.goBottom(delta);
            }
        });
        keyMappings.put(Set.of(Input.Keys.D), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.goRight(delta);
            }
        });
        keyMappings.put(Set.of(Input.Keys.SPACE), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.dodge(delta);
            }
        });

        //Dodges
        keyMappings.put(Set.of(Input.Keys.SPACE, Input.Keys.D), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.dodge(delta);
            }
        });
        keyMappings.put(Set.of(Input.Keys.SPACE, Input.Keys.A), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.dodge(delta);
            }
        });
        keyMappings.put(Set.of(Input.Keys.SPACE, Input.Keys.W), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.dodge(delta);
            }
        });
        keyMappings.put(Set.of(Input.Keys.SPACE, Input.Keys.S), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.dodge(delta);
            }
        });
        keyMappings.put(Set.of(Input.Keys.SPACE, Input.Keys.W, Input.Keys.D), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.dodge(delta);
            }
        });
        keyMappings.put(Set.of(Input.Keys.SPACE, Input.Keys.W, Input.Keys.A), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.dodge(delta);
            }
        });
        keyMappings.put(Set.of(Input.Keys.SPACE, Input.Keys.S, Input.Keys.D), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.dodge(delta);
            }
        });
        keyMappings.put(Set.of(Input.Keys.SPACE, Input.Keys.S, Input.Keys.A), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.dodge(delta);
            }
        });


        //Equipment Selection inGame
        keyMappings.put(Set.of(Input.Keys.RIGHT), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {player.getPlayerInventory().changeActiveEquipedConsumableItem();
            }
        });

        //Use ActivItem
        keyMappings.put(Set.of(Input.Keys.R), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {
                try {
                    ConsumableItem tempItem = player.getPlayerInventory().getActiveEquipedConsumableItem();
                    tempItem.consumItem(player);
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        //Debug Methods
        keyMappings.put(Set.of(Input.Keys.H), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {
                player.setDebugDisplayState();
                for (Entity monster : monsters) {
                    monster.setDebugDisplayState();
                }
            }
        });

        keyMappings.put(Set.of(Input.Keys.L), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {
                //player.heal(1f);
            }
        });
        keyMappings.put(Set.of(Input.Keys.SEMICOLON), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {//player.upMaxHealth(2f);
            }
        });
        keyMappings.put(Set.of(Input.Keys.T), new BaseCommand() {
            @Override
            public void execute(Player player, List<Monster> monsters) {//player.takeDamages(2.5f);
            }
        });

        keyMappings.put(Set.of(Input.Keys.I), new BaseCommand() {

            @Override
            public void execute(Player player, List<Monster> monsters) {}

            @Override
            public void execute(InventoryUI inventoryUI) {
                inventoryUI.setInvDrawn(!inventoryUI.getIsInvDrawn());
            }
        });
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public Command getCommand(Set<Integer> PressedKeys) {
        return keyMappings.get(PressedKeys);
    }

}
