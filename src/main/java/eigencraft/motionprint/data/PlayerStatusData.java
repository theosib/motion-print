package eigencraft.motionprint.data;

import eigencraft.motionprint.util.IPlayerVelocityGetter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nullable;

public class PlayerStatusData implements IDataEntry
{
    protected final DimensionType dim;
    protected final Vec3d pos;
    protected final Vec3d velocity;
    protected final float yaw;
    protected final float pitch;
    protected final long worldTick;
    protected final boolean sneaking;
    protected final boolean onGround;
    @Nullable protected final String event;

    private PlayerStatusData(DimensionType dim, Vec3d pos, Vec3d velocity, float yaw, float pitch,
                             long worldTick, boolean sneaking, boolean onGround, @Nullable String event) {
        this.dim = dim;
        this.pos = pos;
        this.velocity = velocity;
        this.yaw = yaw;
        this.pitch = pitch;
        this.worldTick = worldTick;
        this.sneaking = sneaking;
        this.onGround = onGround;
        this.event = event;
    }

    @Override
    public String getFormattedOutput() {
        Vec3d pos = this.pos;
        Vec3d vel = this.velocity;
        String onGroundStr = this.onGround ? "G" : "A"; // ground vs. air
        String sneakingStr = this.sneaking ? "S" : "-"; // sneaking vs. not sneaking
        String event = this.event != null ? this.event : "-";

        return String.format("%d p[%.4f %.4f %.4f] v[%.3f %.3f %.3f] %.4f %.4f %s %s %s\n",
                             this.worldTick, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z,
                             this.yaw, this.pitch, onGroundStr, sneakingStr, event);
    }

    public static PlayerStatusData of(PlayerEntity player) {
        return withEvent(player, null);
    }

    public static PlayerStatusData withEvent(PlayerEntity player, @Nullable String event) {
    
        World world = player.getEntityWorld();
        Vec3d pos = player.getPos();
        Vec3d velocity = player instanceof ServerPlayerEntity ? ((IPlayerVelocityGetter) player).getLastVelocity() : Vec3d.ZERO;

        return new PlayerStatusData(world.getDimension().getType(),
                                    pos, velocity,
                                    player.getYaw(0f), player.getPitch(0f),
                                    world.getTime(), player.isSneaking(), player.onGround, event);
    }
}
