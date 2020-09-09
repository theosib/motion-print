package eigencraft.motionprint.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import eigencraft.motionprint.data.LoggingManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class SubCommandFlushData {
    public static CommandNode<ServerCommandSource> registerSubCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> subCommandRootNode = CommandManager.literal("flush-data")
                .executes(c -> flushData(c.getSource())).build();
        return subCommandRootNode;
    }

    private static int flushData(ServerCommandSource source) {
        LoggingManager.INSTANCE.flushData();
        source.sendFeedback(new LiteralText("All logged data flushed to log files"), false);
        return 1;
    }
}
