package me.yourname.proba;

import me.yourname.proba.commands.getbankblock;
import me.yourname.proba.commands.getfineblock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static me.yourname.proba.menu.bank_menu_class.bank_menu;
import static me.yourname.proba.menu.fine_menu_class.fine_menu;

public class DataManager implements Listener {

    public static proba plugin;

    private File blocksFile;
    private YamlConfiguration blocksConfig;
    private File playerDataFile;
    private YamlConfiguration playerDataConfig;
    private File configFile;
    private YamlConfiguration configConfig;
    private File translationsFile;
    private YamlConfiguration translationsConfig;

    // Загрузка файлов
    public void loadALL() {
        // Расположения блоков
        blocksFile = new File(plugin.getDataFolder(), "blocks.yml");
        blocksConfig = YamlConfiguration.loadConfiguration(blocksFile);

        if (!blocksConfig.contains("atmBlocks")) {
            blocksConfig.set("atmBlocks", new ArrayList<>());
        }
        if (!blocksConfig.contains("fineBlocks")) {
            blocksConfig.set("fineBlocks", new ArrayList<>());
        }
        if (!blocksConfig.contains("pricePlates")) {
            blocksConfig.set("pricePlates", new ArrayList<>());
        }

        // Данные об игроках
        playerDataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);

        if (!playerDataConfig.contains("players")) {
            playerDataConfig.createSection("players");
        }
        if (!playerDataConfig.contains("fines")) {
            playerDataConfig.createSection("fines");
        }

        // Конфиг файл
        configFile = new File(plugin.getDataFolder(), "config.yml");
        configConfig = YamlConfiguration.loadConfiguration(configFile);

        if (!configConfig.contains("treasury-account")) {
            configConfig.set("treasury-account", " ");
        }
        if (!configConfig.contains("RP-MODE")) {
            configConfig.set("RP-MODE", false);
        }
        if (!configConfig.contains("allowed-players")) {
            configConfig.set("allowed-players", new ArrayList<>());
        }
        if (!configConfig.contains("fine-allowed-players")) {
            configConfig.set("fine-allowed-players", new ArrayList<>());
        }
        if (!configConfig.contains("currency-item")) {
            configConfig.set("currency-item", new ArrayList<>());
        }
        if (!configConfig.contains("language")) {
            configConfig.set("language", "RU");
        }

        configConfig.set("currency-item", "deepslate_diamond_ore");

        // История переводов
        translationsFile = new File(plugin.getDataFolder(), "translations.yml");
        translationsConfig = YamlConfiguration.loadConfiguration(translationsFile);

