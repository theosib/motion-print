package eigencraft.motionprint.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.data.LoggingManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

public class PluginSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        System.out.println(builder.getInput());
        for(MotionPrintPlugin plugin: LoggingManager.INSTANCE.getPlugins()){
            builder.suggest(plugin.getName());
        }
        return builder.buildFuture();
    }
}
