package me.seetch.sixSchedules.command;


import me.seetch.sixSchedules.SixSchedules;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class SixSchedulesCommand implements CommandExecutor {
    private final Plugin plugin;

    public SixSchedulesCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            ((SixSchedules) plugin).reloadPluginConfig();
            sender.sendMessage("§aКонфигурация SixSchedules перезагружена.");
            return true;
        }
        return false;
    }
}