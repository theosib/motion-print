package eigencraft.motionprint.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import eigencraft.motionprint.data.LoggingManager;

public class SubCommandRotateSessions {
    public static CommandNode<ServerCommandSource> registerSubCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> subCommandRootNode = CommandManager.literal("rotate-sessions")
                .executes(c -> rotateSession(c.getSource())).build();
        return subCommandRootNode;
    }

    private static int rotateSession(ServerCommandSource source) {
        LoggingManager.INSTANCE.rotateSessions(source);
        return 1;
    }
}
