package eigencraft.motionprint.plugins.message;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import eigencraft.motionprint.api.MotionPrintUtils;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.command.arguments.MessageArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import eigencraft.motionprint.data.LoggingManager;

public class SubCommandLogText {
    public static CommandNode<ServerCommandSource> registerSubCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> subCommandRootNode = CommandManager.literal("log-text").build();
        ArgumentCommandNode<ServerCommandSource, EntitySelector> argPlayer = CommandManager.argument("player", EntityArgumentType.player()).build();

        ArgumentCommandNode<ServerCommandSource, MessageArgumentType.MessageFormat> argMessage = CommandManager.argument("message", MessageArgumentType.message())
            .executes(c -> logText(EntityArgumentType.getPlayer(c, "player"), MessageArgumentType.getMessage(c, "message")))
            .build();

        subCommandRootNode.addChild(argPlayer);
        argPlayer.addChild(argMessage);

        return subCommandRootNode;
    }

    private static int logText(ServerPlayerEntity player, Text message) {
        if (MessagePlugin.INSTANCE.isEnabled()&& MotionPrintUtils.shouldTrackPlayer(player)){
            MotionPrintUtils.getPlayerDataLogger(player).logData(LogMessageDataEntry.of(player,message.getString()));
        }
        return 1;
    }
}
