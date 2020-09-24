package eigencraft.motionprint.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.data.LoggingManager;
import net.minecraft.text.LiteralText;

public class PluginArgumentType implements ArgumentType<MotionPrintPlugin> {
    private static final SimpleCommandExceptionType PLUGIN_NOT_FOUND = new SimpleCommandExceptionType(new LiteralText("Plugin doesn't exist!"));

    @Override
    public MotionPrintPlugin parse(StringReader reader) throws CommandSyntaxException {
        String pluginName = reader.readUnquotedString();
        for(MotionPrintPlugin plugin: LoggingManager.INSTANCE.getPlugins()){
            if (plugin.getName().equals(pluginName)){
                return plugin;
            }
        }
        throw PLUGIN_NOT_FOUND.create();
    }

    public static MotionPrintPlugin getPlugin(final CommandContext<?> context, final String name) {
        return context.getArgument(name, MotionPrintPlugin.class);
    }
}
