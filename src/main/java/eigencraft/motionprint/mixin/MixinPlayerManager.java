package eigencraft.motionprint.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import eigencraft.motionprint.data.LoggingManager;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void onPlayerConnectEnd(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        LoggingManager.INSTANCE.onPlayerLogin(player);
    }

    @Inject(method = "remove", at = @At("RETURN"))
    private void onPlayerRemoved(ServerPlayerEntity player, CallbackInfo ci) {
        LoggingManager.INSTANCE.onPlayerLogout(player);
    }
}
