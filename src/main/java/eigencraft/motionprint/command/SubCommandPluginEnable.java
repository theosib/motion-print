package eigencraft.motionprint.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.data.LoggingManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class SubCommandPluginEnable {
    public static CommandNode<ServerCommandSource> registerSubCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> subCommandRootNode = CommandManager.literal("enable-plugin").build();

        ArgumentCommandNode<ServerCommandSource, String> pluginArgument = CommandManager.argument("plugin", StringArgumentType.word())
                .suggests(new PluginSuggestionProvider())
                .executes(c -> enablePlugin(c.getSource(), StringArgumentType.getString(c,"plugin"))).build();

        subCommandRootNode.addChild(pluginArgument);

        return  subCommandRootNode;
    }

    private static int enablePlugin(ServerCommandSource source, String pluginName) {
        MotionPrintPlugin toEnable = null;
        for (MotionPrintPlugin plugin: LoggingManager.INSTANCE.getPlugins()){
            if (plugin.getName().equals(pluginName)){
                toEnable = plugin;
                break;
            }
        }
        if (toEnable==null){
            source.sendFeedback(new LiteralText("Plugin Doesn't exist!"),false);
            return 0;
        }
        toEnable.enable();
        source.sendFeedback(new LiteralText(String.format("Enabled plugin '%s'",toEnable.getName())),true);
        return 0;
    }
}
