package eigencraft.motionprint.mixin;

import eigencraft.motionprint.MotionPrint;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        MotionPrint.currentPlayer = player;
        player.sendChatMessage(new LiteralText("Do you consent? Respond with \"yes\" or \"no\"."), MessageType.SYSTEM);
    }

    @Inject(method = "broadcastChatMessage", at = @At("TAIL"))
    public void onChatMessage(Text text, boolean system, CallbackInfo ci) {
        String message = text.asFormattedString().substring(text.asFormattedString().indexOf(">")+2);
        if(!MotionPrint.hasConsented) {
            if (message.equalsIgnoreCase("yes")) {
                MotionPrint.currentPlayer.sendChatMessage(new LiteralText("Thanks for your consent!"), MessageType.SYSTEM);
                MotionPrint.hasConsented = true;
            } else if (message.equalsIgnoreCase("no")) {
                MotionPrint.currentPlayer.sendChatMessage(new LiteralText("No?"), MessageType.SYSTEM);
            } else {
                MotionPrint.currentPlayer.sendChatMessage(new LiteralText("Please answer \"yes\" or \"no\"."), MessageType.SYSTEM);
            }
        }
    }
}