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
import org.bukkit.inventory.Inventory;

import java.util.UUID;

import static me.yourname.proba.GeneralSettings.PlayerChatAmount.amount;
import static me.yourname.proba.GeneralSettings.PlayerChatAmount.pendingInput;
import static me.yourname.proba.GeneralSettings.myself_class.myself;
import static me.yourname.proba.GeneralSettings.myself_class.selectedPlayer;

public class bankMe_menu_class implements Listener {

    public static DataManager dataManager;
    public static proba plugin;

    public static Inventory bankMe_menu = Bukkit.createInventory(null, 9, Component.text("Личный кабинет (1 стр)"));
    public static Inventory perevod_menu = Bukkit.createInventory(null, 9, Component.text("Меню для переводов"));
    public static Inventory perevod_casna_menu = Bukkit.createInventory(null, 9, Component.text("Пожертвовать в казну"));
    public static Inventory next_menu = Bukkit.createInventory(null, 9, Component.text("Личный кабинет (2 стр)"));
    public static Inventory history_pere_menu = Bukkit.createInventory(null, 9, Component.text("3"));
    public static Inventory history_all_menu = Bukkit.createInventory(null, 9, Component.text("4"));

    //static String treasuryAccount = dataManager.getTreasuryAccount();

    static {
        // Личный кабинет меню
        bankMe_menu.setItem(1, createButt_class.createButt(Material.EMERALD, "Ваш баланс", "Нажмите чтобы посмотреть"));
        bankMe_menu.setItem(3, createButt_class.createButt(Material.NETHERITE_INGOT, "Перевести средства", ""));
        bankMe_menu.setItem(5, createButt_class.createButt(Material.GOLD_INGOT, "Пожертвовать в казну", ""));
        bankMe_menu.setItem(7, createButt_class.createButt(Material.ARROW, "Следующая страница", ""));
        // перевод меню
        perevod_menu.setItem(0, createButt_class.createButt(Material.ARROW, "Назад", ""));
        perevod_menu.setItem(6, createButt_class.createButt(Material.NETHER_STAR, "Сколько отправить", "Нажмите чтобы выбрать сумму"));
        // перевод в казну меню
        perevod_casna_menu.setItem(0, createButt_class.createButt(Material.ARROW, "Назад", ""));
        perevod_casna_menu.setItem(2, createButt_class.createButt(Material.PLAYER_HEAD, "Отправить ___", "Получатель: " + null));
        //perevod_casna_menu.setItem(4, createButt_class.createButt(Material.GOLD_INGOT, "Ваш баланс ___", ""));
        perevod_casna_menu.setItem(6, createButt_class.createButt(Material.NETHER_STAR, "Сколько отправить", "Нажмите чтобы выбрать"));
        //perevod_casna_menu.setItem(8, createButt_class.createButt(Material.SLIME_BALL, "Отправить ___", ""));
        // 2 страница меню
        next_menu.setItem(1, createButt_class.createButt(Material.ARROW, "Назад (1 стр)", ""));
        next_menu.setItem(3, createButt_class.createButt(Material.PAPER, "История переводов", ""));
        next_menu.setItem(5, createButt_class.createButt(Material.NAME_TAG, "История штрафов", ""));
    }

    @EventHandler
    public void bankMe_menu_click(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        String treasuryAccount = dataManager.getTreasuryAccount();

        // личный кабинет меню
        if (event.getInventory().equals(bankMe_menu)){
            resetState_class.resetState();
            if (event.getCurrentItem() != null){
                if (event.getSlot() == 1){
                    int balance = dataManager.getBalance(uuid, 0);
                    player.sendMessage("Ваш баланс " + balance);
                    player.closeInventory();
                }
                else if(event.getSlot() == 3){
                    myself(player);
                    player.openInventory(perevod_menu);
                }
                else if(event.getSlot() == 5){
                    if (treasuryAccount == null || treasuryAccount.trim().isEmpty()){
                        player.closeInventory();
                        player.sendMessage("Президент не выбран");
                    } else {
                        myself(player);
                        player.openInventory(perevod_casna_menu);
                    }
                }
                else if(event.getSlot() == 7){player.openInventory(next_menu);}
                event.setCancelled(true);
            }
        }
        // перевод меню
        if (event.getInventory().equals(perevod_menu)){
            event.setCancelled(true);
            int balance = dataManager.getBalance(myself_class.selectedPlayer, 0);
            if (event.getCurrentItem() != null){
                if (event.getSlot() == 0){player.openInventory(bankMe_menu);}
                else if(event.getSlot() == 2){open_vibor_menu_heads_class.open_vibor_menu_heads(player, 0, "pere");}
                else if(event.getSlot() == 4){player.sendMessage("Вы нажали кнопку");}
                else if(event.getSlot() == 6){
                    closeMenyMess_class.closeMenyMess(player);
                    pendingInput.put(player.getUniqueId(), "pere");
                }
                else if(event.getSlot() == 8){
                    int myBalance = dataManager.getBalance(player.getUniqueId(), 0);
                    if (selectedPlayer == uuid || amount == 0){player.sendMessage("себя нельзя выбрать или не выбрали сумму");
                    } else {
                        if (amount > myBalance){player.sendMessage("недостаточно");
                        } else {
                            dataManager.setBalance(myself_class.selectedPlayer, balance + PlayerChatAmount.amount, 0);
                            dataManager.setBalance(player.getUniqueId(), myBalance - PlayerChatAmount.amount, 0);
                            player.sendMessage("всё гуд");
                            player.closeInventory();
                        }
                    }
                }
            }
        }
        // перевод в казну меню
        if (event.getInventory().equals(perevod_casna_menu)){
            event.setCancelled(true);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(treasuryAccount);
            UUID uuidTreasury = offlinePlayer.getUniqueId();
            int balanceTreasury = dataManager.getBalance(uuidTreasury, 0);
            if (event.getCurrentItem() != null){
                if (event.getSlot() == 0){player.openInventory(bankMe_menu);
                } else if(event.getSlot() == 4){player.sendMessage("Вы нажали кнопку");
                } else if(event.getSlot() == 6){
                    closeMenyMess_class.closeMenyMess(player);
                    pendingInput.put(player.getUniqueId(), "pojer");
                } else if(event.getSlot() == 8){
                    int myBalance = dataManager.getBalance(player.getUniqueId(), 0);
                    if (amount == 0){player.sendMessage("не выбрали сумму");
                    } else {
                        if (amount > myBalance){player.sendMessage("недостаточно");
                        } else {
                            dataManager.setBalance(player.getUniqueId(), myBalance - PlayerChatAmount.amount, 0);
                            dataManager.setBalance(uuidTreasury, balanceTreasury + PlayerChatAmount.amount, 0);
                            player.sendMessage("всё гуд");
                            player.closeInventory();
                        }
                    }
                }
            }
        }
        // 2 страница меню
        if (event.getInventory().equals(next_menu)){
            event.setCancelled(true);
            if (event.getCurrentItem() != null){
                if (event.getSlot() == 1){player.openInventory(bankMe_menu);}
                else if(event.getSlot() == 3){player.openInventory(history_pere_menu);
                } else if(event.getSlot() == 5){player.openInventory(history_all_menu);}
            }
        }
    }
}
