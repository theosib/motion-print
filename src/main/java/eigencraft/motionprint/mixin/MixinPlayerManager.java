package eigencraft.motionprint.mixin;

import eigencraft.motionprint.data.LoggingManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void onPlayerConnectEnd(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        LoggingManager.INSTANCE.onPlayerLogin(player);
        //LoggingManager.INSTANCE.onPlayerEvent(player, "LOGIN|" + Registry.DIMENSION_TYPE.getId(player.dimension).toString());
    }

    @Inject(method = "remove", at = @At("RETURN"))
    private void onPlayerRemoved(ServerPlayerEntity player, CallbackInfo ci) {
        //LoggingManager.INSTANCE.onPlayerEvent(player, "LOGOUT|" + Registry.DIMENSION_TYPE.getId(player.dimension).toString());
        LoggingManager.INSTANCE.onPlayerLogout(player);
    }
}
