package eigencraft.motionprint.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class PlayerStatusData {
    protected final DimensionType dim;
    protected final Vec3d velocity;
    protected final Vec3d pos;
    protected final float yaw;
    protected final float pitch;
    protected final long worldTick;
    protected final boolean onGround;

    public PlayerStatusData(DimensionType dim, Vec3d velocity, Vec3d pos, float yaw, float pitch, long worldTick, boolean onGround) {
        this.dim = dim;
        this.velocity = velocity;
        this.pos = pos;
        this.yaw = yaw;
        this.pitch = pitch;
        this.worldTick = worldTick;
        this.onGround = onGround;
    }

    public String formatData() {
        Vec3d pos = this.pos;
        Vec3d vel = this.velocity;
        String onGround = this.onGround ? "G" : "A"; // ground vs. air

        return String.format("%d p[%.4f %.4f %.4f] v[%.3f %.3f %.3f] %.4f %.4f %s\n",
                             this.worldTick, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, this.yaw, this.pitch, onGround);
    }

    public static PlayerStatusData of(PlayerEntity player) {
        World world = player.getEntityWorld();

        return new PlayerStatusData(world.getDimension().getType(),
                                    player.getVelocity(), player.getPos(), player.getYaw(0f), player.getPitch(0f),
                                    world.getTime(), player.onGround);
    }
}
