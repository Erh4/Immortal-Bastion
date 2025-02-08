package gamePackages.items;

import gamePackages.entities.*;
import gamePackages.exceptions.*;

import java.lang.reflect.Array;
import java.util.*;


/**
 * Represents a player's inventory in the game, managing items and equipment.
 *
 * The `Inventory` class provides functionality to store, manage, and manipulate
 * different types of items, including consumable items and equipment.
 */
public class Inventory{
    private static final int INV_SIZE = 16;
    private static final int EQUIPMENT_SIZE = 2;
    protected Map<String,Item> equipment = new HashMap<>();
    private Map<String,Item> items; //on va devoir caster au cas par cas pour appeler les sous-méthodes ...
    protected ArrayList<Item> EquipedConsumableItems;
    protected ConsumableItem consumableActiveItem;
    public Inventory(){
        items = new HashMap<>();
        EquipedConsumableItems = new ArrayList<>();
    }

    /**
     * Creates a deep copy of the inventory, including items and equipment.
     *
     * @return A new `Inventory` object with the same items and equipment.
     */
    public Inventory deepCopy(){
        Inventory copy = new Inventory();

        for (String key : equipment.keySet()){
            copy.addItem(equipment.get(key));
        }

        for (String key : getItems().keySet()){

            if (items.get(key).getType().equals("ConsumableItem")){
                ConsumableItem tempItem = ((ConsumableItem) items.get(key)).deepCopy();
                tempItem.setQuantity(((ConsumableItem) items.get(key)).getQuantity());
                copy.addNewEquipedConsumableItem(tempItem);
                copy.addItem(tempItem);
            }
        }

        return copy;
    }


    /**
     * Removes all occurrences of an item with the specified key from the inventory.
     *
     * @param key The key of the item to remove.
     */
    public void clearUp(String key){
        List<String> keys = new ArrayList<>();
        items.forEach( (k,v) -> {if (v.getName().equals(key)) keys.add(k);} ); // https://www.w3schools.com/java/ref_hashmap_foreach.asp
        keys.forEach((k)->items.remove(k));
    }

    /**
     * Adds an item to the inventory. If the item is a consumable and already exists,
     * its quantity is increased.
     *
     * @param <I>  The type of the item, extending `Item`.
     * @param item The item to add.
     */
    public <I extends Item> void addItem(I item){

        if (items.size()<INV_SIZE){
            if (item.getType().equals("ConsumableItem") && items.containsKey(item.getName())){
                ConsumableItem consumable = (ConsumableItem) items.get(item.getName()); // impossible d'utiliser "increment" sinon (c'est pas une méthode d'Item)
                consumable.addQuantity(((ConsumableItem) item).getQuantity());
            }
            else items.put(item.getName(), item);
        }
    }

    /**
     * Gets the map of items in the inventory.
     *
     * @return A map of item names to items.
     */
    public Map<String,Item> getItems(){
        return items;
    }


    /**
     * Deletes an item from the inventory by its name.
     *
     * @param itemName The name of the item to delete.
     * @throws IllegalItemException If the item does not exist in the inventory.
     */
    public void deleteItem(String itemName) throws IllegalItemException{
        if (!items.containsKey(itemName)){
            throw new IllegalItemException(itemName+" n'est pas dans l'inventaire ...");
        }
        if (items.get(itemName).getType().equals("ConsumableItem")){
            ConsumableItem consumable = (ConsumableItem) items.get(itemName);
            consumable.decrement();
        }
        items.remove(itemName);
        clearUp("None");
    }

