package eigencraft.motionprint.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import eigencraft.motionprint.data.LoggingManager;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void onPlayerConnectEnd(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        LoggingManager.INSTANCE.onPlayerLogin(player);
        LoggingManager.INSTANCE.onPlayerEvent(player, "LOGIN|" + Registry.DIMENSION_TYPE.getId(player.dimension).toString());
    }

    @Inject(method = "remove", at = @At("RETURN"))
    private void onPlayerRemoved(ServerPlayerEntity player, CallbackInfo ci) {
        LoggingManager.INSTANCE.onPlayerEvent(player, "LOGOUT|" + Registry.DIMENSION_TYPE.getId(player.dimension).toString());
        LoggingManager.INSTANCE.onPlayerLogout(player);
    }

    @Inject(method = "respawnPlayer", at = @At("RETURN"))
    private void onPlayerRespawn(ServerPlayerEntity player, DimensionType dimension, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        LoggingManager.INSTANCE.onPlayerEvent(cir.getReturnValue(), "RESPAWN|" + Registry.DIMENSION_TYPE.getId(player.dimension).toString());
    }

    @Inject(method = "method_14594", at = @At("RETURN"))
    private void onPlayerChangedDimension(ServerPlayerEntity player, CallbackInfo ci) {
        LoggingManager.INSTANCE.onPlayerEvent(player, "CHANGED_DIMENSION|" + Registry.DIMENSION_TYPE.getId(player.dimension).toString());
    }
}
