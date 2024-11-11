package me.seetch.sixSchedules.event;

import java.util.List;

public record EventCommand(int id, int chance, boolean randomizeCommands, List<String> commands) {
}
