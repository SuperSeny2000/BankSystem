package me.yourname.proba.menu;

import me.yourname.proba.DataManager;
import me.yourname.proba.GeneralSettings.*;
import me.yourname.proba.proba;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static me.yourname.proba.GeneralSettings.PlayerChatAmount.amount;
import static me.yourname.proba.GeneralSettings.PlayerChatAmount.pendingInput;
import static me.yourname.proba.GeneralSettings.PlayerChatMess.xuyInput;
import static me.yourname.proba.GeneralSettings.myself_class.selectedPlayer;
import static me.yourname.proba.GeneralSettings.myself_class.selectedPlayerForFine;

public class fine_menu_class implements Listener {

    public static DataManager dataManager;
    public static proba plugin;

    public static Inventory fine_menu = Bukkit.createInventory(null, 9, Component.text("Штраф меню"));
    public static Inventory give_fine_menu = Bukkit.createInventory(null, 9, Component.text("Выдать штраф"));
    public static Inventory remove_fine_menu = Bukkit.createInventory(null, 27, Component.text("Снять штраф"));
    public static Inventory spisok_fine_menu = Bukkit.createInventory(null, 54, Component.text("123"));

    public static boolean nal_bezNal = true;

    public boolean nal_bezNalMet (boolean status){
        if (status == true){status = false;
        } else {status = true;}
        return status;
    }

    static {
        // штраф меню
        fine_menu.setItem(2, createButt_class.createButt(Material.LECTERN, "Выдать штраф", ""));
        fine_menu.setItem(4, createButt_class.createButt(Material.STONECUTTER, "Снять штраф", ""));
        fine_menu.setItem(6, createButt_class.createButt(Material.PAPER, "Список должников", ""));
        // выдать штраф меню
        give_fine_menu.setItem(0, createButt_class.createButt(Material.ARROW, "Назад", ""));
        give_fine_menu.setItem(4, createButt_class.createButt(Material.WRITABLE_BOOK, "Причина штрафа", ""));
        give_fine_menu.setItem(6, createButt_class.createButt(Material.NETHER_STAR, "Выбрать сумму", ""));
        // снять штраф меню
        remove_fine_menu.setItem(18, createButt_class.createButt(Material.ARROW, "Назад", ""));
        remove_fine_menu.setItem(17, createButt_class.createButt(Material.BARRIER, "Оплатить штраф ___", ""));
        // списов должников
        spisok_fine_menu.setItem(45, createButt_class.createButt(Material.ARROW, "Следующая страница", ""));
        spisok_fine_menu.setItem(49, createButt_class.createButt(Material.BARRIER, "Назад", ""));
        spisok_fine_menu.setItem(53, createButt_class.createButt(Material.ARROW, "Следующая страница", ""));
    }

    @EventHandler
    public void fine_menu_click(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String currencyIt = dataManager.getCurrencyItem();
        Material mat = Material.matchMaterial(currencyIt);
        ItemStack currencyStack = new ItemStack(mat);

        // Основное меню
        if (event.getInventory().equals(fine_menu)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                resetState_class.resetState();
                if (event.getSlot() == 2) {
                    myself_class.myself(player);
                    if (dataManager.getRPmode()){
                        List<String> allowed = dataManager.getFineAllowedPlayers();
                        if (!allowed.contains(player.getName())) {
                            player.sendMessage("§cУ вас нет прав для выдачи штрафов.");
                            return;
                        }
                    }
                    player.openInventory(fine_menu_class.give_fine_menu);
                } else if (event.getSlot() == 4) {
                    String treasuryName = dataManager.getTreasuryAccount();
                    if (treasuryName == null || treasuryName.trim().isEmpty()) {player.sendMessage("§cКазна не установлена! Использую вас как казну.");}
                    myself_class.myself(player);
                    player.openInventory(fine_menu_class.remove_fine_menu);
                } else if (event.getSlot() == 6) {
                    myself_class.myself(player);
                    open_fine_players_menu.open_fine_players(player, 0, "xuy");
                }
            }
        }
        // выдать штраф меню
        if (event.getInventory().equals(give_fine_menu)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                if (event.getSlot() == 0) {player.openInventory(fine_menu_class.fine_menu);
                } else if (event.getSlot() == 2) {open_vibor_menu_heads_class.open_vibor_menu_heads(player, 0, "give_fine");
                } else if (event.getSlot() == 4) {
                    closeMenyMess_class.closeMenyMess(player);
                    xuyInput.put(player.getUniqueId(), "give_fine_mess");
                } else if (event.getSlot() == 6) {
                    closeMenyMess_class.closeMenyMess(player);
                    pendingInput.put(player.getUniqueId(), "give_fine");
                }
                else if (event.getSlot() == 8) {
                    if (amount <= 0){player.sendMessage("§cСначала введите сумму!1");
                    } else {
                        if (PlayerChatMess.message1 == null) PlayerChatMess.message1 = "Причина не указана";
                        dataManager.addFinePlayer(myself_class.selectedPlayer, PlayerChatMess.message1, amount);
                        player.sendMessage("всё гуд");
                        player.closeInventory();
                    }
                }
            }
        }
        // снять штраф меню
        if (event.getInventory().equals(remove_fine_menu)) {
            event.setCancelled(true);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(selectedPlayerForFine);
            UUID uuidTreasury = offlinePlayer.getUniqueId();
            int balanceTreasury = dataManager.getBalance(uuidTreasury, 0);
            if (event.getCurrentItem() != null) {
                int balance = dataManager.getBalance(myself_class.selectedPlayer, 0);
                if (event.getSlot() == 0) {open_vibor_menu_heads_class.open_vibor_menu_heads(player, 0, "remove_fine");
                } else if (event.getSlot() == 18) {player.openInventory(fine_menu_class.fine_menu);
                } else if (event.getSlot() == 11) {open_vibor_menu_heads_class.open_vibor_menu_heads(player, 0, "remove_fine_perevesti");
                } else if (event.getSlot() == 13) {
                    nal_bezNal = nal_bezNalMet(nal_bezNal);
                    updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.remove_fine_menu, balance, amount, "remove_menu");
                    player.openInventory(remove_fine_menu);
                } else if (event.getSlot() == 15) {
                    player.sendMessage("§eВведите номер штрафа из списка (или 'отмена'):");
                    player.closeInventory();
                    dataManager.printFines(myself_class.selectedPlayer, player);
                    PlayerChatAmount.pendingInput.put(player.getUniqueId(), "select_fine");
                } else if (event.getSlot() == 17) {
                    if (amount <= 0) {
                        player.sendMessage("§cСначала выберите штраф!");
                        return;
                    }
                    if (nal_bezNal){
                        removeItems_class.removeItems(player, amount, currencyStack);
                        dataManager.setBalance(uuidTreasury, balanceTreasury + amount, 0);
                    } else {
                        dataManager.setBalance(uuidTreasury, balanceTreasury + amount, 0);
                        dataManager.setBalance(player.getUniqueId(), balance - amount, 0);
                    }
                    dataManager.removeFinePlayer(selectedPlayer, PlayerChatAmount.fineIndex);
                    player.closeInventory();
                }
            }
        }
    }
}