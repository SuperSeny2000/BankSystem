package me.yourname.proba.commands;

import me.yourname.proba.DataManager;
import me.yourname.proba.menu.bankMe_menu_class;
import me.yourname.proba.proba;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class bank_help implements CommandExecutor, TabCompleter {

    public static proba plugin;
    public static DataManager dataManager;

    private void help(@NotNull CommandSender commandSender) {
        commandSender.sendMessage("§6=== Помощь по командам банка ===");
        commandSender.sendMessage("§6Доступные команды:");
        commandSender.sendMessage("§9/bank - Открыть банковское меню.");
        commandSender.sendMessage("§9/bank help - Показать это сообщение.");
        commandSender.sendMessage("§9/bank status - Просмотр статуса.");}

    private void nelsa(String[] xuy, CommandSender sender) {if (xuy.length < 2) {sender.sendMessage("коротко");}}

    private boolean proverkaMaterial(String inputMaterial){
        Material material = Material.matchMaterial(inputMaterial);
        return material != null;
    }
    private void proverkaPlayer(String name, String deistvie, CommandSender player){
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        if (offlinePlayer.hasPlayedBefore() && deistvie.equals("TreasuryAccount")) {
            dataManager.setTreasuryAccount(name);
            player.sendMessage("Теперь игрок " + name + " президент.");
        } else if (offlinePlayer.hasPlayedBefore() && deistvie.equals("add")) {
            dataManager.setAllowedPlayers(name);
            player.sendMessage("Теперь игрок " + name + " банкир.");
        } else if (offlinePlayer.hasPlayedBefore() && deistvie.equals("addfine")) {
            dataManager.setFineAllowedPlayers(name);
            player.sendMessage("Теперь игрок " + name + " штраф.");
        } else {player.sendMessage("Игрок не найден");}
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            if (commandSender instanceof Player player) {player.openInventory(bankMe_menu_class.bankMe_menu);
            } else {commandSender.sendMessage("Эта команда только для игроков.");
            } return true;
        }
        String sub = strings[0].toLowerCase();
        if (commandSender.hasPermission("bankplugin.op")) {
            switch (sub) {
                case "help":
                    help(commandSender);
                    commandSender.sendMessage("§6Команды администратора:");
                    commandSender.sendMessage("§9/bank add <игрок> - Добавить игрока в список разрешенных.");
                    commandSender.sendMessage("§9/bank remove <игрок> - Удалить игрока из списка разрешенных.");
                    commandSender.sendMessage("§9/bank addfine <игрок> - Добавить игрока в список штрафного терминала.");
                    commandSender.sendMessage("§9/bank removefine <игрок> - Удалить игрока из списка штрафного терминала.");
                    commandSender.sendMessage("§9/bank list - Показать все списки доступа.");
                    commandSender.sendMessage("§9/bank rpmode <on|off> - Управление режимом RP.");
                    commandSender.sendMessage("§9/bank reload - Перезапустить плагин.");
                    commandSender.sendMessage("§6================================");
                    return true;
                case "rpmode":
                    nelsa(strings, commandSender);
                    if (strings[1].equals("true")){
                        dataManager.setRpMode(true);
                        commandSender.sendMessage("true");}
                    else if(strings[1].equals("false")){
                        dataManager.setRpMode(false);
                        commandSender.sendMessage("false");}
                    else{commandSender.sendMessage("Не получилось");}
                    return true;
                case "add":
                    nelsa(strings, commandSender);
                    proverkaPlayer(strings[1], "add", commandSender);

                    return true;
                case "remove":
                    nelsa(strings, commandSender);
                    dataManager.removeAllowedPlayers(strings[1]);
                    commandSender.sendMessage("Игрок " + strings[1] +" уволен.");
                    return true;
                case "addfine":
                    nelsa(strings, commandSender);
                    proverkaPlayer(strings[1], "addfine", commandSender);
                    return true;
                case "removefine":
                    nelsa(strings, commandSender);
                    dataManager.removeFineAllowedPlayers(strings[1]);
                    commandSender.sendMessage("Игрок " + strings[1] + " уволен.");
                    return true;
                case "reload":
                    dataManager.saveAll();
                    plugin.reloadConfig();
                    commandSender.sendMessage("Плагин перезапущен.");
                    return true;
                case "status":
                    boolean RPmode = dataManager.getRPmode();
                    commandSender.sendMessage("RP-MODE: " + RPmode);
                    return true;
                case "list":
                    String treasury = dataManager.getTreasuryAccount();
                    List<String> list1 = dataManager.getAllowedPlayers();
                    List<String>  list2 = dataManager.getFineAllowedPlayers();
                    commandSender.sendMessage( "Президент " + treasury + "\nСписок банкиров: " + list1 + "\n Список судей: " + list2);
                    return true;
                case "addcurrencyitem":
                    nelsa(strings, commandSender);
                    if (proverkaMaterial(strings[1])){
                        dataManager.setCurrencyItem(strings[1]);
                        commandSender.sendMessage("Теперь " + strings[1] + " " + "валюта плагина.");
                    } else {commandSender.sendMessage("Предмет " + strings[1] + " не найден.");}
                    return true;
                case"removecurrencyitem":
                    nelsa(strings, commandSender);



                    return true;
                case "addtreasury":
                    nelsa(strings, commandSender);
                    proverkaPlayer(strings[1], "TreasuryAccount", commandSender);
                    return true;
                case "sss":
                    Player player1 = (Player) commandSender;
                    //dataManager.printFines(player1.getUniqueId());
                    //dataManager.removeFinePlayer(player1.getUniqueId());




                    return true;
                default:
                    commandSender.sendMessage("хз чё за команда");
                    return true;
            }
        }

        if (commandSender.hasPermission("bankplugin.use")) {
            switch (sub) {
                case "help":
                    help(commandSender);
                    return true;
                case "status":
                    boolean RPmode = dataManager.getRPmode();
                    commandSender.sendMessage("RPmode: " + RPmode);
                    return true;
                case "list":
                    commandSender.sendMessage("текс8");
                    return true;
                default:
                    commandSender.sendMessage("хз чё за команда");
                    return true;
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        String firstWord = strings[0];
        if (strings.length == 1) {
            if (commandSender.hasPermission("bankplugin.op")) {
                return List.of("add", "remove", "addfine", "removefine", "rpmode", "reload", "help", "status", "list", "addtreasury", "addcurrencyitem", "removecurrencyitem");
            }
            if (commandSender.hasPermission("bankplugin.use")) {
                return List.of("help", "status", "list");
            }
        }
        else if(strings.length == 2 && firstWord.equals("rpmode")){
            if (commandSender.hasPermission("bankplugin.op")) {
                return List.of("true", "false");
            }
        }
        else if (strings.length == 2 && (firstWord.equals("addcurrencyitem") || firstWord.equals("removecurrencyitem"))){
            if (commandSender.hasPermission("bankplugin.op")) {
                return List.of("добавить или удалить");
            }
        }
        else if (strings.length == 2){
            if (commandSender.hasPermission("bankplugin.op")) {
                return List.of("Ник игрока");
            }
        }
        return List.of();
    }
}