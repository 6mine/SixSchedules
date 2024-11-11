package me.seetch.sixSchedules.manager;

import me.seetch.sixSchedules.event.EventCommand;
import me.seetch.sixSchedules.event.EventScheduler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ScheduleManager {
    private final Plugin plugin;
    private final Map<String, Map<Integer, Integer>> lastCommandIndex = new HashMap<>();

    public ScheduleManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void loadEvents() {
        for (String eventName : plugin.getConfig().getConfigurationSection("").getKeys(false)) {
            if (!plugin.getConfig().getBoolean(eventName + ".Enabled")) continue;

            int minPlayers = plugin.getConfig().getInt(eventName + ".MinPlayers");
            int maxPlayers = plugin.getConfig().getInt(eventName + ".MaxPlayers");
            int delay = plugin.getConfig().getInt(eventName + ".Delay");
            Map<Integer, EventCommand> eventCommands = loadEventCommands(eventName);
            new EventScheduler(plugin, eventName, minPlayers, maxPlayers, delay, eventCommands).startEventTimer();
        }
    }

    private Map<Integer, EventCommand> loadEventCommands(String eventName) {
        Map<Integer, EventCommand> commandsMap = new HashMap<>();
        ConfigurationSection commandsSection = plugin.getConfig().getConfigurationSection(eventName + ".Commands");

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

    public int getLastCommandIndex(String eventName, int commandId) {
        return lastCommandIndex.getOrDefault(eventName, new HashMap<>()).getOrDefault(commandId, 0);
    }

    public void setLastCommandIndex(String eventName, int commandId, int index) {
        lastCommandIndex.computeIfAbsent(eventName, k -> new HashMap<>()).put(commandId, index);
    }
}
