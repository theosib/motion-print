package eigencraft.motionprint;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.network.ServerPlayerEntity;

public class MotionPrint implements DedicatedServerModInitializer {
    public static boolean hasConsented = true;
    public static ServerPlayerEntity currentPlayer = null;

    @Override
    public void onInitializeServer() {

    }
}
