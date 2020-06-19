package eigencraft.motionprint.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import eigencraft.motionprint.data.LoggingManager;

public class SubCommandSetEnabled {
    public static CommandNode<ServerCommandSource> registerSubCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> subCommandRootNode = CommandManager.literal("set-logging-enabled")
                .executes(c -> printCurrentValue(c.getSource())).build();

        ArgumentCommandNode<ServerCommandSource, Boolean> argEnabled = CommandManager.argument("enabled", BoolArgumentType.bool())
                .executes(c -> setEnabled(c.getSource(), BoolArgumentType.getBool(c, "enabled")))
                .build();

        subCommandRootNode.addChild(argEnabled);

        return subCommandRootNode;
    }

    private static int printCurrentValue(ServerCommandSource source) {
        String enabledStr = LoggingManager.INSTANCE.isEnabled() ? "enabled" : "disabled";
        source.sendFeedback(new LiteralText("Logging is currently " + enabledStr), false);
        return 1;
    }

    private static int setEnabled(ServerCommandSource source, boolean enabled) {
        LoggingManager.INSTANCE.setEnabled(enabled);
        String enabledStr = LoggingManager.INSTANCE.isEnabled() ? "enabled" : "disabled";
        source.sendFeedback(new LiteralText("Logging is now " + enabledStr), false);
        return 1;
    }
}
