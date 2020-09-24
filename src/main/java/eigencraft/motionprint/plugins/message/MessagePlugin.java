package eigencraft.motionprint.plugins.message;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import eigencraft.motionprint.api.MotionPrintPlugin;
import eigencraft.motionprint.api.MotionPrintUtils;
import eigencraft.motionprint.data.PlayerDataLogger;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.command.arguments.MessageArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class MessagePlugin extends MotionPrintPlugin {
    public static final MessagePlugin INSTANCE = new MessagePlugin();

    public MessagePlugin() {
        super("log-messages");
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
        LiteralCommandNode<ServerCommandSource> subCommandRootNode = CommandManager.literal("log-text").build();
        ArgumentCommandNode<ServerCommandSource, EntitySelector> argPlayer = CommandManager.argument("player", EntityArgumentType.player()).build();

        ArgumentCommandNode<ServerCommandSource, MessageArgumentType.MessageFormat> argMessage = CommandManager.argument("message", MessageArgumentType.message())
                .executes(c -> logText(EntityArgumentType.getPlayer(c, "player"), MessageArgumentType.getMessage(c, "message")))
                .build();

        subCommandRootNode.addChild(argPlayer);
        argPlayer.addChild(argMessage);

        return subCommandRootNode;
    }

    private int logText(ServerPlayerEntity player, Text message) {
        if (isEnabled()&& MotionPrintUtils.shouldTrackPlayer(player)){
            MotionPrintUtils.getPlayerDataLogger(player).logData(LogMessageDataEntry.of(player,message.getString()));
        }
        return 1;
    }
}