        if (!translationsConfig.contains("translations")) {
            translationsConfig.set("translations", new ArrayList<>());
        }
    }

    // Сохранение
    public void save(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить " + file.getName() + ": " + e.getMessage());
        }
    }

    // Всё сохраняет
    public void saveAll() {
        save(blocksConfig, blocksFile);
        save(playerDataConfig, playerDataFile);
        save(configConfig, configFile);
        save(translationsConfig, translationsFile);
    }

    public DataManager(JavaPlugin plugin) {
        DataManager.plugin = (proba) plugin;
        if (!plugin.getDataFolder().exists()) {plugin.getDataFolder().mkdirs();}
        loadALL();
    }

    // Нажатие по блоку
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null) return;

        Material type = block.getType();
        if (type != Material.LODESTONE && type != Material.LECTERN) return;

        String serialized = block.getWorld().getName() + " " + block.getX() + "/" + block.getY() + "/" + block.getZ();
        List<String> atmList = blocksConfig.getStringList("atmBlocks");
        List<String> fineList = blocksConfig.getStringList("fineBlocks");
        Player player = event.getPlayer();

        if (atmList.contains(serialized)) {
            if (getRPmode()) {
                List<String> allowed = getAllowedPlayers();
                if (!allowed.contains(player.getName())) {player.sendMessage("Не в списке");}
                return;
            }
            event.getPlayer().openInventory(bank_menu);
        } else if (fineList.contains(serialized)) {
            if (getRPmode()) {
                List<String> allowed = getFineAllowedPlayers();
                if (!allowed.contains(player.getName())) {player.sendMessage("Не в списке");}
                return;
            }
            event.getPlayer().openInventory(fine_menu);
        }
    }

    // Проверяет поставленный блок
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.LODESTONE) {
            ItemStack item = event.getItemInHand();
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.getPersistentDataContainer().has(getbankblock.bankKey, PersistentDataType.BOOLEAN)) {
                    addBankBlock(event.getBlock().getLocation(), "atm");
                    event.getPlayer().sendMessage("Блок зарегистрирован.");
                }
            }
        } else if (event.getBlock().getType() == Material.LECTERN){
            ItemStack item = event.getItemInHand();
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.getPersistentDataContainer().has(getfineblock.fineKey, PersistentDataType.BOOLEAN)) {
                    addBankBlock(event.getBlock().getLocation(), "fine");
                    event.getPlayer().sendMessage("Блок зарегистрирован.");
                }
            }
        }
    }

    // Сохраняет координаты банковского блока
    public void addBankBlock(Location loc, String block) {
        if (block.equals("atm")){
            List<String> list = blocksConfig.getStringList("atmBlocks");
            String serialized = loc.getWorld().getName() + " " + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ();
            if (!list.contains(serialized)) {
                list.add(serialized);
                blocksConfig.set("atmBlocks", list);
                save(blocksConfig, blocksFile);
            }
        } else if (block.equals("fine")){
            List<String> list = blocksConfig.getStringList("fineBlocks");
            String serialized = loc.getWorld().getName() + " " + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ();
            if (!list.contains(serialized)) {
                list.add(serialized);
                blocksConfig.set("fineBlocks", list);
                save(blocksConfig, blocksFile);
            }
        }
    }

    // Смотрит всех игроков в playerdata
    public List<String> getAllPlayers() {
        ConfigurationSection playersSection = playerDataConfig.getConfigurationSection("players");
        if (playersSection == null) {return new ArrayList<>();}
        List<String> names = new ArrayList<>();
        for (String uuid : playersSection.getKeys(false)) {
            String nickname = playersSection.getString(uuid + ".nickname");
            if (nickname != null) {
                names.add(nickname);
            }
        }
        return names;
    }

    // Устанавливает рп мод
    public void setRpMode(boolean value) {
        configConfig.set("RP-MODE", value);
        save(configConfig, configFile);
    }

    // Устанавливает казну
    public void setTreasuryAccount(String name) {
        configConfig.set("treasury-account", name);
        save(configConfig, configFile);
    }

    // Устанавливает банкиров
    public void setAllowedPlayers(String name) {
        configConfig.set("allowed-players", name);
        save(configConfig, configFile);
    }

    // Устанавливает судей
    public void setFineAllowedPlayers(String name) {
        configConfig.set("fine-allowed-players", name);
        save(configConfig, configFile);
    }

    // Установить баланс
    public void setBalance(UUID uuid, double amount) {
        ConfigurationSection section = getPlayerSection(uuid);
        if (uuid == null) {
            plugin.getLogger().warning("section = null");
        }
        if (section != null) {
            section.set("balance", amount);
            save(playerDataConfig, playerDataFile);
        } else {
            plugin.getLogger().warning("Секция для UUID " + uuid + " не найдена");
        }
    }

    // Устанавливает валюту
    public void setCurrencyItem(String item) {
        configConfig.set("currency-item", item);
        save(configConfig, configFile);
    }

    // Выводит язык
    public boolean getLanguage() {
        String language = configConfig.getString("language");
        boolean lan;
        if (language.equals("RU")) {lan = true;
        } else if (language.equals("EU")) {lan = false;
        } else {
            plugin.getLogger().warning("§cОшибка: Неизвестный язык!!!");
            lan = true;
        }return lan;
    }

    // Выводит президента
    public String getTreasuryAccount() {return configConfig.getString("treasury-account");}

    // Выводит список банкиров
    public List<String> getAllowedPlayers() {return configConfig.getStringList("allowed-players");}

    // Выводит список судей
    public List<String> getFineAllowedPlayers() {return configConfig.getStringList("fine-allowed-players");}

    // Выводит рп мод
    public boolean getRPmode() {return configConfig.getBoolean("RP-MODE", false);}

    // Получить баланс
    public int getBalance(UUID uuid) {
        if (uuid == null) {
            plugin.getLogger().warning("getBalance вызван с null UUID");
            return 0;
        }
        ConfigurationSection players = playerDataConfig.getConfigurationSection("players");
        if (players == null) {
            plugin.getLogger().warning("Секция 'players' отсутствует в playerdata.yml");
            return 0;
        }
        ConfigurationSection section = players.getConfigurationSection(uuid.toString());
        if (section == null) {
            plugin.getLogger().warning("Секция для UUID " + uuid + " не найдена");
            return 0;
        }
        int balance = section.getInt("balance", 0);
        plugin.getLogger().info("Баланс для " + uuid + " = " + balance);
        return balance;
    }

    // Получить секцию игрока по UUID
    private ConfigurationSection getPlayerSection(UUID uuid) {
        if (uuid == null) return null;
        ConfigurationSection players = playerDataConfig.getConfigurationSection("players");
        return players != null ? players.getConfigurationSection(uuid.toString()) : null;
    }

    // Получает валюту плагин
    public String getCurrencyItem() {return configConfig.getString("currency-item");}

    // Удаляет банкира
    public void removeAllowedPlayers(String name) {
        List<String> list = configConfig.getStringList("allowed-players");
        if (list.remove(name)) {
            configConfig.set("allowed-players", list);
            save(configConfig, configFile);
        }
    }

    // Удаляет судью
    public void removeFineAllowedPlayers(String name) {
        List<String> list = configConfig.getStringList("fine-allowed-players");
        if (list.remove(name)) {
            configConfig.set("fine-allowed-players", list);
            save(configConfig, configFile);
        }
    }

    //  Записывает данные об игроке в playerdata
    public void addPlayer(UUID uuid, boolean hasAcc, int amount) {
        String uuidStr = uuid.toString();
        ConfigurationSection playersSection = playerDataConfig.getConfigurationSection("players");
        if (playersSection == null) {
            playersSection = playerDataConfig.createSection("players");
        }
        if (!playersSection.contains(uuidStr)) {
            ConfigurationSection playerSection = playersSection.createSection(uuidStr);
            playerSection.set("nickname", Bukkit.getOfflinePlayer(uuid).getName());
            playerSection.set("hasAccount", hasAcc);
            playersSection.set("balance", 0);
            playerSection.set("mainAccount", -1);
            playersSection.createSection("accounts");
            save(playerDataConfig, playerDataFile);
        }
    }

    // Создаёт аккаунт
    public boolean addAcc(UUID uuid, int numberAcc, String nameAcc, boolean isMain) {
        String uuidStr = uuid.toString();
        ConfigurationSection playersSection = playerDataConfig.getConfigurationSection("players");
        if (playersSection == null || !playersSection.contains(uuidStr)) {return false;}
        ConfigurationSection playerSection = playersSection.getConfigurationSection(uuidStr);

        // Если у игрока ещё не было аккаунтов, устанавливаем hasAccount в true
        if (!playerSection.getBoolean("hasAccount", false)) {playerSection.set("hasAccount", true);}

        ConfigurationSection accountsSection = playerSection.getConfigurationSection("accounts");
        if (accountsSection == null) {accountsSection = playerSection.createSection("accounts");}

        // Определяем следующий доступный индекс
        int nextIndex = accountsSection.getKeys(false).size();
        if (nextIndex >= 3) {
            return false;
        }

        ConfigurationSection newAcc = accountsSection.createSection(String.valueOf(nextIndex));
        newAcc.set("name", nameAcc);
        newAcc.set("balance", 0);
        newAcc.set("isMain", isMain);

        // Обновляем главный аккаунт
        if (isMain) {
            playerSection.set("mainAccount", nextIndex);
            // У всех остальных аккаунтов снимаем флаг isMain
            for (String key : accountsSection.getKeys(false)) {
                if (!key.equals(String.valueOf(nextIndex))) {
                    ConfigurationSection acc = accountsSection.getConfigurationSection(key);
                    if (acc != null) acc.set("isMain", false);
                }
            }
        } else {
            // Если это первый аккаунт и главный ещё не задан – делаем его главным автоматически
            if (playerSection.getInt("mainAccount", -1) == -1 && nextIndex == 0) {
                playerSection.set("mainAccount", 0);
                newAcc.set("isMain", true);
            }
        }
        save(playerDataConfig, playerDataFile);
        return true;
    }

    // Проверяет заход игрока на сервер
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        addPlayer(playerId, false, 0);
    }
    // Записывает штрафы игроков
    public void addFinePlayer(UUID uuid, String reason, int price) {
        String uuidStr = uuid.toString();
        // Получаем или создаём секцию "fines"
        ConfigurationSection playersSection = playerDataConfig.getConfigurationSection("fines");
        if (playersSection == null) {playersSection = playerDataConfig.createSection("fines");}

        if (reason == null) {reason = "Причина не указана";}

        // Получаем или создаём секцию для конкретного игрока
        ConfigurationSection playerSection = playersSection.getConfigurationSection(uuidStr);
        if (playerSection == null) {
            playerSection = playersSection.createSection(uuidStr);
            playerSection.set("nickname", Bukkit.getOfflinePlayer(uuid).getName());
            // Инициализируем пустой список штрафов
            playerSection.set("fines", new ArrayList<Map<String, Object>>());
        }

        // Получаем текущий список штрафов
        List<Map<String, Object>> fines = (List<Map<String, Object>>) playerSection.getList("fines");
        if (fines == null) {
            fines = new ArrayList<>();
        }

        // Создаём новый штраф
        Map<String, Object> fine = new LinkedHashMap<>();
        fine.put("reason", reason);
        fine.put("price", price);
        fine.put("active", true);
        //fine.put("date", System.currentTimeMillis());

        fines.add(fine);
        playerSection.set("fines", fines);

        save(playerDataConfig, playerDataFile);
    }

    public void removeFinePlayer(UUID uuid, int amount){

    }
}