package eigencraft.motionprint;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MotionPrint implements DedicatedServerModInitializer {
    public static LinkedHashMap<String, Boolean> playersNameHasConsented = new LinkedHashMap<>();

    @Override
    public void onInitializeServer() {

    }
}
