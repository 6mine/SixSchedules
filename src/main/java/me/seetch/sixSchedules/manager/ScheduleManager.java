package me.seetch.sixSchedules.manager;

import me.seetch.sixSchedules.event.EventCommand;
import me.seetch.sixSchedules.event.EventScheduler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ScheduleManager {
    private final Plugin plugin;
    private final Map<String, Map<Integer, Integer>> lastCommandIndex = new HashMap<>();
    private final Map<String, Long> lastEventTime = new HashMap<>();
    private final File dataFile;

    public ScheduleManager(Plugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
    }

    public void loadEvents() {
        loadData();

        for (String eventName : plugin.getConfig().getConfigurationSection("Events").getKeys(false)) {
            if (!plugin.getConfig().getBoolean("Events." + eventName + ".Enabled")) continue;

            int minPlayers = plugin.getConfig().getInt("Events." + eventName + ".MinPlayers");
            int maxPlayers = plugin.getConfig().getInt("Events." + eventName + ".MaxPlayers");
            int delay = plugin.getConfig().getInt("Events." + eventName + ".Delay");
            Map<Integer, EventCommand> eventCommands = loadEventCommands(eventName);

            long currentTime = System.currentTimeMillis();
            lastEventTime.put(eventName, currentTime);

            new EventScheduler(plugin, eventName, minPlayers, maxPlayers, delay, eventCommands).startEventTimer();
        }

        saveData();
    }

    private Map<Integer, EventCommand> loadEventCommands(String eventName) {
        Map<Integer, EventCommand> commandsMap = new HashMap<>();
        ConfigurationSection commandsSection = plugin.getConfig().getConfigurationSection("Events." + eventName + ".Commands");

        if (commandsSection != null) {
            for (String key : commandsSection.getKeys(false)) {
                int id = Integer.parseInt(key);
                int chance = commandsSection.getInt(key + ".Chance");
                boolean randomize = commandsSection.getBoolean(key + ".RandomizeCommands");
                var commands = commandsSection.getStringList(key + ".Commands");
                commandsMap.put(id, new EventCommand(id, chance, randomize, commands));
            }
        }
        return commandsMap;
    }

    public long getTimeUntilNextEvent(String eventName) {
        if (!lastEventTime.containsKey(eventName)) {
            return -1;
        }

        long currentTime = System.currentTimeMillis();
        long lastTriggerTime = lastEventTime.get(eventName);
        long delay = plugin.getConfig().getInt("Events." + eventName + ".Delay") * 1000L;
        long timeUntilNext = delay - (currentTime - lastTriggerTime);

        if (timeUntilNext < 0) {
            timeUntilNext = 0;
        }

        return timeUntilNext / 1000;
    }

    public int getLastCommandIndex(String eventName, int commandId) {
        return lastCommandIndex.getOrDefault(eventName, new HashMap<>()).getOrDefault(commandId, 0);
    }

    public void setLastCommandIndex(String eventName, int commandId, int index) {
        lastCommandIndex.computeIfAbsent(eventName, k -> new HashMap<>()).put(commandId, index);
        saveData();
    }

    private void loadData() {
        if (!dataFile.exists()) {
            return;
        }

        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(dataFile);

        for (String eventName : yamlConfig.getKeys(false)) {
            long lastEvent = yamlConfig.getLong(eventName + ".lastEventTime");
            lastEventTime.put(eventName, lastEvent);

            Map<Integer, Integer> commandIndexMap = new HashMap<>();
            if (yamlConfig.contains(eventName + ".lastCommandIndex")) {
                for (String commandId : yamlConfig.getConfigurationSection(eventName + ".lastCommandIndex").getKeys(false)) {
                    int id = Integer.parseInt(commandId);
                    int index = yamlConfig.getInt(eventName + ".lastCommandIndex." + commandId);
                    commandIndexMap.put(id, index);
                }
            }
            lastCommandIndex.put(eventName, commandIndexMap);
        }
    }

    private void saveData() {
        YamlConfiguration yamlConfig = new YamlConfiguration();

        for (String eventName : lastEventTime.keySet()) {
            yamlConfig.set(eventName + ".lastEventTime", lastEventTime.get(eventName));

            Map<Integer, Integer> commandIndexMap = lastCommandIndex.get(eventName);
            for (Map.Entry<Integer, Integer> entry : commandIndexMap.entrySet()) {
                yamlConfig.set(eventName + ".lastCommandIndex." + entry.getKey(), entry.getValue());
            }
        }

        try {
            yamlConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
