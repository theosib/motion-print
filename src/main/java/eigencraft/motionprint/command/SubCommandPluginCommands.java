package eigencraft.motionprint.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.data.LoggingManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class SubCommandPluginCommands {
    public static CommandNode<ServerCommandSource> registerSubCommand(CommandDispatcher<ServerCommandSource> dispatcher) {

        LiteralArgumentBuilder<ServerCommandSource> subCommandRootNode = CommandManager.literal("plugin");

        for (MotionPrintPlugin plugin:LoggingManager.INSTANCE.getPlugins()){
            CommandNode<ServerCommandSource> pluginCommand = plugin.registerConfigCommand(dispatcher);
            if (pluginCommand!=null) {
                subCommandRootNode.then(pluginCommand);
            }
        }

        return subCommandRootNode.build();
    }
}
