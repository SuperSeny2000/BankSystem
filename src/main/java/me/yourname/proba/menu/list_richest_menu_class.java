package me.yourname.proba.menu;

import me.yourname.proba.DataManager;
import me.yourname.proba.GeneralSettings.createButt_class;
import me.yourname.proba.GeneralSettings.myself_class;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class list_richest_menu_class implements Listener {

    public static DataManager dataManager;

    public static int currentPage2 = 0;
    private static String currentMenuType2 = "pizda";

    // Пишет ник игрока
    private static ItemStack NamePlayerHead(String playerName) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(playerName);
        meta.setDisplayName("§a" + playerName);
        head.setItemMeta(meta);
        return head;
    }

    // очищает и ставит головы
    private static void menu_heads(Inventory inv, List<String> playersOnPage) {
        for (int i = 1; i < 6; i++) {inv.setItem(i, null);}
        for (int i = 0; i < playersOnPage.size() && i < 6; i++) {inv.setItem(i + 1, NamePlayerHead(playersOnPage.get(i)));}
    }

    // открывает и настраивает меню выбора игрока
    public static void list_richest_menu(Player player, int page, String menuType) {
        currentMenuType2 = menuType;
        currentPage2 = page;

        String title = "Список богатейших";
        Inventory inv = Bukkit.createInventory(null, 9, Component.text(title));

        List<String> allPlayers = dataManager.getAllPlayers();
        int totalPages = (int) Math.ceil(allPlayers.size() / 7.0);
        int start = page * 7;
        int end = Math.min(start + 7, allPlayers.size());
        List<String> pagePlayers = allPlayers.subList(start, end);

        menu_heads(inv, pagePlayers);

        inv.setItem(0, createButt_class.createButt(Material.ARROW, "Назад", ""));

        if (page > 0) {
            inv.setItem(7, createButt_class.createButt(Material.ARROW, "Предыдущая стр.", ""));
        }
        if (page < totalPages - 1) {
            inv.setItem(8, createButt_class.createButt(Material.ARROW, "Следующая стр.", ""));
        }

        /*
        // Заполняем пустые слоты нижнего ряда серыми панелями
        for (int i = 45; i < 54; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, createButt_class.createButt(Material.GRAY_STAINED_GLASS_PANE, " ", " "));
            }
        }

         */
        player.openInventory(inv);
    }

    // Обработчик кликов в меню выбора игрока
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Список богатейших")) return;
        if (event.getClickedInventory() != event.getView().getTopInventory()) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;

        int slot = event.getSlot();

        // Кнопка "Назад" (слот 49)
        if (slot == 0) {if (currentMenuType2.equals("pizda")) {player.openInventory(bank_menu_class.bank_menu);}}

        // Кнопки навигации
        if (slot == 8 && clicked.getType() == Material.ARROW) {list_richest_menu(player, currentPage2 + 1, currentMenuType2);}
        if (slot == 7 && clicked.getType() == Material.ARROW) {if (currentPage2 > 0) {list_richest_menu(player, currentPage2 - 1, currentMenuType2);}}

        // Выбор игрока (головы в слотах 0-44)
        if (slot >= 0 && slot < 7 && clicked.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) clicked.getItemMeta();
            String owner = meta.getOwner();
            if (owner != null) {
                UUID uuid = Bukkit.getOfflinePlayer(owner).getUniqueId();
                myself_class.selectedPlayer = uuid;
                int balance = dataManager.getBalance(myself_class.selectedPlayer);
                if (currentMenuType2.equals("pizda")) {
                    dataManager.printFines(uuid, player);
                    list_richest_menu(player, 0, "pizda");
                }
            }
        }
    }


}
