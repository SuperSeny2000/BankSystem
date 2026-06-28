package me.yourname.proba.menu;

import me.yourname.proba.DataManager;
import me.yourname.proba.GeneralSettings.*;
import me.yourname.proba.language.EU;
import me.yourname.proba.proba;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static me.yourname.proba.GeneralSettings.PlayerChatAmount.amount;
import static me.yourname.proba.GeneralSettings.PlayerChatAmount.pendingInput;
import static me.yourname.proba.GeneralSettings.myself_class.myself;

public class bank_menu_class implements Listener {

    public static DataManager dataManager;
    public static proba plugin;

    public static Inventory bank_menu;
    public static Inventory spisok_mmm_menu;
    public static Inventory popolnit_menu;
    public static Inventory snat_menu;
    public static Inventory addAcc_menu;

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
        addAcc_menu.setItem(2, createButt_class.createButt(Material.EMERALD_BLOCK, "Открыть счёт №1", ""));
        addAcc_menu.setItem(4, createButt_class.createButt(Material.REDSTONE_BLOCK, "Открыть счёт №2", ""));
        addAcc_menu.setItem(6, createButt_class.createButt(Material.GOLD_BLOCK, "Открыть счёт №3", ""));
        addAcc_menu.setItem(8, createButt_class.createButt(Material.PLAYER_HEAD, "Выбрать игрока", ""));
    }

    @EventHandler
    public void bank_menu_click(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack xese = event.getCurrentItem();
        String currencyIt = dataManager.getCurrencyItem();
        Material mat = Material.matchMaterial(currencyIt);
        ItemStack currencyStack = new ItemStack(mat);
        if (xese == null) return;

        // Основное меню
        if (inv.equals(bank_menu)) {
            if (event.getCurrentItem() != null) {
                resetState_class.resetState();
                if (event.getSlot() == 1) player.openInventory(spisok_mmm_menu);
                else if (event.getSlot() == 3) {
                    player.openInventory(popolnit_menu);
                    myself(player);
                } else if (event.getSlot() == 5) {
                    player.openInventory(snat_menu);
                    myself(player);
                } else if (event.getSlot() == 7) player.openInventory(addAcc_menu);
                event.setCancelled(true);
            }
        }
        // Список богатых
        if (inv.equals(spisok_mmm_menu)) {
            if (event.getSlot() == 0) player.openInventory(bank_menu);
            else if (event.getSlot() == 8) player.sendMessage("Вы нажали кнопку");
            event.setCancelled(true);
        }
        // Пополнить счёт
        if (inv.equals(popolnit_menu)) {
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
        if (inv.equals(snat_menu)) {
            int balance = dataManager.getBalance(myself_class.selectedPlayer);
            event.setCancelled(true);
            if (event.getSlot() == 0) {
                player.openInventory(bank_menu);
            } else if (event.getSlot() == 2) {open_vibor_menu_heads_class.open_vibor_menu_heads(player, 0, "sna");
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
        if (inv.equals(addAcc_menu)) {
            if (event.getSlot() == 0) player.openInventory(bank_menu);
            event.setCancelled(true);
        }
    }
}