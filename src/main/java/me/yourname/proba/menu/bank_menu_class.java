package me.yourname.proba.menu;

import me.yourname.proba.DataManager;
import me.yourname.proba.GeneralSettings.*;
import me.yourname.proba.language.EU;
import me.yourname.proba.proba;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static me.yourname.proba.GeneralSettings.PlayerChatAmount.amount;
import static me.yourname.proba.GeneralSettings.PlayerChatAmount.pendingInput;
import static me.yourname.proba.GeneralSettings.PlayerChatMess.xuyInput;
import static me.yourname.proba.GeneralSettings.myself_class.myself;

public class bank_menu_class implements Listener {

    public static DataManager dataManager;
    public static proba plugin;

    public static Inventory bank_menu;
    public static Inventory spisok_mmm_menu;
    public static Inventory popolnit_menu;
    public static Inventory snat_menu;
    public static Inventory addAcc_menu;
    private static Inventory currentAccMenu;

    private static Inventory current_Acc_menu(Inventory inv, boolean hasAcc){
        inv.setItem(9, createButt_class.createButt(Material.ARROW, "Назад", " "));

        if (hasAcc){
            inv.setItem(17, createButt_class.createButt(Material.BARRIER, "Нельзя", " "));
            inv.setItem(2, createButt_class.createButt(Material.WRITABLE_BOOK, "Переименовать счёт", " "));
            inv.setItem(4, createButt_class.createButt(Material.BARRIER, "Сделать счёт основным", " "));
            inv.setItem(6, createButt_class.createButt(Material.TNT, "Удалить счёт", " "));
            inv.setItem(20, createButt_class.createButt(Material.PLAYER_HEAD, "Поделиться со счётом с игроком", " "));
            inv.setItem(22, createButt_class.createButt(Material.ROTTEN_FLESH, "Удалить игрока со счёта", " "));
            inv.setItem(24, createButt_class.createButt(Material.BOOK, "Список игроков с доступом ко счёту", " "));
        } else {inv.setItem(17, createButt_class.createButt(Material.SLIME_BALL, "Создать новый счёт", "Нажмите, чтобы создать новый счёт."));}
        return inv;
    }

    // Выдает валюту
    public void addItems(Player player, int amount, ItemStack currencyIt){
        // Защита от некорректных данных
        if (player == null || currencyIt == null || amount <= 0) return;

        // Клонируем предмет, чтобы не изменять оригинальный объект
        ItemStack toGive = currencyIt.clone();
        toGive.setAmount(amount);

        // Пытаемся добавить весь стек в инвентарь
        // addItem возвращает карту с предметами, которые не удалось поместить
        java.util.HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(toGive);

        // Если есть остатки, выбрасываем их на землю
        if (!leftover.isEmpty()) {
            for (ItemStack rest : leftover.values()) {
                if (rest != null && rest.getAmount() > 0) {
                    player.getWorld().dropItem(player.getLocation(), rest);
                }
            }
        }
    }

    // меню
    public static void menus() {
        String[] m = EU.Messages(EU.MessageCategory.MAIN_MENU);
        bank_menu = Bukkit.createInventory(null, 9, Component.text(m[0]));
        spisok_mmm_menu = Bukkit.createInventory(null, 9, Component.text("Список богатых"));
        popolnit_menu = Bukkit.createInventory(null, 9, Component.text("Пополнить баланс"));
        snat_menu = Bukkit.createInventory(null, 9, Component.text("Снять с баланса"));
        addAcc_menu = Bukkit.createInventory(null, 9, Component.text("пепе"));
        // основное меню
        bank_menu.setItem(1, createButt_class.createButt(Material.PAPER, "Список богатых", "Нажмите, чтобы посмотреть топ игроков."));
        bank_menu.setItem(3, createButt_class.createButt(Material.DEEPSLATE_DIAMOND_ORE, "Пополнить баланс", "Нажмите, чтобы пополнить баланс."));
        bank_menu.setItem(5, createButt_class.createButt(Material.DROPPER, "Снять с баланса", "Нажмите, чтобы вывести средства."));
        bank_menu.setItem(7, createButt_class.createButt(Material.ENDER_CHEST, "Открыть счёт", "Нажмите, чтобы открыть банковский счёт."));
        // Список богатых
        spisok_mmm_menu.setItem(0, createButt_class.createButt(Material.ARROW, "Назад", "Вернуться в банкомат."));
        spisok_mmm_menu.setItem(8, createButt_class.createButt(Material.ARROW, "Следующая страница", ""));
        // Пополнить счёт
        popolnit_menu.setItem(0, createButt_class.createButt(Material.ARROW, "Назад", "Вернуться в банкомат."));
        popolnit_menu.setItem(6, createButt_class.createButt(Material.NETHER_STAR, "Выбрать сумму", "Нажмите, чтобы ввести сумму."));
        // Снять средства
        snat_menu.setItem(0, createButt_class.createButt(Material.ARROW, "Назад", "Вернуться в банкомат."));
        snat_menu.setItem(6, createButt_class.createButt(Material.NETHER_STAR, "Выбрать сумму", "Нажмите, чтобы ввести сумму."));
        // Создать аккаунт
        addAcc_menu.setItem(0, createButt_class.createButt(Material.ARROW, "Назад", ""));
    }

