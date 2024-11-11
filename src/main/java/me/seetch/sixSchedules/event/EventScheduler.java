package me.seetch.sixSchedules.event;

import me.seetch.sixSchedules.SixSchedules;
import me.seetch.sixSchedules.manager.ScheduleManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class EventScheduler implements Runnable {
    private final Plugin plugin;
    private final String eventName;
    private final int minPlayers;
    private final int maxPlayers;
    private final int delay;
    private final Map<Integer, EventCommand> eventCommands;
    private final ScheduleManager manager;

    public EventScheduler(Plugin plugin, String eventName, int minPlayers, int maxPlayers, int delay, Map<Integer, EventCommand> eventCommands) {
        this.plugin = plugin;
        this.eventName = eventName;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.delay = delay;
        this.eventCommands = eventCommands;
        this.manager = ((SixSchedules) plugin).getScheduleManager();
    }

    @Override
    public void run() {
        int onlinePlayers = Bukkit.getOnlinePlayers().size();

        if (onlinePlayers < minPlayers || (maxPlayers > 0 && onlinePlayers >= maxPlayers)) return;

        for (EventCommand eventCommand : eventCommands.values()) {
            if (new Random().nextDouble() * 100 < eventCommand.chance()) {
                List<String> commands = eventCommand.commands();
                int lastIndex = manager.getLastCommandIndex(eventName, eventCommand.id());

                String commandToExecute;
                if (eventCommand.randomizeCommands()) {
                    commandToExecute = commands.get(new Random().nextInt(commands.size()));
                } else {
                    commandToExecute = commands.get(lastIndex);
                    lastIndex = (lastIndex + 1) % commands.size();
                    manager.setLastCommandIndex(eventName, eventCommand.id(), lastIndex);
                }

                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), commandToExecute.replace("broadcast!", "say"));
                break;
            }
        }
    }

    public void startEventTimer() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, delay * 20L);
    }
}
