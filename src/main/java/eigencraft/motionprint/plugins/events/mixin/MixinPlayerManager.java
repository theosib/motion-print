package eigencraft.motionprint.plugins.events.mixin;

import eigencraft.motionprint.api.MotionPrintUtils;
import eigencraft.motionprint.data.PlayerDataLogger;
import eigencraft.motionprint.plugins.events.EventDataEntry;
import eigencraft.motionprint.plugins.events.EventPlugin;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {
    @Inject(method = "respawnPlayer", at = @At("RETURN"))
    private void onPlayerRespawn(ServerPlayerEntity player, DimensionType dimension, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        //LoggingManager.INSTANCE.onPlayerEvent(cir.getReturnValue(), "RESPAWN|" + Registry.DIMENSION_TYPE.getId(player.dimension).toString());

        if (EventPlugin.INSTANCE.isEnabled()&& MotionPrintUtils.shouldTrackPlayer(player)){
            PlayerDataLogger logger = MotionPrintUtils.getPlayerDataLogger(player);
            logger.logData(new EventDataEntry(String.format("respawn %s",Registry.DIMENSION_TYPE.getId(player.dimension).toString())));
        }
    }

    @Inject(method = "method_14594", at = @At("RETURN"))
    private void onPlayerChangedDimension(ServerPlayerEntity player, CallbackInfo ci) {
        //LoggingManager.INSTANCE.onPlayerEvent(player, "CHANGED_DIMENSION|" + Registry.DIMENSION_TYPE.getId(player.dimension).toString());

        if (EventPlugin.INSTANCE.isEnabled()&& MotionPrintUtils.shouldTrackPlayer(player)){
            PlayerDataLogger logger = MotionPrintUtils.getPlayerDataLogger(player);
            logger.logData(new EventDataEntry(String.format("changed_dimension %s", Registry.DIMENSION_TYPE.getId(player.dimension).toString())));
        }
    }
}
