package eigencraft.motionprint.plugins.events.mixin;

import eigencraft.motionprint.api.MotionPrintUtils;
import eigencraft.motionprint.data.PlayerDataLogger;
import eigencraft.motionprint.plugins.events.EventDataEntry;
import eigencraft.motionprint.plugins.events.EventPlugin;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
    @Inject(method = "applyDamage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V"))
    private void onPlayerDamaged(DamageSource source, float amount, CallbackInfo ci) {
        if (EventPlugin.INSTANCE.isEnabled()&& MotionPrintUtils.shouldTrackPlayer((PlayerEntity)((Object)this))){
            PlayerDataLogger logger = MotionPrintUtils.getPlayerDataLogger((PlayerEntity)((Object)this));
            logger.logData(new EventDataEntry(String.format("DAMAGE|%s|%.3f", source.getName(), amount)));
        }
        //LoggingManager.INSTANCE.onPlayerEvent((ServerPlayerEntity) (Object) this, String.format("DAMAGE|%s|%.3f", source.getName(), amount));
    }
}
