package eigencraft.motionprint.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import eigencraft.motionprint.data.LoggingManager;
import eigencraft.motionprint.data.ScoreboardDataEntry;
import net.minecraft.command.arguments.ObjectiveArgumentType;
import net.minecraft.command.arguments.ScoreHolderArgumentType;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;

public class SubCommandLogScoreboard {
    public static CommandNode<ServerCommandSource> registerSubCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> subCommandRootNode = CommandManager.literal("log-scoreboard").build();
        ArgumentCommandNode<ServerCommandSource, ScoreHolderArgumentType.ScoreHolder> argPlayer = CommandManager.argument("players", ScoreHolderArgumentType.scoreHolders()).suggests(ScoreHolderArgumentType.SUGGESTION_PROVIDER).build();
        ArgumentCommandNode<ServerCommandSource, String> argScoreboard = CommandManager.argument("scoreboard", ObjectiveArgumentType.objective()).executes(
                c -> logScoreboard(c.getSource().getMinecraftServer(), ScoreHolderArgumentType.getScoreHolders(c, "players"), ObjectiveArgumentType.getObjective(c, "scoreboard"))
        ).build();

        subCommandRootNode.addChild(argPlayer);
        argPlayer.addChild(argScoreboard);

        return subCommandRootNode;
    }

    private static int logScoreboard(MinecraftServer server, Collection<String> players, ScoreboardObjective objective) {
        for (String player : players) {
            int score = objective.getScoreboard().getPlayerScore(player, objective).getScore();
            //LoggingManager.INSTANCE.addLogMessage(server.getPlayerManager().getPlayer(player), String.format("scoreboard %s: %d", objective.getName(), score));
            LoggingManager.INSTANCE.addLogDataEntry(server.getPlayerManager().getPlayer(player),
                    ScoreboardDataEntry.of(server.getPlayerManager().getPlayer(player), objective.getName(), score)
            );
        }
        return 1;
    }
}
