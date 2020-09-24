package eigencraft.motionprint.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import eigencraft.motionprint.data.LoggingManager;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
    @Inject(method = "applyDamage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V"))
    private void onPlayerDamaged(DamageSource source, float amount, CallbackInfo ci) {
        //LoggingManager.INSTANCE.onPlayerEvent((ServerPlayerEntity) (Object) this, String.format("DAMAGE|%s|%.3f", source.getName(), amount));
    }
}
