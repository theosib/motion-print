package eigencraft.motionprint.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import eigencraft.motionprint.api.MotionPrintPlugin;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class SubCommandPluginEnable {
    public static CommandNode<ServerCommandSource> registerSubCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> subCommandRootNode = CommandManager.literal("enable-plugin").build();

        ArgumentCommandNode<ServerCommandSource, MotionPrintPlugin> pluginArgument = CommandManager.argument("plugin", new PluginArgumentType())
                .suggests(new PluginSuggestionProvider())
                .executes(c -> enablePlugin(c.getSource(), PluginArgumentType.getPlugin(c,"plugin"))).build();

        subCommandRootNode.addChild(pluginArgument);

        return  subCommandRootNode;
    }

    private static int enablePlugin(ServerCommandSource source, MotionPrintPlugin plugin) {
        plugin.enable();
        source.sendFeedback(new LiteralText(String.format("Enabled plugin '%s'",plugin.getName())),true);
        return 0;
    }
}
