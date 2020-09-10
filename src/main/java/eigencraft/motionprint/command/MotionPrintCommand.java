package eigencraft.motionprint.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class MotionPrintCommand {
    public static void registerServerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        register(dispatcher, "motion-print", 2);
    }

    protected static void register(CommandDispatcher<ServerCommandSource> dispatcher, String baseCommandName, int permissionLevel) {
        dispatcher.register(
                CommandManager.literal(baseCommandName)
                        .requires((src) -> src.hasPermissionLevel(permissionLevel))
                        .then(SubCommandFlushData.registerSubCommand(dispatcher))
                        .then(SubCommandLogText.registerSubCommand(dispatcher))
                        .then(SubCommandRotateSessions.registerSubCommand(dispatcher))
                        .then(SubCommandSetEnabled.registerSubCommand(dispatcher))
                        .then(SubCommandSetFlushInterval.registerSubCommand(dispatcher))
                        .then(SubCommandSetLoggingInterval.registerSubCommand(dispatcher))
                        .then(SubCommandLogScoreboard.registerSubCommand(dispatcher))
        );
    }
}
