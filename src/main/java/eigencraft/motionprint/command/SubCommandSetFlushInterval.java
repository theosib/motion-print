package eigencraft.motionprint.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import eigencraft.motionprint.data.LoggingManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class SubCommandSetFlushInterval {
    public static CommandNode<ServerCommandSource> registerSubCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> subCommandRootNode = CommandManager.literal("set-flush-interval")
            .executes(c -> printCurrentValue(c.getSource())).build();

        ArgumentCommandNode<ServerCommandSource, Integer> argInterval = CommandManager.argument("interval", IntegerArgumentType.integer(0, 10000))
            .executes(c -> setLoggingInterval(c.getSource(), IntegerArgumentType.getInteger(c, "interval")))
            .build();

        subCommandRootNode.addChild(argInterval);

        return subCommandRootNode;
    }

    private static int printCurrentValue(ServerCommandSource source) {
        source.sendFeedback(new LiteralText("Data flush to disk interval is currently " + LoggingManager.INSTANCE.getFlushInterval() + " entries"), false);
        return 1;
    }

    private static int setLoggingInterval(ServerCommandSource source, int interval) {
        LoggingManager.INSTANCE.setFlushInterval(interval);
        source.sendFeedback(new LiteralText("Data flush to disk interval is now set to " + LoggingManager.INSTANCE.getFlushInterval() + " entries"), false);
        return 1;
    }
}
