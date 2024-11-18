package gamePackages.items;

import java.util.List;
import java.util.ArrayList;

// Singleton
public class Inventory{
    private static Inventory inventory;
    private static List<Item> items = new ArrayList<>();
    private Inventory(){}
    public static Inventory getInstance(){
        if (inventory == null){
            inventory = new Inventory();
            items = new ArrayList<>();
        }
        return inventory;
    }
    public int addItem(Item item){
        for (Item i : items) {
            if (i.getName().equals("None")){
                items.remove(i);
            }
        }
        if (items.size()==16){
            return -1; // afficher que c'est pas passé ...
        }
        else{
            items.add(item);
            return 0;
        }
    }


}

