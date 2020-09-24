package eigencraft.motionprint.plugins.message;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.data.PlayerDataLogger;
import eigencraft.motionprint.plugins.scoreboard.SubCommandLogScoreboard;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;

public class MessagePlugin extends MotionPrintPlugin {
    public static final MessagePlugin INSTANCE = new MessagePlugin();

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
        return SubCommandLogText.registerSubCommand(dispatcher);
    }
}
