package me.yourname.proba.GeneralSettings;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class removeItems_class {

    // Удаляет валюту
    public static void removeItems(Player player, int amount, ItemStack currencyIt) {
        ItemStack toRemove = currencyIt.clone();
        toRemove.setAmount(amount);
        player.getInventory().removeItem(toRemove);
    }

}
