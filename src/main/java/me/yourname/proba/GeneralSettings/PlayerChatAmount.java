package me.yourname.proba.GeneralSettings;

import me.yourname.proba.DataManager;
import me.yourname.proba.menu.bank_menu_class;
import me.yourname.proba.menu.fine_menu_class;
import me.yourname.proba.proba;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.yourname.proba.menu.bankMe_menu_class.*;
import static me.yourname.proba.menu.bank_menu_class.*;
import static me.yourname.proba.menu.fine_menu_class.give_fine_menu;
import static me.yourname.proba.menu.fine_menu_class.remove_fine_menu;

public class PlayerChatAmount implements Listener {

    // Принимает соо игрока
    public static final Map<UUID, String> pendingInput = new HashMap<>();
    public static proba plugin;
    public static DataManager dataManager;
    public static int amount = 0;

    // Писить в чат
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Если игрок не ожидает ввода — игнорируем
        if (!pendingInput.containsKey(uuid)) return;
        event.setCancelled(true);

        String action = pendingInput.get(uuid);
        String message = event.getMessage().trim();
        UUID targetPlayer = myself_class.selectedPlayer;

        // Обработка отмены
        if (message.equalsIgnoreCase("отмена")){
            pendingInput.remove(uuid);
            player.sendMessage("§cДействие отменено.");
            return;
        }

        int enteredAmount;
        try {
            enteredAmount = Integer.parseInt(message);
            if (enteredAmount <= 0) {
                player.sendMessage("§cСумма должна быть положительным числом.");
                return;
            }
        } catch (NumberFormatException e) {
            player.sendMessage("§cВведите целое число.");
            return;
        }

        // Сохраняем введённую сумму в статическую переменную
        amount = enteredAmount;
        String playerName = Bukkit.getOfflinePlayer(targetPlayer).getName();
        int balance = dataManager.getBalance(targetPlayer);
        int myBalance = dataManager.getBalance(player.getUniqueId());
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (action.equals("popolnit")) {
                updateMenuDisplay_class.updateMenuDisplay(bank_menu_class.popolnit_menu, balance, amount, "popolnit");
                player.openInventory(popolnit_menu);
                player.sendMessage("Вы вписали: " + amount);
                System.out.println("Выбор на сколько пополнить: " + amount + " Баланс игрока " + playerName + " " + balance);
            } else if (action.equals("snat")) {
                updateMenuDisplay_class.updateMenuDisplay(bank_menu_class.snat_menu, balance, amount, "snat");
                player.openInventory(snat_menu);
                player.sendMessage("Вы вписали: " + amount);
            } else if (action.equals("pere")){
                updateMenuDisplay_class.updateMenuDisplay(perevod_menu, myBalance , amount, "perevod");
                player.openInventory(perevod_menu);
                player.sendMessage("Вы вписали: " + amount);
            } else if (action.equals("pojer")){
                updateMenuDisplay_class.updateMenuDisplay(perevod_casna_menu, myBalance , amount, "snat");
                player.openInventory(perevod_casna_menu);
                player.sendMessage("Вы вписали: " + amount);
            } else if (action.equals("give_fine")){
                updateMenuDisplay_class.updateMenuDisplay(fine_menu_class.give_fine_menu, balance, amount, "give_menu");
                player.openInventory(give_fine_menu);
                player.sendMessage("Вы вписали: " + amount);
            } else if (action.equals("remove_fine")){

                player.openInventory(remove_fine_menu);
                player.sendMessage("Вы вписали: " + amount);

            }
            pendingInput.remove(uuid);
        });
    }
}