    @EventHandler
    public void bank_menu_click(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack xese = event.getCurrentItem();
        String currencyIt = dataManager.getCurrencyItem();
        Material mat = Material.matchMaterial(currencyIt);
        ItemStack currencyStack = new ItemStack(mat);
        if (xese == null) return;

        // Основное меню
        if (event.getInventory().equals(bank_menu)) {
            resetState_class.resetState();
            if (event.getSlot() == 1) {list_richest_menu_class.list_richest_menu(player, 0, "pizda");}
            else if (event.getSlot() == 3) {
                player.openInventory(popolnit_menu);
                myself(player);
            } else if (event.getSlot() == 5) {
                player.openInventory(snat_menu);
                myself(player);
            } else if (event.getSlot() == 7) {
                player.openInventory(addAcc_menu);
                myself(player);
            }
            event.setCancelled(true);
        }
        // Пополнить счёт
        if (event.getInventory().equals(popolnit_menu)) {
            event.setCancelled(true);
            int balance = dataManager.getBalance(myself_class.selectedPlayer);
            updateMenuDisplay_class.updateMenuDisplay(bank_menu_class.popolnit_menu, balance, amount, "popolnit");
            if (event.getSlot() == 0) {player.openInventory(bank_menu);
            } else if (event.getSlot() == 2) {open_vibor_menu_heads_class.open_vibor_menu_heads(player, 0, "po");
            } else if(event.getSlot() == 4){player.sendMessage("Вы нажали кнопку");
            } else if (event.getSlot() == 6) {
                closeMenyMess_class.closeMenyMess(player);
                pendingInput.put(player.getUniqueId(), "popolnit");
            } else if (event.getSlot() == 8){
                if (PlayerChatAmount.amount <= 0) {player.sendMessage("§cСначала введите сумму!1");
                } else {
                    if (!player.getInventory().containsAtLeast(currencyStack, PlayerChatAmount.amount)) {player.sendMessage("Вам нужно " + PlayerChatAmount.amount + " " + currencyStack);
                    } else {
                        dataManager.setBalance(myself_class.selectedPlayer, PlayerChatAmount.amount + balance);
                        removeItems_class.removeItems(player, PlayerChatAmount.amount, currencyStack);
                        player.closeInventory();
                    }
                }
            }
        }
        // Снять средства
        if (event.getInventory().equals(snat_menu)) {
            int balance = dataManager.getBalance(myself_class.selectedPlayer);
            event.setCancelled(true);
            if (event.getSlot() == 0) {player.openInventory(bank_menu);
            } else if (event.getSlot() == 2) {
                if (!(dataManager.getRPmode())){
                    player.sendMessage("рп мод фалзе нельзя ");
                    return;
                }
                open_vibor_menu_heads_class.open_vibor_menu_heads(player, 0, "sna");
            } else if(event.getSlot() == 4){player.sendMessage("Вы нажали кнопку");
            } else if (event.getSlot() == 6) {
                closeMenyMess_class.closeMenyMess(player);
                pendingInput.put(player.getUniqueId(), "snat");
            } else if (event.getSlot() == 8) {
                if (PlayerChatAmount.amount <= 0) {player.sendMessage("§cСначала введите сумму!");
                } else {
                    if (amount > balance){player.sendMessage("недостаточно");
                    } else {
                        dataManager.setBalance(myself_class.selectedPlayer, balance - PlayerChatAmount.amount);
                        addItems(player, PlayerChatAmount.amount, currencyStack);
                        player.closeInventory();
                    }
                }
            }
        }
        // pepe
        if (event.getInventory().equals(addAcc_menu)) {
            event.setCancelled(true);
            if (event.getSlot() == 0) {player.openInventory(bank_menu);
            } else if (event.getSlot() == 2 || event.getSlot() == 4 || event.getSlot() == 6) {
                Inventory accMenu = Bukkit.createInventory(null, 27, Component.text("Управление счётом"));
                boolean hasAcc = dataManager.hasAnyAccount(myself_class.selectedPlayer);
                currentAccMenu = current_Acc_menu(accMenu, hasAcc);
                player.openInventory(currentAccMenu);
            } else if (event.getSlot() == 8) {open_vibor_menu_heads_class.open_vibor_menu_heads(player, 0, "addacc");}
        }
        if (event.getInventory().equals(currentAccMenu)){
            event.setCancelled(true);
            if (event.getSlot() == 9){ player.openInventory(addAcc_menu);
            } else if (event.getSlot() == 2) {
                closeMenyMess_class.closeMenyMess(player);
                //xuyInput.put(player.getUniqueId(), "addacc");
            } else if (event.getSlot() == 4) {

            } else if (event.getSlot() == 6) {

            } else if (event.getSlot() == 20) {

            } else if (event.getSlot() == 22) {

            } else if (event.getSlot() == 24) {

            } else if (event.getSlot() == 17) {
                closeMenyMess_class.closeMenyMess(player);
                xuyInput.put(player.getUniqueId(), "create_new_acc");
                currentAccMenu = current_Acc_menu(event.getInventory(), true);
                player.closeInventory();
                player.openInventory(currentAccMenu);
            }
        }
    }
}