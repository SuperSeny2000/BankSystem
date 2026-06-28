package me.yourname.proba.GeneralSettings;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class createButt_class {
    public static ItemStack createButt(Material material, String name, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null && !lore.isEmpty()) {
            // Разбиваем по \n и убираем пустые строки
            List<String> loreList = Arrays.asList(lore.split("\n"));
            meta.setLore(loreList);
        }
        item.setItemMeta(meta);
        return item;
    }
}
