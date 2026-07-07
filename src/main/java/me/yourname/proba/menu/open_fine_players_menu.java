package me.yourname.proba.menu;

import me.yourname.proba.DataManager;
import me.yourname.proba.GeneralSettings.createButt_class;
import me.yourname.proba.GeneralSettings.myself_class;
import me.yourname.proba.GeneralSettings.updateMenuDisplay_class;
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

import static me.yourname.proba.menu.fine_menu_class.*;

public class open_fine_players_menu implements Listener {

    public static DataManager dataManager;

    public static int currentPage1 = 0;
    private static String currentMenuType1 = "xuy";

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
        for (int i = 0; i < 45; i++) {inv.setItem(i, null);}
        for (int i = 0; i < playersOnPage.size() && i < 45; i++) {inv.setItem(i, NamePlayerHead(playersOnPage.get(i)));}
    }

    // открывает и настраивает меню выбора игрока
    public static void open_fine_players(Player player, int page, String menuType) {
        currentMenuType1 = menuType;
        currentPage1 = page;

        String title = "Список должников";
        Inventory inv = Bukkit.createInventory(null, 54, Component.text(title));

        List<String> allPlayers = dataManager.getAllFinePlayers();
        int totalPages = (int) Math.ceil(allPlayers.size() / 45.0);
        int start = page * 45;
        int end = Math.min(start + 45, allPlayers.size());
        List<String> pagePlayers = allPlayers.subList(start, end);

        menu_heads(inv, pagePlayers);

        inv.setItem(49, createButt_class.createButt(Material.ARROW, "Назад", ""));
        inv.setItem(45, createButt_class.createButt(Material.COMPASS, "Найти игрока", ""));

        if (page > 0) {inv.setItem(47, createButt_class.createButt(Material.ARROW, "Предыдущая стр.", ""));}
        if (page < totalPages - 1) {inv.setItem(53, createButt_class.createButt(Material.ARROW, "Следующая стр.", ""));}

        // Заполняем пустые слоты нижнего ряда серыми панелями
        for (int i = 45; i < 54; i++) {if (inv.getItem(i) == null) {inv.setItem(i, createButt_class.createButt(Material.GRAY_STAINED_GLASS_PANE, " ", " "));}}
        player.openInventory(inv);
    }

    // Обработчик кликов в меню выбора игрока
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Список должников")) return;
        if (event.getClickedInventory() != event.getView().getTopInventory()) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;

        int slot = event.getSlot();

        // Кнопка "Назад" (слот 49)
        if (slot == 49) {if (currentMenuType1.equals("xuy")) {player.openInventory(fine_menu);}}

        // Кнопки навигации
        if (slot == 53 && clicked.getType() == Material.ARROW) {open_fine_players(player, currentPage1 + 1, currentMenuType1);}
        if (slot == 47 && clicked.getType() == Material.ARROW) {if (currentPage1 > 0) {open_fine_players(player, currentPage1 - 1, currentMenuType1);}}

        // Выбор игрока (головы в слотах 0-44)
        if (slot >= 0 && slot < 45 && clicked.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) clicked.getItemMeta();
            String owner = meta.getOwner();
            if (owner != null) {
                UUID uuid = Bukkit.getOfflinePlayer(owner).getUniqueId();
                myself_class.selectedPlayer = uuid;
                int balance = dataManager.getBalance(myself_class.selectedPlayer);
                if (currentMenuType1.equals("xuy")) {
                    dataManager.printFines(uuid, player);
                    open_fine_players(player, 0, "xuy");
                }
            }
        }
    }
}
