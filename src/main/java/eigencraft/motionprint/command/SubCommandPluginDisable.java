package eigencraft.motionprint.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.data.LoggingManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class SubCommandPluginDisable {
    public static CommandNode<ServerCommandSource> registerSubCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> subCommandRootNode = CommandManager.literal("disable-plugin").build();

        ArgumentCommandNode<ServerCommandSource, String> pluginArgument = CommandManager.argument("plugin", StringArgumentType.word())
                .suggests(new PluginSuggestionProvider())
                .executes(c -> disablePlugin(c.getSource(), StringArgumentType.getString(c,"plugin"))).build();

        subCommandRootNode.addChild(pluginArgument);

        return  subCommandRootNode;
    }

    private static int disablePlugin(ServerCommandSource source, String pluginName) {
        MotionPrintPlugin toDisable = null;
        for (MotionPrintPlugin plugin: LoggingManager.INSTANCE.getPlugins()){
            if (plugin.getName().equals(pluginName)){
                toDisable = plugin;
                break;
            }
        }
        if (toDisable==null){
            source.sendFeedback(new LiteralText("Plugin Doesn't exist!"),false);
            return 0;
        }
        toDisable.disable();
        source.sendFeedback(new LiteralText(String.format("Disabled plugin '%s'",toDisable.getName())),true);
        return 0;
    }
}
