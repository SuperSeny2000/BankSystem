package me.yourname.proba;

import me.yourname.proba.GeneralSettings.PlayerChatMess;
import me.yourname.proba.GeneralSettings.myself_class;
import me.yourname.proba.commands.*;
import me.yourname.proba.GeneralSettings.PlayerChatAmount;
import me.yourname.proba.menu.open_vibor_menu_heads_class;
import me.yourname.proba.language.EU;
import me.yourname.proba.menu.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class proba extends JavaPlugin {

    private DataManager dataManager;

    @Override
    public void onEnable() {
        dataManager = new DataManager(this);
        dataManager.saveAll();

        bank_help.plugin = this;
        bank_menu_class.plugin = this;
        bankMe_menu_class.plugin = this;
        fine_menu_class.plugin = this;
        PlayerChatAmount.plugin = this;
        PlayerChatMess.plugin = this;
        getbankblock.plugin = this;
        getfineblock.plugin = this;

        getbankblock.dataManager = dataManager;
        getfineblock.dataManager = dataManager;
        EU.dataManager = dataManager;
        bank_menu_class.menus();
        bankMe_menu_class.dataManager = dataManager;
        bank_menu_class.dataManager = dataManager;
        bank_help.dataManager = dataManager;
        open_vibor_menu_heads_class.dataManager = dataManager;
        PlayerChatAmount.dataManager = dataManager;
        PlayerChatMess.dataManager = dataManager;
        fine_menu_class.dataManager = dataManager;
        myself_class.dataManager = dataManager;

        getCommand("bank").setExecutor(new bank_help());
        getCommand("avito").setExecutor(new avito());
        getCommand("getbankblock").setExecutor(new getbankblock());
        getCommand("getfineblock").setExecutor(new getfineblock());
        getCommand("createpricelist").setExecutor(new createpricelist());

        Bukkit.getPluginManager().registerEvents(dataManager, this);
        Bukkit.getPluginManager().registerEvents(new bankMe_menu_class(), this);
        Bukkit.getPluginManager().registerEvents(new bank_menu_class(), this);
        Bukkit.getPluginManager().registerEvents(new fine_menu_class(), this);
        Bukkit.getPluginManager().registerEvents(new open_vibor_menu_heads_class(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatAmount(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatMess(), this);

        String currencyIt = dataManager.getCurrencyItem();
        if (currencyIt == null) {
            getLogger().warning("§cОшибка: Валюта не настроена!!!");
        }
        String treasuryAccount = dataManager.getTreasuryAccount();
        if (treasuryAccount == null || treasuryAccount.trim().isEmpty()) {
            dataManager.setTreasuryAccount(" ");
            getLogger().warning("§cОшибка: Вставьте ник!!!");
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(treasuryAccount);
            if (offlinePlayer.hasPlayedBefore()) {
                getLogger().warning("президент " + treasuryAccount);
            } else {
                dataManager.setTreasuryAccount(" ");
                getLogger().warning("§cОшибка: Веден не верный никнейм!!!");
            }
        }
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveAll();}
    }
}
