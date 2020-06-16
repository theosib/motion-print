package eigencraft.motionprint.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        List<ServerPlayerEntity> players = this.server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : players) {
            // Get your data from here, for example:
            player.getX();
            player.getY();
            player.getZ();
        }
    }
}
