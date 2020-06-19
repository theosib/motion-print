package eigencraft.motionprint.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import eigencraft.motionprint.data.ConsentTracker;

public class GrantConsentCommand {
    public static void registerServerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("motion-print-grant-consent")
                        //.requires((src) -> src.hasPermissionLevel(permissionLevel))
                        .executes(c -> grantConsent(c.getSource()))
        );
    }

    private static int grantConsent(ServerCommandSource source) {
        Entity entity = source.getEntity();

        if (entity instanceof PlayerEntity) {
            ConsentTracker.INSTANCE.grantConsent((PlayerEntity) entity);
        }
        else {
            source.sendError(new LiteralText("This command can only be run by a player"));
        }

        return 1;
    }
}
