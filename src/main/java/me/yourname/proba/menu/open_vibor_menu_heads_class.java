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

import static me.yourname.proba.GeneralSettings.PlayerChatAmount.amount;
import static me.yourname.proba.menu.bankMe_menu_class.perevod_menu;
import static me.yourname.proba.menu.bank_menu_class.*;
import static me.yourname.proba.menu.fine_menu_class.give_fine_menu;
import static me.yourname.proba.menu.fine_menu_class.remove_fine_menu;

public class open_vibor_menu_heads_class implements Listener {

    public static DataManager dataManager;

    public static int currentPage = 0;
    private static String currentMenuType = "po";

    // очищает и ставит головы
    private static void vibor_menu_heads(Inventory inv, List<String> playersOnPage) {
        for (int i = 0; i < 45; i++) {inv.setItem(i, null);}
        for (int i = 0; i < playersOnPage.size() && i < 45; i++) {inv.setItem(i, createButt_class.createButt(Material.PLAYER_HEAD, playersOnPage.get(i), "Онлине??"));}
    }

    // открывает и настраивает меню выбора игрока
    public static void open_vibor_menu_heads(Player player, int page, String menuType) {
        // Используем общие статические поля из bank_menu_class
        currentMenuType = menuType;
        currentPage = page;

        String title = "Выбор игрока";
        Inventory inv = Bukkit.createInventory(null, 54, Component.text(title));

        List<String> allPlayers = dataManager.getAllPlayers();
        int totalPages = (int) Math.ceil(allPlayers.size() / 45.0);
        int start = page * 45;
        int end = Math.min(start + 45, allPlayers.size());
        List<String> pagePlayers = allPlayers.subList(start, end);

        vibor_menu_heads(inv, pagePlayers);

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
        if (!event.getView().getTitle().equals("Выбор игрока")) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;

        int slot = event.getSlot();

        // Кнопка "Назад" (слот 49)
        if (slot == 49) {
            if (currentMenuType.equals("po")) {player.openInventory(popolnit_menu);
            } else  if(currentMenuType.equals("sna")){player.openInventory(snat_menu);
            } else if (currentMenuType.equals("pere")){player.openInventory(perevod_menu);
            } else if (currentMenuType.equals("give_fine")){player.openInventory(give_fine_menu);
            } else if (currentMenuType.equals("remove_fine")){player.openInventory(remove_fine_menu);
            }else if (currentMenuType.equals("remove_fine_perevesti")) {player.openInventory(remove_fine_menu);
            }
        }

        // Кнопки навигации
        if (slot == 53 && clicked.getType() == Material.ARROW) {open_vibor_menu_heads(player, currentPage + 1, currentMenuType);}
        if (slot == 47 && clicked.getType() == Material.ARROW) {
            if (currentPage > 0) {open_vibor_menu_heads(player, currentPage - 1, currentMenuType);}}

        // Выбор игрока (головы в слотах 0-44)
        if (slot >= 0 && slot < 45 && clicked.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) clicked.getItemMeta();
            String owner = meta.getOwner();
            if (owner != null) {
                UUID uuid = Bukkit.getOfflinePlayer(owner).getUniqueId();
                myself_class.selectedPlayer = uuid;
                int balance = dataManager.getBalance(myself_class.selectedPlayer, 0);
                if (currentMenuType.equals("po")) {
                    updateMenuDisplay_class.updateMenuDisplay(bank_menu_class.popolnit_menu, balance, amount, "popolnit");
                    player.openInventory(popolnit_menu);
                } else if (currentMenuType.equals("sna")){
                    updateMenuDisplay_class.updateMenuDisplay(bank_menu_class.snat_menu, balance, amount, "snat");
                    player.openInventory(snat_menu);
                } else if (currentMenuType.equals("pere")){
                    int senderBalance = dataManager.getBalance(player.getUniqueId(), 0);
                    updateMenuDisplay_class.updateMenuDisplay(perevod_menu, senderBalance, amount, "snat");
                    player.openInventory(perevod_menu);
                } else if (currentMenuType.equals("give_fine")){
                    updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.give_fine_menu, balance, amount, "give_menu");
                    player.openInventory(give_fine_menu);
                } else if (currentMenuType.equals("remove_fine")){
                    updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.remove_fine_menu, balance, amount, "remove_menu");
                    player.openInventory(remove_fine_menu);
                } else if (currentMenuType.equals("remove_fine_perevesti")) {
                    myself_class.selectedPlayerForFine = uuid;
                    updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.remove_fine_menu, balance, amount, "remove_menu");
                    player.openInventory(remove_fine_menu);
                } else if (currentMenuType.equals("addacc")){
                    myself_class.selectedPlayerForFine = uuid;
                    updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.remove_fine_menu, balance, amount, "remove_menu");
                    player.openInventory(addAcc_menu);
                }
            }
        }
    }
}