    /**
     * Adds a new consumable item to the equipped consumable items list.
     *
     * @param item The item to add as an equipped consumable.
     *
     * If no consumable is currently active, the added item becomes the active item.
     */
    public void addNewEquipedConsumableItem(Item item) {
        try {
            if (consumableActiveItem == null){
                consumableActiveItem = (ConsumableItem) item;
            }
            EquipedConsumableItems.add((ConsumableItem) item);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    /**
     * Removes a consumable item from the equipped consumable items list.
     *
     * @param item The item to remove from the equipped consumable list.
     *
     * If the list becomes empty, the active consumable is set to `null`.
     * If there are remaining consumables, the next one in the list becomes active.
     */
    public void removeEquipedConsumableItem(Item item) {
        try {
            for (int i = 0; i < EquipedConsumableItems.size(); i++) {
                if (EquipedConsumableItems.get(i).getName().equals(item.getName())){
                    EquipedConsumableItems.remove(i);
                    Iterator<Item> itemIterator = EquipedConsumableItems.iterator();
                    if (itemIterator.hasNext()){
                        consumableActiveItem = (ConsumableItem) itemIterator.next();
                    }
                    else {
                        consumableActiveItem = null;
                    }
                    break;
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Gets the list of equipped consumable items.
     *
     * @return An `ArrayList` of items currently equipped as consumables.
     */
    public ArrayList<Item> getEquipedConsumableItems() {
        return EquipedConsumableItems;
    }


    /**
     * Gets the currently active consumable item.
     *
     * @return The active consumable item.
     */
    public ConsumableItem getActiveEquipedConsumableItem() {
        return consumableActiveItem;
    }

    /**
     * Checks if an item with the given name is in the equipped consumable items list.
     *
     * @param itemName The name of the item to check.
     * @return True if the item is in the equipped list, false otherwise.
     */
    public boolean isInEquipedConsumableItems(String itemName) {
        for (Item item : EquipedConsumableItems) {
            if (item.getName().equals(itemName)) return true;
        }
        return false;
    }

    /**
     * Changes the active consumable item to the next one in the equipped consumable items list.
     *
     * If the current item is the last in the list, the active item loops back to the first item.
     */
    public void changeActiveEquipedConsumableItem() {

        for (int i = 0; i < EquipedConsumableItems.size(); i++){
            if (EquipedConsumableItems.get(i).getName().equals(consumableActiveItem.getName())){
                if (i+1 == EquipedConsumableItems.size()-1) {   //Si on arrive à l'avant dernier item
                    consumableActiveItem = (ConsumableItem) EquipedConsumableItems.getLast();
                }
                else if (i+1 > EquipedConsumableItems.size()-1) {  //Si on arrive au dernier Item on boucle au début
                    consumableActiveItem = (ConsumableItem) EquipedConsumableItems.getFirst();
                }
                else {          //Sinon on prend juste l'item d'après dans la liste
                    consumableActiveItem = (ConsumableItem) EquipedConsumableItems.get(i+1);
                }
                break;
            }

        }
    }


    /**
     * Equips an item for the player, applying its effects.
     *
     * @param item The item to equip.
     * @param player The player to apply the item's effects.
     * @return An integer status code:
     *         - `0`: Success.
     *         - `-1`: Inventory is full.
     *         - `-2`: Consumables cannot be equipped.
     *         - `-3`: Cannot equip two items of the same type (e.g., two weapons).
     *         - `-4`: The item is already equipped.
     * @throws IllegalItemException If the item is not in the inventory.
     */
    public int setEquipment(Item item, Player player) throws Exception{
        if (equipment.size()==EQUIPMENT_SIZE){return -1; /* il faut enlever un equipement avant d'en mettre un nouveau*/}
        if (!items.containsKey(item.getName())) throw new IllegalItemException(item.getName()+" n'est pas dans l'inventaire ...");
        if (items.get(item.getName()).getType().equals("ConsumableItem")){ return -2; } // un consumable ne peut pas être un equipement
        if (equipment.containsKey(item.getName())){ return -4; } // on ne peut pas rééquiper un equipement
        for (String key : equipment.keySet()){
            if (equipment.get(key).getType().equals(items.get(item.getName()).getType())){
                return -3; // on peut pas mettre 2 armures ou 2 armes par exemple
            }
        }

        equipment.put(item.getName(), item);
        item.use(player);
        return 0;
    }

    /**
     * Removes an equipped item from the player, reversing its effects.
     *
     * @param itemName The name of the item to remove.
     * @param player The player to reverse the item's effects.
     * @throws IllegalItemException If the item is not equipped.
     */
    public void removeEquipment(String itemName, Player player) throws Exception{
        if (!equipment.containsKey(itemName)) throw new IllegalItemException(itemName+" n'est pas dans l'inventaire ...");
        equipment.get(itemName).use(player); //ici on remet la stat bonus à 0 donc
        equipment.remove(itemName);

    }


    /**
     * Gets the total number of items in the inventory.
     *
     * @return The number of items in the inventory.
     */
    public int getNumberOfItems() {
        return items.size();
    }

    /**
     * Gets the map of currently equipped items.
     *
     * @return A map of item names to equipped items.
     */
    public Map<String, Item> getEquipment(){ return equipment; }

    /**
     * Checks if an item with the given name is currently equipped.
     *
     * @param itemName The name of the item to check.
     * @return True if the item is equipped, false otherwise.
     */
    public boolean isEquipped(String itemName){ return equipment.containsKey(itemName); }

    /**
     * Checks if a given item is currently equipped.
     *
     * @param <I>  The type of the item, extending `Item`.
     * @param item The item to check.
     * @return True if the item is equipped, false otherwise.
     */
    public <I extends Item> boolean isEquipped(I item){ return equipment.containsKey(item.getName()); }


}

