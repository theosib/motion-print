package eigencraft.motionprint.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import eigencraft.motionprint.data.LoggingManager;
import eigencraft.motionprint.util.IPlayerVelocityGetter;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity implements IPlayerVelocityGetter {
    private Vec3d lastPosition = Vec3d.ZERO;
    private Vec3d lastVelocity = Vec3d.ZERO;

    private MixinServerPlayerEntity(World world, GameProfile profile) { super(world, profile); }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource source, CallbackInfo ci) {
        //LoggingManager.INSTANCE.onPlayerEvent((ServerPlayerEntity) (Object) this, "DEATH|" + source.getName());
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
