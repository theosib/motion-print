package eigencraft.motionprint.plugins.events.mixin;

import com.mojang.authlib.GameProfile;
import eigencraft.motionprint.api.MotionPrintUtils;
import eigencraft.motionprint.data.PlayerDataLogger;
import eigencraft.motionprint.plugins.events.EventDataEntry;
import eigencraft.motionprint.plugins.events.EventPlugin;
import eigencraft.motionprint.util.IPlayerVelocityGetter;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity implements IPlayerVelocityGetter {
    private Vec3d lastPosition = Vec3d.ZERO;
    private Vec3d lastVelocity = Vec3d.ZERO;

    private MixinServerPlayerEntity(World world, GameProfile profile) { super(world, profile); }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource source, CallbackInfo ci) {
        if (EventPlugin.INSTANCE.isEnabled()&& MotionPrintUtils.shouldTrackPlayer((PlayerEntity)((Object)this))){
            PlayerDataLogger logger = MotionPrintUtils.getPlayerDataLogger((PlayerEntity)((Object)this));
            logger.logData(new EventDataEntry(String.format("death %s", source.getName())));
        }
    }

    @Inject(method = "playerTick", at = @At("HEAD"))
    private void onTickEnd(CallbackInfo ci) {
        Vec3d pos = this.getPos();

        // Avoid the first velocity data being garbage due to the last position
        // not getting initialized to the same value as the position.
        if (! this.lastPosition.equals(Vec3d.ZERO) || pos.equals(Vec3d.ZERO)) {
            this.lastVelocity = pos.subtract(this.lastPosition);
        }

        this.lastPosition = pos;
    }

    @Override
    public Vec3d getLastVelocity() {
        return this.lastVelocity;
    }
}
