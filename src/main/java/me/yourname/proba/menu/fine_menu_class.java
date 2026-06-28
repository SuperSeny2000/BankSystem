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

import java.util.UUID;

import static me.yourname.proba.GeneralSettings.PlayerChatAmount.amount;
import static me.yourname.proba.GeneralSettings.PlayerChatAmount.pendingInput;
import static me.yourname.proba.GeneralSettings.PlayerChatMess.xuyInput;
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
        //remove_fine_menu.setItem(0, createButt_class.createButt(Material.PLAYER_HEAD, "Выбрать игрока", "Текущий выбор: себе"));
        remove_fine_menu.setItem(18, createButt_class.createButt(Material.ARROW, "Назад", ""));
        //remove_fine_menu.setItem(11, createButt_class.createButt(Material.CREEPER_HEAD, "Перевести валюту игроку", "Нажмите, чтобы выбрать игрока" + "\nдля перевода валюты после снятия штрафа" + "\nТекущий выбор: " + null));
        //remove_fine_menu.setItem(13, createButt_class.createButt(Material.END_CRYSTAL, "Способ оплаты", "Выбор: Наличными"));
        remove_fine_menu.setItem(15, createButt_class.createButt(Material.NAME_TAG, "Выбрать штраф", ""));
        remove_fine_menu.setItem(17, createButt_class.createButt(Material.BARRIER, "Оплатить штраф ___", ""));
        // списов должников
        spisok_fine_menu.setItem(45, createButt_class.createButt(Material.ARROW, "Следующая страница", ""));
        spisok_fine_menu.setItem(49, createButt_class.createButt(Material.BARRIER, "Назад", ""));
        spisok_fine_menu.setItem(53, createButt_class.createButt(Material.ARROW, "Следующая страница", ""));
    }

    @EventHandler
    // Основное меню
    public void fine_menu_click(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String currencyIt = dataManager.getCurrencyItem();
        Material mat = Material.matchMaterial(currencyIt);
        ItemStack currencyStack = new ItemStack(mat);
        //String treasuryAccount = dataManager.getTreasuryAccount();
        /*
        if (treasuryAccount == null || treasuryAccount.trim().isEmpty()) {
            player.sendMessage("§cКазна не установлена! Обратитесь к администратору.");
            event.setCancelled(true);
            return; // Прерываем обработку, чтобы избежать ошибки
        }

         */

        // Основное меню
        if (event.getInventory().equals(fine_menu)) {
            if (event.getCurrentItem() != null) {
                resetState_class.resetState();
                if (event.getSlot() == 2) {
                    myself_class.myself(player);
                    player.openInventory(fine_menu_class.give_fine_menu);
                } else if (event.getSlot() == 4) {
                    myself_class.myself(player);
                    player.openInventory(fine_menu_class.remove_fine_menu);
                } else if (event.getSlot() == 6) {
                    myself_class.myself(player);
                    player.openInventory(fine_menu_class.spisok_fine_menu);
                }
                event.setCancelled(true);
            }
        }
        // выдать штраф меню
        if (event.getInventory().equals(give_fine_menu)) {
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);
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
            int balanceTreasury = dataManager.getBalance(uuidTreasury);
            if (event.getCurrentItem() != null) {
                int balance = dataManager.getBalance(myself_class.selectedPlayer);
                if (event.getSlot() == 0) {open_vibor_menu_heads_class.open_vibor_menu_heads(player, 0, "remove_fine");
                } else if (event.getSlot() == 18) {player.openInventory(fine_menu_class.fine_menu);
                } else if (event.getSlot() == 11) {open_vibor_menu_heads_class.open_vibor_menu_heads(player, 0, "remove_fine_perevesti");
                } else if (event.getSlot() == 13) {
                    nal_bezNal = nal_bezNalMet(nal_bezNal);
                    updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.remove_fine_menu, balance, amount, "remove_menu");
                    player.openInventory(remove_fine_menu);
                } else if (event.getSlot() == 15) {
                    player.sendMessage("Вы нажали кнопку");




                } else if (event.getSlot() == 17) {
                    if (nal_bezNal){
                        removeItems_class.removeItems(player, 5555, currencyStack);
                        dataManager.setBalance(uuidTreasury, balanceTreasury + 5555);
                    } else {
                        dataManager.setBalance(uuidTreasury, balanceTreasury + 555555);
                        dataManager.setBalance(player.getUniqueId(), balance - 555555);
                    }
                }
            }
        }

        // список должников
        if (event.getInventory().equals(spisok_fine_menu)) {
            if (event.getCurrentItem() != null) {
                if (event.getSlot() == 45) {player.sendMessage("Вы нажали кнопку");}
                else if (event.getSlot() == 49) {player.openInventory(fine_menu_class.fine_menu);}
                else if (event.getSlot() == 53) {player.sendMessage("Вы нажали кнопку");}
                event.setCancelled(true);
            }
        }
    }
}