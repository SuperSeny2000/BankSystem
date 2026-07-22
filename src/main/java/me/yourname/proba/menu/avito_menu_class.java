package me.yourname.proba.menu;

import me.yourname.proba.GeneralSettings.createButt_class;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class avito_menu_class implements Listener {

    // pepe

    public static Inventory avito_menu = Bukkit.createInventory(null, 9, Component.text("Авито"));
    public static Inventory buy_menu = Bukkit.createInventory(null, 54, Component.text("КУПИТЬ"));


    static {
        avito_menu.setItem(2, createButt_class.createButt(Material.PAPER, "Продать товар", "Продавайте любые предметы."));
        avito_menu.setItem(4, createButt_class.createButt(Material.PAPER, "Купить товар", "Здесь вы можете купить любой товар."));
        avito_menu.setItem(6, createButt_class.createButt(Material.PAPER, "Отзывы", "Отзывы которые вам оставили игроки."));
    }

    @EventHandler
    public void avito_menu_click(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();

        // Основное меню
        if (inv.equals(avito_menu)) {}
        //
        if (inv.equals(buy_menu)) {}
    }
}
