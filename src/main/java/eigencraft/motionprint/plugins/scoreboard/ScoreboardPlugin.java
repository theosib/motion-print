package eigencraft.motionprint.plugins.scoreboard;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.api.MotionPrintUtils;
import eigencraft.motionprint.data.PlayerDataLogger;
import net.minecraft.command.arguments.ObjectiveArgumentType;
import net.minecraft.command.arguments.ScoreHolderArgumentType;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

public class ScoreboardPlugin extends MotionPrintPlugin {

    public static final ScoreboardPlugin INSTANCE = new ScoreboardPlugin();

    public ScoreboardPlugin() {
        super("log-scoreboard");
    }

    @Override
    public void onEnabled() {

    }

    @Override
    public void onDisabled() {

    }

    @Override
    public void startLogPlayer(PlayerDataLogger newLogger) {

    }

    @Override
    public void stopLogPlayer(PlayerDataLogger logger) {

    }

    @Override
    public void tick(Collection<PlayerDataLogger> players) {

    }

    @Override
    public CommandNode<ServerCommandSource> registerConfigCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> subCommandRootNode = CommandManager.literal("log-scoreboard").build();
        ArgumentCommandNode<ServerCommandSource, ScoreHolderArgumentType.ScoreHolder> argPlayer = CommandManager.argument("players", ScoreHolderArgumentType.scoreHolders()).suggests(ScoreHolderArgumentType.SUGGESTION_PROVIDER).build();
        ArgumentCommandNode<ServerCommandSource, String> argScoreboard = CommandManager.argument("scoreboard", ObjectiveArgumentType.objective()).executes(
                c -> logScoreboard(c.getSource().getMinecraftServer(), ScoreHolderArgumentType.getScoreHolders(c, "players"), ObjectiveArgumentType.getObjective(c, "scoreboard"))
        ).build();

        subCommandRootNode.addChild(argPlayer);
        argPlayer.addChild(argScoreboard);

        return subCommandRootNode;
    }

    private int logScoreboard(MinecraftServer server, Collection<String> players, ScoreboardObjective objective) {

        if (isEnabled()) {

            for (String player : players) {
                int score = server.getScoreboard().getPlayerScore(player, objective).getScore();
                ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player);
                if (playerEntity != null && MotionPrintUtils.shouldTrackPlayer(playerEntity)) {
                    PlayerDataLogger logger = MotionPrintUtils.getPlayerDataLogger(playerEntity);
                    logger.logData(ScoreboardDataEntry.of(playerEntity, objective.getName(), score));
                }
            }
            return 1;
        }
        return 1;
    }
}
