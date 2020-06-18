package eigencraft.motionprint.mixin;

import eigencraft.motionprint.MotionPrint;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        MotionPrint.playersNameHasConsented.put(player.getName().asFormattedString(), false);
        player.sendChatMessage(new TranslatableText("consent.askMessage"), MessageType.SYSTEM);
    }

    @Inject(method = "broadcastChatMessage", at = @At("TAIL"))
    public void onChatMessage(Text text, boolean system, CallbackInfo ci) {
        if(!system) {
            String playerName = text.asFormattedString().substring(text.asFormattedString().indexOf("<")+1, text.asFormattedString().indexOf(">"));
            String message = text.asFormattedString().substring(text.asFormattedString().indexOf(">") + 2);
            MotionPrint.playersNameHasConsented.forEach((name, consentStatus) -> {
                if (playerName.equals(name) && !consentStatus) {
                    ServerPlayerEntity targetPlayer = server.getPlayerManager().getPlayer(playerName);
                    if (targetPlayer != null) { // Sanity check
                        if (message.equalsIgnoreCase("yes")) {
                            targetPlayer.sendChatMessage(new LiteralText("consent.replyToYes"), MessageType.SYSTEM);
                            MotionPrint.playersNameHasConsented.replace(name, true);
                        } else if (message.equalsIgnoreCase("no")) {
                            targetPlayer.sendChatMessage(new TranslatableText("consent.replyToNo"), MessageType.SYSTEM);
                        } else {
                            targetPlayer.sendChatMessage(new TranslatableText("consent.replyToOther"), MessageType.SYSTEM);
                        }
                    }
                }
            });
        }
    }
}