package eigencraft.motionprint.mixin;

import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ConsentListenerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onChatMessage", at = @At("HEAD"))
    public void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        String message = packet.getChatMessage();
        System.out.println(message);
        if(message.endsWith("yes")) {
            player.sendChatMessage(new LiteralText("Thanks for your consent!"), MessageType.SYSTEM);
        } else if(message.endsWith("no")) {
            player.sendChatMessage(new LiteralText("No?"), MessageType.SYSTEM);
        }
    }
}